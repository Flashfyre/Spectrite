package com.samuel.spectritemod.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds {
	
	public static SoundEvent charge, explosion, fatality;
	public static final ResourceLocation charge_rl = new ResourceLocation("spectritemod:charge"), explosion_rl = new ResourceLocation("spectritemod:explosion"),
			fatality_rl = new ResourceLocation("spectritemod:fatality");
	
	public static void initSounds() {
		charge = new SoundEvent(charge_rl).setRegistryName(charge_rl);
		explosion = new SoundEvent(explosion_rl).setRegistryName(explosion_rl);
		fatality = new SoundEvent(fatality_rl).setRegistryName(fatality_rl);
	}
	
	@SubscribeEvent
	public void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
		IForgeRegistry<SoundEvent> soundRegistry = event.getRegistry();
		registerSound(soundRegistry, charge);
		registerSound(soundRegistry, explosion);
		registerSound(soundRegistry, fatality);
	}
	
	private void registerSound(IForgeRegistry<SoundEvent> registry, SoundEvent soundEvent) {
		registry.register(soundEvent);
	}
}
