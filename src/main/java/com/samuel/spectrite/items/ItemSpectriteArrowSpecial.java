package com.samuel.spectrite.items;

import net.minecraft.entity.item.EntityItem;

public class ItemSpectriteArrowSpecial extends ItemSpectriteArrow implements IPerfectSpectriteItem {

	public ItemSpectriteArrowSpecial() {
		super();
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return this.onEntitySpectriteItemUpdate(entityItem);
	}
}
