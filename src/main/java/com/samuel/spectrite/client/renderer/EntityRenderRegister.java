package com.samuel.spectrite.client.renderer;

import com.samuel.spectrite.client.renderer.entity.RenderSpectriteAreaEffectCloud;
import com.samuel.spectrite.client.renderer.entity.RenderSpectriteArrow;
import com.samuel.spectrite.client.renderer.entity.RenderSpectriteCreeper;
import com.samuel.spectrite.client.renderer.entity.RenderSpectriteGolem;
import com.samuel.spectrite.client.renderer.entity.RenderSpectriteSkeleton;
import com.samuel.spectrite.client.renderer.entity.RenderSpectriteTippedArrow;
import com.samuel.spectrite.client.renderer.entity.RenderSpectriteWitherSkeleton;
import com.samuel.spectrite.entities.EntitySpectriteAreaEffectCloud;
import com.samuel.spectrite.entities.EntitySpectriteArrow;
import com.samuel.spectrite.entities.EntitySpectriteCreeper;
import com.samuel.spectrite.entities.EntitySpectriteGolem;
import com.samuel.spectrite.entities.EntitySpectriteSkeleton;
import com.samuel.spectrite.entities.EntitySpectriteTippedArrow;
import com.samuel.spectrite.entities.EntitySpectriteWitherSkeleton;

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