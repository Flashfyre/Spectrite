package com.samuel.spectrite.init;

import java.util.HashMap;
import java.util.Map;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.potions.PotionSpectrite;
import com.samuel.spectrite.potions.PotionSpectriteDamage;
import com.samuel.spectrite.potions.PotionSpectriteResistance;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModPotions {
	
	public static Potion SPECTRITE;
	public static Potion SPECTRITE_DAMAGE;
	public static Potion SPECTRITE_RESISTANCE;
	
	public static PotionType SPECTRITE_BASE;
	
	public static PotionType SPECTRITE_DAMAGE_I;
	public static PotionType SPECTRITE_DAMAGE_II;
	public static PotionType SPECTRITE_DAMAGE_III;
	public static PotionType SPECTRITE_DAMAGE_IV;
	public static PotionType SPECTRITE_DAMAGE_V;
	
	public static PotionType SPECTRITE_RESISTANCE_SHORT;
	public static PotionType SPECTRITE_RESISTANCE_LONG;
	public static PotionType SPECTRITE_RESISTANCE_STRONG;
	
	private static Map<String, IForgeRegistryEntry> registeredPotions = new HashMap<String, IForgeRegistryEntry>();
	private static Map<String, IForgeRegistryEntry> registeredPotionTypes = new HashMap<String, IForgeRegistryEntry>();

	public static void initPotionEffects() {
		SPECTRITE = new PotionSpectrite().setRegistryName(Spectrite.MOD_ID, "spectrite").setPotionName("effect.spectrite");
		SPECTRITE_DAMAGE = new PotionSpectriteDamage().setRegistryName(Spectrite.MOD_ID, "spectrite_damage").setPotionName("effect.spectrite_damage").setBeneficial();
		SPECTRITE_RESISTANCE = new PotionSpectriteResistance().setRegistryName(Spectrite.MOD_ID, "spectrite_resistance").setPotionName("effect.spectrite_resistance").setBeneficial();
		
		SPECTRITE_BASE = new PotionType(new PotionEffect(SPECTRITE)).setRegistryName(Spectrite.MOD_ID, "spectrite");
		SPECTRITE_DAMAGE_I = new PotionType(new PotionEffect(SPECTRITE_DAMAGE, 5)).setRegistryName(Spectrite.MOD_ID, "spectrite_damage_1");
		SPECTRITE_DAMAGE_II = new PotionType(new PotionEffect(SPECTRITE_DAMAGE, 5, 1)).setRegistryName(Spectrite.MOD_ID, "spectrite_damage_2");
		SPECTRITE_DAMAGE_III = new PotionType(new PotionEffect(SPECTRITE_DAMAGE, 5, 2)).setRegistryName(Spectrite.MOD_ID, "spectrite_damage_3");
		SPECTRITE_DAMAGE_IV = new PotionType(new PotionEffect(SPECTRITE_DAMAGE, 5, 3)).setRegistryName(Spectrite.MOD_ID, "spectrite_damage_4");
		SPECTRITE_DAMAGE_V = new PotionType(new PotionEffect(SPECTRITE_DAMAGE, 5, 4)).setRegistryName(Spectrite.MOD_ID, "spectrite_damage_5");
		SPECTRITE_RESISTANCE_SHORT = new PotionType(new PotionEffect(SPECTRITE_RESISTANCE, 3600)).setRegistryName(Spectrite.MOD_ID, "spectrite_resistance_short");
		SPECTRITE_RESISTANCE_LONG = new PotionType(new PotionEffect(SPECTRITE_RESISTANCE, 9600)).setRegistryName(Spectrite.MOD_ID, "spectrite_resistance_long");
		SPECTRITE_RESISTANCE_STRONG = new PotionType(new PotionEffect(SPECTRITE_RESISTANCE, 1800, 1)).setRegistryName(Spectrite.MOD_ID, "spectrite_resistance_strong");
	}
	
	@SubscribeEvent
	public void onRegisterPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry().registerAll(SPECTRITE, SPECTRITE_DAMAGE, SPECTRITE_RESISTANCE);
		
		SpectriteHelper.populateRegisteredObjectsList(registeredPotions, SPECTRITE, SPECTRITE_DAMAGE, SPECTRITE_RESISTANCE);
	}
	
	@SubscribeEvent
	public void onRegisterPotionTypes(RegistryEvent.Register<PotionType> event) {
		event.getRegistry().registerAll(SPECTRITE_BASE, SPECTRITE_DAMAGE_I, SPECTRITE_DAMAGE_II, SPECTRITE_DAMAGE_III, SPECTRITE_DAMAGE_IV, SPECTRITE_DAMAGE_V,
			SPECTRITE_RESISTANCE_SHORT, SPECTRITE_RESISTANCE_LONG, SPECTRITE_RESISTANCE_STRONG);
		
		SpectriteHelper.populateRegisteredObjectsList(registeredPotionTypes, SPECTRITE_BASE, SPECTRITE_DAMAGE_I, SPECTRITE_DAMAGE_II, SPECTRITE_DAMAGE_III, SPECTRITE_DAMAGE_IV,
			SPECTRITE_DAMAGE_V, SPECTRITE_RESISTANCE_SHORT, SPECTRITE_RESISTANCE_LONG, SPECTRITE_RESISTANCE_STRONG);
	}
	
	@SubscribeEvent
	public void onMissingPotionMapping(RegistryEvent.MissingMappings<Potion> e) {
		for (RegistryEvent.MissingMappings.Mapping<Potion> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getResourceDomain())) {
				String resourcePath =  mapping.key.getResourcePath();
				if (registeredPotions.containsKey(resourcePath)) {
					mapping.remap((Potion) registeredPotions.get(resourcePath));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onMissingPotionTypeMapping(RegistryEvent.MissingMappings<PotionType> e) {
		for (RegistryEvent.MissingMappings.Mapping<PotionType> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getResourceDomain())) {
				String resourcePath =  mapping.key.getResourcePath();
				if (registeredPotionTypes.containsKey(resourcePath)) {
					mapping.remap((PotionType) registeredPotionTypes.get(resourcePath));
				}
			}
		}
	}
}
