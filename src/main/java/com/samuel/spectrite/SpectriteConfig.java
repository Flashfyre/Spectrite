package com.samuel.spectrite;

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

public class SpectriteConfig {
	
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
			
			return Spectrite.SPECTRITE_ARMOUR_BONUS_MODE_DEFAULT;
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
			
			return Spectrite.SPECTRITE_ARROW_DAMAGE_MODE_DEFAULT;
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
			
			return Spectrite.SPECTRITE_DUNGEON_CHEST_MODE_DEFAULT;
		}
		
		public boolean shouldChestTierUseSpectriteChest(int chestTier) {
			return (2 - this.ordinal()) - chestTier < 0;
		}
	}
	
	public static int spectriteCountSurface = Spectrite.SPECTRITE_COUNT_SURFACE_DEFAULT;
	public static int spectriteMinSizeSurface = Spectrite.SPECTRITE_MIN_SIZE_SURFACE_DEFAULT;
	public static int spectriteMaxSizeSurface = Spectrite.SPECTRITE_MAX_SIZE_SURFACE_DEFAULT;
	public static int spectriteMinYSurface = Spectrite.SPECTRITE_MIN_Y_SURFACE_DEFAULT;
	public static int spectriteMaxYSurface = Spectrite.SPECTRITE_MAX_Y_SURFACE_DEFAULT;
	public static int spectriteCountNether = Spectrite.SPECTRITE_COUNT_NETHER_DEFAULT;
	public static int spectriteMinSizeNether = Spectrite.SPECTRITE_MIN_SIZE_NETHER_DEFAULT;
	public static int spectriteMaxSizeNether = Spectrite.SPECTRITE_MAX_SIZE_NETHER_DEFAULT;
	public static int spectriteMinYNether = Spectrite.SPECTRITE_MIN_Y_NETHER_DEFAULT;
	public static int spectriteMaxYNether = Spectrite.SPECTRITE_MAX_Y_NETHER_DEFAULT;
	public static int spectriteCountEnd = Spectrite.SPECTRITE_COUNT_END_DEFAULT;
	public static int spectriteMinSizeEnd = Spectrite.SPECTRITE_MIN_SIZE_END_DEFAULT;
	public static int spectriteMaxSizeEnd = Spectrite.SPECTRITE_MAX_SIZE_END_DEFAULT;
	public static int spectriteMinYEnd = Spectrite.SPECTRITE_MIN_Y_END_DEFAULT;
	public static int spectriteMaxYEnd = Spectrite.SPECTRITE_MAX_Y_END_DEFAULT;
	public static EnumSpectriteArmourBonusMode spectriteArmourBonusMode = Spectrite.SPECTRITE_ARMOUR_BONUS_MODE_DEFAULT;
	public static EnumSpectriteArrowDamageMode spectriteArrowDamageMode = Spectrite.SPECTRITE_ARROW_DAMAGE_MODE_DEFAULT;
	public static double spectriteToolCooldown = Spectrite.SPECTRITE_TOOL_COOLDOWN_DEFAULT;
	public static double spectriteMobSpawnRate = Spectrite.SPECTRITE_MOB_SPAWN_RATE_DEFAULT;
	public static double spectriteMobBossSpawnRate = Spectrite.SPECTRITE_MOB_BOSS_SPAWN_RATE_DEFAULT;
	public static double spectriteMobPerfectWeaponRate = Spectrite.SPECTRITE_MOB_PERFECT_WEAPON_RATE_DEFAULT;
	public static double spectriteMobLegendSwordRate = Spectrite.SPECTRITE_MOB_LEGEND_SWORD_RATE_DEFAULT;
	public static double spectriteMobArmourDropRate = Spectrite.SPECTRITE_MOB_ARMOUR_DROP_RATE_DEFAULT;
	public static double spectriteMobSwordDropRate = Spectrite.SPECTRITE_MOB_SWORD_DROP_RATE_DEFAULT;
	public static double spectriteMobOrbDropRate = Spectrite.SPECTRITE_MOB_ORB_DROP_RATE_DEFAULT;
	public static double spectriteMobBowDropRate = Spectrite.SPECTRITE_MOB_BOW_DROP_RATE_DEFAULT;
	public static double spectriteMobArrowDropRate = Spectrite.SPECTRITE_MOB_ARROW_DROP_RATE_DEFAULT;
	public static double spectriteChestEnchantRate = Spectrite.SPECTRITE_CHEST_ENCHANT_RATE_DEFAULT;
	public static boolean generateSpectriteDungeon = Spectrite.GENERATE_SPECTRITE_DUNGEON_DEFAULT;
	public static EnumSpectriteDungeonChestMode spectriteDungeonChestMode = Spectrite.SPECTRITE_DUNGEON_CHEST_MODE_DEFAULT;
	public static boolean generateSpectriteSkull = Spectrite.GENERATE_SPECTRITE_SKULL_DEFAULT;
	public static double spectriteSkullSpawnRate = Spectrite.SPECTRITE_SKULL_SPAWN_RATE_DEFAULT;
	public static double spectriteSkullSurfaceRate = Spectrite.SPECTRITE_SKULL_SURFACE_RATE_DEFAULT;
	
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
	public static Property propGenerateSpectriteSkull = null;
	public static Property propSpectriteSkullSpawnRate = null;
	public static Property propSpectriteSkullSurfaceRate = null;
	
	public static List<String> propertyNames = null;
	public static List<String> propertyDescriptions = null;
	
	public final Configuration configuration;
	
	public SpectriteConfig(File file)
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
			"spectrite_dungeon_chest_mode",
			"generate_spectrite_skull",
			"spectrite_skull_spawn_rate",
			"spectrite_skull_surface_rate"
		};
		
		SpectriteConfig.propertyNames = new ArrayList<String>();
		SpectriteConfig.propertyDescriptions = new ArrayList<String>();
		
		for (String k : propertyKeys) {
			SpectriteConfig.propertyNames.add(I18n.translateToLocal("config." + k + ".name"));
			SpectriteConfig.propertyDescriptions.add(I18n.translateToLocal("config." + k + ".desc"));
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
				propertyNames.get(0), Spectrite.SPECTRITE_COUNT_SURFACE_DEFAULT,
				propertyDescriptions.get(0), 0, 20);
		propSpectriteMinSizeSurface = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(1), Spectrite.SPECTRITE_MIN_SIZE_SURFACE_DEFAULT,
				propertyDescriptions.get(1), 0, 20);
		propSpectriteMaxSizeSurface = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(2), Spectrite.SPECTRITE_MAX_SIZE_SURFACE_DEFAULT,
				propertyDescriptions.get(2), Math.max(0, propSpectriteMinSizeSurface.getInt(Spectrite.SPECTRITE_MIN_SIZE_SURFACE_DEFAULT)), 20);
		propSpectriteMinSizeSurface.setMaxValue(Math.min(20, propSpectriteMaxSizeSurface.getInt(Spectrite.SPECTRITE_MAX_SIZE_SURFACE_DEFAULT)));
		propSpectriteMinYSurface = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(3), Spectrite.SPECTRITE_MIN_Y_SURFACE_DEFAULT,
				propertyDescriptions.get(3), 1, 128);
		propSpectriteMaxYSurface = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(4), Spectrite.SPECTRITE_MAX_Y_SURFACE_DEFAULT,
				propertyDescriptions.get(4), Math.max(1, propSpectriteMinYSurface.getInt(Spectrite.SPECTRITE_MIN_Y_SURFACE_DEFAULT)), 128);
		propSpectriteMinYSurface.setMaxValue(Math.min(128, propSpectriteMaxYSurface.getInt(Spectrite.SPECTRITE_MAX_Y_SURFACE_DEFAULT)));
		propSpectriteCountNether = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(5), Spectrite.SPECTRITE_COUNT_NETHER_DEFAULT,
				propertyDescriptions.get(5), 0, 20);
		propSpectriteMinSizeNether = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(6), Spectrite.SPECTRITE_MIN_SIZE_NETHER_DEFAULT,
				propertyDescriptions.get(6), 0, 20);
		propSpectriteMaxSizeNether = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(7), Spectrite.SPECTRITE_MAX_SIZE_NETHER_DEFAULT,
				propertyDescriptions.get(7), Math.max(0, propSpectriteMinSizeNether.getInt(Spectrite.SPECTRITE_MIN_SIZE_NETHER_DEFAULT)), 20);
		propSpectriteMinSizeNether.setMaxValue(Math.min(20, propSpectriteMaxSizeNether.getInt(Spectrite.SPECTRITE_MAX_SIZE_NETHER_DEFAULT)));
		propSpectriteMinYNether = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(8), Spectrite.SPECTRITE_MIN_Y_NETHER_DEFAULT,
				propertyDescriptions.get(8), 1, 127);
		propSpectriteMaxYNether = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(9), Spectrite.SPECTRITE_MAX_Y_NETHER_DEFAULT,
				propertyDescriptions.get(9), Math.max(1, propSpectriteMinYNether.getInt(Spectrite.SPECTRITE_MIN_Y_NETHER_DEFAULT)), 127);
		propSpectriteMinYNether.setMaxValue(Math.min(127, propSpectriteMaxYNether.getInt(Spectrite.SPECTRITE_MAX_Y_NETHER_DEFAULT)));
		propSpectriteCountEnd = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(10), Spectrite.SPECTRITE_COUNT_END_DEFAULT,
				propertyDescriptions.get(10), 0, 20);
		propSpectriteMinSizeEnd = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(11), Spectrite.SPECTRITE_MIN_SIZE_END_DEFAULT,
				propertyDescriptions.get(11), 0, 20);
		propSpectriteMaxSizeEnd = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(12), Spectrite.SPECTRITE_MAX_SIZE_END_DEFAULT,
				propertyDescriptions.get(12), Math.max(0, propSpectriteMinSizeEnd.getInt(Spectrite.SPECTRITE_MIN_SIZE_END_DEFAULT)), 20);
		propSpectriteMinSizeEnd.setMaxValue(Math.min(20, propSpectriteMaxSizeEnd.getInt(Spectrite.SPECTRITE_MAX_SIZE_END_DEFAULT)));
		propSpectriteMinYEnd = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(13), Spectrite.SPECTRITE_MIN_Y_END_DEFAULT,
				propertyDescriptions.get(13), 4, 70);
		propSpectriteMaxYEnd = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(14), Spectrite.SPECTRITE_MAX_Y_END_DEFAULT,
				propertyDescriptions.get(14), Math.max(4, propSpectriteMinYEnd.getInt(Spectrite.SPECTRITE_MIN_Y_END_DEFAULT)), 70);
		propSpectriteMinYEnd.setMaxValue(Math.min(55, propSpectriteMaxYEnd.getInt(Spectrite.SPECTRITE_MAX_Y_END_DEFAULT)));
		propSpectriteArmourBonusMode = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(15), Spectrite.SPECTRITE_ARMOUR_BONUS_MODE_DEFAULT.getName(),
				propertyDescriptions.get(15), EnumSpectriteArmourBonusMode.getAllNames());
		propSpectriteArrowDamageMode = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(16), Spectrite.SPECTRITE_ARROW_DAMAGE_MODE_DEFAULT.getName(),
				propertyDescriptions.get(16), EnumSpectriteArrowDamageMode.getAllNames());
		propSpectriteToolCooldown = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(17), Spectrite.SPECTRITE_TOOL_COOLDOWN_DEFAULT,
				propertyDescriptions.get(17), 0, 60);
		propSpectriteMobSpawnRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(18), Spectrite.SPECTRITE_MOB_SPAWN_RATE_DEFAULT,
				propertyDescriptions.get(18), 0, 100);
		propSpectriteMobBossSpawnRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(19), Spectrite.SPECTRITE_MOB_BOSS_SPAWN_RATE_DEFAULT,
				propertyDescriptions.get(19), 0, 100);
		propSpectriteMobPerfectWeaponRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(20), Spectrite.SPECTRITE_MOB_PERFECT_WEAPON_RATE_DEFAULT,
				propertyDescriptions.get(20), 0, 100);
		propSpectriteMobLegendSwordRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(21), Spectrite.SPECTRITE_MOB_LEGEND_SWORD_RATE_DEFAULT,
				propertyDescriptions.get(21), 0, 100);
		propSpectriteMobArmourDropRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(22), Spectrite.SPECTRITE_MOB_ARMOUR_DROP_RATE_DEFAULT,
				propertyDescriptions.get(22), 0, 100);
		propSpectriteMobSwordDropRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(23), Spectrite.SPECTRITE_MOB_SWORD_DROP_RATE_DEFAULT,
				propertyDescriptions.get(23), 0, 100);
		propSpectriteMobOrbDropRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(24), Spectrite.SPECTRITE_MOB_ORB_DROP_RATE_DEFAULT,
				propertyDescriptions.get(24), 0, 100);
		propSpectriteMobBowDropRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(25), Spectrite.SPECTRITE_MOB_BOW_DROP_RATE_DEFAULT,
				propertyDescriptions.get(25), 0, 100);
		propSpectriteMobArrowDropRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(26), Spectrite.SPECTRITE_MOB_ARROW_DROP_RATE_DEFAULT,
				propertyDescriptions.get(26), 0, 100);
		propSpectriteChestEnchantRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(27), Spectrite.SPECTRITE_CHEST_ENCHANT_RATE_DEFAULT,
				propertyDescriptions.get(27), 0, 100);
		propGenerateSpectriteDungeon = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(28), Spectrite.GENERATE_SPECTRITE_DUNGEON_DEFAULT,
				propertyDescriptions.get(28));
		propSpectriteDungeonChestMode = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(29), Spectrite.SPECTRITE_DUNGEON_CHEST_MODE_DEFAULT.getName(),
				propertyDescriptions.get(29), EnumSpectriteDungeonChestMode.getAllNames());
		propGenerateSpectriteSkull = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(30), Spectrite.GENERATE_SPECTRITE_SKULL_DEFAULT,
				propertyDescriptions.get(30));
		propSpectriteSkullSpawnRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(31), Spectrite.SPECTRITE_SKULL_SPAWN_RATE_DEFAULT,
				propertyDescriptions.get(31), 0, 10);
		propSpectriteSkullSurfaceRate = configuration.get(
				Configuration.CATEGORY_GENERAL,
				propertyNames.get(32), Spectrite.SPECTRITE_SKULL_SURFACE_RATE_DEFAULT,
				propertyDescriptions.get(32), 0, 100);
		
		SpectriteConfig.spectriteCountSurface = propSpectriteCountSurface.getInt(Spectrite.SPECTRITE_COUNT_SURFACE_DEFAULT);
		SpectriteConfig.spectriteMinSizeSurface = propSpectriteMinSizeSurface.getInt(Spectrite.SPECTRITE_MIN_SIZE_SURFACE_DEFAULT);
		SpectriteConfig.spectriteMaxSizeSurface = propSpectriteMaxSizeSurface.getInt(Spectrite.SPECTRITE_MAX_SIZE_SURFACE_DEFAULT);
		SpectriteConfig.spectriteMinYSurface = propSpectriteMinYSurface.getInt(Spectrite.SPECTRITE_MIN_Y_SURFACE_DEFAULT);
		SpectriteConfig.spectriteMaxYSurface = propSpectriteMaxYSurface.getInt(Spectrite.SPECTRITE_MAX_Y_SURFACE_DEFAULT);
		SpectriteConfig.spectriteCountNether = propSpectriteCountNether.getInt(Spectrite.SPECTRITE_COUNT_NETHER_DEFAULT);
		SpectriteConfig.spectriteMinSizeNether = propSpectriteMinSizeNether.getInt(Spectrite.SPECTRITE_MIN_SIZE_NETHER_DEFAULT);
		SpectriteConfig.spectriteMaxSizeNether = propSpectriteMaxSizeNether.getInt(Spectrite.SPECTRITE_MAX_SIZE_NETHER_DEFAULT);
		SpectriteConfig.spectriteMinYNether = propSpectriteMinYNether.getInt(Spectrite.SPECTRITE_MIN_Y_NETHER_DEFAULT);
		SpectriteConfig.spectriteMaxYNether = propSpectriteMaxYNether.getInt(Spectrite.SPECTRITE_MAX_Y_NETHER_DEFAULT);
		SpectriteConfig.spectriteCountEnd = propSpectriteCountEnd.getInt(Spectrite.SPECTRITE_COUNT_END_DEFAULT);
		SpectriteConfig.spectriteMinSizeEnd = propSpectriteMinSizeEnd.getInt(Spectrite.SPECTRITE_MIN_SIZE_END_DEFAULT);
		SpectriteConfig.spectriteMaxSizeEnd = propSpectriteMaxSizeEnd.getInt(Spectrite.SPECTRITE_MAX_SIZE_END_DEFAULT);
		SpectriteConfig.spectriteMinYEnd = propSpectriteMinYEnd.getInt(Spectrite.SPECTRITE_MIN_Y_END_DEFAULT);
		SpectriteConfig.spectriteMaxYEnd = propSpectriteMaxYEnd.getInt(Spectrite.SPECTRITE_MAX_Y_END_DEFAULT);
		SpectriteConfig.spectriteArmourBonusMode = EnumSpectriteArmourBonusMode.forName(propSpectriteArmourBonusMode.getString());
		SpectriteConfig.spectriteArrowDamageMode = EnumSpectriteArrowDamageMode.forName(propSpectriteArrowDamageMode.getString());
		SpectriteConfig.spectriteToolCooldown = propSpectriteToolCooldown.getDouble(Spectrite.SPECTRITE_TOOL_COOLDOWN_DEFAULT);
		SpectriteConfig.spectriteMobSpawnRate = propSpectriteMobSpawnRate.getDouble(Spectrite.SPECTRITE_MOB_SPAWN_RATE_DEFAULT);
		SpectriteConfig.spectriteMobBossSpawnRate = propSpectriteMobBossSpawnRate.getDouble(Spectrite.SPECTRITE_MOB_BOSS_SPAWN_RATE_DEFAULT);
		SpectriteConfig.spectriteMobPerfectWeaponRate = propSpectriteMobPerfectWeaponRate.getDouble(Spectrite.SPECTRITE_MOB_PERFECT_WEAPON_RATE_DEFAULT);
		SpectriteConfig.spectriteMobLegendSwordRate = propSpectriteMobLegendSwordRate.getDouble(Spectrite.SPECTRITE_MOB_LEGEND_SWORD_RATE_DEFAULT);
		SpectriteConfig.spectriteMobArmourDropRate = propSpectriteMobArmourDropRate.getDouble(Spectrite.SPECTRITE_MOB_ARMOUR_DROP_RATE_DEFAULT);
		SpectriteConfig.spectriteMobSwordDropRate = propSpectriteMobSwordDropRate.getDouble(Spectrite.SPECTRITE_MOB_SWORD_DROP_RATE_DEFAULT);
		SpectriteConfig.spectriteMobOrbDropRate = propSpectriteMobOrbDropRate.getDouble(Spectrite.SPECTRITE_MOB_ORB_DROP_RATE_DEFAULT);
		SpectriteConfig.spectriteMobBowDropRate = propSpectriteMobBowDropRate.getDouble(Spectrite.SPECTRITE_MOB_BOW_DROP_RATE_DEFAULT);
		SpectriteConfig.spectriteMobArrowDropRate = propSpectriteMobArrowDropRate.getDouble(Spectrite.SPECTRITE_MOB_ARROW_DROP_RATE_DEFAULT);
		SpectriteConfig.spectriteChestEnchantRate = propSpectriteChestEnchantRate.getDouble(Spectrite.SPECTRITE_CHEST_ENCHANT_RATE_DEFAULT);
		SpectriteConfig.generateSpectriteDungeon = propGenerateSpectriteDungeon.getBoolean(Spectrite.GENERATE_SPECTRITE_DUNGEON_DEFAULT);
		SpectriteConfig.spectriteDungeonChestMode = EnumSpectriteDungeonChestMode.forName(propSpectriteDungeonChestMode.getString());
		SpectriteConfig.generateSpectriteSkull = propGenerateSpectriteSkull.getBoolean(Spectrite.GENERATE_SPECTRITE_SKULL_DEFAULT);
		SpectriteConfig.spectriteSkullSpawnRate = propSpectriteSkullSpawnRate.getDouble(Spectrite.SPECTRITE_SKULL_SPAWN_RATE_DEFAULT);
		SpectriteConfig.spectriteSkullSurfaceRate = propSpectriteSkullSurfaceRate.getDouble(Spectrite.SPECTRITE_SKULL_SURFACE_RATE_DEFAULT);

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
		if(e.getModID().equals(Spectrite.MOD_ID))
		{
			this.loadConfig();
			
			int temp = 0;
			if (SpectriteConfig.spectriteMinSizeSurface > SpectriteConfig.spectriteMaxSizeSurface) {
				temp = SpectriteConfig.spectriteMinSizeSurface;
				SpectriteConfig.spectriteMinSizeSurface = SpectriteConfig.spectriteMaxSizeSurface;
				SpectriteConfig.spectriteMaxSizeSurface = temp;
			}
			if (SpectriteConfig.spectriteMinYSurface > SpectriteConfig.spectriteMaxYSurface) {
				temp = SpectriteConfig.spectriteMinYSurface;
				SpectriteConfig.spectriteMinYSurface = SpectriteConfig.spectriteMaxYSurface;
				SpectriteConfig.spectriteMaxYSurface = temp;
			}
			if (SpectriteConfig.spectriteMinSizeNether > SpectriteConfig.spectriteMaxSizeNether) {
				temp = SpectriteConfig.spectriteMinSizeNether;
				SpectriteConfig.spectriteMinSizeNether = SpectriteConfig.spectriteMaxSizeNether;
				SpectriteConfig.spectriteMaxSizeNether = temp;
			}
			if (SpectriteConfig.spectriteMinYNether > SpectriteConfig.spectriteMaxYNether) {
				temp = SpectriteConfig.spectriteMinYNether;
				SpectriteConfig.spectriteMinYNether = SpectriteConfig.spectriteMaxYNether;
				SpectriteConfig.spectriteMaxYNether = temp;
			}
			if (SpectriteConfig.spectriteMinSizeEnd > SpectriteConfig.spectriteMaxSizeEnd) {
				temp = SpectriteConfig.spectriteMinSizeEnd;
				SpectriteConfig.spectriteMinSizeEnd = SpectriteConfig.spectriteMaxSizeEnd;
				SpectriteConfig.spectriteMaxSizeEnd = temp;
			}
			if (SpectriteConfig.spectriteMinYEnd > SpectriteConfig.spectriteMaxYEnd) {
				temp = SpectriteConfig.spectriteMinYEnd;
				SpectriteConfig.spectriteMinYEnd = SpectriteConfig.spectriteMaxYEnd;
				SpectriteConfig.spectriteMaxYEnd = temp;
			}
		}
	}

}