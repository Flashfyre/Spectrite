package com.samuel.spectritemod.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.etc.ISpectriteTool;
import com.samuel.spectritemod.init.ModSounds;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemSpectritePickaxe extends ItemPickaxe implements ISpectriteTool {
	
	public ItemSpectritePickaxe(ToolMaterial material) {
        super(material);
        this.addPropertyOverride(new ResourceLocation("time"), SpectriteMod.ItemPropertyGetterSpectrite);
        attackSpeed = -3.0F;
    }
	
	public ItemSpectritePickaxe() {
		this(SpectriteMod.SPECTRITE_TOOL);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		
		String displayName = super.getItemStackDisplayName(stack);
		displayName = (stack.getItem() instanceof ItemSpectritePickaxeSpecial ? TextFormatting.RED :
			TextFormatting.LIGHT_PURPLE) + displayName;
		return displayName;
	}
	
	@Override
	public void addInformation(ItemStack stack,
		EntityPlayer player, List list, boolean Adva) {
		int lineCount = 0;
		boolean isLastLine = false;
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = I18n
				.translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + ".l" +
				++lineCount))).endsWith("@");
			if (lineCount == 1) {
				curLine = curLine.replace("#", String.valueOf(SpectriteMod.Config.spectriteToolCooldown));
			}
			list.add(!isLastLine ? curLine : curLine
				.substring(0, curLine.length() - 1));
		}
		if (stack.isItemEnchanted()) {
			list.add("----------");
		}
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
    	if (!worldIn.isRemote) {
    		if (player.getCooldownTracker().getCooldown(this, 0f) == 0f && !player.isSneaking()) {
	    		WorldServer worldServer = (WorldServer) worldIn;
	    		BlockPos curPos;
				Block curBlock;
				IBlockState curState;
				final int posX = pos.getX(), posY = pos.getY(), posZ = pos.getZ();
				Iterator<BlockPos> targetBlocks = getPlayerBreakableBlocks(itemstack, pos, player).iterator();
				
				if (targetBlocks.hasNext()) {
					do {
						curPos = targetBlocks.next();
						curState = worldIn.getBlockState(curPos);
						worldIn.destroyBlock(curPos, true);
						curState.getBlock().onBlockDestroyedByPlayer(worldIn, curPos, curState);
					} while (targetBlocks.hasNext());	
					
					if (!(this instanceof ItemSpectritePickaxeSpecial)) {
						worldIn.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.75F,
							1.0F + (worldIn.rand.nextFloat()) * 0.4F);
					} else {
						worldIn.playSound(null, pos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F,
							1.0F + (worldIn.rand.nextFloat()) * 0.4F);
					}
					
					worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE,
						EnumParticleTypes.EXPLOSION_LARGE.getShouldIgnoreRange(),
						posX, posY, posZ, !(this instanceof ItemSpectritePickaxeSpecial) ? 1 : 7,
						worldIn.rand.nextFloat() * 0.5f, worldIn.rand.nextFloat() * 0.5f,
						worldIn.rand.nextFloat() * 0.5f, 0.0D, new int[0]);
					
					if (!player.isCreative()) {
						player.getCooldownTracker().setCooldown(this, (int) Math.round(SpectriteMod.Config.spectriteToolCooldown * 20));
					}
				}
    		}
    	}
        return false;
    }
	
	@Override
	public List<BlockPos> getPlayerBreakableBlocks(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
		World worldIn = player.world;
		List<BlockPos> breakableBlocks = new ArrayList<BlockPos>();
    	if (getStrVsBlock(itemstack,  worldIn.getBlockState(pos)) > 1.0f) {
			Vec3d lookVec = player.getLookVec();
			EnumFacing facing = EnumFacing.getFacingFromVector((float) lookVec.xCoord,
				(float) lookVec.yCoord, (float) lookVec.zCoord);
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
			float strengthVsCenterBlock = getStrVsBlock(itemstack, centerState);
			float strengthVsCurBlock;
			
			if (axis != Axis.Y && posY < player.posY)
				axis = Axis.Y;
						
			targetBlocks = BlockPos.getAllInBox(new BlockPos(axis == Axis.X ?
				posX : posX - 1, axis == Axis.Y ? posY : posY - 1, axis == Axis.Z ?
				posZ : posZ - 1), new BlockPos(axis == Axis.X ? posX : posX + 1, axis == Axis.Y ?
				posY : posY + 1, axis == Axis.Z ? posZ : posZ + 1)).iterator();
			
			while (targetBlocks.hasNext()) {
				curPos = targetBlocks.next();
				curState = worldIn.getBlockState(curPos);
				strengthVsCurBlock = getStrVsBlock(itemstack, curState);
				blockCount++;
				curBlock = curState.getBlock();
				if ((!(itemstack.getItem() instanceof ItemSpectritePickaxeSpecial) &&
					((isDiagonalFacing && (blockCount == 2 || blockCount == 4 || blockCount == 6 || blockCount == 8)) ||
					(!isDiagonalFacing && (blockCount == 1 || blockCount == 3 || blockCount == 7 || blockCount == 9)))) ||
					strengthVsCurBlock <= 1.0f || strengthVsCenterBlock < strengthVsCurBlock) {
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
				if (curBlock.canHarvestBlock(worldIn, curPos, player)) {
					curBlock.onBlockHarvested(worldIn, curPos, curState, player);
				}
				if (curBlock.getBlockHardness(curState, worldIn, curPos) > 0.0) {
					breakableBlocks.add(curPos);
				}
			}
    	}
    	
    	return breakableBlocks;
	}
}