package com.samuel.spectritemod.init;

import com.samuel.spectritemod.SpectriteMod;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModWorldGen {

	public static void initWorldGen() {
		GameRegistry.registerWorldGenerator(
			SpectriteMod.spectrite, 618);
	}
}