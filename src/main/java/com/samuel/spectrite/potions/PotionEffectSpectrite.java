package com.samuel.spectrite.potions;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionEffectSpectrite extends PotionEffect {

	public PotionEffectSpectrite(Potion potionIn)
    {
        this(potionIn, 0, 0);
    }
	
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
}