package com.samuel.spectrite.items;

import net.minecraft.entity.item.EntityItem;

public class ItemSpectriteBowSpecial extends ItemSpectriteBow implements IPerfectSpectriteItem {

	public ItemSpectriteBowSpecial() {
		super();
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return this.onEntitySpectriteItemUpdate(entityItem);
	}
}
