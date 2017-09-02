package com.samuel.spectrite.etc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface ISpectriteTool {
	public List<BlockPos> getPlayerBreakableBlocks(ItemStack itemstack, BlockPos pos, EntityPlayer player);
}
