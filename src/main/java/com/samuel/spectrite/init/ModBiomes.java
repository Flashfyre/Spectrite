package com.samuel.spectrite.init;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.world.BiomeSpectriteDungeon;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class ModBiomes {
	
	public static BiomeSpectriteDungeon spectrite_dungeon;
	
	private static Map<String, IForgeRegistryEntry> registeredBiomes = new HashMap<String, IForgeRegistryEntry>();

	public static void initBiomes() {
		spectrite_dungeon = (BiomeSpectriteDungeon) new BiomeSpectriteDungeon(new BiomeProperties("Spectrite Dungeon")).setRegistryName(Spectrite.MOD_ID, "spectrite_dungeon");
	}
	
	@SubscribeEvent
	public void onRegisterBiomes(RegistryEvent.Register<Biome> event) {
		BiomeManager.addBiome(BiomeType.DESERT, new BiomeEntry(spectrite_dungeon, 0));
		event.getRegistry().register(spectrite_dungeon);
		BiomeDictionary.addTypes(spectrite_dungeon, BiomeDictionary.Type.HOT);
		
		SpectriteHelper.populateRegisteredObjectsList(registeredBiomes, spectrite_dungeon);
	}
	
	@SubscribeEvent
	public void onMissingMapping(RegistryEvent.MissingMappings<Biome> e) {
		for (RegistryEvent.MissingMappings.Mapping<Biome> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getResourceDomain())) {
				String resourcePath =  mapping.key.getResourcePath();
				if (registeredBiomes.containsKey(resourcePath)) {
					mapping.remap((Biome) registeredBiomes.get(resourcePath));
				}
			}
		}
	}
}
