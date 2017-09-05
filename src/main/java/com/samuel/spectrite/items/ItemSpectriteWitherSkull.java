package com.samuel.spectrite.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ItemSpectriteWitherSkull extends ItemSpectriteSkull implements IPerfectSpectriteItem, ICustomTooltipItem {

    public ItemSpectriteWitherSkull(int skullType) {
        super(skullType);
        if (skullType == 2) {
            this.setMaxDamage(0);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String displayName = super.getItemStackDisplayName(stack);
        return TextFormatting.LIGHT_PURPLE  + displayName;
    }
}
