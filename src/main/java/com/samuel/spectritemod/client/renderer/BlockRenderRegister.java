package com.samuel.spectritemod.client.renderer;

import java.util.HashMap;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockRenderRegister {
	public static String modid = SpectriteMod.MOD_ID;

	@SubscribeEvent
	public void onRegisterItemBlockModels(ModelRegistryEvent event) {
		IStateMapper builtInStateMapper = blockIn -> new HashMap();
		
		registerBlockVariants(ModBlocks.spectrite_ore,
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
		
		registerItemBlockModel(ModBlocks.spectrite_chest, 0);
		registerItemBlockModel(ModBlocks.spectrite_chest_trapped, 0);
		registerItemBlockModel(ModBlocks.spectrite_ore, 0, "spectrite_ore_surface");
		registerItemBlockModel(ModBlocks.spectrite_ore, 1, "spectrite_ore_nether");
		registerItemBlockModel(ModBlocks.spectrite_ore, 2, "spectrite_ore_end");
		registerItemBlockModel(ModBlocks.spectrite_block, 0);
		registerItemBlockModel(ModBlocks.spectrite_sand, 0);
		registerItemBlockModel(ModBlocks.spectrite_bricks, 0);
		registerItemBlockModel(ModBlocks.spectrite_brick_stairs, 0);
		registerItemBlockModel(ModBlocks.spectrite_brick_slab_half, 0);
		registerItemBlockModel(ModBlocks.diamond_ladder, 0);
		registerItemBlockModel(ModBlocks.spectrite_ladder, 0);
	}
	
	public static void registerBlockVariants(Block block, ResourceLocation... names) {
		Item item = Item.getItemFromBlock(block);
		ModelBakery.registerItemVariants(item, names);
	}
	
	public static void registerItemBlockModel(Block block, int meta) {
		Item item = Item.getItemFromBlock(block);
		ModelLoader.setCustomModelResourceLocation(item, meta,
			new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	public static void registerItemBlockModel(Block block, int meta,
		String name) {
		Item item = Item.getItemFromBlock(block);
		ModelLoader.setCustomModelResourceLocation(item, meta,
			new ModelResourceLocation(modid + ":" + name, "inventory"));
	}
}
