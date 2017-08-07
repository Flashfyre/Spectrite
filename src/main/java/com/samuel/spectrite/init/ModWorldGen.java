package com.samuel.spectrite.init;

import com.samuel.spectrite.world.WorldGenSpectrite;
import com.samuel.spectrite.world.WorldGenSpectriteDungeon;
import com.samuel.spectrite.world.WorldProviderSpectrite;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModWorldGen {

	public static WorldGenSpectrite spectrite = null;
	public static WorldGenSpectriteDungeon spectriteDungeon = null;
	public static DimensionType SPECTRITE;
	public static final int SPECTRITE_DIM_ID = 1618033989;
	
	public static void initWorldGen() {
		GameRegistry.registerWorldGenerator(spectrite = new WorldGenSpectrite(), 618);
		GameRegistry.registerWorldGenerator(spectriteDungeon = new WorldGenSpectriteDungeon(), 1236);
		
		// This dimension is incomplete and may never be used due to the high lag of a dimension where all its blocks are animated
		//SPECTRITE = DimensionType.register("", "_dimension", SPECTRITE_DIM_ID, WorldProviderSpectrite.class, true);
		//DimensionManager.registerDimension(SPECTRITE_DIM_ID, SPECTRITE);
	}
}