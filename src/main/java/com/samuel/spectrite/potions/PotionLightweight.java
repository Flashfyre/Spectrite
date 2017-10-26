package com.samuel.spectrite.potions;

import com.samuel.spectrite.items.ItemSpectriteOrb;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class PotionLightweight extends PotionSpectriteOrb {

    public PotionLightweight() {
        super(new Color(ItemSpectriteOrb.ORB_COLOUR_RGB[1][0], ItemSpectriteOrb.ORB_COLOUR_RGB[1][1],
            ItemSpectriteOrb.ORB_COLOUR_RGB[1][2]).getRGB());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex()
    {
        return 9;
    }

    public int getGuiSortColor(PotionEffect potionEffect)
    {
        return 4;
    }
}
