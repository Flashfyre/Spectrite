package com.samuel.spectrite.client.renderer.entity;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.entities.EntitySpectriteCrystal;
import com.samuel.spectrite.helpers.SpectriteHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectriteCrystal extends Render<EntitySpectriteCrystal>
{
    private static final Int2ObjectMap<ResourceLocation> CRYSTAL_TEXTURE_RES_MAP = new Int2ObjectOpenHashMap<>();

    private final ModelBase modelEnderCrystal = new ModelEnderCrystal(0.0F, true);
    private final ModelBase modelEnderCrystalNoBase = new ModelEnderCrystal(0.0F, false);

    public RenderSpectriteCrystal(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntitySpectriteCrystal entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        float f = (float)entity.innerRotation + partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);

        this.bindTexture(getEntityTexture(entity));

        float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
        f1 = f1 * f1 + f1;

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        if (entity.shouldShowBottom())
        {
            this.modelEnderCrystal.render(entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);
        }
        else
        {
            this.modelEnderCrystalNoBase.render(entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);
        }

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySpectriteCrystal entity)
    {
        int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());

        ResourceLocation resourceLocation;
        if (CRYSTAL_TEXTURE_RES_MAP.containsKey(curFrame)) {
            resourceLocation = CRYSTAL_TEXTURE_RES_MAP.get(curFrame);
        } else {
            resourceLocation = new ResourceLocation(String.format("%s:textures/entities/spectrite_crystal/%d.png", Spectrite.MOD_ID, curFrame));
            CRYSTAL_TEXTURE_RES_MAP.put(curFrame, resourceLocation);
        }

        return resourceLocation;
    }

    public boolean shouldRender(EntitySpectriteCrystal livingEntity, ICamera camera, double camX, double camY, double camZ)
    {
        return super.shouldRender(livingEntity, camera, camX, camY, camZ);
    }
}