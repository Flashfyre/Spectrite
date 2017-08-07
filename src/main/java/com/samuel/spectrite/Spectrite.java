package com.samuel.spectrite;

import com.samuel.spectrite.SpectriteConfig.EnumSpectriteDungeonChestMode;
import com.samuel.spectrite.etc.CommandSpectriteDungeon;
import com.samuel.spectrite.packets.PacketSyncSpectriteBoss;
import com.samuel.spectrite.packets.PacketSyncSpectriteDungeonSpawnPos;
import com.samuel.spectrite.proxy.CommonProxy;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Spectrite.MOD_ID, name = Spectrite.MOD_NAME, version = Spectrite.VERSION, guiFactory = "com.samuel."
	+ Spectrite.MOD_ID + ".client.gui.GUIFactorySpectrite", acceptedMinecraftVersions="[1.12]")
public class Spectrite {
	public static final String MOD_NAME = "Spectrite";
	public static final String MOD_ID = "spectrite";
	public static final String VERSION = "1.3.5";

	@Mod.Instance
	public static Spectrite Instance = new Spectrite();
	public static ArmorMaterial SPECTRITE = EnumHelper
		.addArmorMaterial("spectrite", "spectrite:spectrite_armor",
		72, new int[]{3, 6, 8, 3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3);
	public static ToolMaterial SPECTRITE_TOOL = EnumHelper
		.addToolMaterial("spectrite_tool", 3, 2400, 10.0F, 4.0F, 15);
	public static ToolMaterial PERFECT_SPECTRITE_TOOL = EnumHelper
		.addToolMaterial("perfect_spectrite_tool", 3, 3600, 12.0F, 5.0F, 19);
	public static ToolMaterial PERFECT_SPECTRITE_2_TOOL = EnumHelper
			.addToolMaterial("perfect_spectrite_2_tool", 3, 7842, 14.0F, 6.0F, 25);
	public static IItemPropertyGetter ItemPropertyGetterSpectrite;

	@SidedProxy(clientSide = "com.samuel.spectrite.proxy.ClientProxy",
		serverSide = "com.samuel.spectrite.proxy.CommonProxy")
	public static CommonProxy Proxy;
	public static SimpleNetworkWrapper Network;
	public static SpectriteConfig Config;
	
	public static final int SPECTRITE_COUNT_SURFACE_DEFAULT = 1;
	public static final int SPECTRITE_MIN_SIZE_SURFACE_DEFAULT = 1;
	public static final int SPECTRITE_MAX_SIZE_SURFACE_DEFAULT = 3;
	public static final int SPECTRITE_MIN_Y_SURFACE_DEFAULT = 1;
	public static final int SPECTRITE_MAX_Y_SURFACE_DEFAULT = 16;
	public static final int SPECTRITE_COUNT_NETHER_DEFAULT = 1;
	public static final int SPECTRITE_MIN_SIZE_NETHER_DEFAULT = 1;
	public static final int SPECTRITE_MAX_SIZE_NETHER_DEFAULT = 4;
	public static final int SPECTRITE_MIN_Y_NETHER_DEFAULT = 1;
	public static final int SPECTRITE_MAX_Y_NETHER_DEFAULT = 127;
	public static final int SPECTRITE_COUNT_END_DEFAULT = 2;
	public static final int SPECTRITE_MIN_SIZE_END_DEFAULT = 1;
	public static final int SPECTRITE_MAX_SIZE_END_DEFAULT = 7;
	public static final int SPECTRITE_MIN_Y_END_DEFAULT = 4;
	public static final int SPECTRITE_MAX_Y_END_DEFAULT = 55;
	public static final SpectriteConfig.EnumSpectriteArmourBonusMode SPECTRITE_ARMOUR_BONUS_MODE_DEFAULT =
		SpectriteConfig.EnumSpectriteArmourBonusMode.NORMAL_BONUSES;
	public static final SpectriteConfig.EnumSpectriteArrowDamageMode SPECTRITE_ARROW_DAMAGE_MODE_DEFAULT =
		SpectriteConfig.EnumSpectriteArrowDamageMode.SPECTRITE_DAMAGE;
	public static final double SPECTRITE_TOOL_COOLDOWN_DEFAULT = 1.75d;
	public static final double SPECTRITE_MOB_SPAWN_RATE_DEFAULT = 0.4d;
	public static final double SPECTRITE_MOB_BOSS_SPAWN_RATE_DEFAULT = 0.4d;
	public static final double SPECTRITE_MOB_PERFECT_WEAPON_RATE_DEFAULT = 2.5d;
	public static final double SPECTRITE_MOB_LEGEND_SWORD_RATE_DEFAULT = 2.5d;
	public static final double SPECTRITE_MOB_ARMOUR_DROP_RATE_DEFAULT = 2.5d;
	public static final double SPECTRITE_MOB_SWORD_DROP_RATE_DEFAULT = 5.0d;
	public static final double SPECTRITE_MOB_ORB_DROP_RATE_DEFAULT = 5.0d;
	public static final double SPECTRITE_MOB_BOW_DROP_RATE_DEFAULT = 5.0d;
	public static final double SPECTRITE_MOB_ARROW_DROP_RATE_DEFAULT = 10.0d;
	public static final boolean GENERATE_SPECTRITE_DUNGEON_DEFAULT = true;
	public static final EnumSpectriteDungeonChestMode SPECTRITE_DUNGEON_CHEST_MODE_DEFAULT =
		SpectriteConfig.EnumSpectriteDungeonChestMode.HIGH_TIER_ONLY;
	public static final double SPECTRITE_CHEST_ENCHANT_RATE_DEFAULT = 0.5d;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		Network = NetworkRegistry.INSTANCE
			.newSimpleChannel("Spectrite");
		Network.registerMessage(
			PacketSyncSpectriteBoss.Handler.class,
			PacketSyncSpectriteBoss.class, 0, Side.CLIENT);
		Network.registerMessage(
			PacketSyncSpectriteDungeonSpawnPos.Handler.class,
			PacketSyncSpectriteDungeonSpawnPos.class, 1, Side.CLIENT);
		Config = new SpectriteConfig(e
			.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS
			.register(Config);
		Proxy.preInit(e);
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSpectriteDungeon());
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		Proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		Proxy.postInit(e);
	}
}