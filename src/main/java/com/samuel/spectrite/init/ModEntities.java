package com.samuel.spectrite.init;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.entities.EntitySpectriteAreaEffectCloud;
import com.samuel.spectrite.entities.EntitySpectriteArrow;
import com.samuel.spectrite.entities.EntitySpectriteCreeper;
import com.samuel.spectrite.entities.EntitySpectriteGolem;
import com.samuel.spectrite.entities.EntitySpectriteSkeleton;
import com.samuel.spectrite.entities.EntitySpectriteTippedArrow;
import com.samuel.spectrite.entities.EntitySpectriteWitherSkeleton;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
	
	private static int entityID = 0;

	public static void initEntities(Spectrite mod) {
		EntityRegistry.registerModEntity(new ResourceLocation(Spectrite.MOD_ID + ":SpectriteArrow"),
			EntitySpectriteArrow.class, "SpectriteArrow",
			entityID++, mod, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Spectrite.MOD_ID + ":SpectriteGolem"),
			EntitySpectriteGolem.class, "SpectriteGolem",
			entityID++, mod, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Spectrite.MOD_ID + ":SpectriteCreeper"),
			EntitySpectriteCreeper.class, "SpectriteCreeper",
			entityID++, mod, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Spectrite.MOD_ID + ":SpectriteSkeleton"),
			EntitySpectriteSkeleton.class, "SpectriteSkeleton",
			entityID++, mod, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Spectrite.MOD_ID + ":SpectriteWitherSkeleton"),
			EntitySpectriteWitherSkeleton.class, "SpectriteWitherSkeleton",
			entityID++, mod, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Spectrite.MOD_ID + ":SpectriteAreaEffectCloud"),
			EntitySpectriteAreaEffectCloud.class, "SpectriteAreaEffectCloud",
			entityID++, mod, 128, 1, false);
		EntityRegistry.registerModEntity(new ResourceLocation(Spectrite.MOD_ID + ":SpectriteTippedArrow"),
			EntitySpectriteTippedArrow.class, "SpectriteTippedArrow",
			entityID++, mod, 128, 1, true);
	}
}
