package com.samuel.spectrite.client.renderer.entity;

import com.samuel.spectrite.entities.EntitySpectriteTippedArrow;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectriteTippedArrow extends RenderArrow<EntitySpectriteTippedArrow>
{
    public static final ResourceLocation RES_TIPPED_ARROW = new ResourceLocation("textures/entity/projectiles/tipped_arrow.png");

    public RenderSpectriteTippedArrow(RenderManager manager)
    {
        super(manager);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(EntitySpectriteTippedArrow entity)
    {
        return RES_TIPPED_ARROW;
    }
}