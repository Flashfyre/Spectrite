package com.samuel.spectritemod.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import com.samuel.spectritemod.SpectriteMod;

public class ItemSpectriteGem extends Item {
	
	public ItemSpectriteGem() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), SpectriteMod.ItemPropertyGetterSpectrite);
	}
}
