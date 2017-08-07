package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemSpectriteBrick extends Item {
	public ItemSpectriteBrick() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), Spectrite.ItemPropertyGetterSpectrite);
	}
}
