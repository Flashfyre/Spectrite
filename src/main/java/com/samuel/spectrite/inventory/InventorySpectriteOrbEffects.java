package com.samuel.spectrite.inventory;

import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.items.ItemSpectriteOrb;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventorySpectriteOrbEffects extends InventoryBasic {

    private ItemStack orbStack = ItemStack.EMPTY;

    public InventorySpectriteOrbEffects(String title, boolean customName)
    {
        super(title, customName, 7);
    }

    public void setOrbStack(ItemStack orbStack) {
        this.orbStack = orbStack;
        if (!orbStack.isEmpty()) {
            this.syncWithOrb();
        } else {
            this.clear();
        }
    }

    private void syncWithOrb() {
        this.clear();

        int[] orbColors = ItemSpectriteOrb.ORB_COLOURS;

        if (this.orbStack.getItem() == ModItems.spectrite_orb) {
            if (ModItems.spectrite_orb.getPotions(orbStack).size() > 1) {
                int orbDamage = orbStack.getItemDamage();
                for (int c = 0; c < orbColors.length; c++) {
                    int colour = orbColors[c];
                    if ((orbDamage & colour) > 0) {
                        this.addItem(new ItemStack(ModItems.spectrite_orb, 1, colour));
                    }
                }
            }
        } else {
            NBTTagCompound tagCompound = orbStack.getSubCompound("OrbEffects");
            if (tagCompound != null) {
                for (int c = 0; c < orbColors.length; c++) {
                    String key = new Integer(c).toString();
                    if (tagCompound.hasKey(key)) {
                        int colour = orbColors[c];
                        if (tagCompound.getBoolean(key)) {
                            this.addItem(new ItemStack(ModItems.spectrite_orb, 1, colour));
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        boolean ret = !stack.isEmpty() && stack.getItem() == ModItems.spectrite_orb
            && this.orbStack != null && ModItems.spectrite_orb.getPotions(stack).size() == 1
            && (this.orbStack.getItemDamage() & stack.getItemDamage()) == 0;

        return ret;
    }

    public ItemStack getOrbStack() {
        return this.orbStack;
    }
}