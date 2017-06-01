package com.samuel.spectritemod.client.renderer.entity;

import java.util.Map;

import com.google.common.collect.Maps;
import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.entities.EntitySpectriteArrow;
import com.samuel.spectritemod.etc.SpectriteHelper;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectriteArrow<T extends EntitySpectriteArrow> extends RenderArrow<T> {
	
	private static final Map<String, ResourceLocation> ARROW_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();

	public RenderSpectriteArrow(RenderManager p_i46193_1_) {
		super(p_i46193_1_);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		
		int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());
        String textureLoc = String.format("%s:textures/entities/spectrite_arrow/%d.png", SpectriteMod.MOD_ID, curFrame);
		ResourceLocation resourceLocation = ARROW_TEXTURE_RES_MAP.get(textureLoc);
		
        if (resourceLocation == null)
        {
            resourceLocation = new ResourceLocation(textureLoc);
            ARROW_TEXTURE_RES_MAP.put(textureLoc, resourceLocation);
        }
		
		return resourceLocation;
	}
}