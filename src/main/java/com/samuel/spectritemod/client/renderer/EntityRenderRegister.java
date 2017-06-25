package com.samuel.spectritemod.client.renderer;

import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteArrow;
import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteGolem;
import com.samuel.spectritemod.entities.EntitySpectriteArrow;
import com.samuel.spectritemod.entities.EntitySpectriteGolem;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class EntityRenderRegister {
	
	public static void registerEntityRenderer() {
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteArrow.class, RenderSpectriteArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteGolem.class, RenderSpectriteGolem::new);
	}
}
