package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;
import net.minecraft.entity.item.EntityItem;

public class ItemSpectriteAxeSpecial extends ItemSpectriteAxe implements IPerfectSpectriteItem {
	
	public ItemSpectriteAxeSpecial() {
        super(Spectrite.PERFECT_SPECTRITE_TOOL);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        return this.onEntitySpectriteItemUpdate(entityItem);
    }
}