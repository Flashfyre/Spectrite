package com.samuel.spectritemod.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModCrafting {

	public static void initCrafting() {
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.spectrite_chest), "XXX", "XYX", "XXX",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(Items.DIAMOND));
		GameRegistry.addShapelessRecipe(new ItemStack(
			ModBlocks.spectrite_chest_trapped), new ItemStack(
			ModBlocks.spectrite_chest), new ItemStack(Blocks.TRIPWIRE_HOOK));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.diamond_rod), "X", "X",
			'X', new ItemStack(Items.DIAMOND));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_rod), "X", "X",
			'X', new ItemStack(ModItems.spectrite_gem));
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.spectrite_block), "XXX", "XXX", "XXX",
			'X', new ItemStack(ModItems.spectrite_gem));
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.spectrite_bricks), "XX", "XX",
			'X', new ItemStack(ModItems.spectrite_brick));
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.spectrite_bricks), "X", "X",
			'X', new ItemStack(ModBlocks.spectrite_brick_slab_half));
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.spectrite_brick_stairs, 4), "  X", " XX", "XXX",
			'X', new ItemStack(ModBlocks.spectrite_bricks));
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.spectrite_brick_slab_half, 6), "XXX",
			'X', new ItemStack(ModBlocks.spectrite_bricks));
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.diamond_ladder, 28), "X X", "XXX", "X X",
			'X', new ItemStack(ModItems.diamond_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModBlocks.spectrite_ladder, 28), "X X", "XXX", "X X",
			'X', new ItemStack(ModItems.spectrite_rod));
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
			ModItems.spectrite_arrow), " X ", " Y ", " Z ",
			'X', new ItemStack(ModItems.spectrite_gem), 'Y', new ItemStack(ModItems.spectrite_rod),
			'Z', new ItemStack(Items.FEATHER));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_bow), " XY", "Z Y", " XY",
			'X', new ItemStack(ModItems.diamond_rod), 'Y', new ItemStack(Items.STRING),
			'Z', new ItemStack(ModItems.spectrite_rod));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_bow_special), " XY", "X Y", " XY",
			'X', new ItemStack(ModItems.spectrite_rod), 'Y', new ItemStack(Items.STRING));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_shield), "XXX", "XYX", " X ",
			'X', new ItemStack(Items.DIAMOND),
			'Y', new ItemStack(Item.getItemFromBlock(ModBlocks.spectrite_block)));
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_shield_special), "XXX", "XYX", " X ",
			'X', new ItemStack(ModItems.spectrite_gem),
			'Y', new ItemStack(Item.getItemFromBlock(ModBlocks.spectrite_block)));
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
		GameRegistry.addRecipe(new ItemStack(
			ModItems.spectrite_compass), "XXX", "XYX", "XXX",
			'X', new ItemStack(ModItems.spectrite_gem),
			'Y', new ItemStack(Items.COMPASS));
		
		GameRegistry.addSmelting(ModBlocks.spectrite_chest, new ItemStack(ModItems.spectrite_gem, 5), 5.0f);
		GameRegistry.addSmelting(ModBlocks.spectrite_chest_trapped, new ItemStack(ModItems.spectrite_gem, 5), 5.0f);
		
	}
}
