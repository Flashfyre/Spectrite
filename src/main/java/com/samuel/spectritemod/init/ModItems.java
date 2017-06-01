package com.samuel.spectritemod.init;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.items.ItemDiamondRod;
import com.samuel.spectritemod.items.ItemSpectriteArmor;
import com.samuel.spectritemod.items.ItemSpectriteArrow;
import com.samuel.spectritemod.items.ItemSpectriteAxe;
import com.samuel.spectritemod.items.ItemSpectriteAxeSpecial;
import com.samuel.spectritemod.items.ItemSpectriteBow;
import com.samuel.spectritemod.items.ItemSpectriteBowSpecial;
import com.samuel.spectritemod.items.ItemSpectriteGem;
import com.samuel.spectritemod.items.ItemSpectriteOrb;
import com.samuel.spectritemod.items.ItemSpectritePickaxe;
import com.samuel.spectritemod.items.ItemSpectritePickaxeSpecial;
import com.samuel.spectritemod.items.ItemSpectriteRod;
import com.samuel.spectritemod.items.ItemSpectriteShield;
import com.samuel.spectritemod.items.ItemSpectriteShieldSpecial;
import com.samuel.spectritemod.items.ItemSpectriteShovel;
import com.samuel.spectritemod.items.ItemSpectriteShovelSpecial;
import com.samuel.spectritemod.items.ItemSpectriteSword;
import com.samuel.spectritemod.items.ItemSpectriteSwordSpecial;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

	public static ItemDiamondRod diamond_rod = SpectriteMod.ItemDiamondRod;
	public static ItemSpectriteRod spectrite_rod = SpectriteMod.ItemSpectriteRod;
	public static ItemSpectriteGem spectrite_gem = SpectriteMod.ItemSpectriteGem;
	public static ItemSpectriteOrb spectrite_orb = SpectriteMod.ItemSpectriteOrb;
	public static ItemSpectriteShovel spectrite_shovel = SpectriteMod.ItemSpectriteShovel;
	public static ItemSpectriteShovelSpecial spectrite_shovel_special = SpectriteMod.ItemSpectriteShovelSpecial;
	public static ItemSpectritePickaxe spectrite_pickaxe = SpectriteMod.ItemSpectritePickaxe;
	public static ItemSpectritePickaxeSpecial spectrite_pickaxe_special = SpectriteMod.ItemSpectritePickaxeSpecial;
	public static ItemSpectriteAxe spectrite_axe = SpectriteMod.ItemSpectriteAxe;
	public static ItemSpectriteAxeSpecial spectrite_axe_special = SpectriteMod.ItemSpectriteAxeSpecial;
	public static ItemSpectriteSword spectrite_sword = SpectriteMod.ItemSpectriteSword;
	public static ItemSpectriteSwordSpecial spectrite_sword_special = SpectriteMod.ItemSpectriteSwordSpecial;
	public static ItemSpectriteSword spectrite_sword_2 = SpectriteMod.ItemSpectriteSword2;
	public static ItemSpectriteSwordSpecial spectrite_sword_2_special = SpectriteMod.ItemSpectriteSword2Special;
	public static ItemSpectriteArmor spectrite_helmet = SpectriteMod.ItemSpectriteHelmet;
	public static ItemSpectriteArmor spectrite_chestplate = SpectriteMod.ItemSpectriteChestplate;
	public static ItemSpectriteArmor spectrite_leggings = SpectriteMod.ItemSpectriteLeggings;
	public static ItemSpectriteArmor spectrite_boots = SpectriteMod.ItemSpectriteBoots;
	public static ItemSpectriteArrow spectrite_arrow = SpectriteMod.ItemSpectriteArrow;
	public static ItemSpectriteBow spectrite_bow = SpectriteMod.ItemSpectriteBow;
	public static ItemSpectriteBowSpecial spectrite_bow_special = SpectriteMod.ItemSpectriteBowSpecial;
	public static ItemSpectriteShield spectrite_shield = SpectriteMod.ItemSpectriteShield;
	public static ItemSpectriteShieldSpecial spectrite_shield_special = SpectriteMod.ItemSpectriteShieldSpecial;

	public static void createItems() {
		registerItem(diamond_rod, "diamond_rod");
		registerItem(spectrite_rod, "spectrite_rod");
		registerItem(spectrite_gem,
			"spectrite_gem");
		registerItem(spectrite_orb,
			"spectrite_orb");
		registerItem(spectrite_shovel,
			"spectrite_shovel");
		registerItem(spectrite_shovel_special,
			"spectrite_shovel_special");
		registerItem(spectrite_pickaxe,
			"spectrite_pickaxe");
		registerItem(spectrite_pickaxe_special,
			"spectrite_pickaxe_special");
		registerItem(spectrite_axe,
			"spectrite_axe");
		registerItem(spectrite_axe_special,
			"spectrite_axe_special");
		registerItem(spectrite_sword,
			"spectrite_sword");
		registerItem(spectrite_sword_special,
			"spectrite_sword_special");
		registerItem(spectrite_sword_2,
			"spectrite_sword_2");
		registerItem(spectrite_sword_2_special,
			"spectrite_sword_2_special");
		registerItem(spectrite_arrow,
			"spectrite_arrow");
		registerItem(spectrite_bow,
			"spectrite_bow");
		registerItem(spectrite_bow_special,
			"spectrite_bow_special");
		registerItem(spectrite_shield,
			"spectrite_shield");
		registerItem(spectrite_shield_special,
			"spectrite_shield_special");
		registerItem(spectrite_helmet,
			"spectrite_helmet");
		registerItem(spectrite_chestplate,
			"spectrite_chestplate");
		registerItem(spectrite_leggings,
			"spectrite_leggings");
		registerItem(spectrite_boots,
			"spectrite_boots");
	}
	
	private static <T extends Item> T registerItem(T item, String name)
	{
		item.setUnlocalizedName(name);
		item.setRegistryName(name);

		GameRegistry.register(item);

		return item;
	}

}
