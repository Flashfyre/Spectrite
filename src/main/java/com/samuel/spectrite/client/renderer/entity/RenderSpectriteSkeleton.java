package com.samuel.spectrite.client.renderer.entity;

import java.util.Map;

import com.google.common.collect.Maps;
import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.etc.SpectriteHelper;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitherSkeleton;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectriteSkeleton extends RenderWitherSkeleton {

	private static final Map<String, ResourceLocation> SKELETON_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();

    public RenderSpectriteSkeleton(RenderManager renderManager)
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
        String textureLoc = String.format("%s:textures/entities/spectrite_skeleton/%d.png", Spectrite.MOD_ID, curFrame);
		ResourceLocation resourceLocation = SKELETON_TEXTURE_RES_MAP.get(textureLoc);
		
        if (resourceLocation == null)
        {
            resourceLocation = new ResourceLocation(textureLoc);
            SKELETON_TEXTURE_RES_MAP.put(textureLoc, resourceLocation);
        }
		
		return resourceLocation;
    }
}
