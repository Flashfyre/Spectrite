package com.samuel.spectritemod.init;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.entities.EntitySpectriteArrow;
import com.samuel.spectritemod.entities.EntitySpectriteGolem;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
	
	private static int entityID = 0;

	public static void initEntities(SpectriteMod mod) {
		EntityRegistry.registerModEntity(new ResourceLocation(mod.MOD_ID + ":SpectriteArrow"),
			EntitySpectriteArrow.class, "SpectriteArrow",
			entityID++, mod, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(mod.MOD_ID + ":SpectriteGolem"),
			EntitySpectriteGolem.class, "SpectriteGolem",
			entityID++, mod, 128, 1, true);
	}
}
