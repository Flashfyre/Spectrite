package com.samuel.spectritemod.init;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.entities.EntitySpectriteAreaEffectCloud;
import com.samuel.spectritemod.entities.EntitySpectriteArrow;
import com.samuel.spectritemod.entities.EntitySpectriteCreeper;
import com.samuel.spectritemod.entities.EntitySpectriteGolem;
import com.samuel.spectritemod.entities.EntitySpectriteSkeleton;
import com.samuel.spectritemod.entities.EntitySpectriteTippedArrow;
import com.samuel.spectritemod.entities.EntitySpectriteWitherSkeleton;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
	
	private static int entityID = 0;

	public static void initEntities(SpectriteMod mod) {
		EntityRegistry.registerModEntity(new ResourceLocation(SpectriteMod.MOD_ID + ":SpectriteArrow"),
			EntitySpectriteArrow.class, "SpectriteArrow",
			entityID++, mod, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(SpectriteMod.MOD_ID + ":SpectriteGolem"),
			EntitySpectriteGolem.class, "SpectriteGolem",
			entityID++, mod, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(SpectriteMod.MOD_ID + ":SpectriteCreeper"),
			EntitySpectriteCreeper.class, "SpectriteCreeper",
			entityID++, mod, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(SpectriteMod.MOD_ID + ":SpectriteSkeleton"),
			EntitySpectriteSkeleton.class, "SpectriteSkeleton",
			entityID++, mod, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(SpectriteMod.MOD_ID + ":SpectriteWitherSkeleton"),
			EntitySpectriteWitherSkeleton.class, "SpectriteWitherSkeleton",
			entityID++, mod, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(SpectriteMod.MOD_ID + ":SpectriteAreaEffectCloud"),
			EntitySpectriteAreaEffectCloud.class, "SpectriteAreaEffectCloud",
			entityID++, mod, 128, 1, false);
		EntityRegistry.registerModEntity(new ResourceLocation(SpectriteMod.MOD_ID + ":SpectriteTippedArrow"),
			EntitySpectriteTippedArrow.class, "SpectriteTippedArrow",
			entityID++, mod, 128, 1, true);
	}
}
