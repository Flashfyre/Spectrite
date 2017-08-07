package com.samuel.spectrite.world;

import com.samuel.spectrite.entities.EntitySpectriteCreeper;
import com.samuel.spectrite.entities.EntitySpectriteGolem;
import com.samuel.spectrite.entities.EntitySpectriteSkeleton;
import com.samuel.spectrite.entities.EntitySpectriteWitherSkeleton;

import net.minecraft.world.biome.Biome;

public class BiomeSpectriteDungeon extends Biome {

	public BiomeSpectriteDungeon(BiomeProperties properties) {
		super(properties);
		
		this.spawnableMonsterList.clear();
		this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpectriteGolem.class, 6, 1, 1));
		this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpectriteCreeper.class, 4, 1, 1));
		this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpectriteSkeleton.class, 12, 1, 1));
		this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpectriteWitherSkeleton.class, 3, 1, 1));
	}

}
