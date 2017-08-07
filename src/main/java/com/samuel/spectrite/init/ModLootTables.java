package com.samuel.spectrite.init;

import com.samuel.spectrite.Spectrite;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class ModLootTables {
	
	public static ResourceLocation spectrite_golem;
	public static ResourceLocation spectrite_dungeon_low;
	public static ResourceLocation spectrite_dungeon_mid;
	public static ResourceLocation spectrite_dungeon_high;

	public static void registerLootTables() {
		LootTableList.register(spectrite_golem = new ResourceLocation(
			Spectrite.MOD_ID, "entities/spectrite_golem"));
		LootTableList.register(spectrite_dungeon_low = new ResourceLocation(
			Spectrite.MOD_ID, "chests/spectrite_dungeon_low"));
		LootTableList.register(spectrite_dungeon_mid = new ResourceLocation(
			Spectrite.MOD_ID, "chests/spectrite_dungeon_mid"));
		LootTableList.register(spectrite_dungeon_high = new ResourceLocation(
			Spectrite.MOD_ID, "chests/spectrite_dungeon_high"));
	}
}
