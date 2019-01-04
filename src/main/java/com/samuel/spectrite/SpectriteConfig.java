package com.samuel.spectrite;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Spectrite.MOD_ID, name = "spectrite/config")
@Mod.EventBusSubscriber(modid = Spectrite.MOD_ID)
public class SpectriteConfig {
	
	public enum EnumSpectriteArmourBonusMode implements IStringSerializable {
		HALF_BONUSES, NORMAL_BONUSES, OVERPOWERED_BONUSES;

		public float getHealthIncreaseMultiplier() {
			return Math.max(this.ordinal(), 0.5f);
		}

		@Override
		public String getName() {
			return I18n.translateToLocal("config.spectrite_armour_bonus_mode.options." + this.ordinal());
		}
	}
	
	public enum EnumSpectriteArrowDamageMode implements IStringSerializable {
		SPECTRITE_DAMAGE, EXPLOSION, SPECTRITE_DAMAGE_EXPLOSION;

		@Override
		public String getName() {
			return I18n.translateToLocal("config.spectrite_arrow_damage_mode.options." + this.ordinal());
		}
	}
	
	public enum EnumSpectriteDungeonChestMode implements IStringSerializable {
		NONE, HIGH_TIER_ONLY, MEDIUM_AND_HIGH_TIER, ALL_TIERS;

		@Override
		public String getName() {
			return I18n.translateToLocal("config.spectrite_dungeon_chest_mode.options." + this.ordinal());
		}

		public boolean shouldChestTierUseSpectriteChest(int chestTier) {
			return (2 - this.ordinal()) - chestTier < 0;
		}
	}

	public enum EnumSpectriteSkullChestMode implements IStringSerializable {
		NONE, HIGH_TIER_ONLY, BOTH_TIERS;

		@Override
		public String getName() {
			return I18n.translateToLocal("config.spectrite_skull_chest_mode.options." + this.ordinal());
		}
	}

	public static class SpectriteOre {
		@Config.LangKey("config.spectrite_count_surface")
		@Config.RangeInt(min=0, max=20)
		public int spectriteCountSurface = 1;
		@Config.LangKey("config.spectrite_minSize_surface")
		@Config.RangeInt(min=0, max=20)
		public int spectriteMinSizeSurface = 1;
		@Config.LangKey("config.spectrite_maxSize_surface")
		@Config.RangeInt(min=0, max=20)
		public int spectriteMaxSizeSurface = 3;
		@Config.LangKey("config.spectrite_minY_surface")
		@Config.RangeInt(min=1, max=128)
		public int spectriteMinYSurface = 1;
		@Config.LangKey("config.spectrite_maxY_surface")
		@Config.RangeInt(min=1, max=128)
		public int spectriteMaxYSurface = 16;
		@Config.LangKey("config.spectrite_count_nether")
		@Config.RangeInt(min=0, max=20)
		public int spectriteCountNether = 1;
		@Config.LangKey("config.spectrite_minSize_nether")
		@Config.RangeInt(min=0, max=20)
		public int spectriteMinSizeNether = 1;
		@Config.LangKey("config.spectrite_maxSize_nether")
		@Config.RangeInt(min=0, max=20)
		public int spectriteMaxSizeNether = 4;
		@Config.LangKey("config.spectrite_minY_nether")
		@Config.RangeInt(min=1, max=127)
		public int spectriteMinYNether = 1;
		@Config.LangKey("config.spectrite_maxY_nether")
		@Config.RangeInt(min=1, max=127)
		public int spectriteMaxYNether = 127;
		@Config.LangKey("config.spectrite_count_end")
		@Config.RangeInt(min=0, max=20)
		public int spectriteCountEnd = 2;
		@Config.LangKey("config.spectrite_minSize_end")
		@Config.RangeInt(min=0, max=20)
		public int spectriteMinSizeEnd = 1;
		@Config.LangKey("config.spectrite_maxSize_end")
		@Config.RangeInt(min=0, max=20)
		public int spectriteMaxSizeEnd = 7;
		@Config.LangKey("config.spectrite_minY_end")
		@Config.RangeInt(min=4, max=70)
		public int spectriteMinYEnd = 4;
		@Config.LangKey("config.spectrite_maxY_end")
		@Config.RangeInt(min=4, max=70)
		public int spectriteMaxYEnd = 55;
	}

	public static class Blocks {
		@Config.LangKey("config.spectrite_chest_enchant_rate")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteChestEnchantRate = 0.5d;
		@Config.LangKey("config.spectrite_crystal_effect_range")
		@Config.RangeInt(min=1, max=256)
		public int spectriteCrystalEffectRange = 32;
	}

	public static class Items {
		@Config.LangKey("config.spectrite_tool_cooldown")
		@Config.RangeDouble(min=0, max=60)
		public double spectriteToolCooldown = 1.75d;
		@Config.LangKey("config.spectrite_weapon_cooldown")
		@Config.RangeDouble(min=0, max=60)
		public double spectriteWeaponCooldown = 3.5d;
		@Config.LangKey("config.spectrite_orb_cooldown")
		@Config.RangeDouble(min=0, max=3600)
		public double spectriteOrbCooldown = 90.0d;
		@Config.LangKey("config.spectrite_orb_duration")
		@Config.RangeDouble(min=5, max=3600)
		public double spectriteOrbDuration = 15.0d;
		@Config.LangKey("config.spectrite_wither_rod_cooldown")
		@Config.RangeDouble(min=0, max=60)
		public double spectriteWitherRodCooldown = 3.5d;
		@Config.LangKey("config.spectrite_armour_bonus_mode")
		public EnumSpectriteArmourBonusMode spectriteArmourBonusMode = EnumSpectriteArmourBonusMode.NORMAL_BONUSES;
		@Config.LangKey("config.spectrite_arrow_damage_mode")
		public EnumSpectriteArrowDamageMode spectriteArrowDamageMode = EnumSpectriteArrowDamageMode.SPECTRITE_DAMAGE;
		@Config.LangKey("config.spectrite_wither_rod_griefing")
		public boolean spectriteWitherRodGriefing = true;
		@Config.LangKey("config.spectrite_wither_rod_weaken_explosion")
		public boolean spectriteWitherRodWeakenExplosion = true;
		@Config.LangKey("config.spectrite_wither_rod_weaken_spectrite_damage")
		public boolean spectriteWitherRodWeakenSpectriteDamage = true;
	}

	public static class Mobs {
		@Config.LangKey("config.spectrite_mob_spawn_rate")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteMobSpawnRate = 0.1d;
		@Config.LangKey("config.spectrite_mob_spawn_rate_crystal")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteMobCrystalSpawnRate = 1d;
		@Config.LangKey("config.spectrite_mob_perfect_weapon_rate")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteMobPerfectWeaponRate = 2.5d;
		@Config.LangKey("config.spectrite_mob_legend_sword_rate")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteMobLegendSwordRate = 2.5d;
		@Config.LangKey("config.spectrite_mob_armour_drop_rate")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteMobArmourDropRate = 2.5d;
		@Config.LangKey("config.spectrite_mob_sword_drop_rate")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteMobSwordDropRate = 5.0d;
		@Config.LangKey("config.spectrite_mob_bow_drop_rate")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteMobBowDropRate = 5.0d;
		@Config.LangKey("config.spectrite_mob_arrow_drop_rate")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteMobArrowDropRate = 10.0d;
		@Config.LangKey("config.spectrite_skeleton_sword_rate")
		@Config.RangeDouble(min=0, max=80)
		public double spectriteSkeletonSwordRate = 12.5d;
		@Config.LangKey("config.spectrite_wither_skeleton_bow_rate")
		@Config.RangeDouble(min=0, max=80)
		public double spectriteWitherSkeletonBowRate = 12.5d;
		@Config.LangKey("config.spectrite_wither_skeleton_use_skeleton_height")
		public boolean spectriteWitherSkeletonUseSkeletonHeight = false;
		@Config.LangKey("config.spectrite_enderman_orb_drop_rate")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteEndermanOrbDropRate = 100.0d;
	}

	public static class SpectriteDungeon {
		@Config.LangKey("config.generate_spectrite_dungeon")
		public boolean generateSpectriteDungeon = true;
		@Config.LangKey("config.spectrite_dungeon_chest_mode")
		public EnumSpectriteDungeonChestMode spectriteDungeonChestMode = EnumSpectriteDungeonChestMode.HIGH_TIER_ONLY;
	}

	public static class SpectriteSkull {
		@Config.LangKey("config.generate_spectrite_skull")
		public boolean generateSpectriteSkull = true;
		@Config.LangKey("config.spectrite_skull_spawn_rate")
		@Config.RangeDouble(min=0, max=10)
		public double spectriteSkullSpawnRate = 0.25d;
		@Config.LangKey("config.spectrite_skull_surface_rate")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteSkullSurfaceRate = 6.25d;
		@Config.LangKey("config.spectrite_skull_high_tier_chest_rate")
		@Config.RangeDouble(min=0, max=100)
		public double spectriteSkullHighTierChestRate = 20.0d;
		@Config.LangKey("config.spectrite_skull_chest_mode")
		public EnumSpectriteSkullChestMode spectriteSkullChestMode = EnumSpectriteSkullChestMode.HIGH_TIER_ONLY;
	}

	@Config.LangKey("config.spectriteOre")
	public static SpectriteOre spectriteOre = new SpectriteOre();
	@Config.LangKey("config.blocks")
	public static Blocks blocks = new Blocks();
	@Config.LangKey("config.items")
	public static Items items = new Items();
	@Config.LangKey("config.mobs")
	public static Mobs mobs = new Mobs();
	@Config.LangKey("config.spectriteDungeon")
	public static SpectriteDungeon spectriteDungeon = new SpectriteDungeon();
	@Config.LangKey("config.spectriteSkull")
	public static SpectriteSkull spectriteSkull = new SpectriteSkull();
	@Config.LangKey("config.check_for_updates")
	public static boolean checkForUpdates = true;

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent e)
	{
		if(e.getModID().equals(Spectrite.MOD_ID))
		{
			int temp;
			SpectriteOre spectriteOre = SpectriteConfig.spectriteOre;
			if (spectriteOre.spectriteMinSizeSurface > spectriteOre.spectriteMaxSizeSurface) {
				temp = spectriteOre.spectriteMinSizeSurface;
				spectriteOre.spectriteMinSizeSurface = spectriteOre.spectriteMaxSizeSurface;
				spectriteOre.spectriteMaxSizeSurface = temp;
			}
			if (spectriteOre.spectriteMinYSurface > spectriteOre.spectriteMaxYSurface) {
				temp = spectriteOre.spectriteMinYSurface;
				spectriteOre.spectriteMinYSurface = spectriteOre.spectriteMaxYSurface;
				spectriteOre.spectriteMaxYSurface = temp;
			}
			if (spectriteOre.spectriteMinSizeNether > spectriteOre.spectriteMaxSizeNether) {
				temp = spectriteOre.spectriteMinSizeNether;
				spectriteOre.spectriteMinSizeNether = spectriteOre.spectriteMaxSizeNether;
				spectriteOre.spectriteMaxSizeNether = temp;
			}
			if (spectriteOre.spectriteMinYNether > spectriteOre.spectriteMaxYNether) {
				temp = spectriteOre.spectriteMinYNether;
				spectriteOre.spectriteMinYNether = spectriteOre.spectriteMaxYNether;
				spectriteOre.spectriteMaxYNether = temp;
			}
			if (spectriteOre.spectriteMinSizeEnd > spectriteOre.spectriteMaxSizeEnd) {
				temp = spectriteOre.spectriteMinSizeEnd;
				spectriteOre.spectriteMinSizeEnd = spectriteOre.spectriteMaxSizeEnd;
				spectriteOre.spectriteMaxSizeEnd = temp;
			}
			if (spectriteOre.spectriteMinYEnd > spectriteOre.spectriteMaxYEnd) {
				temp = spectriteOre.spectriteMinYEnd;
				spectriteOre.spectriteMinYEnd = spectriteOre.spectriteMaxYEnd;
				spectriteOre.spectriteMaxYEnd = temp;
			}
			if (SpectriteConfig.items.spectriteOrbCooldown < SpectriteConfig.items.spectriteOrbDuration) {
				SpectriteConfig.items.spectriteOrbCooldown = SpectriteConfig.items.spectriteOrbDuration;
			}
			ConfigManager.sync(Spectrite.MOD_ID, Config.Type.INSTANCE);
		}
	}

}