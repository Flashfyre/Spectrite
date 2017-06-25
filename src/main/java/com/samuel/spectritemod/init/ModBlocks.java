package com.samuel.spectritemod.init;

import com.samuel.spectritemod.blocks.BlockDiamondLadder;
import com.samuel.spectritemod.blocks.BlockMoltenSpectrite;
import com.samuel.spectritemod.blocks.BlockSpectrite;
import com.samuel.spectritemod.blocks.BlockSpectriteBrickSlabDouble;
import com.samuel.spectritemod.blocks.BlockSpectriteBrickSlabHalf;
import com.samuel.spectritemod.blocks.BlockSpectriteBrickStairs;
import com.samuel.spectritemod.blocks.BlockSpectriteBricks;
import com.samuel.spectritemod.blocks.BlockSpectriteChest;
import com.samuel.spectritemod.blocks.BlockSpectriteLadder;
import com.samuel.spectritemod.blocks.BlockSpectriteOre;
import com.samuel.spectritemod.blocks.BlockSpectritePortal;
import com.samuel.spectritemod.etc.FluidMoltenSpectrite;
import com.samuel.spectritemod.items.ItemBlockMeta;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockSlab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static BlockSpectriteChest spectrite_chest;
	public static BlockSpectriteChest spectrite_chest_trapped;
	public static BlockSpectriteChest spectrite_chest_trapped_fake;
	public static BlockSpectriteOre spectrite_ore;
	public static BlockSpectrite spectrite_block;
	public static BlockSpectriteBricks spectrite_bricks;
	public static BlockSpectriteBricks spectrite_bricks_fake;
	public static BlockSpectriteBrickStairs spectrite_brick_stairs;
	public static BlockSpectriteBrickSlabHalf spectrite_brick_slab_half;
	public static BlockSpectriteBrickSlabDouble spectrite_brick_slab_double;
	public static BlockDiamondLadder diamond_ladder;
	public static BlockSpectriteLadder spectrite_ladder;
	public static BlockSpectritePortal spectrite_portal;
	public static BlockMoltenSpectrite molten_spectrite;
	public static FluidMoltenSpectrite fluid_molten_spectrite;
	

	public static void createBlocks() {
		
		fluid_molten_spectrite = (FluidMoltenSpectrite) new FluidMoltenSpectrite(
				"moltenspectrite", new ResourceLocation(
					"spectritemod:blocks/molten_spectrite_still"),
				new ResourceLocation(
					"spectritemod:blocks/molten_spectrite_flow"),
				new ResourceLocation(
					"spectritemod:blocks/molten_spectrite_still_odd"),
				new ResourceLocation(
					"spectritemod:blocks/molten_spectrite_flow_odd"))
		    		.setLuminosity(15).setDensity(200).setViscosity(2000).setTemperature(2000);
		FluidRegistry.registerFluid(fluid_molten_spectrite);
		FluidRegistry.addBucketForFluid(fluid_molten_spectrite);
		    
		(spectrite_chest = new BlockSpectriteChest(BlockChest.Type.BASIC))
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F);
	    (spectrite_chest_trapped = new BlockSpectriteChest(BlockChest.Type.TRAP))
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F);
	    (spectrite_chest_trapped_fake = new BlockSpectriteChest(BlockChest.Type.TRAP))
			.setLightLevel(0.75F).setCreativeTab(null);
		(spectrite_ore = new BlockSpectriteOre())
			.setHardness(6.0F).setResistance(500.0F).setLightLevel(0.5F);
		(spectrite_block = new BlockSpectrite())
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.875F)
			.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		(spectrite_bricks = new BlockSpectriteBricks())
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F)
			.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		(spectrite_bricks_fake = new BlockSpectriteBricks())
			.setLightLevel(0.75F);
		(spectrite_brick_stairs = new BlockSpectriteBrickStairs())
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F);
		(spectrite_brick_slab_half = new BlockSpectriteBrickSlabHalf())
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F)
			.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		(spectrite_brick_slab_double = new BlockSpectriteBrickSlabDouble())
			.setHardness(50.0F).setResistance(6000.0F).setLightLevel(0.75F);
		(diamond_ladder = new BlockDiamondLadder())
			.setHardness(5.0F).setResistance(30.0F);
		(spectrite_ladder = new BlockSpectriteLadder())
			.setHardness(35.0F).setResistance(6000.0F).setLightLevel(0.75F);
		(spectrite_portal = new BlockSpectritePortal()).setHardness(-1.0F).setResistance(6000000.0F);
		molten_spectrite = new BlockMoltenSpectrite(fluid_molten_spectrite);
		fluid_molten_spectrite.setUnlocalizedName("moltenspectrite");
	}
	
	public static void registerBlocks() {
		registerBlock(spectrite_chest, "spectrite_chest");
		registerBlock(spectrite_chest_trapped, "spectrite_chest_trapped");
		registerBlock(spectrite_chest_trapped_fake, "spectrite_chest_trapped_fake");
		registerBlock(spectrite_ore, new ItemBlockMeta(spectrite_ore), "spectrite_ore");
		registerBlock(spectrite_block, "spectrite_block");
		registerBlock(spectrite_bricks, "spectrite_bricks");
		registerBlock(spectrite_bricks_fake, "spectrite_bricks_fake");
		registerBlock(spectrite_brick_stairs, "spectrite_brick_stairs");
		registerSlabBlock(spectrite_brick_slab_half, spectrite_brick_slab_double, "spectrite_brick_slab");
		registerBlock(diamond_ladder, "diamond_ladder");
		registerBlock(spectrite_ladder, "spectrite_ladder");
		registerBlock(spectrite_portal, null, "spectrite_portal");
		registerBlock(molten_spectrite, "molten_spectrite");
	}
	
	private static void registerBlock(Block block, ItemBlock item, String name)
	{
		block.setUnlocalizedName(name);
		block.setRegistryName(name);

		GameRegistry.register(block);

		if (item != null)
		{
			registerItemBlock(item);
		}
	}
	
	private static void registerSlabBlock(BlockSlab halfSlab, BlockSlab doubleSlab, String name)
	{
		halfSlab.setUnlocalizedName(name + "_half");
		halfSlab.setRegistryName(name + "_half");
		doubleSlab.setUnlocalizedName(name + "_double");
		doubleSlab.setRegistryName(name + "_double");

		GameRegistry.register(halfSlab);
		GameRegistry.register(doubleSlab);

		registerItemBlock(new ItemSlab(halfSlab, halfSlab, doubleSlab));
	}
	
	private static void registerBlock(Block block, String name)
	{
		registerBlock(block, new ItemBlock(block), name);
	}

	private static void registerItemBlock(ItemBlock item)
	{
		item.setRegistryName(item.getBlock().getRegistryName());

		GameRegistry.register(item);
	}


}