package com.samuel.spectrite.client.renderer.entity;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.client.model.ModelSpectriteWither;
import com.samuel.spectrite.client.renderer.entity.layers.LayerSpectriteWitherAura;
import com.samuel.spectrite.entities.EntitySpectriteWither;
import com.samuel.spectrite.helpers.SpectriteHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectriteWither extends RenderLiving<EntitySpectriteWither> {

    private static final Int2ObjectMap<ResourceLocation> INVULNERABLE_SPECTRITE_WITHER_TEXTURE_RES_MAP = new Int2ObjectOpenHashMap<>();
    private static final Int2ObjectMap<ResourceLocation> SPECTRITE_WITHER_TEXTURE_RES_MAP = new Int2ObjectOpenHashMap<>();

    public RenderSpectriteWither(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSpectriteWither(0.0F), 1.0F);
        this.addLayer(new LayerSpectriteWitherAura(this));
    }

    @Override
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySpectriteWither entity)
    {
        int i = entity.getInvulTime();
        boolean invulnerable = (i > 0 && (i > 80 || i / 5 % 2 != 1));

        int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());
        ResourceLocation resourceLocation;
        Int2ObjectMap<ResourceLocation> textureMap = invulnerable ? INVULNERABLE_SPECTRITE_WITHER_TEXTURE_RES_MAP : SPECTRITE_WITHER_TEXTURE_RES_MAP;

        if (textureMap.containsKey(curFrame)) {
            resourceLocation = textureMap.get(curFrame);
        } else {
            resourceLocation = new ResourceLocation(String.format("%s:textures/entities/spectrite_wither/%s/%d.png", Spectrite.MOD_ID, invulnerable ? "invulnerable" : "normal", curFrame));
        }

        return resourceLocation;
    }

    @Override
    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntitySpectriteWither entitylivingbaseIn, float partialTickTime)
    {
        float f = 2.0F;
        int i = entitylivingbaseIn.getInvulTime();

        if (i > 0)
        {
            f -= ((float)i - partialTickTime) / 220.0F * 0.5F;
        }

        GlStateManager.scale(f, f, f);
    }
}
