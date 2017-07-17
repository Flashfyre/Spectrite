package com.samuel.spectritemod.client.renderer;

import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteAreaEffectCloud;
import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteArrow;
import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteCreeper;
import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteGolem;
import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteSkeleton;
import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteTippedArrow;
import com.samuel.spectritemod.client.renderer.entity.RenderSpectriteWitherSkeleton;
import com.samuel.spectritemod.entities.EntitySpectriteAreaEffectCloud;
import com.samuel.spectritemod.entities.EntitySpectriteArrow;
import com.samuel.spectritemod.entities.EntitySpectriteCreeper;
import com.samuel.spectritemod.entities.EntitySpectriteGolem;
import com.samuel.spectritemod.entities.EntitySpectriteSkeleton;
import com.samuel.spectritemod.entities.EntitySpectriteTippedArrow;
import com.samuel.spectritemod.entities.EntitySpectriteWitherSkeleton;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityRenderRegister {
	
	@SubscribeEvent
	public void onRegisterEntityModels(ModelRegistryEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteArrow.class, RenderSpectriteArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteGolem.class, RenderSpectriteGolem::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteCreeper.class, RenderSpectriteCreeper::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteSkeleton.class, RenderSpectriteSkeleton::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteWitherSkeleton.class, RenderSpectriteWitherSkeleton::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteTippedArrow.class, RenderSpectriteTippedArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteAreaEffectCloud.class, RenderSpectriteAreaEffectCloud::new);
	}
}