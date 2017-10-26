package com.samuel.spectrite.potions;

import com.samuel.spectrite.items.ItemSpectriteOrb;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class PotionProsperity extends PotionSpectriteOrb {

    public PotionProsperity() {
        super(new Color(ItemSpectriteOrb.ORB_COLOUR_RGB[2][0], ItemSpectriteOrb.ORB_COLOUR_RGB[2][1],
            ItemSpectriteOrb.ORB_COLOUR_RGB[2][2]).getRGB());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex()
    {
        return 10;
    }

    public int getGuiSortColor(PotionEffect potionEffect)
    {
        return 5;
    }
}
