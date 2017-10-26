package com.samuel.spectrite.client.renderer.entity.layers;

import com.samuel.spectrite.client.model.ModelSpectriteWither;
import com.samuel.spectrite.client.renderer.entity.RenderSpectriteWither;
import com.samuel.spectrite.entities.EntitySpectriteWither;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerSpectriteWitherAura implements LayerRenderer<EntitySpectriteWither>
{
    private static final ResourceLocation SPECTRITE_WITHER_ARMOR = new ResourceLocation("textures/entity/wither/wither_armor.png");
    private final RenderSpectriteWither witherRenderer;
    private final ModelSpectriteWither witherModel = new ModelSpectriteWither(0.5F);

    public LayerSpectriteWitherAura(RenderSpectriteWither witherRendererIn)
    {
        this.witherRenderer = witherRendererIn;
    }

    @Override
    public void doRenderLayer(EntitySpectriteWither entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (entityLivingBaseIn.isArmored())
        {
            GlStateManager.depthMask(!entityLivingBaseIn.isInvisible());
            this.witherRenderer.bindTexture(SPECTRITE_WITHER_ARMOR);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = (float)entityLivingBaseIn.ticksExisted + partialTicks;
            float f1 = MathHelper.cos(f * 0.02F) * 3.0F;
            float f2 = f * 0.01F;
            GlStateManager.translate(f1, f2, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            this.witherModel.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.witherModel.setModelAttributes(this.witherRenderer.getMainModel());
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.witherModel.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}