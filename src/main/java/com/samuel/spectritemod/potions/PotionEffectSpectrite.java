package com.samuel.spectritemod.potions;

import com.google.common.collect.ComparisonChain;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionEffectSpectrite extends PotionEffect {

	public PotionEffectSpectrite(Potion potionIn, int durationIn)
    {
        super(potionIn, durationIn, 0);
    }
	
	public PotionEffectSpectrite(Potion potionIn, int durationIn, int amplifierIn)
    {
        super(potionIn, durationIn, amplifierIn, false, true);
    }
	
	public PotionEffectSpectrite(Potion potionIn, int durationIn, int amplifierIn, boolean ambientIn, boolean showParticlesIn)
    {
        super(potionIn, durationIn, amplifierIn, ambientIn, showParticlesIn);
    }

    public PotionEffectSpectrite(PotionEffect other)
    {
        super(other);
    }
	
	@Override
	public int compareTo(PotionEffect p_compareTo_1_)
    {
        int i = 32147;
        return (this.getDuration() <= 32147 || p_compareTo_1_.getDuration() <= 32147) && (!this.getIsAmbient() || !p_compareTo_1_.getIsAmbient()) ? ComparisonChain.start().compare(Boolean.valueOf(this.getIsAmbient()), Boolean.valueOf(p_compareTo_1_.getIsAmbient())).compare(this.getDuration(), p_compareTo_1_.getDuration()).compare(16777215, p_compareTo_1_.getPotion().getLiquidColor()).result() : ComparisonChain.start().compare(Boolean.valueOf(this.getIsAmbient()), Boolean.valueOf(p_compareTo_1_.getIsAmbient())).compare(16777215, p_compareTo_1_.getPotion().getLiquidColor()).result();
    }
}