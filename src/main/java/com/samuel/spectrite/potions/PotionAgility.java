package com.samuel.spectrite.potions;

import com.samuel.spectrite.items.ItemSpectriteOrb;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class PotionAgility extends PotionSpectriteOrb {

    public PotionAgility() {
        super(new Color(ItemSpectriteOrb.ORB_COLOUR_RGB[3][0], ItemSpectriteOrb.ORB_COLOUR_RGB[3][1],
            ItemSpectriteOrb.ORB_COLOUR_RGB[3][2]).getRGB());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex()
    {
        return 11;
    }

    public int getGuiSortColor(PotionEffect potionEffect)
    {
        return 6;
    }
}
