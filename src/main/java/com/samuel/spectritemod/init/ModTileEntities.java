package com.samuel.spectritemod.init;

import com.samuel.spectritemod.tileentity.TileEntitySpectriteChest;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {

	public static Class mineral_chest;

	public static void initTileEntities() {
		GameRegistry.registerTileEntity(
			mineral_chest = TileEntitySpectriteChest.class,
			"mineral_chest");
	}
}