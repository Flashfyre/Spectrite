package com.samuel.spectrite.creative;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabSpectrite extends CreativeTabs {

    public static CreativeTabSpectrite INSTANCE = new CreativeTabSpectrite();

    public CreativeTabSpectrite() {
        super(Spectrite.MOD_ID);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.spectrite_gem);
    }
}
