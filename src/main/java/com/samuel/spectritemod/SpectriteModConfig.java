package com.samuel.spectritemod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpectriteModConfig {
	
	public static enum EnumSpectriteArmourBonusMode implements IStringSerializable {
		HALF_BONUSES, NORMAL_BONUSES, OVERPOWERED_BONUSES, BEAST_MODE, YGTBK, BROKEN_BONUSES;
		
		public float getHealthIncreaseMultiplier() {
			return Math.max(this.ordinal(), 0.5f);
		}
		
		public int getAbsorptionDelay() {
			int absorptionDelay = 0;
			
			switch (this.ordinal()) {
				case 0:
				case 1:
					absorptionDelay = 100;
					break;
				case 2:
					absorptionDelay = 75;
					break;
				case 3:
					absorptionDelay = 50;
					break;
				case 4:
					absorptionDelay = 25;
					break;
				case 5:
					absorptionDelay = 10;
					break;
			}
			
			return absorptionDelay;
		}

		@Override
		public String getName() {
			return I18n.translateToLocal("config.spectrite_armour_bonus_mode.options." + this.ordinal());
		}
		
		public static String[] getAllNames() {
			List<String> names = new ArrayList<String>();
			
			for (EnumSpectriteArmourBonusMode mode : values()) {
				names.add(mode.getName());
			}
			
			return names.toArray(new String[0]);
		}
		
		public static EnumSpectriteArmourBonusMode forName(String name) {
			if (name != null) {
				for (EnumSpectriteArmourBonusMode mode : values()) {
					if (name.equals(mode.getName())) {
						return mode;
					}
				}
			}
			
			return SpectriteMod.SPECTRITE_ARMOUR_BONUS_MODE_DEFAULT;
		}
	}
	
	public static enum EnumSpectriteArrowDamageMode implements IStringSerializable {
		SPECTRITE_DAMAGE, EXPLOSION, SPECTRITE_DAMAGE_EXPLOSION;
		
		@Override
		public String getName() {
			return I18n.translateToLocal("config.spectrite_arrow_damage_mode.options." + this.ordinal());
		}
		
		public static String[] getAllNames() {
			List<String> names = new ArrayList<String>();
			
			for (EnumSpectriteArrowDamageMode mode : values()) {
				names.add(mode.getName());
			}
			
			return names.toArray(new String[0]);
		}
		
		public static EnumSpectriteArrowDamageMode forName(String name) {
			if (name != null) {
				for (EnumSpectriteArrowDamageMode mode : values()) {
					if (name.equals(mode.getName())) {
						return mode;
					}
				}
			}
			
			return SpectriteMod.SPECTRITE_ARROW_DAMAGE_MODE_DEFAULT;
		}
	}
	
	public static enum EnumSpectriteDungeonChestMode implements IStringSerializable {
		NONE, HIGH_TIER_ONLY, MEDIUM_AND_HIGH_TIER, ALL_TIERS;
		
		@Override
		public String getName() {
			return I18n.translateToLocal("config.spectrite_dungeon_chest_mode.options." + this.ordinal());
		}
		
		public static String[] getAllNames() {
			List<String> names = new ArrayList<String>();
			
			for (EnumSpectriteDungeonChestMode mode : values()) {
				names.add(mode.getName());
			}
			
			return names.toArray(new String[0]);
		}
		
		public static EnumSpectriteDungeonChestMode forName(String name) {
			if (name != null) {
				for (EnumSpectriteDungeonChestMode mode : values()) {
					if (name.equals(mode.getName())) {
						return mode;
					}
				}
			}
			
			return SpectriteMod.SPECTRITE_DUNGEON_CHEST_MODE_DEFAULT;
		}
		
		public boolean shouldChestTierUseSpectriteChest(int chestTier) {
			return (2 - this.ordinal()) - chestTier < 0;
		}
	}
	
	public static int spectriteCountSurface = SpectriteMod.SPECTRITE_COUNT_SURFACE_DEFAULT;
	public static int spectriteMinSizeSurface = SpectriteMod.SPECTRITE_MIN_SIZE_SURFACE_DEFAULT;
	public static int spectriteMaxSizeSurface = SpectriteMod.SPECTRITE_MAX_SIZE_SURFACE_DEFAULT;
	public static int spectriteMinYSurface = SpectriteMod.SPECTRITE_MIN_Y_SURFACE_DEFAULT;
	public static int spectriteMaxYSurface = SpectriteMod.SPECTRITE_MAX_Y_SURFACE_DEFAULT;
	public static int spectriteCountNether = SpectriteMod.SPECTRITE_COUNT_NETHER_DEFAULT;
	public static int spectriteMinSizeNether = SpectriteMod.SPECTRITE_MIN_SIZE_NETHER_DEFAULT;
	public static int spectriteMaxSizeNether = SpectriteMod.SPECTRITE_MAX_SIZE_NETHER_DEFAULT;
	public static int spectriteMinYNether = SpectriteMod.SPECTRITE_MIN_Y_NETHER_DEFAULT;
	public static int spectriteMaxYNether = SpectriteMod.SPECTRITE_MAX_Y_NETHER_DEFAULT;
	public static int spectriteCountEnd = SpectriteMod.SPECTRITE_COUNT_END_DEFAULT;
	public static int spectriteMinSizeEnd = SpectriteMod.SPECTRITE_MIN_SIZE_END_DEFAULT;
	public static int spectriteMaxSizeEnd = SpectriteMod.SPECTRITE_MAX_SIZE_END_DEFAULT;
	public static int spectriteMinYEnd = SpectriteMod.SPECTRITE_MIN_Y_END_DEFAULT;
	public static int spectriteMaxYEnd = SpectriteMod.SPECTRITE_MAX_Y_END_DEFAULT;
	public static EnumSpectriteArmourBonusMode spectriteArmourBonusMode = SpectriteMod.SPECTRITE_ARMOUR_BONUS_MODE_DEFAULT;
	public static EnumSpectriteArrowDamageMode spectriteArrowDamageMode = SpectriteMod.SPECTRITE_ARROW_DAMAGE_MODE_DEFAULT;
	public static double spectriteToolCooldown = SpectriteMod.SPECTRITE_TOOL_COOLDOWN_DEFAULT;
	public static double spectriteMobSpawnRate = SpectriteMod.SPECTRITE_MOB_SPAWN_RATE_DEFAULT;
	public static double spectriteMobBossSpawnRate = SpectriteMod.SPECTRITE_MOB_BOSS_SPAWN_RATE_DEFAULT;
	public static double spectriteMobPerfectWeaponRate = SpectriteMod.SPECTRITE_MOB_PERFECT_WEAPON_RATE_DEFAULT;
	public static double spectriteMobLegendSwordRate = SpectriteMod.SPECTRITE_MOB_LEGEND_SWORD_RATE_DEFAULT;
	public static double spectriteMobArmourDropRate = SpectriteMod.SPECTRITE_MOB_ARMOUR_DROP_RATE_DEFAULT;
	public static double spectriteMobSwordDropRate = SpectriteMod.SPECTRITE_MOB_SWORD_DROP_RATE_DEFAULT;
	public static double spectriteMobOrbDropRate = SpectriteMod.SPECTRITE_MOB_ORB_DROP_RATE_DEFAULT;
	public static double spectriteMobBowDropRate = SpectriteMod.SPECTRITE_MOB_BOW_DROP_RATE_DEFAULT;
	public static double spectriteMobArrowDropRate = SpectriteMod.SPECTRITE_MOB_ARROW_DROP_RATE_DEFAULT;
	public static double spectriteChestEnchantRate = SpectriteMod.SPECTRITE_CHEST_ENCHANT_RATE_DEFAULT;
	public static boolean generateSpectriteDungeon = SpectriteMod.GENERATE_SPECTRITE_DUNGEON_DEFAULT;
	public static EnumSpectriteDungeonChestMode spectriteDungeonChestMode = SpectriteMod.SPECTRITE_DUNGEON_CHEST_MODE_DEFAULT;
	
	public static Property propSpectriteCountSurface = null;
	public static Property propSpectriteMinSizeSurface = null;
	public static Property propSpectriteMaxSizeSurface = null;
	public static Property propSpectriteMinYSurface = null;
	public static Property propSpectriteMaxYSurface = null;
	public static Property propSpectriteCountNether = null;
	public static Property propSpectriteMinSizeNether = null;
	public static Property propSpectriteMaxSizeNether = null;
	public static Property propSpectriteMinYNether = null;
	public static Property propSpectriteMaxYNether = null;
	public static Property propSpectriteCountEnd = null;
	public static Property propSpectriteMinSizeEnd = null;
	public static Property propSpectriteMaxSizeEnd = null;
	public static Property propSpectriteMinYEnd = null;
	public static Property propSpectriteMaxYEnd = null;
	public static Property propSpectriteArmourBonusMode = null;
	public static Property propSpectriteArrowDamageMode = null;
	public static Property propSpectriteToolCooldown = null;
	public static Property propSpectriteMobSpawnRate = null;
	public static Property propSpectriteMobBossSpawnRate = null;
	public static Property propSpectriteMobPerfectWeaponRate = null;
	public static Property propSpectriteMobLegendSwordRate = null;
	public static Property propSpectriteMobArmourDropRate = null;
	public static Property propSpectriteMobSwordDropRate = null;
	public static Property propSpectriteMobOrbDropRate = null;
	public static Property propSpectriteMobBowDropRate = null;
	public static Property propSpectriteMobArrowDropRate = null;
	public static Property propSpectriteChestEnchantRate = null;
	public static Property propGenerateSpectriteDungeon = null;
	public static Property propSpectriteDungeonChestMode = null;
	
	public static List<String> propertyNames = null;
	public static List<String> propertyDescriptions = null;
	
	public final Configuration configuration;
	
	public SpectriteModConfig(File file)
	{
		this.configuration = new Configuration(file, true);
		
		final String[] propertyKeys = new String[] {
			"spectrite_count_surface",
			"spectrite_minSize_surface",
			"spectrite_maxSize_surface",
			"spectrite_minY_surface",
			"spectrite_maxY_surface",
			"spectrite_count_nether",
			"spectrite_minSize_nether",
			"spectrite_maxSize_nether",
			"spectrite_minY_nether",
			"spectrite_maxY_nether",
			"spectrite_count_end",
			"spectrite_minSize_end",
			"spectrite_maxSize_end",
			"spectrite_minY_end",
			"spectrite_maxY_end",
			"spectrite_armour_bonus_mode",
			"spectrite_arrow_damage_mode",
			"spectrite_tool_cooldown",
			"spectrite_mob_spawn_rate",
			"spectrite_mob_boss_spawn_rate",
			"spectrite_mob_perfect_weapon_rate",
			"spectrite_mob_legend_sword_rate",
			"spectrite_mob_armour_drop_rate",
			"spectrite_mob_sword_drop_rate",
			"spectrite_mob_orb_drop_rate",
			"spectrite_mob_bow_drop_rate",
			"spectrite_mob_arrow_drop_rate",
			"spectrite_chest_enchant_rate",
			"generate_spectrite_dungeon",
			"spectrite_dungeon_chest_mode"
		};
		
		SpectriteModConfig.propertyNames = new ArrayList<String>();
		SpectriteModConfig.propertyDescriptions = new ArrayList<String>();
		
		for (String k : propertyKeys) {
			SpectriteModConfig.propertyNames.add(I18n.translateToLocal("config." + k + ".name"));
			SpectriteModConfig.propertyDescriptions.add(I18n.translateToLocal("config." + k + ".desc"));
		}
		
		configuration.getCategory(Configuration.CATEGORY_GENERAL).keySet().removeIf(p -> !propertyNames.contains(p));
		

		this.loadConfig();
		
		configuration.setCategoryPropertyOrder(Configuration.CATEGORY_GENERAL, propertyNames);
	}

	private void loadConfig()
	{
		configuration.setCategoryRequiresMcRestart(Configuration.CATEGORY_GENERAL, false);
		propSpectriteCountSurface = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(0), SpectriteMod.SPECTRITE_COUNT_SURFACE_DEFAULT,
				propertyDescriptions.get(0), 0, 20);
		propSpectriteMinSizeSurface = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(1), SpectriteMod.SPECTRITE_MIN_SIZE_SURFACE_DEFAULT,
				propertyDescriptions.get(1), 0, 20);
		propSpectriteMaxSizeSurface = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(2), SpectriteMod.SPECTRITE_MAX_SIZE_SURFACE_DEFAULT,
				propertyDescriptions.get(2), Math.max(0, propSpectriteMinSizeSurface.getInt(SpectriteMod.SPECTRITE_MIN_SIZE_SURFACE_DEFAULT)), 20);
		propSpectriteMinSizeSurface.setMaxValue(Math.min(20, propSpectriteMaxSizeSurface.getInt(SpectriteMod.SPECTRITE_MAX_SIZE_SURFACE_DEFAULT)));
		propSpectriteMinYSurface = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(3), SpectriteMod.SPECTRITE_MIN_Y_SURFACE_DEFAULT,
				propertyDescriptions.get(3), 1, 128);
		propSpectriteMaxYSurface = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(4), SpectriteMod.SPECTRITE_MAX_Y_SURFACE_DEFAULT,
				propertyDescriptions.get(4), Math.max(1, propSpectriteMinYSurface.getInt(SpectriteMod.SPECTRITE_MIN_Y_SURFACE_DEFAULT)), 128);
		propSpectriteMinYSurface.setMaxValue(Math.min(128, propSpectriteMaxYSurface.getInt(SpectriteMod.SPECTRITE_MAX_Y_SURFACE_DEFAULT)));
		propSpectriteCountNether = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(5), SpectriteMod.SPECTRITE_COUNT_NETHER_DEFAULT,
				propertyDescriptions.get(5), 0, 20);
		propSpectriteMinSizeNether = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(6), SpectriteMod.SPECTRITE_MIN_SIZE_NETHER_DEFAULT,
				propertyDescriptions.get(6), 0, 20);
		propSpectriteMaxSizeNether = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(7), SpectriteMod.SPECTRITE_MAX_SIZE_NETHER_DEFAULT,
				propertyDescriptions.get(7), Math.max(0, propSpectriteMinSizeNether.getInt(SpectriteMod.SPECTRITE_MIN_SIZE_NETHER_DEFAULT)), 20);
		propSpectriteMinSizeNether.setMaxValue(Math.min(20, propSpectriteMaxSizeNether.getInt(SpectriteMod.SPECTRITE_MAX_SIZE_NETHER_DEFAULT)));
		propSpectriteMinYNether = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(8), SpectriteMod.SPECTRITE_MIN_Y_NETHER_DEFAULT,
				propertyDescriptions.get(8), 1, 127);
		propSpectriteMaxYNether = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(9), SpectriteMod.SPECTRITE_MAX_Y_NETHER_DEFAULT,
				propertyDescriptions.get(9), Math.max(1, propSpectriteMinYNether.getInt(SpectriteMod.SPECTRITE_MIN_Y_NETHER_DEFAULT)), 127);
		propSpectriteMinYNether.setMaxValue(Math.min(127, propSpectriteMaxYNether.getInt(SpectriteMod.SPECTRITE_MAX_Y_NETHER_DEFAULT)));
		propSpectriteCountEnd = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(10), SpectriteMod.SPECTRITE_COUNT_END_DEFAULT,
				propertyDescriptions.get(10), 0, 20);
		propSpectriteMinSizeEnd = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(11), SpectriteMod.SPECTRITE_MIN_SIZE_END_DEFAULT,
				propertyDescriptions.get(11), 0, 20);
		propSpectriteMaxSizeEnd = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(12), SpectriteMod.SPECTRITE_MAX_SIZE_END_DEFAULT,
				propertyDescriptions.get(12), Math.max(0, propSpectriteMinSizeEnd.getInt(SpectriteMod.SPECTRITE_MIN_SIZE_END_DEFAULT)), 20);
		propSpectriteMinSizeEnd.setMaxValue(Math.min(20, propSpectriteMaxSizeEnd.getInt(SpectriteMod.SPECTRITE_MAX_SIZE_END_DEFAULT)));
		propSpectriteMinYEnd = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(13), SpectriteMod.SPECTRITE_MIN_Y_END_DEFAULT,
				propertyDescriptions.get(13), 4, 70);
		propSpectriteMaxYEnd = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(14), SpectriteMod.SPECTRITE_MAX_Y_END_DEFAULT,
				propertyDescriptions.get(14), Math.max(4, propSpectriteMinYEnd.getInt(SpectriteMod.SPECTRITE_MIN_Y_END_DEFAULT)), 70);
		propSpectriteMinYEnd.setMaxValue(Math.min(55, propSpectriteMaxYEnd.getInt(SpectriteMod.SPECTRITE_MAX_Y_END_DEFAULT)));
		propSpectriteArmourBonusMode = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(15), SpectriteMod.SPECTRITE_ARMOUR_BONUS_MODE_DEFAULT.getName(),
				propertyDescriptions.get(15), EnumSpectriteArmourBonusMode.getAllNames());
		propSpectriteArrowDamageMode = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(16), SpectriteMod.SPECTRITE_ARROW_DAMAGE_MODE_DEFAULT.getName(),
				propertyDescriptions.get(16), EnumSpectriteArrowDamageMode.getAllNames());
		propSpectriteToolCooldown = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(17), SpectriteMod.SPECTRITE_TOOL_COOLDOWN_DEFAULT,
				propertyDescriptions.get(17), 0, 60);
		propSpectriteMobSpawnRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(18), SpectriteMod.SPECTRITE_MOB_SPAWN_RATE_DEFAULT,
				propertyDescriptions.get(18), 0, 100);
		propSpectriteMobBossSpawnRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(19), SpectriteMod.SPECTRITE_MOB_BOSS_SPAWN_RATE_DEFAULT,
				propertyDescriptions.get(19), 0, 100);
		propSpectriteMobPerfectWeaponRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(20), SpectriteMod.SPECTRITE_MOB_PERFECT_WEAPON_RATE_DEFAULT,
				propertyDescriptions.get(20), 0, 100);
		propSpectriteMobLegendSwordRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(21), SpectriteMod.SPECTRITE_MOB_LEGEND_SWORD_RATE_DEFAULT,
				propertyDescriptions.get(21), 0, 100);
		propSpectriteMobArmourDropRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(22), SpectriteMod.SPECTRITE_MOB_ARMOUR_DROP_RATE_DEFAULT,
				propertyDescriptions.get(22), 0, 100);
		propSpectriteMobSwordDropRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(23), SpectriteMod.SPECTRITE_MOB_SWORD_DROP_RATE_DEFAULT,
				propertyDescriptions.get(23), 0, 100);
		propSpectriteMobOrbDropRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(24), SpectriteMod.SPECTRITE_MOB_ORB_DROP_RATE_DEFAULT,
				propertyDescriptions.get(24), 0, 100);
		propSpectriteMobBowDropRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(25), SpectriteMod.SPECTRITE_MOB_BOW_DROP_RATE_DEFAULT,
				propertyDescriptions.get(25), 0, 100);
		propSpectriteMobArrowDropRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(26), SpectriteMod.SPECTRITE_MOB_ARROW_DROP_RATE_DEFAULT,
				propertyDescriptions.get(26), 0, 100);
		propSpectriteChestEnchantRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(27), SpectriteMod.SPECTRITE_CHEST_ENCHANT_RATE_DEFAULT,
				propertyDescriptions.get(27), 0, 100);
		propGenerateSpectriteDungeon = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(28), SpectriteMod.GENERATE_SPECTRITE_DUNGEON_DEFAULT,
				propertyDescriptions.get(28));
		propSpectriteDungeonChestMode = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(29), SpectriteMod.SPECTRITE_DUNGEON_CHEST_MODE_DEFAULT.getName(),
				propertyDescriptions.get(29), EnumSpectriteDungeonChestMode.getAllNames());
		SpectriteModConfig.spectriteCountSurface = propSpectriteCountSurface.getInt(SpectriteMod.SPECTRITE_COUNT_SURFACE_DEFAULT);
		SpectriteModConfig.spectriteMinSizeSurface = propSpectriteMinSizeSurface.getInt(SpectriteMod.SPECTRITE_MIN_SIZE_SURFACE_DEFAULT);
		SpectriteModConfig.spectriteMaxSizeSurface = propSpectriteMaxSizeSurface.getInt(SpectriteMod.SPECTRITE_MAX_SIZE_SURFACE_DEFAULT);
		SpectriteModConfig.spectriteMinYSurface = propSpectriteMinYSurface.getInt(SpectriteMod.SPECTRITE_MIN_Y_SURFACE_DEFAULT);
		SpectriteModConfig.spectriteMaxYSurface = propSpectriteMaxYSurface.getInt(SpectriteMod.SPECTRITE_MAX_Y_SURFACE_DEFAULT);
		SpectriteModConfig.spectriteCountNether = propSpectriteCountNether.getInt(SpectriteMod.SPECTRITE_COUNT_NETHER_DEFAULT);
		SpectriteModConfig.spectriteMinSizeNether = propSpectriteMinSizeNether.getInt(SpectriteMod.SPECTRITE_MIN_SIZE_NETHER_DEFAULT);
		SpectriteModConfig.spectriteMaxSizeNether = propSpectriteMaxSizeNether.getInt(SpectriteMod.SPECTRITE_MAX_SIZE_NETHER_DEFAULT);
		SpectriteModConfig.spectriteMinYNether = propSpectriteMinYNether.getInt(SpectriteMod.SPECTRITE_MIN_Y_NETHER_DEFAULT);
		SpectriteModConfig.spectriteMaxYNether = propSpectriteMaxYNether.getInt(SpectriteMod.SPECTRITE_MAX_Y_NETHER_DEFAULT);
		SpectriteModConfig.spectriteCountEnd = propSpectriteCountEnd.getInt(SpectriteMod.SPECTRITE_COUNT_END_DEFAULT);
		SpectriteModConfig.spectriteMinSizeEnd = propSpectriteMinSizeEnd.getInt(SpectriteMod.SPECTRITE_MIN_SIZE_END_DEFAULT);
		SpectriteModConfig.spectriteMaxSizeEnd = propSpectriteMaxSizeEnd.getInt(SpectriteMod.SPECTRITE_MAX_SIZE_END_DEFAULT);
		SpectriteModConfig.spectriteMinYEnd = propSpectriteMinYEnd.getInt(SpectriteMod.SPECTRITE_MIN_Y_END_DEFAULT);
		SpectriteModConfig.spectriteMaxYEnd = propSpectriteMaxYEnd.getInt(SpectriteMod.SPECTRITE_MAX_Y_END_DEFAULT);
		SpectriteModConfig.spectriteArmourBonusMode = EnumSpectriteArmourBonusMode.forName(propSpectriteArmourBonusMode.getString());
		SpectriteModConfig.spectriteArrowDamageMode = EnumSpectriteArrowDamageMode.forName(propSpectriteArrowDamageMode.getString());
		SpectriteModConfig.spectriteToolCooldown = propSpectriteToolCooldown.getDouble(SpectriteMod.SPECTRITE_TOOL_COOLDOWN_DEFAULT);
		SpectriteModConfig.spectriteMobSpawnRate = propSpectriteMobSpawnRate.getDouble(SpectriteMod.SPECTRITE_MOB_SPAWN_RATE_DEFAULT);
		SpectriteModConfig.spectriteMobBossSpawnRate = propSpectriteMobBossSpawnRate.getDouble(SpectriteMod.SPECTRITE_MOB_BOSS_SPAWN_RATE_DEFAULT);
		SpectriteModConfig.spectriteMobPerfectWeaponRate = propSpectriteMobPerfectWeaponRate.getDouble(SpectriteMod.SPECTRITE_MOB_PERFECT_WEAPON_RATE_DEFAULT);
		SpectriteModConfig.spectriteMobLegendSwordRate = propSpectriteMobLegendSwordRate.getDouble(SpectriteMod.SPECTRITE_MOB_LEGEND_SWORD_RATE_DEFAULT);
		SpectriteModConfig.spectriteMobArmourDropRate = propSpectriteMobArmourDropRate.getDouble(SpectriteMod.SPECTRITE_MOB_ARMOUR_DROP_RATE_DEFAULT);
		SpectriteModConfig.spectriteMobSwordDropRate = propSpectriteMobSwordDropRate.getDouble(SpectriteMod.SPECTRITE_MOB_SWORD_DROP_RATE_DEFAULT);
		SpectriteModConfig.spectriteMobOrbDropRate = propSpectriteMobOrbDropRate.getDouble(SpectriteMod.SPECTRITE_MOB_ORB_DROP_RATE_DEFAULT);
		SpectriteModConfig.spectriteMobBowDropRate = propSpectriteMobBowDropRate.getDouble(SpectriteMod.SPECTRITE_MOB_BOW_DROP_RATE_DEFAULT);
		SpectriteModConfig.spectriteMobArrowDropRate = propSpectriteMobArrowDropRate.getDouble(SpectriteMod.SPECTRITE_MOB_ARROW_DROP_RATE_DEFAULT);
		SpectriteModConfig.spectriteChestEnchantRate = propSpectriteChestEnchantRate.getDouble(SpectriteMod.SPECTRITE_CHEST_ENCHANT_RATE_DEFAULT);
		SpectriteModConfig.generateSpectriteDungeon = propGenerateSpectriteDungeon.getBoolean(SpectriteMod.GENERATE_SPECTRITE_DUNGEON_DEFAULT);
		SpectriteModConfig.spectriteDungeonChestMode = EnumSpectriteDungeonChestMode.forName(propSpectriteDungeonChestMode.getString());

		if (this.configuration.hasChanged())
		{
			this.configuration.save();
		}
	}
	
	private int getInt(ConfigCategory category, String name, int defaultValue)
	{
		return this.configuration.get(category.getName(), name, defaultValue).getInt();
	}


	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e)
	{
		if(e.getModID().equals(SpectriteMod.MOD_ID))
		{
			this.loadConfig();
			
			int temp = 0;
			if (SpectriteModConfig.spectriteMinSizeSurface > SpectriteModConfig.spectriteMaxSizeSurface) {
				temp = SpectriteModConfig.spectriteMinSizeSurface;
				SpectriteModConfig.spectriteMinSizeSurface = SpectriteModConfig.spectriteMaxSizeSurface;
				SpectriteModConfig.spectriteMaxSizeSurface = temp;
			}
			if (SpectriteModConfig.spectriteMinYSurface > SpectriteModConfig.spectriteMaxYSurface) {
				temp = SpectriteModConfig.spectriteMinYSurface;
				SpectriteModConfig.spectriteMinYSurface = SpectriteModConfig.spectriteMaxYSurface;
				SpectriteModConfig.spectriteMaxYSurface = temp;
			}
			if (SpectriteModConfig.spectriteMinSizeNether > SpectriteModConfig.spectriteMaxSizeNether) {
				temp = SpectriteModConfig.spectriteMinSizeNether;
				SpectriteModConfig.spectriteMinSizeNether = SpectriteModConfig.spectriteMaxSizeNether;
				SpectriteModConfig.spectriteMaxSizeNether = temp;
			}
			if (SpectriteModConfig.spectriteMinYNether > SpectriteModConfig.spectriteMaxYNether) {
				temp = SpectriteModConfig.spectriteMinYNether;
				SpectriteModConfig.spectriteMinYNether = SpectriteModConfig.spectriteMaxYNether;
				SpectriteModConfig.spectriteMaxYNether = temp;
			}
			if (SpectriteModConfig.spectriteMinSizeEnd > SpectriteModConfig.spectriteMaxSizeEnd) {
				temp = SpectriteModConfig.spectriteMinSizeEnd;
				SpectriteModConfig.spectriteMinSizeEnd = SpectriteModConfig.spectriteMaxSizeEnd;
				SpectriteModConfig.spectriteMaxSizeEnd = temp;
			}
			if (SpectriteModConfig.spectriteMinYEnd > SpectriteModConfig.spectriteMaxYEnd) {
				temp = SpectriteModConfig.spectriteMinYEnd;
				SpectriteModConfig.spectriteMinYEnd = SpectriteModConfig.spectriteMaxYEnd;
				SpectriteModConfig.spectriteMaxYEnd = temp;
			}
		}
	}

}
