package com.samuel.spectritemod.etc;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import scala.Char;

import com.samuel.spectritemod.SpectriteMod;

public class SpectriteHelper {
	
	private static final TextFormatting[] textColours = new TextFormatting[] { TextFormatting.RED, TextFormatting.GOLD, TextFormatting.YELLOW, TextFormatting.GREEN,
			TextFormatting.BLUE, TextFormatting.AQUA, TextFormatting.LIGHT_PURPLE };
	
	public static Field findObfuscatedField(Class<?> clazz,
		String... names) {
		return ReflectionHelper.findField(clazz,
			ObfuscationReflectionHelper.remapFieldNames(
				clazz.getName(), names));
	}
	
	public static <E> Method findObfuscatedMethod(Class<? super E> clazz, E instance,
		String[] names, Class<?>... methodTypes) {
		return ReflectionHelper.findMethod(clazz, instance,
			ObfuscationReflectionHelper.remapFieldNames(
				clazz.getName(), names), methodTypes);
	}
	
	public static int getCurrentSpectriteFrame(World worldIn) {
		if (worldIn == null) {
            return 0;
		} else {
        	float time = MathHelper.ceil((((worldIn.getTotalWorldTime() >> 1) % 36)
        		* 0.2777F) * 1000F) / 10000F;
            return Math.round(time * 36);
        }
	}
	
	public static String getMultiColouredString(String text, int colourIndex) {
		StringBuilder formattedText = new StringBuilder();
		final int textColourCount = textColours.length;
		for (int c = 0; c < text.length(); c++) {
			formattedText.append(textColours[(c + colourIndex) % textColourCount]).append(String.valueOf(text.charAt(c)));
		}
		
		return formattedText.toString();
	}
}