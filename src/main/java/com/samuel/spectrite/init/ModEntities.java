package com.samuel.spectrite.init;

import java.util.HashMap;
import java.util.Map;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.entities.EntitySpectriteAreaEffectCloud;
import com.samuel.spectrite.entities.EntitySpectriteArrow;
import com.samuel.spectrite.entities.EntitySpectriteBlaze;
import com.samuel.spectrite.entities.EntitySpectriteCreeper;
import com.samuel.spectrite.entities.EntitySpectriteGolem;
import com.samuel.spectrite.entities.EntitySpectriteSkeleton;
import com.samuel.spectrite.entities.EntitySpectriteTippedArrow;
import com.samuel.spectrite.entities.EntitySpectriteWitherSkeleton;
import com.samuel.spectrite.etc.SpectriteHelper;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ModEntities {
	
	private static int entityID = 0;
	
	private static Map<String, IForgeRegistryEntry> registeredEntities = new HashMap<String, IForgeRegistryEntry>();

	public static void initEntities(Spectrite mod) {
		registerEntity("SpectriteArrow", EntitySpectriteArrow.class, mod);
		registerEntity("SpectriteGolem", EntitySpectriteGolem.class, mod);
		registerEntity("SpectriteCreeper", EntitySpectriteCreeper.class, mod);
		registerEntity("SpectriteSkeleton", EntitySpectriteSkeleton.class, mod);
		registerEntity("SpectriteWitherSkeleton", EntitySpectriteWitherSkeleton.class, mod);
		registerEntity("SpectriteBlaze", EntitySpectriteBlaze.class, mod);
		registerEntity("SpectriteAreaEffectCloud", EntitySpectriteAreaEffectCloud.class, mod);
		registerEntity("SpectriteTippedArrow", EntitySpectriteTippedArrow.class, mod);
	}
	
	@SubscribeEvent
	public void onMissingMapping(RegistryEvent.MissingMappings<EntityEntry> e) {
		for (RegistryEvent.MissingMappings.Mapping<EntityEntry> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getResourceDomain())) {
				String resourcePath =  mapping.key.getResourcePath();
				if (registeredEntities.containsKey(resourcePath)) {
					mapping.remap((EntityEntry) registeredEntities.get(resourcePath));
				}
			}
		}
	}
	
	private static void registerEntity(String name, Class<? extends Entity> clazz, Spectrite mod) {
		ResourceLocation location = new ResourceLocation(String.format("%s:%s", Spectrite.MOD_ID, name));
		EntityRegistry.registerModEntity(location, clazz, name, entityID++, mod, 128, 1, true);
		
		SpectriteHelper.populateRegisteredObjectsList(registeredEntities, ForgeRegistries.ENTITIES.getValue(location));
	}
}
