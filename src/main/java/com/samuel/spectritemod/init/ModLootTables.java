package com.samuel.spectritemod.init;

import com.samuel.spectritemod.SpectriteMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class ModLootTables {
	
	public static ResourceLocation spectrite_golem;
	public static ResourceLocation spectrite_dungeon_low;
	public static ResourceLocation spectrite_dungeon_mid;
	public static ResourceLocation spectrite_dungeon_high;

	public static void registerLootTables() {
		LootTableList.register(spectrite_golem = new ResourceLocation(
			SpectriteMod.MOD_ID, "entities/spectrite_golem"));
		LootTableList.register(spectrite_dungeon_low = new ResourceLocation(
			SpectriteMod.MOD_ID, "chests/spectrite_dungeon_low"));
		LootTableList.register(spectrite_dungeon_mid = new ResourceLocation(
			SpectriteMod.MOD_ID, "chests/spectrite_dungeon_mid"));
		LootTableList.register(spectrite_dungeon_high = new ResourceLocation(
			SpectriteMod.MOD_ID, "chests/spectrite_dungeon_high"));
	}
}
