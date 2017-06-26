package com.samuel.spectritemod.init;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.world.BiomeSpectriteDungeon;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class ModBiomes {
	
	public static BiomeSpectriteDungeon spectrite_dungeon;

	public static void initBiomes() {
		spectrite_dungeon = (BiomeSpectriteDungeon) new BiomeSpectriteDungeon(new BiomeProperties("Spectrite Dungeon")).setRegistryName(SpectriteMod.MOD_ID, "spectrite_dungeon");
		
		Biome.REGISTRY.register(219, spectrite_dungeon.getRegistryName(), spectrite_dungeon);
		BiomeManager.addBiome(BiomeType.DESERT, new BiomeEntry(spectrite_dungeon, 0));
	}
}
