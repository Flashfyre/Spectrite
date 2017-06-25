package com.samuel.spectritemod.world;

import com.samuel.spectritemod.entities.EntitySpectriteGolem;

import net.minecraft.world.biome.Biome;

public class BiomeSpectriteDungeon extends Biome {

	public BiomeSpectriteDungeon(BiomeProperties properties) {
		super(properties);
		
		this.spawnableMonsterList.clear();
		this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpectriteGolem.class, 1, 1, 1));
	}

}
