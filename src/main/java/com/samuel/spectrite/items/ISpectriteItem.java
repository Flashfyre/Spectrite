package com.samuel.spectrite.items;

import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface ISpectriteItem {

    default String getMultiColouredDisplayName(ItemStack stack, String displayName) {
        String ret;
        if (stack.getItem() instanceof IPerfectSpectriteItem) {
            ret = SpectriteHelper.getMultiColouredString(displayName,this == ModItems.spectrite_sword_2 || (this == ModItems.spectrite_wither_rod_invulnerable));
        } else {
            ret = TextFormatting.LIGHT_PURPLE + displayName;
        }
        return ret;
    }

    @SideOnly(Side.CLIENT)
    default void addTooltipLines(ItemStack stack, List<String> list) {
        list.set(0, ((ISpectriteItem) stack.getItem()).getMultiColouredDisplayName(stack, stack.getDisplayName()));
    }
}
