package com.samuel.spectritemod.client.renderer;

import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteArrow;
import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteGolem;
import com.samuel.spectritemod.entities.EntitySpectriteArrow;
import com.samuel.spectritemod.entities.EntitySpectriteGolem;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityRenderRegister {
	
	@SubscribeEvent
	public void onRegisterEntityModels(ModelRegistryEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteArrow.class, RenderSpectriteArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteGolem.class, RenderSpectriteGolem::new);
	}
}
