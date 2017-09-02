package com.samuel.spectrite.items;

import net.minecraft.entity.item.EntityItem;

public class ItemSpectriteShieldSpecial extends ItemSpectriteShield implements IPerfectSpectriteItem {
	
	public ItemSpectriteShieldSpecial() {
		super();
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return this.onEntitySpectriteItemUpdate(entityItem);
	}
}
