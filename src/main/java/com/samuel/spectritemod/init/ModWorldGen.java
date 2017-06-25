package com.samuel.spectritemod.init;

import com.samuel.spectritemod.world.WorldGenSpectrite;
import com.samuel.spectritemod.world.WorldGenSpectriteDungeon;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModWorldGen {

	public static WorldGenSpectrite spectrite = null;
	public static WorldGenSpectriteDungeon spectriteDungeon = null;
	
	public static void initWorldGen() {
		GameRegistry.registerWorldGenerator(spectrite = new WorldGenSpectrite(), 618);
		GameRegistry.registerWorldGenerator(spectriteDungeon = new WorldGenSpectriteDungeon(), 1236);
	}
}