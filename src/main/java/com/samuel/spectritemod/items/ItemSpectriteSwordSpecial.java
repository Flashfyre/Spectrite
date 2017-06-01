package com.samuel.spectritemod.items;

import com.samuel.spectritemod.SpectriteMod;

public class ItemSpectriteSwordSpecial extends ItemSpectriteSword {

	public ItemSpectriteSwordSpecial(boolean isLegendBlade) {
		super(!isLegendBlade ? SpectriteMod.PERFECT_SPECTRITE_TOOL : SpectriteMod.PERFECT_SPECTRITE_2_TOOL, isLegendBlade);
	}
}