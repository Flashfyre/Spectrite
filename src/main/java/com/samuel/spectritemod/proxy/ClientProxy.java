package com.samuel.spectritemod.proxy;

import java.util.HashMap;
import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.client.eventhandlers.SpectriteClientEventHandler;
import com.samuel.spectritemod.client.model.ModelMoltenSpectrite;
import com.samuel.spectritemod.client.renderer.BlockRenderRegister;
import com.samuel.spectritemod.client.renderer.EntityRenderRegister;
import com.samuel.spectritemod.client.renderer.ItemRenderRegister;
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
		
		EntityRenderRegister.registerEntityRenderer();
		ItemRenderRegister.registerItemRenderer();
		
		ModelLoaderRegistry.registerLoader(ModelMoltenSpectrite.FluidLoader.INSTANCE);
		
		IStateMapper builtInStateMapper = blockIn -> new HashMap();
		
		ModelLoader.setCustomModelResourceLocation(Item
			.getItemFromBlock(ModBlocks.spectrite_ore), 0,
			new ModelResourceLocation("spectritemod:spectrite_ore_surface"));
		ModelLoader.setCustomModelResourceLocation(Item
			.getItemFromBlock(ModBlocks.spectrite_ore), 1,
			new ModelResourceLocation("spectritemod:spectrite_ore_nether"));
		ModelLoader.setCustomModelResourceLocation(Item
			.getItemFromBlock(ModBlocks.spectrite_ore), 2,
			new ModelResourceLocation("spectritemod:spectrite_ore_end"));
		ModelLoader.setCustomModelResourceLocation(Item
			.getItemFromBlock(ModBlocks.spectrite_ore), 0,
			new ModelResourceLocation("spectritemod:spectrite_chest"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocks.spectrite_ore),
			new ResourceLocation("spectritemod:spectrite_ore_surface"),
			new ResourceLocation("spectritemod:spectrite_ore_nether"),
			new ResourceLocation("spectritemod:spectrite_ore_end"));
		ModelLoader.setCustomModelResourceLocation(Item
			.getItemFromBlock(ModBlocks.spectrite_bricks_fake), 0,
			new ModelResourceLocation("spectritemod:spectrite_bricks"));
		ModelLoader.setCustomModelResourceLocation(Item
			.getItemFromBlock(ModBlocks.spectrite_chest_trapped_fake), 0,
			new ModelResourceLocation("spectritemod:spectrite_chest_trapped"));
		ModelLoader.setCustomStateMapper(ModBlocks.spectrite_portal, builtInStateMapper);
		ModelLoader.setCustomStateMapper(ModBlocks.molten_spectrite,
			(new StateMap.Builder()).ignore(BlockFluidBase.LEVEL).build());
		ModelLoader.setBucketModelDefinition(ModItems.molten_spectrite_bucket);
		
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
			.register(new SpectriteClientEventHandler());
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		BlockRenderRegister.registerBlockRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(
			TileEntitySpectriteChest.class,
			new TileEntitySpectriteChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(
			TileEntitySpectritePortal.class,
			new TileEntitySpectritePortalRenderer());
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}
}
