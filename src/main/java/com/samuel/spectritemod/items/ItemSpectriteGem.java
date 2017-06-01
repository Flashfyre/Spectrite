package com.samuel.spectritemod.items;

import com.samuel.spectritemod.SpectriteMod;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemSpectriteGem extends Item {
	
	public ItemSpectriteGem() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), SpectriteMod.ItemPropertyGetterSpectrite);
	}
}
