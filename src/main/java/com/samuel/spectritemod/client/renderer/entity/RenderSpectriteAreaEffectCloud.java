package com.samuel.spectritemod.client.renderer.entity;

import javax.annotation.Nullable;

import com.samuel.spectritemod.entities.EntitySpectriteAreaEffectCloud;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectriteAreaEffectCloud extends Render<EntitySpectriteAreaEffectCloud>
{
    public RenderSpectriteAreaEffectCloud(RenderManager manager)
    {
        super(manager);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	@Nullable
    protected ResourceLocation getEntityTexture(EntitySpectriteAreaEffectCloud entity)
    {
        return null;
    }
}
