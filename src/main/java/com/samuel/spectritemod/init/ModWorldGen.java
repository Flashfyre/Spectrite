package com.samuel.spectritemod.init;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.samuel.spectritemod.SpectriteMod;

public class ModWorldGen {

	public static void initWorldGen() {
		GameRegistry.registerWorldGenerator(
			SpectriteMod.spectrite, 618);
	}
}