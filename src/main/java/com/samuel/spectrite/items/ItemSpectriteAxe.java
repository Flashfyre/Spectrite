package com.samuel.spectrite.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.etc.ISpectriteTool;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModSounds;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
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

public class ItemSpectriteAxe extends ItemAxe implements ISpectriteTool {
	
	public ItemSpectriteAxe(ToolMaterial material) {
        super(material, material == Spectrite.SPECTRITE_TOOL ? 6.0F : 7.0F, -3.0f);
        this.addPropertyOverride(new ResourceLocation("time"), Spectrite.ItemPropertyGetterSpectrite);
    }
	
	public ItemSpectriteAxe() {
		this(Spectrite.SPECTRITE_TOOL);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		
		String displayName = super.getItemStackDisplayName(stack);
		displayName = stack.getItem() instanceof IPerfectSpectriteItem ? ((IPerfectSpectriteItem) this).getMultiColouredDisplayName(stack, displayName)
			: (TextFormatting.LIGHT_PURPLE + displayName);
		return displayName;
	}
	
	@Override
	public void addInformation(ItemStack stack,
		World worldIn, List<String> list, ITooltipFlag adva) {
		int lineCount = 0;
		boolean isLastLine = false;
		double cooldown = SpectriteConfig.spectriteToolCooldown;
		if (SpectriteHelper.isStackSpectriteEnhanced(stack)) {
			cooldown *= 0.5d;
		}
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = I18n
				.translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + ".l" +
				++lineCount))).endsWith("@");
			if (lineCount == 1) {
				curLine = curLine.replace("#", String.format("%.2f", cooldown));
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
     * Called before a block is broken. Return true to prevent default block harvesting.
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
    	if (!worldIn.isRemote && getStrVsBlock(itemstack,  worldIn.getBlockState(pos)) > 1.0f) {
    		if (player.getCooldownTracker().getCooldown(this, 0) == 0f && !player.isSneaking()) {
				WorldServer worldServer = (WorldServer) worldIn;
				BlockPos curPos;
				Block curBlock;
				Block centerBlock = worldIn.getBlockState(pos).getBlock();
				IBlockState curState;
				final int posX = pos.getX(), posY = pos.getY(), posZ = pos.getZ();
				Iterator<BlockPos> targetBlocks = getPlayerBreakableBlocks(itemstack, pos, player).iterator();
				
				if (targetBlocks.hasNext()) {
					do {
						curPos = targetBlocks.next();
						curState = worldIn.getBlockState(curPos);
						worldIn.destroyBlock(curPos, true);
						curState.getBlock().onBlockDestroyedByPlayer(worldIn, curPos, curState);
					} while(targetBlocks.hasNext());
					
					if (!(this instanceof ItemSpectriteAxeSpecial)) {
						worldIn.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.75F,
							1.0F + (worldIn.rand.nextFloat()) * 0.4F);
					} else {
						worldIn.playSound(null, pos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F,
							1.0F + (worldIn.rand.nextFloat()) * 0.4F);
					}
					
					worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE,
							EnumParticleTypes.EXPLOSION_LARGE.getShouldIgnoreRange(),
							posX, posY, posZ, !(this instanceof ItemSpectriteAxeSpecial) ? 1 : 7,
							worldIn.rand.nextFloat() * 0.5f, worldIn.rand.nextFloat() * 0.5f,
							worldIn.rand.nextFloat() * 0.5f, 0.0D, new int[0]);
					
					if (!player.isCreative()) {
						boolean isEnhanced = SpectriteHelper.isStackSpectriteEnhanced(itemstack);
						player.getCooldownTracker().setCooldown(this, (int) Math.round(SpectriteConfig.spectriteToolCooldown * (isEnhanced ? 10 : 20)));
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
		float centerBlockStrVsBlock = getStrVsBlock(itemstack, worldIn.getBlockState(pos));
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
				if ((!(itemstack.getItem() instanceof ItemSpectriteAxeSpecial) &&
					((isDiagonalFacing && (blockCount == 2 || blockCount == 4 || blockCount == 6 || blockCount == 8)) ||
					(!isDiagonalFacing && (blockCount == 1 || blockCount == 3 || blockCount == 7 || blockCount == 9)))) ||
					getStrVsBlock(itemstack, curState) < 10.0f || curBlockHardness == -1.0f || centerBlockHardness > curBlockHardness) {
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
				breakableBlocks.add(curPos);
			}
    	}
    	
    	return breakableBlocks;
	}
}
