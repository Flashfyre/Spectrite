package com.samuel.spectrite.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModCrafting {

	public static void initCrafting() {
		GameRegistry.addSmelting(ModBlocks.spectrite_chest, new ItemStack(ModBlocks.spectrite_block, 5), 5.0f);
		GameRegistry.addSmelting(ModBlocks.spectrite_chest_trapped, new ItemStack(ModBlocks.spectrite_block, 5), 5.0f);
	}
}
