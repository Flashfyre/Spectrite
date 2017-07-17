package com.samuel.spectritemod.init;

import com.google.common.base.Predicate;
import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.enchantments.EnchantmentSpectriteEnhance;
import com.samuel.spectritemod.items.IPerfectSpectriteItem;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModEnchantments {
	
	public static EnumEnchantmentType PERFECT_SPECTRITE = EnumHelper.addEnchantmentType("perfect_spectrite", new Predicate<Item>() {
	   @Override
	   public boolean apply(Item item) {
	        return item instanceof IPerfectSpectriteItem;
	    }
	});
	public static EnchantmentSpectriteEnhance spectrite_enhance;

	public static void initEnchantments() {
		(spectrite_enhance = new EnchantmentSpectriteEnhance(Rarity.VERY_RARE,
			PERFECT_SPECTRITE, new EntityEquipmentSlot[] { EntityEquipmentSlot.CHEST, EntityEquipmentSlot.FEET, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.LEGS,
			EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND })).setRegistryName(SpectriteMod.MOD_ID, "spectrite_enhance");
	}
	
	@SubscribeEvent
	public void onRegisterEnchantments(RegistryEvent.Register<Enchantment> event) {
		event.getRegistry().register(spectrite_enhance);
	}
}
