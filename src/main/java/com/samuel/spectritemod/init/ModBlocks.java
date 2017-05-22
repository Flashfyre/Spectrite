package com.samuel.spectritemod.init;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.blocks.BlockSpectriteChest;
import com.samuel.spectritemod.blocks.BlockSpectrite;
import com.samuel.spectritemod.blocks.BlockSpectriteOre;
import com.samuel.spectritemod.items.ItemBlockMeta;

public class ModBlocks {

	public static BlockSpectriteChest mineralChestSpectrite;
	public static BlockSpectriteChest mineralChestSpectriteTrapped;
	public static BlockSpectriteOre spectriteOre;
	public static BlockSpectrite spectriteBlock;

	public static void createBlocks() {
		registerBlock(mineralChestSpectrite = SpectriteMod.BlockSpectriteChest,
			"spectrite_chest");
		registerBlock(mineralChestSpectriteTrapped = SpectriteMod.BlockTrappedSpectriteChest,
			"spectrite_chest_trapped");
		registerBlock(spectriteOre = SpectriteMod.BlockSpectriteOre, new ItemBlockMeta(spectriteOre),
			"spectrite_ore");
		registerBlock(spectriteBlock = SpectriteMod.BlockSpectrite, "spectrite_block");
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