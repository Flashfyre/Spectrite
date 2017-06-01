package com.samuel.spectritemod.items;

import com.samuel.spectritemod.SpectriteMod;

import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class ItemSpectriteShield extends ItemShield {
	
	public ItemSpectriteShield() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), SpectriteMod.ItemPropertyGetterSpectrite);
		this.setMaxDamage(this instanceof ItemSpectriteShieldSpecial ? 777 : 512);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		
		String displayName = super.getItemStackDisplayName(stack);
		if (displayName.equals(I18n.translateToLocal("item.shield.name"))) {
			displayName = I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
		}
		displayName = (stack.getItem() instanceof ItemSpectriteShieldSpecial ? TextFormatting.RED :
			TextFormatting.LIGHT_PURPLE) + displayName;
		return displayName;
	}
}
