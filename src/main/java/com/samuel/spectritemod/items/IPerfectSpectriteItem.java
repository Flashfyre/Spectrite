package com.samuel.spectritemod.items;

import com.samuel.spectritemod.etc.SpectriteHelper;

import net.minecraft.item.ItemStack;

public interface IPerfectSpectriteItem {

	default String getMultiColouredDisplayName(ItemStack stack, String displayName) {
		return SpectriteHelper.getMultiColouredString(displayName, this instanceof ItemSpectriteLegendBlade);
	}
}
