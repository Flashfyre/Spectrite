package com.samuel.spectritemod.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.samuel.spectritemod.SpectriteMod;

public final class ModCrafting {

	public static void initCrafting() {
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.mineralChestIron), "XXX", "X X", "XXX",
			'X', new ItemStack(Items.IRON_INGOT));
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.mineralChestGold), "XXX", "X X", "XXX",
			'X', new ItemStack(Items.GOLD_INGOT));
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.mineralChestDiamond), "XXX", "X X", "XXX",
			'X', new ItemStack(Items.DIAMOND));
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.mineralChestSpectrite), "XXX", "X X", "XXX",
			'X', new ItemStack(ModItems.spectrite_gem));
		GameRegistry.addShapelessRecipe(new ItemStack(
			ModBlocks.mineralChestIronTrapped), new ItemStack(
			ModBlocks.mineralChestIron), new ItemStack(Blocks.TRIPWIRE_HOOK));
		GameRegistry.addShapelessRecipe(new ItemStack(
			ModBlocks.mineralChestGoldTrapped), new ItemStack(
			ModBlocks.mineralChestGold), new ItemStack(Blocks.TRIPWIRE_HOOK));
		GameRegistry.addShapelessRecipe(new ItemStack(
			ModBlocks.mineralChestDiamondTrapped), new ItemStack(
			ModBlocks.mineralChestDiamond), new ItemStack(Blocks.TRIPWIRE_HOOK));
		GameRegistry.addShapelessRecipe(new ItemStack(
			ModBlocks.mineralChestSpectriteTrapped), new ItemStack(
			ModBlocks.mineralChestSpectrite), new ItemStack(Blocks.TRIPWIRE_HOOK));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.diamond_rod), "X", "X",
			'X', new ItemStack(Items.DIAMOND));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_rod), "X", "X",
			'X', new ItemStack(SpectriteMod.ItemSpectriteGem));
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.spectriteBlock), "XXX", "XXX", "XXX",
			'X', new ItemStack(ModItems.spectrite_gem));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_shovel), "X", "Y", "Y",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.diamond_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_shovel), "  X", " Y ", "Y  ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.diamond_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_shovel_special), "X", "Y", "Y",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.spectrite_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_shovel_special), "  X", " Y ", "Y  ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.spectrite_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_axe), "XX ", "XY ", " Y ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.diamond_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_axe), " XX", "XY ", "Y  ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.diamond_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_axe_special), "XX ", "XY ", " Y ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.spectrite_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_axe_special), " XX", "XY ", "Y  ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.spectrite_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_pickaxe), "XXX", " Y ", " Y ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.diamond_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_pickaxe), " XX", " YX", "Y  ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.diamond_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_pickaxe_special), "XXX", " Y ", " Y ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.spectrite_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_pickaxe_special), " XX", " YX", "Y  ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.spectrite_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_sword), " X ", " X ", "YZY",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(Items.DIAMOND),
			'Z', new ItemStack(ModItems.diamond_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_sword), "  X", "YX ", "ZY ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(Items.DIAMOND),
			'Z', new ItemStack(ModItems.diamond_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_sword_special), " X ", " X ", "XYX",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.spectrite_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_sword_special), "  X", "XX ", "YX ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.spectrite_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_helmet), "XXX", "X X",
			'X', new ItemStack(ModItems.spectrite_gem));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_chestplate), "X X", "XXX", "XXX",
			'X', new ItemStack(ModItems.spectrite_gem));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_leggings), "XXX", "X X", "X X",
			'X', new ItemStack(ModItems.spectrite_gem));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_boots), "X X", "X X",
			'X', new ItemStack(ModItems.spectrite_gem));
		
		GameRegistry.addSmelting(ModBlocks.mineralChestIron, new ItemStack(Items.IRON_INGOT, 5), 1.75f);
		GameRegistry.addSmelting(ModBlocks.mineralChestIronTrapped, new ItemStack(Items.IRON_INGOT, 5), 1.75f);
		GameRegistry.addSmelting(ModBlocks.mineralChestGold, new ItemStack(Items.GOLD_INGOT, 5), 2.5f);
		GameRegistry.addSmelting(ModBlocks.mineralChestGoldTrapped, new ItemStack(Items.GOLD_INGOT, 5), 2.5f);
		GameRegistry.addSmelting(ModBlocks.mineralChestDiamond, new ItemStack(Items.DIAMOND, 5), 2.5f);
		GameRegistry.addSmelting(ModBlocks.mineralChestDiamondTrapped, new ItemStack(Items.DIAMOND, 5), 2.5f);
		GameRegistry.addSmelting(ModBlocks.mineralChestSpectrite, new ItemStack(ModItems.spectrite_gem, 5), 5.0f);
		GameRegistry.addSmelting(ModBlocks.mineralChestSpectriteTrapped, new ItemStack(ModItems.spectrite_gem, 5), 5.0f);
		
	}
}
