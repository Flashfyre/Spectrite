package com.samuel.spectrite.potions;

import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModDamageSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShield;
import net.minecraft.potion.PotionHealth;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;

public class PotionSpectriteDamage extends PotionHealth {
	
	public PotionSpectriteDamage() {
		super(true, SpectriteHelper.getCurrentSpectriteColour(1));
	}

	@Override
	public int getLiquidColor()
    {
        return SpectriteHelper.getCurrentSpectriteColour(1);
    }
	
	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
    {
		amplifier = SpectriteHelper.getSpectriteDamageAmplifierAfterResistance(amplifier, entityLivingBaseIn);
		
		if (amplifier >= 0) {	
			if (entityLivingBaseIn instanceof EntityPlayer) {
				amplifier -= SpectriteHelper.getPlayerReceivedSpectriteDamageDecreaseForDifficulty(entityLivingBaseIn.getEntityWorld().getDifficulty());
			}
			
			amplifier = damageShield(ModDamageSources.SPECTRITE_DAMAGE, entityLivingBaseIn, amplifier);
			
			if (amplifier >= 0) {
				entityLivingBaseIn.attackEntityFrom(ModDamageSources.SPECTRITE_DAMAGE, 6 << amplifier);
			}
		}
    }

	@Override
    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health)
    {
		amplifier = SpectriteHelper.getSpectriteDamageAmplifierAfterResistance(amplifier, entityLivingBaseIn);
		
		if (amplifier >= 0) {
			if (entityLivingBaseIn instanceof EntityPlayer) {
				amplifier -= SpectriteHelper.getPlayerReceivedSpectriteDamageDecreaseForDifficulty(entityLivingBaseIn.getEntityWorld().getDifficulty());
			}
			
				amplifier = damageShield(ModDamageSources.SPECTRITE_DAMAGE, entityLivingBaseIn, amplifier);
			
			if (amplifier >= 0) {
	            int j = (int)(health * (6 << amplifier) + 0.5D);
	
	            if (source == null)
	            {
	                entityLivingBaseIn.attackEntityFrom(ModDamageSources.SPECTRITE_DAMAGE, j);
	            }
	            else
	            {
	                entityLivingBaseIn.attackEntityFrom(ModDamageSources.causeIndirectSpectriteDamage(source, indirectSource), j);
	            }
			}
		}
    }
	
	private int damageShield(DamageSource source, EntityLivingBase target, int amplifier) {
		if (!target.getActiveItemStack().isEmpty() && target.isActiveItemStackBlocking() 
			&& target.getActiveItemStack().getItem() instanceof ItemShield) {
			int shieldTier = SpectriteHelper.getSpectriteShieldTier(target.getActiveItemStack());
			int damageLevel = amplifier - shieldTier;
			if (damageLevel >= 0) {
				amplifier -= shieldTier + 1;
				int shieldDamage = 4 << (damageLevel << 1);
				if (target instanceof EntityPlayer) {
					SpectriteHelper.damageShield((EntityPlayer) target, shieldDamage);
					target.world.setEntityState(target, (byte) 29);
				}
			} else {
				amplifier = -1;
			}
		}
		
		return amplifier;
	}
}
