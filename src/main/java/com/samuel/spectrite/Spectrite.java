package com.samuel.spectrite;

import com.samuel.spectrite.etc.CommandSpectriteDungeon;
import com.samuel.spectrite.packets.PacketSpectriteExplosion;
import com.samuel.spectrite.packets.PacketSpectriteParticles;
import com.samuel.spectrite.packets.PacketSyncSpectriteBoss;
import com.samuel.spectrite.packets.PacketSyncSpectriteDungeonSpawnPos;
import com.samuel.spectrite.proxy.CommonProxy;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Spectrite.MOD_ID, name = Spectrite.MOD_NAME, version = Spectrite.VERSION, acceptedMinecraftVersions="[1.12,1.13)", certificateFingerprint = "@FINGERPRINT@")
public class Spectrite {
	public static final String MOD_NAME = "Spectrite";
	public static final String MOD_ID = "spectrite";
	public static final String VERSION = "@VERSION@";
	public static final String MC_VERSION = "1.12.1";

	@Mod.Instance
	public static Spectrite Instance = new Spectrite();

	public static final Logger LOGGER = LogManager.getLogger("Spectrite");

	public static ArmorMaterial SPECTRITE = EnumHelper
		.addArmorMaterial("spectrite", "spectrite:spectrite_armor",
		72, new int[]{3, 6, 8, 3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3);
	public static ArmorMaterial SPECTRITE_WITHER_SKELETON_SKULL = EnumHelper
		.addArmorMaterial("spectrite_wither_skeleton_skull", "spectrite:spectrite_wither_skeleton_skull",
		24, new int[]{0, 0, 0, 1}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1);
	public static ArmorMaterial SPECTRITE_WITHER_SKULL = EnumHelper
		.addArmorMaterial("spectrite_wither_skull", "spectrite:spectrite_wither_skull",
			64, new int[]{0, 0, 0, 2}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2);
	public static ArmorMaterial SPECTRITE_WITHER_INVULNERABLE_SKULL = EnumHelper
		.addArmorMaterial("spectrite_wither_invulnerable_skull", "spectrite:spectrite_wither_invulnerable_skull",
			0, new int[]{0, 0, 0, 3}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 3);
	public static ToolMaterial SPECTRITE_TOOL = EnumHelper
		.addToolMaterial("spectrite_tool", 3, 2400, 10.0F, 6.0F, 15);
	public static ToolMaterial PERFECT_SPECTRITE_TOOL = EnumHelper
		.addToolMaterial("perfect_spectrite_tool", 3, 3600, 12.0F, 10.0F, 19);
	public static ToolMaterial PERFECT_SPECTRITE_2_TOOL = EnumHelper
			.addToolMaterial("perfect_spectrite_2_tool", 3, 7842, 14.0F, 16.0F, 25);
	public static ToolMaterial SPECTRITE_WITHER_TOOL = EnumHelper
			.addToolMaterial("spectrite_wither_tool", 3, 128, 13.0F, 8.0F, 22);
	public static ToolMaterial SPECTRITE_INVULNERABLE_WITHER_TOOL = EnumHelper
			.addToolMaterial("spectrite_wither_tool_invulnerable", 3, 0, 14.0F, 10.0F, 25);
	public static IItemPropertyGetter ItemPropertyGetterSpectrite;

	@SidedProxy(clientSide = "com.samuel.spectrite.proxy.ClientProxy",
		serverSide = "com.samuel.spectrite.proxy.CommonProxy")
	public static CommonProxy Proxy;
	public static SimpleNetworkWrapper Network;

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
		Network.registerMessage(
			PacketSpectriteParticles.Handler.class,
			PacketSpectriteParticles.class, 2, Side.CLIENT);
		Network.registerMessage(
			PacketSpectriteExplosion.Handler.class,
			PacketSpectriteExplosion.class, 3, Side.CLIENT);
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

	@EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		LOGGER.warn("Invalid fingerprint detected! The file may have been tampered with. This version will NOT be supported by the author!", event.getSource().getName());
	}
}
