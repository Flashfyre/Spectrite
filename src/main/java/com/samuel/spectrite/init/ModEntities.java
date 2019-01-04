package com.samuel.spectrite.init;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.entities.*;
import com.samuel.spectrite.helpers.SpectriteHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class ModEntities {
	
	private static int entityID = 0;
	
	private static Map<String, IForgeRegistryEntry> registeredEntities = new HashMap<String, IForgeRegistryEntry>();

	public static void initEntities(Spectrite mod) {
		registerEntity("SpectriteArrow", EntitySpectriteArrow.class, mod, false);
		registerEntity("SpectriteGolem", EntitySpectriteGolem.class, mod, true);
		registerEntity("SpectriteCreeper", EntitySpectriteCreeper.class, mod, true);
		registerEntity("SpectriteSkeleton", EntitySpectriteSkeleton.class, mod, true);
		registerEntity("SpectriteWitherSkeleton", EntitySpectriteWitherSkeleton.class, mod, true);
		registerEntity("SpectriteBlaze", EntitySpectriteBlaze.class, mod, true);
		registerEntity("SpectriteEnderman", EntitySpectriteEnderman.class, mod, true);
		registerEntity("SpectriteWither", EntitySpectriteWither.class, mod, false);
		registerEntity("SpectriteWitherSkull", EntitySpectriteWitherSkull.class, mod, false);
		registerEntity("SpectriteAreaEffectCloud", EntitySpectriteAreaEffectCloud.class, mod, false);
		registerEntity("SpectriteTippedArrow", EntitySpectriteTippedArrow.class, mod, false);
		registerEntity("SpectriteCrystal", EntitySpectriteCrystal.class, mod, false);
	}
	
	@SubscribeEvent
	public void onMissingMapping(RegistryEvent.MissingMappings<EntityEntry> e) {
		for (RegistryEvent.MissingMappings.Mapping<EntityEntry> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getNamespace())) {
				String resourcePath =  mapping.key.getPath();
				if (registeredEntities.containsKey(resourcePath)) {
					mapping.remap((EntityEntry) registeredEntities.get(resourcePath));
				}
			}
		}
	}
	
	private static void registerEntity(String name, Class<? extends Entity> clazz, Spectrite mod, boolean addSpawnEgg) {
		ResourceLocation location = new ResourceLocation(String.format("%s:%s", Spectrite.MOD_ID, name));
		if (addSpawnEgg) {
			EntityRegistry.registerModEntity(location, clazz, name, entityID++, mod, 128, 1,
				true, 0, 0);
		} else {
			EntityRegistry.registerModEntity(location, clazz, name, entityID++, mod, 128, 1, true);
		}
		
		SpectriteHelper.populateRegisteredObjectsList(registeredEntities, ForgeRegistries.ENTITIES.getValue(location));
	}
}
