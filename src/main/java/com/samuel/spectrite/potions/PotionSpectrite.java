package com.samuel.spectrite.potions;

import com.samuel.spectrite.etc.SpectriteHelper;

import net.minecraft.potion.Potion;

public class PotionSpectrite extends Potion {
	
	public PotionSpectrite() {
		super(true, SpectriteHelper.getCurrentSpectriteColour(0));
	}

	@Override
	public int getLiquidColor()
    {
        return SpectriteHelper.getCurrentSpectriteColour(0);
    }
}
