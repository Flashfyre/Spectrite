package com.samuel.spectritemod.init;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.potions.PotionEffectSpectrite;
import com.samuel.spectritemod.potions.PotionSpectriteDamage;
import com.samuel.spectritemod.potions.PotionSpectriteResistance;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModPotions {
	
	public static Potion SPECTRITE_DAMAGE;
	public static Potion SPECTRITE_RESISTANCE;
	
	public static PotionType SPECTRITE_DAMAGE_I;
	public static PotionType SPECTRITE_DAMAGE_II;
	public static PotionType SPECTRITE_DAMAGE_III;
	public static PotionType SPECTRITE_DAMAGE_IV;
	public static PotionType SPECTRITE_DAMAGE_V;
	
	public static PotionType SPECTRITE_RESISTANCE_SHORT;
	public static PotionType SPECTRITE_RESISTANCE_LONG;
	public static PotionType SPECTRITE_RESISTANCE_STRONG;

	public static void initPotionEffects() {
		SPECTRITE_DAMAGE = new PotionSpectriteDamage().setRegistryName(SpectriteMod.MOD_ID, "spectrite_damage").setPotionName("effect.spectrite_damage").setBeneficial();
		SPECTRITE_RESISTANCE = new PotionSpectriteResistance().setRegistryName(SpectriteMod.MOD_ID, "spectrite_resistance").setPotionName("effect.spectrite_resistance").setBeneficial();
		
		SPECTRITE_DAMAGE_I = new PotionType(new PotionEffectSpectrite(SPECTRITE_DAMAGE, 5)).setRegistryName(SpectriteMod.MOD_ID, "spectrite_damage_1");
		SPECTRITE_DAMAGE_II = new PotionType(new PotionEffectSpectrite(SPECTRITE_DAMAGE, 5, 1)).setRegistryName(SpectriteMod.MOD_ID, "spectrite_damage_2");
		SPECTRITE_DAMAGE_III = new PotionType(new PotionEffectSpectrite(SPECTRITE_DAMAGE, 5, 2)).setRegistryName(SpectriteMod.MOD_ID, "spectrite_damage_3");
		SPECTRITE_DAMAGE_IV = new PotionType(new PotionEffectSpectrite(SPECTRITE_DAMAGE, 5, 3)).setRegistryName(SpectriteMod.MOD_ID, "spectrite_damage_4");
		SPECTRITE_DAMAGE_V = new PotionType(new PotionEffectSpectrite(SPECTRITE_DAMAGE, 5, 4)).setRegistryName(SpectriteMod.MOD_ID, "spectrite_damage_5");
		SPECTRITE_RESISTANCE_SHORT = new PotionType(new PotionEffectSpectrite(SPECTRITE_RESISTANCE, 3600)).setRegistryName(SpectriteMod.MOD_ID, "spectrite_resistance_short");
		SPECTRITE_RESISTANCE_LONG = new PotionType(new PotionEffectSpectrite(SPECTRITE_RESISTANCE, 9600)).setRegistryName(SpectriteMod.MOD_ID, "spectrite_resistance_long");
		SPECTRITE_RESISTANCE_STRONG = new PotionType(new PotionEffectSpectrite(SPECTRITE_RESISTANCE, 1800, 1)).setRegistryName(SpectriteMod.MOD_ID, "spectrite_resistance_strong");
	}
	
	@SubscribeEvent
	public void onRegisterPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry().registerAll(SPECTRITE_DAMAGE, SPECTRITE_RESISTANCE);
	}
	
	@SubscribeEvent
	public void onRegisterPotionTypes(RegistryEvent.Register<PotionType> event) {
		event.getRegistry().registerAll(SPECTRITE_DAMAGE_I, SPECTRITE_DAMAGE_II, SPECTRITE_DAMAGE_III, SPECTRITE_DAMAGE_IV, SPECTRITE_DAMAGE_V,
			SPECTRITE_RESISTANCE_SHORT, SPECTRITE_RESISTANCE_LONG, SPECTRITE_RESISTANCE_STRONG);
	}
}
