package com.samuel.spectrite.client.renderer;

import com.samuel.spectrite.client.renderer.entity.*;
import com.samuel.spectrite.entities.*;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityRenderRegister {
	
	@SubscribeEvent
	public void onRegisterEntityModels(ModelRegistryEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteArrow.class, RenderSpectriteArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteGolem.class, RenderSpectriteGolem::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteCreeper.class, RenderSpectriteCreeper::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteSkeleton.class, RenderSpectriteSkeleton::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteWitherSkeleton.class, RenderSpectriteWitherSkeleton::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteBlaze.class, RenderSpectriteBlaze::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteEnderman.class, RenderSpectriteEnderman::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteWither.class, RenderSpectriteWither::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteWitherSkull.class, RenderSpectriteWitherSkull::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteTippedArrow.class, RenderSpectriteTippedArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpectriteAreaEffectCloud.class, RenderSpectriteAreaEffectCloud::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySmallSpectriteFireball.class, RenderSpectriteFireball::new);
	}
}