package com.samuel.spectritemod.client.renderer;

import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteArrow;
import com.samuel.spectritemod.entities.EntitySpectriteArrow;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class EntityRenderRegister {
	
	public static void registerEntityRenderer() {
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteArrow.class, RenderSpectriteArrow::new);
	}
}
