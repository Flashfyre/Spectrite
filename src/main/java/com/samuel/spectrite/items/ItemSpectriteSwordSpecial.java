package com.samuel.spectrite.items;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSpectriteSwordSpecial extends ItemSpectriteSword implements IPerfectSpectriteItem {

	public ItemSpectriteSwordSpecial(ToolMaterial material) {
		super(material);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return this.onEntitySpectriteItemUpdate(entityItem);
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return this.getEntitySpectriteItemLifespan();
	}
}