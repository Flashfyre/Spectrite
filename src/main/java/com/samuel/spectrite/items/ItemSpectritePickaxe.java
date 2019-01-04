package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.etc.ISpectriteTool;
import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.init.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemSpectritePickaxe extends ItemPickaxe implements ISpectriteTool, ISpectriteCustomTooltipItem {
	
	public ItemSpectritePickaxe(ToolMaterial material) {
        super(material);
        this.addPropertyOverride(new ResourceLocation("time"), Spectrite.ItemPropertyGetterSpectrite);
        attackSpeed = -3.0F;
    }
	
	public ItemSpectritePickaxe() {
		this(Spectrite.SPECTRITE_TOOL);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addTooltipLines(ItemStack stack, List<String> list) {
		int lineCount = 0;
		boolean isLastLine = false;
		double toolCooldown = SpectriteConfig.items.spectriteToolCooldown;
		double weaponCooldown = SpectriteConfig.items.spectriteWeaponCooldown;
		if (SpectriteHelper.isStackSpectriteEnhanced(stack)) {
			toolCooldown *= 0.5d;
			weaponCooldown *= 0.5d;
		}
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = I18n
				.translateToLocal(("iteminfo." + getTranslationKey().substring(5) + ".l" +
				++lineCount))).endsWith("@");
			if (lineCount % 2 == 1) {
				curLine = curLine.replace("#", String.format("%.2f", lineCount == 1 ? toolCooldown : weaponCooldown));
			}
			list.add(!isLastLine ? curLine : curLine
				.substring(0, curLine.length() - 1));
		}
		list.set(0, getMultiColouredDisplayName(stack, stack.getDisplayName()));
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		return state.getBlock() instanceof BlockSkull || state.getBlock() instanceof BlockBreakable ? this.efficiency : super.getDestroySpeed(stack, state);
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		return !(blockIn instanceof BlockSkull) && super.canHarvestBlock(blockIn);
	}
	
	@Override
	/**
     * Called before a block is broken.  Return true to prevent default block harvesting.
     *
     * Note: In SMP, this is called on both client and server sides!
     *
     * @param itemstack The current ItemStack
     * @param pos Block's position in world
     * @param player The Player that is wielding the item
     * @return True to prevent harvesting, false to continue as normal
     */
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
    {
		World worldIn = player.world;
		if (player.getCooldownTracker().getCooldown(this, 0f) == 0f && !player.isSneaking()) {
			BlockPos curPos;
			Block curBlock;
			IBlockState curState;
			Iterator<BlockPos> targetBlocks = getPlayerBreakableBlocks(itemstack, pos, player).iterator();

			if (targetBlocks.hasNext()) {
				do {
					curPos = targetBlocks.next();
					curState = worldIn.getBlockState(curPos);
					curBlock = curState.getBlock();
					int exp = !worldIn.isRemote ? ForgeHooks.onBlockBreakEvent(worldIn, ((EntityPlayerMP) player).interactionManager.getGameType(), (EntityPlayerMP) player, curPos) : -1;
					boolean canHarvest = !player.isCreative() && ForgeHooks.canHarvestBlock(curBlock, player, worldIn, curPos) && curBlock.canHarvestBlock(worldIn, curPos, player);

					if (player.isCreative()) {
						curBlock.onBlockHarvested(worldIn, curPos, curState, player);
					}

					boolean removedByPlayer = curBlock.removedByPlayer(curState, worldIn, curPos, player, canHarvest);

					if (removedByPlayer) {
						curBlock.onPlayerDestroy(worldIn, curPos, curState);
					}

					itemstack.onBlockDestroyed(worldIn, curState, curPos, player);

					if (!worldIn.isRemote) {
						if (!player.isCreative() && removedByPlayer) {
							curBlock.harvestBlock(worldIn, player, curPos, curState, worldIn.getTileEntity(curPos), itemstack);
							if (exp > 0) {
								curBlock.dropXpOnBlockBreak(worldIn, curPos, exp);
							}
						}
						((EntityPlayerMP) player).connection.sendPacket(new SPacketBlockChange(worldIn, curPos));

						Spectrite.Proxy.spawnSpectriteExplosionParticle(worldIn, true,curPos.getX() + 0.5F,curPos.getY() + 0.5F, curPos.getZ() + 0.5F, 0, 0, 0);
					} else {
						worldIn.playEvent(2001, curPos, Block.getStateId(curState));
						if (curBlock.removedByPlayer(curState, worldIn, curPos, player, true)) {
							curBlock.onPlayerDestroy(worldIn, curPos, curState);
						}

						itemstack.onBlockDestroyed(worldIn, curState, curPos, player);

						if (itemstack.isEmpty() && itemstack == player.getHeldItemMainhand()) {
							ForgeEventFactory.onPlayerDestroyItem(player, itemstack, EnumHand.MAIN_HAND);
							player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
						}

						NetHandlerPlayClient netHandlerPlayClient = Minecraft.getMinecraft().getConnection();
						if (netHandlerPlayClient != null) {
							netHandlerPlayClient.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, curPos, Minecraft
									.getMinecraft().objectMouseOver.sideHit));
						}
					}
				} while (targetBlocks.hasNext());

				if (!worldIn.isRemote) {
					if (!(this instanceof IPerfectSpectriteItem)) {
						worldIn.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.75F,
								1.0F + (worldIn.rand.nextFloat()) * 0.4F);
					} else {
						worldIn.playSound(null, pos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F,
								1.0F + (worldIn.rand.nextFloat()) * 0.4F);
					}

					if (!player.isCreative()) {
						boolean isEnhanced = SpectriteHelper.isStackSpectriteEnhanced(itemstack);
						player.getCooldownTracker().setCooldown(this, (int) Math.round(SpectriteConfig.items.spectriteToolCooldown * (isEnhanced ? 10 : 20)));
					}
				}
			}
		}
		return super.onBlockStartBreak(itemstack, pos, player);
    }
	
	@Override
	public List<BlockPos> getPlayerBreakableBlocks(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
		World worldIn = player.world;
		List<BlockPos> breakableBlocks = new ArrayList<BlockPos>();
		float centerBlockStrVsBlock = getDestroySpeed(itemstack, worldIn.getBlockState(pos));
    	if (centerBlockStrVsBlock > 1.0f) {
			Vec3d lookVec = player.getLookVec();
			EnumFacing facing = EnumFacing.getFacingFromVector((float) lookVec.x,
				(float) lookVec.y, (float) lookVec.z);
			float relYaw = !worldIn.isRemote ? player.getRotationYawHead() >= 0 ? player.getRotationYawHead() % 90
				: 90 - Math.abs(player.getRotationYawHead() % 90) : (player.getRotationYawHead() % 90) + (player.getRotationYawHead() >= 0 ? 0 : 90);
			boolean isDiagonalFacing = relYaw >= 22.5f && relYaw < 67.5f;
			Axis axis = facing.getAxis();
			BlockPos curPos;
			Block curBlock;
			IBlockState centerState = worldIn.getBlockState(pos);
			IBlockState curState;
			final int posX = pos.getX(), posY = pos.getY(), posZ = pos.getZ();
			Iterator<BlockPos> targetBlocks;
			int blockCount = 0;
			float centerBlockHardness = centerState.getBlock().getPlayerRelativeBlockHardness(centerState, player, worldIn, pos);
			float curBlockHardness;
			
			if (axis != Axis.Y && posY < player.posY)
				axis = Axis.Y;
						
			targetBlocks = BlockPos.getAllInBox(new BlockPos(axis == Axis.X ?
				posX : posX - 1, axis == Axis.Y ? posY : posY - 1, axis == Axis.Z ?
				posZ : posZ - 1), new BlockPos(axis == Axis.X ? posX : posX + 1, axis == Axis.Y ?
				posY : posY + 1, axis == Axis.Z ? posZ : posZ + 1)).iterator();
			
			while (targetBlocks.hasNext()) {
				curPos = targetBlocks.next();
				curState = worldIn.getBlockState(curPos);
				curBlock = curState.getBlock();
				curBlockHardness = curBlock.getPlayerRelativeBlockHardness(curState, player, worldIn, curPos);
				blockCount++;
				if ((!(itemstack.getItem() instanceof ItemSpectritePickaxeSpecial) &&
					((isDiagonalFacing && (blockCount == 2 || blockCount == 4 || blockCount == 6 || blockCount == 8)) ||
					(!isDiagonalFacing && (blockCount == 1 || blockCount == 3 || blockCount == 7 || blockCount == 9)))) ||
					getDestroySpeed(itemstack, curState) < 10.0f || curBlockHardness == -1.0f || centerBlockHardness > curBlockHardness) {
					continue;
				} else {
					boolean isVisible = false;
					for (EnumFacing side : EnumFacing.values()) {
						BlockPos offsetPos = curPos.offset(side);
						if (offsetPos.equals(pos) || worldIn.getBlockState(curPos.offset(side)).getMaterial().equals(Material.AIR)
							|| !worldIn.getBlockState(curPos.offset(side)).isFullBlock()) {
							isVisible = true;
							break;
						}
					}
					if (!isVisible) {
						continue;
					}
				}
				breakableBlocks.add(curPos);
			}
    	}
    	
    	return breakableBlocks;
	}
}