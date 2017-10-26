package com.samuel.spectrite.potions;

import com.samuel.spectrite.items.ItemSpectriteOrb;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class PotionResilience extends PotionSpectriteOrb {

    public PotionResilience() {
        super(new Color(ItemSpectriteOrb.ORB_COLOUR_RGB[6][0], ItemSpectriteOrb.ORB_COLOUR_RGB[6][1],
            ItemSpectriteOrb.ORB_COLOUR_RGB[6][2]).getRGB());
        this.setEffectiveness(0.25D);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth()) {
            entityLivingBaseIn.heal(1.0F);
        }
    }

    @Override
    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    public boolean isReady(int duration, int amplifier) {
        int k = 66 >> amplifier;

        return duration % k == 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex()
    {
        return 14;
    }

    public int getGuiSortColor(PotionEffect potionEffect)
    {
        return 9;
    }
}
