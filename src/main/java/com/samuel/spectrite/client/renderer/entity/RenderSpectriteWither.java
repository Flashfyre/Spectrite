package com.samuel.spectrite.client.renderer.entity;

import com.google.common.collect.Maps;
import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.client.model.ModelSpectriteWither;
import com.samuel.spectrite.client.renderer.entity.layers.LayerSpectriteWitherAura;
import com.samuel.spectrite.entities.EntitySpectriteWither;
import com.samuel.spectrite.etc.SpectriteHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderSpectriteWither extends RenderLiving<EntitySpectriteWither> {

    private static final Map<String, ResourceLocation> INVULNERABLE_SPECTRITE_WITHER_TEXTURE_RES_MAP = Maps.newHashMap();
    private static final Map<String, ResourceLocation> SPECTRITE_WITHER_TEXTURE_RES_MAP = Maps.newHashMap();

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
        String textureLoc = "%s:textures/entities/spectrite_wither/%s/%d.png";
        ResourceLocation resourceLocation;

        if (invulnerable) {
            textureLoc = String.format(textureLoc, Spectrite.MOD_ID, "invulnerable", curFrame);
        } else {
            textureLoc = String.format(textureLoc, Spectrite.MOD_ID, "normal", curFrame);
        }

        resourceLocation = new ResourceLocation(textureLoc);

        if (resourceLocation == null) {
            if (invulnerable) {
                INVULNERABLE_SPECTRITE_WITHER_TEXTURE_RES_MAP.put(textureLoc, resourceLocation);
            } else {
                SPECTRITE_WITHER_TEXTURE_RES_MAP.put(textureLoc, resourceLocation);
            }
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
