package com.samuel.spectrite.client.renderer.entity;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.helpers.SpectriteHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitherSkeleton;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectriteSkeleton extends RenderWitherSkeleton {

	private static final Int2ObjectMap<ResourceLocation> SKELETON_TEXTURE_RES_MAP = new Int2ObjectOpenHashMap<>();

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

        ResourceLocation resourceLocation;
        if (SKELETON_TEXTURE_RES_MAP.containsKey(curFrame)) {
            resourceLocation = SKELETON_TEXTURE_RES_MAP.get(curFrame);
        } else {
            resourceLocation = new ResourceLocation(String.format("%s:textures/entities/spectrite_skeleton/%d.png", Spectrite.MOD_ID, curFrame));
            SKELETON_TEXTURE_RES_MAP.put(curFrame, resourceLocation);
        }

        return resourceLocation;
    }
}
