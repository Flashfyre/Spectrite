package com.samuel.spectritemod.init;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.blocks.BlockSpectrite;
import com.samuel.spectritemod.blocks.BlockSpectriteChest;
import com.samuel.spectritemod.blocks.BlockSpectriteOre;
import com.samuel.spectritemod.items.ItemBlockMeta;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static BlockSpectriteChest mineral_chest_spectrite;
	public static BlockSpectriteChest mineral_chest_spectrite_trapped;
	public static BlockSpectriteOre spectrite_ore;
	public static BlockSpectrite spectrite_block;

	public static void createBlocks() {
		registerBlock(mineral_chest_spectrite = SpectriteMod.BlockSpectriteChest,
			"spectrite_chest");
		registerBlock(mineral_chest_spectrite_trapped = SpectriteMod.BlockTrappedSpectriteChest,
			"spectrite_chest_trapped");
		registerBlock(spectrite_ore = SpectriteMod.BlockSpectriteOre, new ItemBlockMeta(spectrite_ore),
			"spectrite_ore");
		registerBlock(spectrite_block = SpectriteMod.BlockSpectrite, "spectrite_block");
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