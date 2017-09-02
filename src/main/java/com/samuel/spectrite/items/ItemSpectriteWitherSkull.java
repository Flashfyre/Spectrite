package com.samuel.spectrite.items;

public class ItemSpectriteWitherSkull extends ItemSpectriteSkull implements IPerfectSpectriteItem, ICustomTooltipItem {

    public ItemSpectriteWitherSkull(int skullType) {
        super(skullType);
        if (skullType == 2) {
            this.setMaxDamage(0);
        }
    }
}
