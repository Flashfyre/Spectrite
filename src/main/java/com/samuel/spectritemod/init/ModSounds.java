package com.samuel.spectritemod.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSounds {
	
	public static SoundEvent explosion, fatality;
	public static final ResourceLocation explosion_rl = new ResourceLocation("spectritemod:explosion"),
			fatality_rl = new ResourceLocation("spectritemod:fatality");
	
	
	private ModSounds() { }
	
	public static void initSounds() {
		GameRegistry.register(explosion = new SoundEvent(explosion_rl).setRegistryName(explosion_rl));
		GameRegistry.register(fatality = new SoundEvent(fatality_rl).setRegistryName(fatality_rl));
	}
}
