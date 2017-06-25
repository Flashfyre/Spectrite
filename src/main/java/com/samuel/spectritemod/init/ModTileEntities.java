package com.samuel.spectritemod.init;

import com.samuel.spectritemod.tileentity.TileEntitySpectriteChest;
import com.samuel.spectritemod.tileentity.TileEntitySpectritePortal;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {

	public static Class spectrite_chest;
	public static Class spectrite_portal;

	public static void initTileEntities() {
		spectrite_chest = TileEntitySpectriteChest.class;
		spectrite_portal = TileEntitySpectritePortal.class;
		
		GameRegistry.registerTileEntity(spectrite_chest, "spectrite_chest");
		GameRegistry.registerTileEntity(spectrite_portal, "spectrite_portal");
	}
}