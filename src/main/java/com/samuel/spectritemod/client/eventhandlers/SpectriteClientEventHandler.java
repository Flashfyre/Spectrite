package com.samuel.spectritemod.client.eventhandlers;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.capabilities.ISpectriteBossCapability;
import com.samuel.spectritemod.capabilities.SpectriteBossProvider;
import com.samuel.spectritemod.client.renderer.entity.layers.LayerSpectriteArmor;
import com.samuel.spectritemod.etc.ISpectriteTool;
import com.samuel.spectritemod.etc.SpectriteHelper;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpectriteClientEventHandler {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderEntity(RenderLivingEvent.Post e) {
		if (e.getEntity().hasCapability(SpectriteBossProvider.sbc, null)) {
			ISpectriteBossCapability sbc = e.getEntity().getCapability(SpectriteBossProvider.sbc, null);
			if (sbc.isEnabled()) {
				RenderManager renderManager = e.getRenderer().getRenderManager();
				boolean isSneaking = e.getEntity().isSneaking();
				float viewerYaw = renderManager.playerViewY;
				float viewerPitch = renderManager.playerViewX;
				boolean isThirdPersonFrontal = renderManager.options.thirdPersonView == 2;
				float f = e.getEntity().height + 0.5F - (isSneaking ? 0.25F : 0.0F) + (e.getEntity().hasCustomName() ? 0.3f : 0.0f);
				int hueFrame = ((int) e.getEntity().getEntityWorld().getWorldTime()) % 180;
				float r = hueFrame >= 120 && hueFrame < 150 ? (1f / 30) * (hueFrame - 120) : hueFrame < 30 || hueFrame >= 150 ? 1f : hueFrame < 60 ? (1f / 30) * (30 - (hueFrame - 30)) : 0f,
					g = hueFrame < 30 ? (1f / 30) * hueFrame : hueFrame < 90 ? 1f : hueFrame < 120 ? (1f / 30) * (30 - (hueFrame - 90)) : 0f,
					b = hueFrame >= 60 && hueFrame < 90 ? (1f / 30) * (hueFrame - 60) : hueFrame >= 90 && hueFrame < 150 ? 1f : hueFrame >= 150 ? (1f / 30) * (30 - (hueFrame - 150)) : 0f;
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(e.getX(), e.getY() + f, e.getZ());
				GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate((float)(isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
				GlStateManager.scale(-0.025F, -0.025F, 0.025F);
				GlStateManager.disableLighting();
				GlStateManager.depthMask(false);
				
				if (!isSneaking)
				{
				    GlStateManager.disableDepth();
				}
				
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
					GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer vertexbuffer = tessellator.getBuffer();
				GlStateManager.color(r, g, b);
				renderManager.renderEngine.bindTexture(new ResourceLocation(String.format("%s:textures/gui/crown.png", SpectriteMod.MOD_ID)));
				vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
				vertexbuffer.pos(-8.0D, 10.0D, 0.0D).tex(0, 1).endVertex();
				vertexbuffer.pos(8.0D, 10.0D, 0.0D).tex(1, 1).endVertex();
				vertexbuffer.pos(8.0D, 0.0D, 0.0D).tex(1, 0).endVertex();
				vertexbuffer.pos(-8.0D, 0.0D, 0.0D).tex(0, 0).endVertex();
				tessellator.draw();
				
				if (!isSneaking)
				{
				    GlStateManager.enableDepth();
				}
				
				GlStateManager.depthMask(true);
				GlStateManager.enableLighting();
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onEntitySpawn(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityPlayer || e.getEntity() instanceof EntityLiving) {
			EntityLivingBase entity = (EntityLivingBase) e.getEntity();
			if (entity instanceof EntityPlayer || entity instanceof EntityZombie || entity instanceof AbstractSkeleton) {
        		Field layerRenderers = SpectriteHelper.findObfuscatedField(RenderLivingBase.class,
        	    		"layerRenderers", "field_177097_h");
    	    	layerRenderers.setAccessible(true);
    	    	if (!(entity instanceof EntityLiving)) {
    	    		List<RenderPlayer> renderers = (Minecraft.getMinecraft().getRenderManager().getSkinMap().values().stream().filter(r -> {
						try {
							return !((List<LayerRenderer>) layerRenderers.get(r)).stream().anyMatch(lr -> lr.getClass() == LayerSpectriteArmor.class);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						return true;
					})).collect(Collectors.toList());
    	    		for (RenderPlayer rp : renderers) {
    	    			try {
							((List<LayerRenderer>) layerRenderers.get(rp)).set(0, new LayerSpectriteArmor(rp));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
    	    		}
    	    	} else {
        	    	try {
        		    	RenderLivingBase<?> renderer = ((RenderLivingBase<?>) Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(entity.getClass()));
        		    	List<LayerRenderer> renderers = (List) layerRenderers.get(renderer);
        		    	
        		    	if (!(renderers).stream().anyMatch(r -> r.getClass() == LayerSpectriteArmor.class)) {
        		    		Optional<LayerRenderer> layerToReplace = (renderers).stream().filter(lr -> lr.getClass().getSuperclass() == LayerBipedArmor.class).findFirst();
        		    		if (layerToReplace.isPresent()) {
        		    			((List) layerRenderers.get(renderer)).set(renderers.indexOf(layerToReplace.get()), new LayerSpectriteArmor(renderer));
        		    		}
        		    	}
        			} catch (Exception e1) {
        				e1.printStackTrace();
        			}
            	}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onArmorStandSpawn(EnteringChunk e) {
		if (e.getEntity().getEntityWorld().isRemote && e.getEntity() instanceof EntityArmorStand) {
			EntityArmorStand entity = (EntityArmorStand) e.getEntity();
    		Field layerRenderers = SpectriteHelper.findObfuscatedField(RenderLivingBase.class,
    	    		"layerRenderers", "field_177097_h");
	    	layerRenderers.setAccessible(true);
	    	try {
		    	RenderLivingBase<?> renderer = ((RenderLivingBase<?>) Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(entity.getClass()));
		    	List<LayerRenderer> renderers = (List) layerRenderers.get(renderer);
		    	
		    	if (!(renderers).stream().anyMatch(r -> r.getClass() == LayerSpectriteArmor.class)) {
	    			((List) layerRenderers.get(renderer)).set(0, new LayerSpectriteArmor(renderer));
		    	}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onDrawBlockHighlight(DrawBlockHighlightEvent e) {
		EntityPlayer player = e.getPlayer();
		BlockPos blockpos = e.getTarget().getBlockPos();
		ItemStack playerHeldItem = player.getHeldItemMainhand();
		if (blockpos != null) {
			if (playerHeldItem != null && playerHeldItem.getItem() instanceof ISpectriteTool) {
				float cooldown = player.getCooldownTracker().getCooldown((playerHeldItem.getItem()), 0f);
				float greenValue = new Double((SpectriteMod.Config.spectriteToolCooldown - (SpectriteMod.Config.spectriteToolCooldown * cooldown)) / SpectriteMod.Config.spectriteToolCooldown).floatValue();
				
				if (cooldown <= 0.25f) {
					List<BlockPos> affectedPosList = ((ISpectriteTool) playerHeldItem.getItem())
						.getPlayerBreakableBlocks(playerHeldItem, blockpos, player);
					Iterator<BlockPos> affectedPosIterator;
					
					affectedPosIterator = affectedPosList.iterator();
					
					while (affectedPosIterator.hasNext()) {
						BlockPos curPos = affectedPosIterator.next();
						if (cooldown > 0f) {
							drawColoredBlockSelectionBox(player, curPos, e.getTarget(), e.getPartialTicks(), 0.0F, greenValue, 0.0F, 0.5F - (!curPos.equals(blockpos) ? (cooldown * 2f) : 0f));
						} else {
							drawColoredBlockSelectionBox(player, curPos, e.getTarget(), e.getPartialTicks(), 0.0F, 1.0F, 0.0F, 0.5F);
						}
					}
				} else {
					drawColoredBlockSelectionBox(player, blockpos, e.getTarget(), e.getPartialTicks(), 0.0F, greenValue, 0.0F, 0.5F);
				}
				e.setCanceled(true);
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

        if (iblockstate.getMaterial() != Material.AIR && world.getWorldBorder().contains(blockpos))
        {
            double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
            Minecraft.getMinecraft().renderGlobal.drawSelectionBoundingBox(iblockstate.getSelectedBoundingBox(world, blockpos)
            	.offset(-d0, -d1, -d2), r, g, b, a);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}
}
