package com.samuel.spectrite.client.renderer;

import com.samuel.spectrite.client.renderer.tileentity.TileEntitySpectriteChestRenderer;
import com.samuel.spectrite.client.renderer.tileentity.TileEntitySpectritePortalRenderer;
import com.samuel.spectrite.client.renderer.tileentity.TileEntitySpectriteWitherSkeletonSkullRenderer;
import com.samuel.spectrite.tileentity.TileEntityFastUpdatingBeacon;
import com.samuel.spectrite.tileentity.TileEntitySpectriteChest;
import com.samuel.spectrite.tileentity.TileEntitySpectritePortal;
import com.samuel.spectrite.tileentity.TileEntitySpectriteWitherSkeletonSkull;

import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TileEntityRenderRegister {

	@SubscribeEvent
	public void onRegisterTESRs(ModelRegistryEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(
			TileEntitySpectriteChest.class,
			new TileEntitySpectriteChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(
			TileEntitySpectritePortal.class,
			new TileEntitySpectritePortalRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(
			TileEntitySpectriteWitherSkeletonSkull.class,
			new TileEntitySpectriteWitherSkeletonSkullRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(
			TileEntityFastUpdatingBeacon.class,
			new TileEntityBeaconRenderer());
	}
}
