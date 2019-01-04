package com.samuel.spectrite.client.eventhandlers;

import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.client.renderer.SpectriteParticleManager;
import com.samuel.spectrite.client.sounds.SpectritePositionedMobDeathSound;
import com.samuel.spectrite.client.sounds.SpectritePositionedSound;
import com.samuel.spectrite.etc.ISpectriteTool;
import com.samuel.spectrite.items.ISpectriteItem;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class SpectriteClientEventHandler implements IResourceManagerReloadListener {

	private static Field isHittingBlock = null;
	private static Field currentBlock = null;
	private static Field blockDamageMP = null;
	private final TextureAtlasSprite[] blockDamageSprites = new TextureAtlasSprite[10];

	@SubscribeEvent
	public void onGameTick(TickEvent.ClientTickEvent event) {
		final EntityPlayer player = Minecraft.getMinecraft().player;
		if (player == null || event.side.isServer()) {
			return;
		}

		SpectriteParticleManager.INSTANCE.updateParticles();
	}

	@SubscribeEvent
	public void onPlaySpectriteEntitySound(PlaySoundEvent e) {
		if (e.getName().startsWith("spectrite")) {
			PositionedSound sound = (PositionedSound) e.getSound();
			e.setResultSound(!e.getName().endsWith(".death") ? new SpectritePositionedSound(sound)
				: new SpectritePositionedMobDeathSound(sound));
		}
	}

	@SubscribeEvent
	public void onRenderBlockBreak(RenderWorldLastEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		PlayerControllerMP playerController = Minecraft.getMinecraft().playerController;

		SpectriteParticleManager.INSTANCE.renderParticles(event.getPartialTicks());

		ItemStack stack = player.getHeldItemMainhand();
		boolean hittingBlock = false;

		if (isHittingBlock == null) {
			isHittingBlock = ReflectionHelper.findField(PlayerControllerMP.class, "isHittingBlock", "field_78778_j");
		}

		try {
			hittingBlock = (boolean) isHittingBlock.get(playerController);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if (!stack.isEmpty() && hittingBlock) {
			if (stack != null && stack.getItem() instanceof ISpectriteTool && !player.isSneaking()
				&& player.getCooldownTracker().getCooldown((stack.getItem()), 0f) == 0f && !player.isSneaking()) {
				if (currentBlock == null) {
					currentBlock = ReflectionHelper.findField(PlayerControllerMP.class, "currentBlock", "field_178895_c");
				}
				BlockPos currentBlockPos = null;
				try {
					currentBlockPos = (BlockPos) currentBlock.get(playerController);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				if (currentBlockPos != null) {
					List<BlockPos> affectedPosList = ((ISpectriteTool) stack.getItem()).getPlayerBreakableBlocks(stack, currentBlockPos, player);
					affectedPosList.remove(currentBlockPos);
					drawBlockDamageTexture(Tessellator.getInstance(),
						Tessellator.getInstance().getBuffer(), player, event.getPartialTicks(), affectedPosList);
				}
			}
		}
	}

	private void drawBlockDamageTexture(Tessellator tessellatorIn, BufferBuilder bufferBuilder, Entity entityIn, float partialTicks, List<BlockPos> blocks) {
		World world = entityIn.getEntityWorld();
		double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
		double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
		double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;

		if (blockDamageMP == null) {
			blockDamageMP = ReflectionHelper.findField(PlayerControllerMP.class, "curBlockDamageMP", "field_78770_f");
		}

		float blockDamage = -1f;

		try {
			blockDamage = (float) blockDamageMP.get(Minecraft.getMinecraft().playerController);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if (blockDamage >= 0f) {
			TextureManager renderEngine = Minecraft.getMinecraft().renderEngine;
			int progress = (int) (blockDamage * 10f) - 1;
			if (progress < 0) {
				return;
			}

			renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
			GlStateManager.enableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
			GlStateManager.doPolygonOffset(-3.0F, -3.0F);
			GlStateManager.enablePolygonOffset();
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.enableAlpha();
			GlStateManager.pushMatrix();

			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			bufferBuilder.setTranslation(-d0, -d1, -d2);
			bufferBuilder.noColor();

			for (BlockPos blockpos : blocks) {
				Block block = world.getBlockState(blockpos).getBlock();
				TileEntity te = world.getTileEntity(blockpos);
				boolean hasBreak = block instanceof BlockChest || block instanceof BlockEnderChest
						|| block instanceof BlockSign || block instanceof BlockSkull;
				if (!hasBreak) {
					hasBreak = te != null && te.canRenderBreaking();
				}

				if (!hasBreak) {
					IBlockState iblockstate = world.getBlockState(blockpos);

					if (iblockstate.getBlock().getMaterial(iblockstate) != Material.AIR) {
						TextureAtlasSprite textureatlassprite = this.blockDamageSprites[progress];
						BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
						blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, world);
					}
				}
			}

			tessellatorIn.draw();
			bufferBuilder.setTranslation(0.0D, 0.0D, 0.0D);

			GlStateManager.disableAlpha();
			GlStateManager.doPolygonOffset(0.0F, 0.0F);
			GlStateManager.disablePolygonOffset();
			GlStateManager.enableAlpha();
			GlStateManager.depthMask(true);
			GlStateManager.popMatrix();
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderTooltip(ItemTooltipEvent e) {
		if (!e.getItemStack().isEmpty() && e.getItemStack().getItem() instanceof ISpectriteItem) {
			((ISpectriteItem) e.getItemStack().getItem()).addTooltipLines(e.getItemStack(), e.getToolTip());
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onDrawBlockHighlight(DrawBlockHighlightEvent e) {
		EntityPlayer player = e.getPlayer();
		BlockPos blockpos = e.getTarget().getBlockPos();
		ItemStack playerHeldItem = player.getHeldItemMainhand();
		if (blockpos != null && !player.isSneaking()) {
				if (playerHeldItem != null && playerHeldItem.getItem() instanceof ISpectriteTool) {
				float cooldown = player.getCooldownTracker().getCooldown((playerHeldItem.getItem()), 0f);
				float greenValue = new Double((SpectriteConfig.items.spectriteToolCooldown - (SpectriteConfig.items.spectriteToolCooldown * cooldown)) / SpectriteConfig.items.spectriteToolCooldown).floatValue();
				
				if (cooldown <= 0.25f) {
					List<BlockPos> affectedPosList = ((ISpectriteTool) playerHeldItem.getItem())
						.getPlayerBreakableBlocks(playerHeldItem, blockpos, player);
					Iterator<BlockPos> affectedPosIterator;
					
					affectedPosIterator = affectedPosList.iterator();
					
					if (affectedPosIterator.hasNext()) {
						do {
							BlockPos curPos = affectedPosIterator.next();
							if (cooldown > 0f) {
								drawColoredBlockSelectionBox(player, curPos, e.getTarget(), e.getPartialTicks(), 0.0F, greenValue, 0.0F, 0.5F - (!curPos.equals(blockpos) ? (cooldown * 2f) : 0f));
							} else {
								drawColoredBlockSelectionBox(player, curPos, e.getTarget(), e.getPartialTicks(), 0.0F, 1.0F, 0.0F, 0.5F);
							}
						} while (affectedPosIterator.hasNext());
						e.setCanceled(true);
					}
				} else {
					drawColoredBlockSelectionBox(player, blockpos, e.getTarget(), e.getPartialTicks(), 0.0F, greenValue, 0.0F, 0.5F);
					e.setCanceled(true);
				}
			}
		}
	}

	private void drawColoredBlockSelectionBox(EntityPlayer player, BlockPos blockpos,
		RayTraceResult rayTraceResult, float partialTicks,
		float r, float g, float b, float a) {
		World world = player.getEntityWorld();
		GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
        	GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
        	GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(3.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        
        IBlockState iblockstate = world.getBlockState(blockpos);

        if (iblockstate.getMaterial() != Material.AIR && world.getWorldBorder().contains(blockpos)) {
            double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
            RenderGlobal.drawSelectionBoundingBox(iblockstate.getSelectedBoundingBox(world, blockpos)
            	.offset(-d0, -d1, -d2), r, g, b, a);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}

	@Override
	public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
		TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();

		for (int s = 0; s < this.blockDamageSprites.length; s++) {
			this.blockDamageSprites[s] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + s);
		}
	}
}
