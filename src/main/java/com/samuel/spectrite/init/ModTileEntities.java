package com.samuel.spectrite.init;

import com.samuel.spectrite.tileentity.TileEntityFastUpdatingBeacon;
import com.samuel.spectrite.tileentity.TileEntitySpectriteChest;
import com.samuel.spectrite.tileentity.TileEntitySpectritePortal;
import com.samuel.spectrite.tileentity.TileEntitySpectriteWitherSkeletonSkull;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {

	public static Class spectrite_chest;
	public static Class spectrite_portal;
	public static Class spectrite_wither_skeleton_skull;
	public static Class fast_updating_beacon;

	public static void initTileEntities() {
		spectrite_chest = TileEntitySpectriteChest.class;
		spectrite_portal = TileEntitySpectritePortal.class;
		spectrite_wither_skeleton_skull = TileEntitySpectriteWitherSkeletonSkull.class;
		fast_updating_beacon = TileEntityFastUpdatingBeacon.class;
		
		GameRegistry.registerTileEntity(spectrite_chest, "spectrite_chest");
		GameRegistry.registerTileEntity(spectrite_portal, "spectrite_portal");
		GameRegistry.registerTileEntity(spectrite_wither_skeleton_skull, "spectrite_wither_skeleton_skull");
		GameRegistry.registerTileEntity(fast_updating_beacon, "fast_updating_beacon");
	}
}