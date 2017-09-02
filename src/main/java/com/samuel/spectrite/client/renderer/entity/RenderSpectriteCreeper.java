package com.samuel.spectrite.client.renderer.entity;

import com.google.common.collect.Maps;
import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.etc.SpectriteHelper;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class RenderSpectriteCreeper extends RenderCreeper {
	
	private static final Map<String, ResourceLocation> CREEPER_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();
	
	public RenderSpectriteCreeper(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityCreeper entity) {
		int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());
        String textureLoc = String.format("%s:textures/entities/spectrite_creeper/%d.png", Spectrite.MOD_ID, curFrame);
		ResourceLocation resourceLocation = CREEPER_TEXTURE_RES_MAP.get(textureLoc);
		
        if (resourceLocation == null)
        {
            resourceLocation = new ResourceLocation(textureLoc);
            CREEPER_TEXTURE_RES_MAP.put(textureLoc, resourceLocation);
        }
		
		return resourceLocation;
	}
}
