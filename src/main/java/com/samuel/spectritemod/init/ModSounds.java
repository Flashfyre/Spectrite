package com.samuel.spectritemod.init;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds {
	
	public static SoundEvent explosion, fatality;
	public static final ResourceLocation explosion_rl = new ResourceLocation("spectritemod:explosion"),
			fatality_rl = new ResourceLocation("spectritemod:fatality");
	
	public static void initSounds() {
		explosion = new SoundEvent(explosion_rl).setRegistryName(explosion_rl);
		fatality = new SoundEvent(fatality_rl).setRegistryName(fatality_rl);
	}
	
	@SubscribeEvent
	public void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
		IForgeRegistry<SoundEvent> soundRegistry = event.getRegistry();
		registerSound(soundRegistry, explosion);
		registerSound(soundRegistry, fatality);
	}
	
	private void registerSound(IForgeRegistry<SoundEvent> registry, SoundEvent soundEvent) {
		registry.register(soundEvent);
	}
}
