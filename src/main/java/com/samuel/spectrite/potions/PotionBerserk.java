package com.samuel.spectrite.potions;

import com.samuel.spectrite.items.ItemSpectriteOrb;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class PotionBerserk extends PotionSpectriteOrb {

    public PotionBerserk() {
        super(new Color(ItemSpectriteOrb.ORB_COLOUR_RGB[0][0], ItemSpectriteOrb.ORB_COLOUR_RGB[0][1],
            ItemSpectriteOrb.ORB_COLOUR_RGB[0][2]).getRGB());
    }

    /*public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier)
    {
        return 3.0D * (amplifier + 1);
    }*/

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex()
    {
        return 8;
    }

    public int getGuiSortColor(PotionEffect potionEffect)
    {
        return 3;
    }
}
