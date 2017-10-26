package com.samuel.spectrite.etc;

import com.samuel.spectrite.items.ISpectriteItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface ISpectriteTool extends ISpectriteItem {
	List<BlockPos> getPlayerBreakableBlocks(ItemStack itemstack, BlockPos pos, EntityPlayer player);
}
