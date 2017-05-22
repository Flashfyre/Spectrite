package com.samuel.spectritemod.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import com.samuel.spectritemod.SpectriteMod;

public class ItemSpectriteRod extends Item {
	public ItemSpectriteRod() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), SpectriteMod.ItemPropertyGetterSpectrite);
	}
}
