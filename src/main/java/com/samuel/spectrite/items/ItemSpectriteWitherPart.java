package com.samuel.spectrite.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemSpectriteWitherPart extends ItemSpectriteSimple {

    public ItemSpectriteWitherPart() {
        super();
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.setMaxDamage(0);
        this.hasSubtypes = true;
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "_" + (stack.getItemDamage() == 0 ? "normal" : "invulnerable");
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
        }
    }
}
