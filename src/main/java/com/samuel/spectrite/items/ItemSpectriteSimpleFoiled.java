package com.samuel.spectrite.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpectriteSimpleFoiled extends ItemSpectriteSimple {

    public ItemSpectriteSimpleFoiled() {
        super();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String displayName = super.getItemStackDisplayName(stack);
        return TextFormatting.LIGHT_PURPLE  + displayName;
    }
}
