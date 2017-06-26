package com.samuel.spectritemod.init;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.world.BiomeSpectriteDungeon;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModBiomes {
	
	public static BiomeSpectriteDungeon spectrite_dungeon;

	public static void initBiomes() {
		spectrite_dungeon = (BiomeSpectriteDungeon) new BiomeSpectriteDungeon(new BiomeProperties("Spectrite Dungeon")).setRegistryName(SpectriteMod.MOD_ID, "spectrite_dungeon");
	}
	
	@SubscribeEvent
	public void onRegisterBiomes(RegistryEvent.Register<Biome> event) {
		event.getRegistry().register(spectrite_dungeon);
		BiomeManager.addBiome(BiomeType.DESERT, new BiomeEntry(spectrite_dungeon, 0));
	}
}
