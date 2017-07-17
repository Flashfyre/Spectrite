package com.samuel.spectritemod.client.renderer.entity;

import java.util.Map;

import com.google.common.collect.Maps;
import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.etc.SpectriteHelper;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectriteWitherSkeleton extends RenderSkeleton {

	private static final Map<String, ResourceLocation> WITHER_SKELETON_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();

    public RenderSpectriteWitherSkeleton(RenderManager renderManager)
    {
        super(renderManager);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(AbstractSkeleton entity)
    {
    	int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());
        String textureLoc = String.format("%s:textures/entities/spectrite_wither_skeleton/%d.png", SpectriteMod.MOD_ID, curFrame);
		ResourceLocation resourceLocation = WITHER_SKELETON_TEXTURE_RES_MAP.get(textureLoc);
		
        if (resourceLocation == null)
        {
            resourceLocation = new ResourceLocation(textureLoc);
            WITHER_SKELETON_TEXTURE_RES_MAP.put(textureLoc, resourceLocation);
        }
		
		return resourceLocation;
    }
}
