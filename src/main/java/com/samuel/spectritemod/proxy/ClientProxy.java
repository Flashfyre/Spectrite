package com.samuel.spectritemod.proxy;

import java.util.HashMap;
import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.client.eventhandlers.SpectriteClientEventHandler;
import com.samuel.spectritemod.client.model.ModelMoltenSpectrite;
import com.samuel.spectritemod.client.renderer.BlockRenderRegister;
import com.samuel.spectritemod.client.renderer.EntityRenderRegister;
import com.samuel.spectritemod.client.renderer.ItemRenderRegister;
import com.samuel.spectritemod.client.renderer.TileEntityRenderRegister;
import com.samuel.spectritemod.client.renderer.tileentity.TileEntitySpectriteChestRenderer;
import com.samuel.spectritemod.client.renderer.tileentity.TileEntitySpectritePortalRenderer;
import com.samuel.spectritemod.init.ModBlocks;
import com.samuel.spectritemod.init.ModItems;
import com.samuel.spectritemod.tileentity.TileEntitySpectriteChest;
import com.samuel.spectritemod.tileentity.TileEntitySpectritePortal;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		
		ModelLoaderRegistry.registerLoader(ModelMoltenSpectrite.FluidLoader.INSTANCE);
		
		SpectriteMod.Config.propSpectriteCountSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMinSizeSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMaxSizeSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMinYSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMaxYSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteCountNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMinSizeNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMaxSizeNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMinYNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMaxYNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteCountEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMinSizeEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMaxSizeEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMinYEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteMod.Config.propSpectriteMaxYEnd.setConfigEntryClass(NumberSliderEntry.class);
		
		MinecraftForge.EVENT_BUS
			.register(new BlockRenderRegister());
		MinecraftForge.EVENT_BUS
			.register(new ItemRenderRegister());
		MinecraftForge.EVENT_BUS
			.register(new EntityRenderRegister());
		MinecraftForge.EVENT_BUS
			.register(new TileEntityRenderRegister());
		MinecraftForge.EVENT_BUS
			.register(new SpectriteClientEventHandler());
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}
}
