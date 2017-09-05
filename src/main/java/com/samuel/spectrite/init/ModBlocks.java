package com.samuel.spectrite.init;

import com.samuel.spectrite.blocks.*;
import com.samuel.spectrite.creative.CreativeTabSpectrite;
import com.samuel.spectrite.etc.FluidMoltenSpectrite;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.items.ItemBlockMeta;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class ModBlocks {

	public static BlockSpectriteChest spectrite_chest;
	public static BlockSpectriteChest spectrite_chest_trapped;
	public static BlockSpectriteChest spectrite_chest_trapped_fake;
	public static BlockSpectriteOre spectrite_ore;
	public static BlockSpectrite spectrite_block;
	public static BlockSpectriteSand spectrite_sand;
	public static BlockSpectriteGlass spectrite_glass;
	public static BlockSpectriteBone spectrite_bone_block;
	public static BlockSpectriteBricks spectrite_bricks;
	public static BlockSpectriteBricks spectrite_bricks_fake;
	public static BlockSpectriteBrickStairs spectrite_brick_stairs;
	public static BlockSpectriteBrickSlabHalf spectrite_brick_slab_half;
	public static BlockSpectriteBrickSlabDouble spectrite_brick_slab_double;
	public static BlockDiamondLadder diamond_ladder;
	public static BlockSpectriteLadder spectrite_ladder;
	public static BlockSpectriteSkull spectrite_skull;
	public static BlockFastUpdatingBeacon fast_updating_beacon;
	public static BlockSpectritePortal spectrite_portal;
	public static BlockSpectriteFire spectrite_fire;
	public static BlockMoltenSpectrite molten_spectrite;
	public static FluidMoltenSpectrite fluid_molten_spectrite;
	
	private static Map<String, IForgeRegistryEntry> registeredBlocks = new HashMap<String, IForgeRegistryEntry>();
	private static Map<String, IForgeRegistryEntry> registeredItemBlocks = new HashMap<String, IForgeRegistryEntry>();

	public static void createBlocks() {
		fluid_molten_spectrite = (FluidMoltenSpectrite) new FluidMoltenSpectrite(
				"moltenspectrite", new ResourceLocation(
					"spectrite:blocks/molten_spectrite_still"),
				new ResourceLocation(
					"spectrite:blocks/molten_spectrite_flow"),
				new ResourceLocation(
					"spectrite:blocks/molten_spectrite_still_odd"),
				new ResourceLocation(
					"spectrite:blocks/molten_spectrite_flow_odd"))
				.setLuminosity(15).setDensity(200).setViscosity(2000).setTemperature(2000);
		FluidRegistry.registerFluid(fluid_molten_spectrite);
		FluidRegistry.addBucketForFluid(fluid_molten_spectrite);
		    
		(spectrite_chest = new BlockSpectriteChest(BlockChest.Type.BASIC))
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F);
	    (spectrite_chest_trapped = new BlockSpectriteChest(BlockChest.Type.TRAP))
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F);
		(spectrite_chest_trapped_fake = new BlockSpectriteChest(BlockChest.Type.TRAP)).setLightLevel(0.75F);
		(spectrite_ore = new BlockSpectriteOre())
			.setHardness(6.0F).setResistance(500.0F).setLightLevel(0.5F);
		(spectrite_block = new BlockSpectrite())
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.875F);
		(spectrite_sand = new BlockSpectriteSand())
			.setHardness(22.5F).setResistance(45.0F).setLightLevel(0.5F);
		(spectrite_glass = new BlockSpectriteGlass())
			.setHardness(22.5F).setResistance(60.0F).setLightLevel(0.75F);
		(spectrite_bone_block = new BlockSpectriteBone())
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F);
		(spectrite_bricks = new BlockSpectriteBricks())
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F);
		(spectrite_bricks_fake = new BlockSpectriteBricks())
			.setLightLevel(0.75F);
		(spectrite_brick_stairs = new BlockSpectriteBrickStairs())
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F);
		(spectrite_brick_slab_half = new BlockSpectriteBrickSlabHalf())
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F);
		(spectrite_brick_slab_double = new BlockSpectriteBrickSlabDouble())
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F);
		(diamond_ladder = new BlockDiamondLadder())
			.setHardness(5.0F).setResistance(30.0F);
		(spectrite_ladder = new BlockSpectriteLadder())
			.setHardness(35.0F).setResistance(6000.0F).setLightLevel(0.75F);
		(spectrite_skull = new BlockSpectriteSkull()).setHardness(50.0F)
			.setResistance(6000.0F).setLightLevel(0.75F);
		(fast_updating_beacon = new BlockFastUpdatingBeacon()).setLightLevel(1.0F);
		(spectrite_portal = new BlockSpectritePortal()).setHardness(-1.0F).setResistance(6000000.0F);
		spectrite_fire = new BlockSpectriteFire();
		molten_spectrite = new BlockMoltenSpectrite(fluid_molten_spectrite);
		fluid_molten_spectrite.setUnlocalizedName("moltenspectrite");

	}
	
	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> blockRegistry = event.getRegistry();
		registerBlock(blockRegistry, spectrite_chest, "spectrite_chest", true);
		registerBlock(blockRegistry, spectrite_chest_trapped, "spectrite_chest_trapped", true);
		registerBlock(blockRegistry, spectrite_chest_trapped_fake, "spectrite_chest_trapped_fake", false);
		registerBlock(blockRegistry, spectrite_ore, new ItemBlockMeta(spectrite_ore), "spectrite_ore", true);
		registerBlock(blockRegistry, spectrite_block, "spectrite_block", true);
		registerBlock(blockRegistry, spectrite_sand, "spectrite_sand", true);
		registerBlock(blockRegistry, spectrite_glass, "spectrite_glass", true);
		registerBlock(blockRegistry, spectrite_bone_block, "spectrite_bone_block", true);
		registerBlock(blockRegistry, spectrite_bricks, "spectrite_bricks", true);
		registerBlock(blockRegistry, spectrite_bricks_fake, "spectrite_bricks_fake", false);
		registerBlock(blockRegistry, spectrite_brick_stairs, "spectrite_brick_stairs", true);
		registerSlabBlock(blockRegistry, spectrite_brick_slab_half, spectrite_brick_slab_double, "spectrite_brick_slab");
		registerBlock(blockRegistry, diamond_ladder, "diamond_ladder", true);
		registerBlock(blockRegistry, spectrite_ladder, "spectrite_ladder", true);
		registerBlock(blockRegistry, spectrite_skull, null, "spectrite_skull", false);
		registerBlock(blockRegistry, fast_updating_beacon, null, "fast_updating_beacon", false);
		registerBlock(blockRegistry, spectrite_portal, null, "spectrite_portal", false);
		registerBlock(blockRegistry, spectrite_fire, null, "spectrite_fire", false);
		registerBlock(blockRegistry, molten_spectrite, null, "molten_spectrite", false);
		
		OreDictionary.registerOre("oreSpectrite", spectrite_ore);
		OreDictionary.registerOre("blockSpectrite", spectrite_block);
		OreDictionary.registerOre("blockGlassSpectrite", spectrite_glass);
	}
	
	@SubscribeEvent
	public void onMissingBlockMapping(RegistryEvent.MissingMappings<Block> e) {
		for (RegistryEvent.MissingMappings.Mapping<Block> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getResourceDomain())) {
				String resourcePath =  mapping.key.getResourcePath();
				if (registeredBlocks.containsKey(resourcePath)) {
					mapping.remap((Block) registeredBlocks.get(resourcePath));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onMissingItemBlockMapping(RegistryEvent.MissingMappings<Item> e) {
		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getResourceDomain())) {
				String resourcePath =  mapping.key.getResourcePath();
				if (registeredItemBlocks.containsKey(resourcePath)) {
					mapping.remap((Item) registeredItemBlocks.get(resourcePath));
				}
			}
		}
	}
	
	private static void registerBlock(IForgeRegistry<Block> registry, Block block, ItemBlock item, String name, boolean addToCreativeTab)
	{
		block.setUnlocalizedName(name);
		block.setRegistryName(name);

		registry.register(block);
		
		SpectriteHelper.populateRegisteredObjectsList(registeredBlocks, block);

		if (item != null)
		{
			if (addToCreativeTab) {
				block.setCreativeTab(CreativeTabSpectrite.INSTANCE);
			} else {
				block.setCreativeTab(null);
			}
			registerItemBlock(registry, item);
		}
	}
	
	private static void registerSlabBlock(IForgeRegistry<Block> registry, BlockSlab halfSlab, BlockSlab doubleSlab, String name)
	{
		halfSlab.setUnlocalizedName(name + "_half");
		halfSlab.setRegistryName(name + "_half");
		doubleSlab.setUnlocalizedName(name + "_double");
		doubleSlab.setRegistryName(name + "_double");

		registry.register(halfSlab);
		registry.register(doubleSlab);

		SpectriteHelper.populateRegisteredObjectsList(registeredBlocks, halfSlab);
		SpectriteHelper.populateRegisteredObjectsList(registeredBlocks, doubleSlab);

		halfSlab.setCreativeTab(CreativeTabSpectrite.INSTANCE);
		registerItemBlock(registry, new ItemSlab(halfSlab, halfSlab, doubleSlab));
	}

	private static void registerBlock(IForgeRegistry<Block> registry, Block block, String name, boolean addToCreativeTab)
	{
		registerBlock(registry, block, new ItemBlock(block), name, addToCreativeTab);
	}

	private static void registerItemBlock(IForgeRegistry<Block> registry, ItemBlock item)
	{
		item.setRegistryName(item.getBlock().getRegistryName());

		ForgeRegistries.ITEMS.register(item);
		
		SpectriteHelper.populateRegisteredObjectsList(registeredItemBlocks, item);
	}


}