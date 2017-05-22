package com.samuel.spectritemod.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.init.ModBlocks;

public class BlockRenderRegister {
	public static String modid = SpectriteMod.MOD_ID;

	public static void registerBlockRenderer() {
		reg(ModBlocks.mineralChestSpectrite, 0);
		reg(ModBlocks.mineralChestSpectriteTrapped, 0);;
		reg(ModBlocks.spectriteOre, 0, "spectrite_ore_surface");
		reg(ModBlocks.spectriteOre, 1, "spectrite_ore_nether");
		reg(ModBlocks.spectriteOre, 2, "spectrite_ore_end");
		reg(ModBlocks.spectriteBlock, 0);
	}

	public static void reg(Block block, int meta) {
		Minecraft.getMinecraft().getRenderItem()
			.getItemModelMesher().register(
				Item.getItemFromBlock(block),
				meta,
				new ModelResourceLocation(modid
					+ ":"
					+ block.getUnlocalizedName().substring(
						5), "inventory"));
	}

	public static void reg(Block block, int meta,
		String name) {
		Minecraft.getMinecraft().getRenderItem()
			.getItemModelMesher().register(
				Item.getItemFromBlock(block),
				meta,
				new ModelResourceLocation(modid + ":"
					+ name, "inventory"));
	}
}
