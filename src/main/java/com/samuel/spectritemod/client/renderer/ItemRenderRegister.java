package com.samuel.spectritemod.client.renderer;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.init.ModItems;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public final class ItemRenderRegister {

	public static String modid = SpectriteMod.MOD_ID;

	public static void registerItemRenderer() {
		reg(ModItems.diamond_rod);
		reg(ModItems.spectrite_rod);
		reg(ModItems.spectrite_brick);
		reg(ModItems.spectrite_gem);
		reg(ModItems.spectrite_orb);
		reg(ModItems.spectrite_shovel);
		reg(ModItems.spectrite_shovel_special);
		reg(ModItems.spectrite_pickaxe);
		reg(ModItems.spectrite_pickaxe_special);
		reg(ModItems.spectrite_axe);
		reg(ModItems.spectrite_axe_special);
		reg(ModItems.spectrite_sword);
		reg(ModItems.spectrite_sword_special);
		reg(ModItems.spectrite_sword_2);
		reg(ModItems.spectrite_sword_2_special);
		reg(ModItems.spectrite_arrow);
		reg(ModItems.spectrite_bow);
		reg(ModItems.spectrite_bow_special);
		reg(ModItems.spectrite_shield);
		reg(ModItems.spectrite_shield_special);
		reg(ModItems.spectrite_helmet);
		reg(ModItems.spectrite_chestplate);
		reg(ModItems.spectrite_leggings);
		reg(ModItems.spectrite_boots);
		reg(ModItems.spectrite_compass);
	}

	public static void reg(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
			new ModelResourceLocation(modid + ":" +
			item.getUnlocalizedName().substring(5), "inventory"));
	}

	public static void reg(Item item, int meta) {
		ModelLoader.setCustomModelResourceLocation(item,
			meta, new ModelResourceLocation(modid
				+ ":"
				+ item.getUnlocalizedName(
					new ItemStack(item, 1, meta))
					.substring(5), "inventory"));
	}

}