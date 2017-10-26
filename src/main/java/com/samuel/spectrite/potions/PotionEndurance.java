package com.samuel.spectrite.potions;

import com.samuel.spectrite.items.ItemSpectriteOrb;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class PotionEndurance extends PotionSpectriteOrb {

    public PotionEndurance() {
        super(new Color(ItemSpectriteOrb.ORB_COLOUR_RGB[5][0], ItemSpectriteOrb.ORB_COLOUR_RGB[5][1],
            ItemSpectriteOrb.ORB_COLOUR_RGB[5][2]).getRGB());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex()
    {
        return 13;
    }

    public int getGuiSortColor(PotionEffect potionEffect)
    {
        return 8;
    }
}
