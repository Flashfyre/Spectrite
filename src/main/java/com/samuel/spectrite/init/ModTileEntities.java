package com.samuel.spectrite.init;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.tileentity.TileEntityFastUpdatingBeacon;
import com.samuel.spectrite.tileentity.TileEntitySpectriteChest;
import com.samuel.spectrite.tileentity.TileEntitySpectritePortal;
import com.samuel.spectrite.tileentity.TileEntitySpectriteSkull;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {

	public static Class spectrite_chest;
	public static Class spectrite_portal;
	public static Class spectrite_wither_skeleton_skull;
	public static Class fast_updating_beacon;

	public static void initTileEntities() {
		spectrite_chest = TileEntitySpectriteChest.class;
		spectrite_portal = TileEntitySpectritePortal.class;
		spectrite_wither_skeleton_skull = TileEntitySpectriteSkull.class;
		fast_updating_beacon = TileEntityFastUpdatingBeacon.class;
		
		GameRegistry.registerTileEntity(spectrite_chest, "spectrite_chest");
		GameRegistry.registerTileEntity(spectrite_portal,  new ResourceLocation(Spectrite.MOD_ID, "spectrite_portal"));
		GameRegistry.registerTileEntity(spectrite_wither_skeleton_skull,  new ResourceLocation(Spectrite.MOD_ID, "spectrite_skull"));
		GameRegistry.registerTileEntity(fast_updating_beacon,  new ResourceLocation(Spectrite.MOD_ID, "fast_updating_beacon"));
	}
}