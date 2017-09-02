package com.samuel.spectrite.init;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.items.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModItems {

	public static Item diamond_rod;
	public static ItemSpectriteSimple spectrite_rod;
	public static ItemSpectriteSimple spectrite_brick;
	public static ItemSpectriteSimple spectrite_bone;
	public static ItemSpectriteSimple spectrite_dust;
	public static ItemSpectriteSimple spectrite_blaze_rod;
	public static ItemSpectriteSimple spectrite_blaze_powder;
	public static ItemSpectriteSimple spectrite_gem;
	public static ItemSpectriteOrb spectrite_orb;
	public static ItemSpectriteShovel spectrite_shovel;
	public static ItemSpectriteShovelSpecial spectrite_shovel_special;
	public static ItemSpectritePickaxe spectrite_pickaxe;
	public static ItemSpectritePickaxeSpecial spectrite_pickaxe_special;
	public static ItemSpectriteAxe spectrite_axe;
	public static ItemSpectriteAxeSpecial spectrite_axe_special;
	public static ItemSpectriteSword spectrite_sword;
	public static ItemSpectriteSwordSpecial spectrite_sword_special;
	public static ItemSpectriteLegendBlade spectrite_sword_2;
	public static ItemSpectriteArrow spectrite_arrow;
	public static ItemSpectriteBow spectrite_bow;
	public static ItemSpectriteBowSpecial spectrite_bow_special;
	public static ItemSpectriteShield spectrite_shield;
	public static ItemSpectriteShieldSpecial spectrite_shield_special;
	public static ItemSpectriteArmor spectrite_helmet;
	public static ItemSpectriteArmor spectrite_chestplate;
	public static ItemSpectriteArmor spectrite_leggings;
	public static ItemSpectriteArmor spectrite_boots;
	public static ItemSpectriteCompass spectrite_compass;
	public static ItemSpectriteSimpleFoiled spectrite_star;
	public static ItemSpectriteSkull spectrite_wither_skeleton_skull;
	public static ItemSpectriteWitherSkull spectrite_wither_skull;
	public static ItemSpectriteWitherSkull spectrite_wither_invulnerable_skull;
	public static ItemSpectriteWitherPart spectrite_wither_torso;
	public static ItemSpectriteWitherPart spectrite_wither_tail;
	public static ItemSpectriteWitherRod spectrite_wither_rod;
	public static ItemSpectriteWitherRod spectrite_wither_rod_invulnerable;
	public static ItemMoltenSpectriteBucket molten_spectrite_bucket;

	public static List<ItemBow> bowItems = new ArrayList<ItemBow>();
	
	private static Map<String, IForgeRegistryEntry> registeredItems = new HashMap<String, IForgeRegistryEntry>();

	public static void createItems() {
		(diamond_rod = new Item()).setCreativeTab(CreativeTabs.MATERIALS);
		(spectrite_rod = new ItemSpectriteSimple()).setCreativeTab(CreativeTabs.MATERIALS);
		(spectrite_brick = new ItemSpectriteSimple()).setCreativeTab(CreativeTabs.MATERIALS);
		(spectrite_bone = new ItemSpectriteSimple()).setCreativeTab(CreativeTabs.MATERIALS);
		(spectrite_dust = new ItemSpectriteSimple()).setCreativeTab(CreativeTabs.BREWING);
		(spectrite_blaze_rod = new ItemSpectriteSimple()).setCreativeTab(CreativeTabs.MATERIALS);
		(spectrite_blaze_powder = new ItemSpectriteSimple()).setCreativeTab(CreativeTabs.BREWING);
		(spectrite_star = new ItemSpectriteSimpleFoiled()).setCreativeTab(CreativeTabs.MATERIALS);
		(spectrite_gem = new ItemSpectriteSimple()).setCreativeTab(CreativeTabs.MATERIALS);
		(spectrite_orb = new ItemSpectriteOrb()).setCreativeTab(CreativeTabs.MISC);
		spectrite_shovel = new ItemSpectriteShovel();
		spectrite_shovel_special = new ItemSpectriteShovelSpecial();
		spectrite_pickaxe = new ItemSpectritePickaxe();
		spectrite_pickaxe_special = new ItemSpectritePickaxeSpecial();
		spectrite_axe = new ItemSpectriteAxe();
		spectrite_axe_special = new ItemSpectriteAxeSpecial();
		spectrite_sword = new ItemSpectriteSword(Spectrite.SPECTRITE_TOOL);
		spectrite_sword_special = new ItemSpectriteSwordSpecial(Spectrite.PERFECT_SPECTRITE_TOOL);
		spectrite_sword_2 = new ItemSpectriteLegendBlade(Spectrite.PERFECT_SPECTRITE_2_TOOL);
		spectrite_arrow = new ItemSpectriteArrow();
		spectrite_bow = new ItemSpectriteBow();
		spectrite_bow_special = new ItemSpectriteBowSpecial();
		spectrite_shield = new ItemSpectriteShield();
		spectrite_shield_special = new ItemSpectriteShieldSpecial();
		spectrite_helmet = new ItemSpectriteArmor(EntityEquipmentSlot.HEAD);
		spectrite_chestplate = new ItemSpectriteArmor(EntityEquipmentSlot.CHEST);
		spectrite_leggings = new ItemSpectriteArmor(EntityEquipmentSlot.LEGS);
		spectrite_boots = new ItemSpectriteArmor(EntityEquipmentSlot.FEET);
		(spectrite_compass = new ItemSpectriteCompass()).setCreativeTab(CreativeTabs.TOOLS);
		spectrite_wither_skeleton_skull = new ItemSpectriteSkull();
		spectrite_wither_skull = new ItemSpectriteWitherSkull(1);
		spectrite_wither_invulnerable_skull = new ItemSpectriteWitherSkull(2);
		spectrite_wither_torso = new ItemSpectriteWitherPart();
		spectrite_wither_tail = new ItemSpectriteWitherPart();
		spectrite_wither_rod = new ItemSpectriteWitherRod(Spectrite.SPECTRITE_WITHER_TOOL, false);
		spectrite_wither_rod_invulnerable = new ItemSpectriteWitherRod(Spectrite.SPECTRITE_INVULNERABLE_WITHER_TOOL, true);
		molten_spectrite_bucket = new ItemMoltenSpectriteBucket();

		Spectrite.SPECTRITE_TOOL.setRepairItem(new ItemStack(spectrite_gem));
		Spectrite.PERFECT_SPECTRITE_TOOL.setRepairItem(new ItemStack(spectrite_gem));
		Spectrite.SPECTRITE_WITHER_TOOL.setRepairItem(new ItemStack(spectrite_bone));
		Spectrite.SPECTRITE.setRepairItem(new ItemStack(spectrite_gem));
		Spectrite.SPECTRITE_WITHER_SKELETON_SKULL.setRepairItem(new ItemStack(spectrite_bone));
		Spectrite.SPECTRITE_WITHER_SKULL.setRepairItem(new ItemStack(spectrite_bone));
	}

	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry itemRegistry = event.getRegistry();
		registerItem(itemRegistry, diamond_rod, "diamond_rod");
		registerItem(itemRegistry, spectrite_rod, "spectrite_rod");
		registerItem(itemRegistry, spectrite_brick, "spectrite_brick");
		registerItem(itemRegistry, spectrite_bone, "spectrite_bone");
		registerItem(itemRegistry, spectrite_dust, "spectrite_dust");
		registerItem(itemRegistry, spectrite_blaze_rod, "spectrite_blaze_rod");
		registerItem(itemRegistry, spectrite_blaze_powder, "spectrite_blaze_powder");
		registerItem(itemRegistry, spectrite_gem,
			"spectrite_gem");
		registerItem(itemRegistry, spectrite_orb,
			"spectrite_orb");
		registerItem(itemRegistry, spectrite_shovel,
			"spectrite_shovel");
		registerItem(itemRegistry, spectrite_shovel_special,
			"spectrite_shovel_special");
		registerItem(itemRegistry, spectrite_pickaxe,
			"spectrite_pickaxe");
		registerItem(itemRegistry, spectrite_pickaxe_special,
			"spectrite_pickaxe_special");
		registerItem(itemRegistry, spectrite_axe,
			"spectrite_axe");
		registerItem(itemRegistry, spectrite_axe_special,
			"spectrite_axe_special");
		registerItem(itemRegistry, spectrite_sword,
			"spectrite_sword");
		registerItem(itemRegistry, spectrite_sword_special,
			"spectrite_sword_special");
		registerItem(itemRegistry, spectrite_sword_2,
			"spectrite_sword_2");
		registerItem(itemRegistry, spectrite_arrow,
			"spectrite_arrow");
		registerItem(itemRegistry, spectrite_bow,
			"spectrite_bow");
		registerItem(itemRegistry, spectrite_bow_special,
			"spectrite_bow_special");
		registerItem(itemRegistry, spectrite_shield,
			"spectrite_shield");
		registerItem(itemRegistry, spectrite_shield_special,
			"spectrite_shield_special");
		registerItem(itemRegistry, spectrite_helmet,
			"spectrite_helmet");
		registerItem(itemRegistry, spectrite_chestplate,
			"spectrite_chestplate");
		registerItem(itemRegistry, spectrite_leggings,
			"spectrite_leggings");
		registerItem(itemRegistry, spectrite_boots,
			"spectrite_boots");
		registerItem(itemRegistry, spectrite_compass,
			"spectrite_compass");
		registerItem(itemRegistry, spectrite_star, "spectrite_star");
		registerItem(itemRegistry, spectrite_wither_skeleton_skull,
			"spectrite_wither_skeleton_skull");
		registerItem(itemRegistry, spectrite_wither_skull,
			"spectrite_wither_skull");
		registerItem(itemRegistry, spectrite_wither_invulnerable_skull,
			"spectrite_wither_invulnerable_skull");
		registerItem(itemRegistry, spectrite_wither_torso, "spectrite_wither_torso");
		registerItem(itemRegistry, spectrite_wither_tail, "spectrite_wither_tail");
		registerItem(itemRegistry, spectrite_wither_rod,
			"spectrite_wither_rod_normal");
		registerItem(itemRegistry, spectrite_wither_rod_invulnerable,
			"spectrite_wither_rod_invulnerable");
		
		OreDictionary.registerOre("spectrite_gem", spectrite_gem);
	}

	public static void populateBowItems() {
		List<ResourceLocation> possibleBowItems = Item.REGISTRY.getKeys().stream().collect(Collectors.toList());
		possibleBowItems.forEach(i -> {
			Item item = Item.REGISTRY.getObject(i);
			if (ItemBow.class.isAssignableFrom(item.getClass())) {
				bowItems.add((ItemBow) item);
			}
		});
	}
	
	@SubscribeEvent
	public void onMissingMapping(RegistryEvent.MissingMappings<Item> e) {
		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getResourceDomain())) {
				String resourcePath =  mapping.key.getResourcePath();
				if (registeredItems.containsKey(resourcePath)) {
					mapping.remap((Item) registeredItems.get(resourcePath));
				}
			}
		}
	}
	
	private static <T extends Item> T registerItem(IForgeRegistry<Item> registry, T item, String name)
	{
		item.setUnlocalizedName(name);
		item.setRegistryName(name);

		registry.register(item);
		
		SpectriteHelper.populateRegisteredObjectsList(registeredItems, item);

		return item;
	}

}
