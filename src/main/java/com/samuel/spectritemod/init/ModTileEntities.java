package com.samuel.spectritemod.init;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.samuel.spectritemod.tileentity.TileEntityMineralChest;

public class ModTileEntities {

	public static Class mineral_chest;

	public static void initTileEntities() {
		GameRegistry.registerTileEntity(
			mineral_chest = TileEntityMineralChest.class,
			"mineral_chest");
	}
}