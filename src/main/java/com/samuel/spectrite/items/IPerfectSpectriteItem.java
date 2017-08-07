package com.samuel.spectrite.items;

import com.samuel.spectrite.etc.SpectriteHelper;

import net.minecraft.item.ItemStack;

public interface IPerfectSpectriteItem {

	default String getMultiColouredDisplayName(ItemStack stack, String displayName) {
		return SpectriteHelper.getMultiColouredString(displayName, this instanceof ItemSpectriteLegendBlade);
	}
}
