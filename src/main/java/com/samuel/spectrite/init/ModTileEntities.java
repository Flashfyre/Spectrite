package com.samuel.spectrite.init;

import com.samuel.spectrite.tileentity.TileEntitySpectriteChest;
import com.samuel.spectrite.tileentity.TileEntitySpectritePortal;

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