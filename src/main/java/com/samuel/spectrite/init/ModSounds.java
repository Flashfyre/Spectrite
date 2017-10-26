package com.samuel.spectrite.init;

import com.samuel.spectrite.helpers.SpectriteHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class ModSounds {
	
	public static SoundEvent charge, explosion, fatality, preexplosion,
		spectrite_golem_attack, spectrite_golem_death, spectrite_golem_hurt,
		spectrite_golem_step, spectrite_creeper_death, spectrite_creeper_hurt,
		spectrite_skeleton_ambient, spectrite_skeleton_death, spectrite_skeleton_hurt, spectrite_skeleton_step,
		spectrite_wither_skeleton_ambient, spectrite_wither_skeleton_death, spectrite_wither_skeleton_hurt,
		spectrite_wither_skeleton_step, spectrite_blaze_ambient, spectrite_blaze_death, spectrite_blaze_hurt,
		spectrite_enderman_ambient, spectrite_enderman_death, spectrite_enderman_hurt,
		spectrite_enderman_scream, spectrite_enderman_stare, spectrite_enderman_teleport,
		spectrite_wither_ambient, spectrite_wither_death, spectrite_wither_hurt, spectrite_wither_spawn;
	public static final ResourceLocation charge_rl = new ResourceLocation("spectrite:charge"), explosion_rl = new ResourceLocation("spectrite:explosion"),
		fatality_rl = new ResourceLocation("spectrite:fatality"), preexplosion_rl = new ResourceLocation("spectrite:preexplosion"),
		spectrite_golem_attack_rl = new ResourceLocation("spectrite:spectrite_golem.attack"),
		spectrite_golem_death_rl = new ResourceLocation("spectrite:spectrite_golem.death"),
		spectrite_golem_hurt_rl = new ResourceLocation("spectrite:spectrite_golem.hurt"),
		spectrite_golem_step_rl = new ResourceLocation("spectrite:spectrite_golem.step"),
		spectrite_creeper_death_rl = new ResourceLocation("spectrite:spectrite_creeper.death"),
		spectrite_creeper_hurt_rl = new ResourceLocation("spectrite:spectrite_creeper.hurt"),
		spectrite_skeleton_ambient_rl = new ResourceLocation("spectrite:spectrite_skeleton.ambient"),
		spectrite_skeleton_death_rl = new ResourceLocation("spectrite:spectrite_skeleton.death"),
		spectrite_skeleton_hurt_rl = new ResourceLocation("spectrite:spectrite_skeleton.hurt"),
		spectrite_skeleton_step_rl = new ResourceLocation("spectrite:spectrite_skeleton.step"),
		spectrite_wither_skeleton_ambient_rl = new ResourceLocation("spectrite:spectrite_wither_skeleton.ambient"),
		spectrite_wither_skeleton_death_rl = new ResourceLocation("spectrite:spectrite_wither_skeleton.death"),
		spectrite_wither_skeleton_hurt_rl = new ResourceLocation("spectrite:spectrite_wither_skeleton.hurt"),
		spectrite_wither_skeleton_step_rl = new ResourceLocation("spectrite:spectrite_wither_skeleton.step"),
		spectrite_blaze_ambient_rl = new ResourceLocation("spectrite:spectrite_blaze.ambient"),
		spectrite_blaze_death_rl = new ResourceLocation("spectrite:spectrite_blaze.death"),
		spectrite_blaze_hurt_rl = new ResourceLocation("spectrite:spectrite_blaze.hurt"),
		spectrite_enderman_ambient_rl = new ResourceLocation("spectrite:spectrite_enderman.ambient"),
		spectrite_enderman_death_rl = new ResourceLocation("spectrite:spectrite_enderman.death"),
		spectrite_enderman_hurt_rl = new ResourceLocation("spectrite:spectrite_enderman.hurt"),
		spectrite_enderman_scream_rl = new ResourceLocation("spectrite:spectrite_enderman.scream"),
		spectrite_enderman_stare_rl = new ResourceLocation("spectrite:spectrite_enderman.stare"),
		spectrite_enderman_teleport_rl = new ResourceLocation("spectrite:spectrite_enderman.teleport"),
		spectrite_wither_ambient_rl = new ResourceLocation("spectrite:spectrite_wither.ambient"),
		spectrite_wither_death_rl = new ResourceLocation("spectrite:spectrite_wither.death"),
		spectrite_wither_hurt_rl = new ResourceLocation("spectrite:spectrite_wither.hurt"),
		spectrite_wither_spawn_rl = new ResourceLocation("spectrite:spectrite_wither.spawn");
	
	private static Map<String, IForgeRegistryEntry> registeredSoundEvents = new HashMap<String, IForgeRegistryEntry>();
	
	public static void initSounds() {
		charge = new SoundEvent(charge_rl).setRegistryName(charge_rl);
		explosion = new SoundEvent(explosion_rl).setRegistryName(explosion_rl);
		fatality = new SoundEvent(fatality_rl).setRegistryName(fatality_rl);
		preexplosion = new SoundEvent(preexplosion_rl).setRegistryName(preexplosion_rl);
		spectrite_golem_attack = new SoundEvent(spectrite_golem_attack_rl).setRegistryName(spectrite_golem_attack_rl);
		spectrite_golem_death = new SoundEvent(spectrite_golem_death_rl).setRegistryName(spectrite_golem_death_rl);
		spectrite_golem_hurt = new SoundEvent(spectrite_golem_hurt_rl).setRegistryName(spectrite_golem_hurt_rl);
		spectrite_golem_step = new SoundEvent(spectrite_golem_step_rl).setRegistryName(spectrite_golem_step_rl);
		spectrite_creeper_death = new SoundEvent(spectrite_creeper_death_rl).setRegistryName(spectrite_creeper_death_rl);
		spectrite_creeper_hurt = new SoundEvent(spectrite_creeper_hurt_rl).setRegistryName(spectrite_creeper_hurt_rl);
		spectrite_skeleton_ambient = new SoundEvent(spectrite_skeleton_ambient_rl).setRegistryName(spectrite_skeleton_ambient_rl);
		spectrite_skeleton_death = new SoundEvent(spectrite_skeleton_death_rl).setRegistryName(spectrite_skeleton_death_rl);
		spectrite_skeleton_hurt = new SoundEvent(spectrite_skeleton_hurt_rl).setRegistryName(spectrite_skeleton_hurt_rl);
		spectrite_skeleton_step = new SoundEvent(spectrite_skeleton_step_rl).setRegistryName(spectrite_skeleton_step_rl);
		spectrite_wither_skeleton_ambient = new SoundEvent(spectrite_wither_skeleton_ambient_rl).setRegistryName(spectrite_wither_skeleton_ambient_rl);
		spectrite_wither_skeleton_death = new SoundEvent(spectrite_wither_skeleton_death_rl).setRegistryName(spectrite_wither_skeleton_death_rl);
		spectrite_wither_skeleton_hurt = new SoundEvent(spectrite_wither_skeleton_hurt_rl).setRegistryName(spectrite_wither_skeleton_hurt_rl);
		spectrite_wither_skeleton_step = new SoundEvent(spectrite_wither_skeleton_step_rl).setRegistryName(spectrite_wither_skeleton_step_rl);
		spectrite_blaze_ambient = new SoundEvent(spectrite_blaze_ambient_rl).setRegistryName(spectrite_blaze_ambient_rl);
		spectrite_blaze_death = new SoundEvent(spectrite_blaze_death_rl).setRegistryName(spectrite_blaze_death_rl);
		spectrite_blaze_hurt = new SoundEvent(spectrite_blaze_hurt_rl).setRegistryName(spectrite_blaze_hurt_rl);
		spectrite_enderman_ambient = new SoundEvent(spectrite_enderman_ambient_rl).setRegistryName(spectrite_enderman_ambient_rl);
		spectrite_enderman_death = new SoundEvent(spectrite_enderman_death_rl).setRegistryName(spectrite_enderman_death_rl);
		spectrite_enderman_hurt = new SoundEvent(spectrite_enderman_hurt_rl).setRegistryName(spectrite_enderman_hurt_rl);
		spectrite_enderman_scream = new SoundEvent(spectrite_enderman_scream_rl).setRegistryName(spectrite_enderman_scream_rl);
		spectrite_enderman_stare = new SoundEvent(spectrite_enderman_stare_rl).setRegistryName(spectrite_enderman_stare_rl);
		spectrite_enderman_teleport = new SoundEvent(spectrite_enderman_teleport_rl).setRegistryName(spectrite_enderman_teleport_rl);
		spectrite_wither_ambient = new SoundEvent(spectrite_wither_ambient_rl).setRegistryName(spectrite_wither_ambient_rl);
		spectrite_wither_death = new SoundEvent(spectrite_wither_death_rl).setRegistryName(spectrite_wither_death_rl);
		spectrite_wither_hurt = new SoundEvent(spectrite_wither_hurt_rl).setRegistryName(spectrite_wither_hurt_rl);
		spectrite_wither_spawn = new SoundEvent(spectrite_wither_spawn_rl).setRegistryName(spectrite_wither_spawn_rl);
	}
	
	@SubscribeEvent
	public void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
		IForgeRegistry<SoundEvent> soundRegistry = event.getRegistry();
		registerSoundEvent(soundRegistry, charge);
		registerSoundEvent(soundRegistry, explosion);
		registerSoundEvent(soundRegistry, fatality);
		registerSoundEvent(soundRegistry, preexplosion);
		registerSoundEvent(soundRegistry, spectrite_golem_attack);
		registerSoundEvent(soundRegistry, spectrite_golem_death);
		registerSoundEvent(soundRegistry, spectrite_golem_hurt);
		registerSoundEvent(soundRegistry, spectrite_golem_step);
		registerSoundEvent(soundRegistry, spectrite_creeper_death);
		registerSoundEvent(soundRegistry, spectrite_creeper_hurt);
		registerSoundEvent(soundRegistry, spectrite_skeleton_ambient);
		registerSoundEvent(soundRegistry, spectrite_skeleton_death);
		registerSoundEvent(soundRegistry, spectrite_skeleton_hurt);
		registerSoundEvent(soundRegistry, spectrite_skeleton_step);
		registerSoundEvent(soundRegistry, spectrite_wither_skeleton_ambient);
		registerSoundEvent(soundRegistry, spectrite_wither_skeleton_death);
		registerSoundEvent(soundRegistry, spectrite_wither_skeleton_hurt);
		registerSoundEvent(soundRegistry, spectrite_wither_skeleton_step);
		registerSoundEvent(soundRegistry, spectrite_blaze_ambient);
		registerSoundEvent(soundRegistry, spectrite_blaze_death);
		registerSoundEvent(soundRegistry, spectrite_blaze_hurt);
		registerSoundEvent(soundRegistry, spectrite_enderman_ambient);
		registerSoundEvent(soundRegistry, spectrite_enderman_death);
		registerSoundEvent(soundRegistry, spectrite_enderman_hurt);
		registerSoundEvent(soundRegistry, spectrite_enderman_scream);
		registerSoundEvent(soundRegistry, spectrite_enderman_stare);
		registerSoundEvent(soundRegistry, spectrite_enderman_teleport);
		registerSoundEvent(soundRegistry, spectrite_wither_ambient);
		registerSoundEvent(soundRegistry, spectrite_wither_death);
		registerSoundEvent(soundRegistry, spectrite_wither_hurt);
		registerSoundEvent(soundRegistry, spectrite_wither_spawn);
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
