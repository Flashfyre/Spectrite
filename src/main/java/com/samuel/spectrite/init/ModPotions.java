package com.samuel.spectrite.init;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.potions.*;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class ModPotions {

	public static Potion SPECTRITE;
	public static Potion SPECTRITE_DAMAGE;
	public static Potion SPECTRITE_STRENGTH;
	public static Potion SPECTRITE_RESISTANCE;
	public static Potion BERSERK;
	public static Potion LIGHTWEIGHT;
	public static Potion PROSPERITY;
	public static Potion AGILITY;
	public static Potion AMPHIBIOUS;
	public static Potion ENDURANCE;
	public static Potion RESILIENCE;
	
	public static PotionType SPECTRITE_BASE;
	
	public static PotionType SPECTRITE_DAMAGE_I;
	public static PotionType SPECTRITE_DAMAGE_II;
	public static PotionType SPECTRITE_DAMAGE_III;
	public static PotionType SPECTRITE_DAMAGE_IV;
	public static PotionType SPECTRITE_DAMAGE_V;

	public static PotionType SPECTRITE_STRENGTH_SHORT;
	public static PotionType SPECTRITE_STRENGTH_LONG;
	public static PotionType SPECTRITE_STRENGTH_STRONG;
	
	public static PotionType SPECTRITE_RESISTANCE_SHORT;
	public static PotionType SPECTRITE_RESISTANCE_LONG;
	public static PotionType SPECTRITE_RESISTANCE_STRONG;
	
	private static Map<String, IForgeRegistryEntry> registeredPotions = new HashMap<String, IForgeRegistryEntry>();
	private static Map<String, IForgeRegistryEntry> registeredPotionTypes = new HashMap<String, IForgeRegistryEntry>();

	public static void initPotionEffects() {
		SPECTRITE = new PotionSpectrite().setRegistryName(Spectrite.MOD_ID, "spectrite").setPotionName("effect.spectrite");
		SPECTRITE_DAMAGE = new PotionSpectriteDamage().setRegistryName(Spectrite.MOD_ID, "spectrite_damage").setPotionName("effect.spectrite_damage").setBeneficial();
		SPECTRITE_STRENGTH = new PotionSpectriteStrength().setRegistryName(Spectrite.MOD_ID, "spectrite_strength").setPotionName("effect.spectrite_strength").setBeneficial();
		SPECTRITE_RESISTANCE = new PotionSpectriteResistance().setRegistryName(Spectrite.MOD_ID, "spectrite_resistance").setPotionName("effect.spectrite_resistance").setBeneficial();
		BERSERK = new PotionBerserk().setRegistryName("spectrite_berserk").setPotionName("effect.berserk").setBeneficial()
			.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 0.5D, 1);
		LIGHTWEIGHT = new PotionLightweight().setRegistryName("spectrite_lightweight").setPotionName("effect.lightweight").setBeneficial();
		PROSPERITY = new PotionProsperity().setRegistryName("spectrite_prosperity").setPotionName("effect.prosperity").setBeneficial()
			.registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, "03C3C89D-7037-4B42-869F-B146BCB64D2E", 2.0D, 0);
		AGILITY = new PotionAgility().setRegistryName("spectrite_agility").setPotionName("effect.agility").setBeneficial()
			.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", 0.20000000298023224D, 2)
			.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", 0.10000000149011612D, 2);
		AMPHIBIOUS = new PotionAmphibious().setRegistryName("spectrite_amphibious").setPotionName("effect.amphibious").setBeneficial();
		ENDURANCE = new PotionEndurance().setRegistryName("spectrite_endurance").setPotionName("effect.endurance").setBeneficial();
		RESILIENCE = new PotionResilience().setRegistryName("spectrite_resilience").setPotionName("effect.resilience").setBeneficial();
		
		SPECTRITE_BASE = new PotionType(new PotionEffect(SPECTRITE)).setRegistryName(Spectrite.MOD_ID, "spectrite");
		SPECTRITE_DAMAGE_I = new PotionType(new PotionEffect(SPECTRITE_DAMAGE, 5)).setRegistryName(Spectrite.MOD_ID, "spectrite_damage_1");
		SPECTRITE_DAMAGE_II = new PotionType(new PotionEffect(SPECTRITE_DAMAGE, 5, 1)).setRegistryName(Spectrite.MOD_ID, "spectrite_damage_2");
		SPECTRITE_DAMAGE_III = new PotionType(new PotionEffect(SPECTRITE_DAMAGE, 5, 2)).setRegistryName(Spectrite.MOD_ID, "spectrite_damage_3");
		SPECTRITE_DAMAGE_IV = new PotionType(new PotionEffect(SPECTRITE_DAMAGE, 5, 3)).setRegistryName(Spectrite.MOD_ID, "spectrite_damage_4");
		SPECTRITE_DAMAGE_V = new PotionType(new PotionEffect(SPECTRITE_DAMAGE, 5, 4)).setRegistryName(Spectrite.MOD_ID, "spectrite_damage_5");
		SPECTRITE_STRENGTH_SHORT = new PotionType(new PotionEffect(SPECTRITE_STRENGTH, 3600)).setRegistryName(Spectrite.MOD_ID, "spectrite_strength_short");
		SPECTRITE_STRENGTH_LONG = new PotionType(new PotionEffect(SPECTRITE_STRENGTH, 9600)).setRegistryName(Spectrite.MOD_ID, "spectrite_strength_long");
		SPECTRITE_STRENGTH_STRONG = new PotionType(new PotionEffect(SPECTRITE_STRENGTH, 1800, 1)).setRegistryName(Spectrite.MOD_ID, "spectrite_strength_strong");
		SPECTRITE_RESISTANCE_SHORT = new PotionType(new PotionEffect(SPECTRITE_RESISTANCE, 3600)).setRegistryName(Spectrite.MOD_ID, "spectrite_resistance_short");
		SPECTRITE_RESISTANCE_LONG = new PotionType(new PotionEffect(SPECTRITE_RESISTANCE, 9600)).setRegistryName(Spectrite.MOD_ID, "spectrite_resistance_long");
		SPECTRITE_RESISTANCE_STRONG = new PotionType(new PotionEffect(SPECTRITE_RESISTANCE, 1800, 1)).setRegistryName(Spectrite.MOD_ID, "spectrite_resistance_strong");
	}
	
	@SubscribeEvent
	public void onRegisterPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry().registerAll(SPECTRITE, SPECTRITE_DAMAGE, SPECTRITE_STRENGTH, SPECTRITE_RESISTANCE,
			BERSERK, LIGHTWEIGHT, PROSPERITY, AGILITY, AMPHIBIOUS, ENDURANCE, RESILIENCE);
		
		SpectriteHelper.populateRegisteredObjectsList(registeredPotions, SPECTRITE, SPECTRITE_DAMAGE, SPECTRITE_STRENGTH,
			SPECTRITE_RESISTANCE, BERSERK, LIGHTWEIGHT, PROSPERITY, AGILITY, AMPHIBIOUS, ENDURANCE, RESILIENCE);
	}
	
	@SubscribeEvent
	public void onRegisterPotionTypes(RegistryEvent.Register<PotionType> event) {
		event.getRegistry().registerAll(SPECTRITE_BASE, SPECTRITE_DAMAGE_I, SPECTRITE_DAMAGE_II, SPECTRITE_DAMAGE_III, SPECTRITE_DAMAGE_IV, SPECTRITE_DAMAGE_V,
			SPECTRITE_STRENGTH_SHORT, SPECTRITE_STRENGTH_LONG, SPECTRITE_STRENGTH_STRONG, SPECTRITE_RESISTANCE_SHORT, SPECTRITE_RESISTANCE_LONG, SPECTRITE_RESISTANCE_STRONG);
		
		SpectriteHelper.populateRegisteredObjectsList(registeredPotionTypes, SPECTRITE_BASE, SPECTRITE_DAMAGE_I, SPECTRITE_DAMAGE_II, SPECTRITE_DAMAGE_III, SPECTRITE_DAMAGE_IV,
			SPECTRITE_DAMAGE_V, SPECTRITE_STRENGTH_SHORT, SPECTRITE_STRENGTH_LONG, SPECTRITE_STRENGTH_STRONG, SPECTRITE_RESISTANCE_SHORT, SPECTRITE_RESISTANCE_LONG, SPECTRITE_RESISTANCE_STRONG);
	}
	
	@SubscribeEvent
	public void onMissingPotionMapping(RegistryEvent.MissingMappings<Potion> e) {
		for (RegistryEvent.MissingMappings.Mapping<Potion> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getNamespace())) {
				String resourcePath =  mapping.key.getPath();
				if (registeredPotions.containsKey(resourcePath)) {
					mapping.remap((Potion) registeredPotions.get(resourcePath));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onMissingPotionTypeMapping(RegistryEvent.MissingMappings<PotionType> e) {
		for (RegistryEvent.MissingMappings.Mapping<PotionType> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getNamespace())) {
				String resourcePath =  mapping.key.getPath();
				if (registeredPotionTypes.containsKey(resourcePath)) {
					mapping.remap((PotionType) registeredPotionTypes.get(resourcePath));
				}
			}
		}
	}
}
