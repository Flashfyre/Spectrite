package com.samuel.spectrite.init;

import com.google.common.base.Predicate;
import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.enchantments.EnchantmentSpectriteEnhance;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.items.IPerfectSpectriteItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class ModEnchantments {
	
	public static EnumEnchantmentType PERFECT_SPECTRITE = EnumHelper.addEnchantmentType("perfect_spectrite", new Predicate<Item>() {
	   @Override
	   public boolean apply(Item item) {
	        return item instanceof IPerfectSpectriteItem;
	    }
	});
	public static EnchantmentSpectriteEnhance spectrite_enhance;
	
	private static Map<String, IForgeRegistryEntry> registeredEnchantments = new HashMap<String, IForgeRegistryEntry>();

	public static void initEnchantments() {
		(spectrite_enhance = new EnchantmentSpectriteEnhance(Rarity.VERY_RARE,
			PERFECT_SPECTRITE, new EntityEquipmentSlot[] { EntityEquipmentSlot.CHEST, EntityEquipmentSlot.FEET, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.LEGS,
			EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND })).setRegistryName(Spectrite.MOD_ID, "spectrite_enhance");
		
		SpectriteHelper.populateRegisteredObjectsList(registeredEnchantments, spectrite_enhance);
	}
	
	@SubscribeEvent
	public void onRegisterEnchantments(RegistryEvent.Register<Enchantment> event) {
		event.getRegistry().register(spectrite_enhance);
	}
	
	@SubscribeEvent
	public void onMissingMapping(RegistryEvent.MissingMappings<Enchantment> e) {
		for (RegistryEvent.MissingMappings.Mapping<Enchantment> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getResourceDomain())) {
				String resourcePath =  mapping.key.getResourcePath();
				if (registeredEnchantments.containsKey(resourcePath)) {
					mapping.remap((Enchantment) registeredEnchantments.get(resourcePath));
				}
			}
		}
	}
}
