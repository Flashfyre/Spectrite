package com.samuel.spectritemod.init;

import com.samuel.spectritemod.SpectriteMod;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModCrafting {

	public static void initCrafting() {
		GameRegistry.addSmelting(ModBlocks.mineral_chest_spectrite, new ItemStack(ModItems.spectrite_gem, 5), 5.0f);
		GameRegistry.addSmelting(ModBlocks.mineral_chest_spectrite_trapped, new ItemStack(ModItems.spectrite_gem, 5), 5.0f);
		
	}
}
