package com.samuel.spectritemod.client.renderer;

import com.samuel.spectritemod.client.renderer.tileentity.TileEntitySpectriteChestRenderer;
import com.samuel.spectritemod.client.renderer.tileentity.TileEntitySpectritePortalRenderer;
import com.samuel.spectritemod.tileentity.TileEntitySpectriteChest;
import com.samuel.spectritemod.tileentity.TileEntitySpectritePortal;

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
	}
}
