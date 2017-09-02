package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;
import net.minecraft.entity.item.EntityItem;

public class ItemSpectriteShovelSpecial extends ItemSpectriteShovel implements IPerfectSpectriteItem {
	
	public ItemSpectriteShovelSpecial() {
        super(Spectrite.PERFECT_SPECTRITE_TOOL);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        return this.onEntitySpectriteItemUpdate(entityItem);
    }
}