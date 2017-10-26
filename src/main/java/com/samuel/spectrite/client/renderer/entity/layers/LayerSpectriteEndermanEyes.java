package com.samuel.spectrite.client.renderer.entity.layers;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.client.renderer.entity.RenderSpectriteEnderman;
import com.samuel.spectrite.entities.EntitySpectriteEnderman;
import com.samuel.spectrite.helpers.SpectriteHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerSpectriteEndermanEyes implements LayerRenderer<EntitySpectriteEnderman>
{
    private static final Int2ObjectMap<ResourceLocation> SPECTRITE_ENDERMAN_EYES_TEXTURE_RES_MAP = new Int2ObjectOpenHashMap<>();
    private final RenderSpectriteEnderman spectriteEndermanRenderer;

    public LayerSpectriteEndermanEyes(RenderSpectriteEnderman spectriteEndermanRendererIn)
    {
        this.spectriteEndermanRenderer = spectriteEndermanRendererIn;
    }

    @Override
    public void doRenderLayer(EntitySpectriteEnderman entitySpectriteEndermanIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.spectriteEndermanRenderer.bindTexture(getLayerTexture(entitySpectriteEndermanIn));
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(!entitySpectriteEndermanIn.isInvisible());
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.spectriteEndermanRenderer.getMainModel().render(entitySpectriteEndermanIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        this.spectriteEndermanRenderer.setLightmap(entitySpectriteEndermanIn);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }

    private ResourceLocation getLayerTexture(EntitySpectriteEnderman entity)
    {
        int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());
        ResourceLocation resourceLocation;

        if (SPECTRITE_ENDERMAN_EYES_TEXTURE_RES_MAP.containsKey(curFrame)) {
            resourceLocation = SPECTRITE_ENDERMAN_EYES_TEXTURE_RES_MAP.get(curFrame);
        } else {
            resourceLocation = new ResourceLocation(String.format("%s:textures/entities/spectrite_enderman/spectrite_enderman_eyes/%d.png", Spectrite.MOD_ID, curFrame));
            SPECTRITE_ENDERMAN_EYES_TEXTURE_RES_MAP.put(curFrame, resourceLocation);
        }

        return resourceLocation;
    }
}