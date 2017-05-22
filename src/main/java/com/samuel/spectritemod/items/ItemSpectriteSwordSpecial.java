package com.samuel.spectritemod.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.samuel.spectritemod.SpectriteMod;

public class ItemSpectriteSwordSpecial extends ItemSpectriteSword {

	public ItemSpectriteSwordSpecial(boolean isLegendBlade) {
		super(!isLegendBlade ? SpectriteMod.PERFECT_SPECTRITE_TOOL : SpectriteMod.PERFECT_SPECTRITE_2_TOOL, isLegendBlade);
	}
}