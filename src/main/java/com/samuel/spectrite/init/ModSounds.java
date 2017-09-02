package com.samuel.spectrite.init;

import com.samuel.spectrite.etc.SpectriteHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class ModSounds {
	
	public static SoundEvent charge, explosion, fatality, preexplosion;
	public static final ResourceLocation charge_rl = new ResourceLocation("spectrite:charge"), explosion_rl = new ResourceLocation("spectrite:explosion"),
			fatality_rl = new ResourceLocation("spectrite:fatality"), preexplosion_rl = new ResourceLocation("spectrite:preexplosion");
	
	private static Map<String, IForgeRegistryEntry> registeredSoundEvents = new HashMap<String, IForgeRegistryEntry>();
	
	public static void initSounds() {
		charge = new SoundEvent(charge_rl).setRegistryName(charge_rl);
		explosion = new SoundEvent(explosion_rl).setRegistryName(explosion_rl);
		fatality = new SoundEvent(fatality_rl).setRegistryName(fatality_rl);
		preexplosion = new SoundEvent(preexplosion_rl).setRegistryName(preexplosion_rl);
	}
	
	@SubscribeEvent
	public void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
		IForgeRegistry<SoundEvent> soundRegistry = event.getRegistry();
		registerSoundEvent(soundRegistry, charge);
		registerSoundEvent(soundRegistry, explosion);
		registerSoundEvent(soundRegistry, fatality);
		registerSoundEvent(soundRegistry, preexplosion);
	}
	
	@SubscribeEvent
	public void onMissingMapping(RegistryEvent.MissingMappings<SoundEvent> e) {
		for (RegistryEvent.MissingMappings.Mapping<SoundEvent> mapping : e.getAllMappings()) {
			if ("spectritemod".equals(mapping.key.getResourceDomain())) {
				String resourcePath =  mapping.key.getResourcePath();
				if (registeredSoundEvents.containsKey(resourcePath)) {
					mapping.remap((SoundEvent) registeredSoundEvents.get(resourcePath));
				}
			}
		}
	}
	
	private void registerSoundEvent(IForgeRegistry<SoundEvent> registry, SoundEvent soundEvent) {
		registry.register(soundEvent);
		
		SpectriteHelper.populateRegisteredObjectsList(registeredSoundEvents, soundEvent);
	}
}
