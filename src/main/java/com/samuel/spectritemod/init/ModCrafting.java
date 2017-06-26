package com.samuel.spectritemod.init;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModCrafting {

	public static void initCrafting() {
		GameRegistry.addSmelting(ModBlocks.spectrite_chest, new ItemStack(ModItems.spectrite_gem, 5), 5.0f);
		GameRegistry.addSmelting(ModBlocks.spectrite_chest_trapped, new ItemStack(ModItems.spectrite_gem, 5), 5.0f);
	}
}
