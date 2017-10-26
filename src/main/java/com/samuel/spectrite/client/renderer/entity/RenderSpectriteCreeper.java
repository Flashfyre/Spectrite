package com.samuel.spectrite.client.renderer.entity;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.helpers.SpectriteHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;

public class RenderSpectriteCreeper extends RenderCreeper {
	
	private static final Int2ObjectMap<ResourceLocation> CREEPER_TEXTURE_RES_MAP = new Int2ObjectOpenHashMap<>();
	
	public RenderSpectriteCreeper(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityCreeper entity) {
		int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());

		ResourceLocation resourceLocation;
		if (CREEPER_TEXTURE_RES_MAP.containsKey(curFrame)) {
			resourceLocation = CREEPER_TEXTURE_RES_MAP.get(curFrame);
		} else {
			resourceLocation = new ResourceLocation(String.format("%s:textures/entities/spectrite_creeper/%d.png", Spectrite.MOD_ID, curFrame));
			CREEPER_TEXTURE_RES_MAP.put(curFrame, resourceLocation);
		}

		return resourceLocation;
	}
}
