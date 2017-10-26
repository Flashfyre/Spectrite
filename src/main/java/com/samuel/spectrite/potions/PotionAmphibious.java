package com.samuel.spectrite.potions;

import com.samuel.spectrite.items.ItemSpectriteOrb;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class PotionAmphibious extends PotionSpectriteOrb {

    public PotionAmphibious() {
        super(new Color(ItemSpectriteOrb.ORB_COLOUR_RGB[4][0], ItemSpectriteOrb.ORB_COLOUR_RGB[4][1],
            ItemSpectriteOrb.ORB_COLOUR_RGB[4][2]).getRGB());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex()
    {
        return 12;
    }

    public int getGuiSortColor(PotionEffect potionEffect)
    {
        return 7;
    }
}
