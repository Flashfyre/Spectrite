package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemSpectriteSimple extends Item {
	public ItemSpectriteSimple() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), Spectrite.ItemPropertyGetterSpectrite);
	}
}
