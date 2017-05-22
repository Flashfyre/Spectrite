package com.samuel.spectritemod.init;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.samuel.spectritemod.tileentity.TileEntitySpectriteChest;

public class ModTileEntities {

	public static Class mineral_chest;

	public static void initTileEntities() {
		GameRegistry.registerTileEntity(
			mineral_chest = TileEntitySpectriteChest.class,
			"mineral_chest");
	}
}