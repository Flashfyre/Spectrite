package com.samuel.spectritemod.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlateWeighted;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.SelectValueEntry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.client.eventhandlers.SpectriteClientEventHandler;
import com.samuel.spectritemod.client.renderer.BlockRenderRegister;
import com.samuel.spectritemod.client.renderer.ItemRenderRegister;
import com.samuel.spectritemod.client.renderer.tileentity.TileEntitySpectriteChestRenderer;
import com.samuel.spectritemod.eventhandlers.SpectriteGeneralEventHandler;
import com.samuel.spectritemod.init.ModBlocks;
import com.samuel.spectritemod.tileentity.TileEntitySpectriteChest;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		
		ItemRenderRegister.registerItemRenderer();
		ModelLoader.setCustomModelResourceLocation(Item
			.getItemFromBlock(SpectriteMod.BlockSpectriteOre), 0,
			new ModelResourceLocation("spectritemod:spectrite_ore_surface"));
		ModelLoader.setCustomModelResourceLocation(Item
			.getItemFromBlock(SpectriteMod.BlockSpectriteOre), 1,
			new ModelResourceLocation("spectritemod:spectrite_ore_nether"));
		ModelLoader.setCustomModelResourceLocation(Item
			.getItemFromBlock(SpectriteMod.BlockSpectriteOre), 2,
			new ModelResourceLocation("spectritemod:spectrite_ore_end"));
		ModelLoader.setCustomModelResourceLocation(Item
			.getItemFromBlock(SpectriteMod.BlockSpectriteOre), 0,
			new ModelResourceLocation("spectritemod:spectrite_chest"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocks.spectriteOre),
			new ResourceLocation("spectritemod:spectrite_ore_surface"),
			new ResourceLocation("spectritemod:spectrite_ore_nether"),
			new ResourceLocation("spectritemod:spectrite_ore_end"));
		
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
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}
}
