package com.samuel.spectrite.init;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModCrafting {

	public static void initSmelting() {
		GameRegistry.addSmelting(ModBlocks.spectrite_ore, new ItemStack(ModItems.spectrite_gem), 2.5f);
		GameRegistry.addSmelting(ModBlocks.spectrite_chest, new ItemStack(ModBlocks.spectrite_block, 5), 25.0f);
		GameRegistry.addSmelting(ModBlocks.spectrite_chest_trapped, new ItemStack(ModBlocks.spectrite_block, 5), 25.0f);
		GameRegistry.addSmelting(ModBlocks.spectrite_sand, new ItemStack(ModBlocks.spectrite_glass), 2.5f);
	}
	
	public static void initBrewing() {
		initBrewingRecipe(PotionTypes.WATER, ModItems.spectrite_blaze_powder, ModPotions.SPECTRITE_BASE);
		initBrewingRecipe(ModPotions.SPECTRITE_BASE, ModItems.spectrite_brick, ModPotions.SPECTRITE_RESISTANCE_SHORT);
		initBrewingRecipe(ModPotions.SPECTRITE_RESISTANCE_SHORT, Items.REDSTONE, ModPotions.SPECTRITE_RESISTANCE_LONG);
		initBrewingRecipe(ModPotions.SPECTRITE_RESISTANCE_SHORT, Items.GLOWSTONE_DUST, ModPotions.SPECTRITE_RESISTANCE_STRONG);
		initBrewingRecipe(ModPotions.SPECTRITE_BASE, ModItems.spectrite_dust, ModPotions.SPECTRITE_DAMAGE_I);
		initBrewingRecipe(ModPotions.SPECTRITE_DAMAGE_I, ModItems.spectrite_dust, ModPotions.SPECTRITE_DAMAGE_II);
		initBrewingRecipe(ModPotions.SPECTRITE_DAMAGE_II, ModItems.spectrite_dust, ModPotions.SPECTRITE_DAMAGE_III);
		initBrewingRecipe(ModPotions.SPECTRITE_DAMAGE_III, ModItems.spectrite_dust, ModPotions.SPECTRITE_DAMAGE_IV);
		initBrewingRecipe(ModPotions.SPECTRITE_DAMAGE_IV, ModItems.spectrite_dust, ModPotions.SPECTRITE_DAMAGE_V);
	}
	
	private static void initBrewingRecipe(PotionType basePotionType, Item ingredient, PotionType outputPotionType) {
		PotionHelper.addMix(basePotionType, ingredient, outputPotionType);
	}
}
