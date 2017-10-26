package com.samuel.spectrite.client.renderer.entity;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.client.model.ModelSpectriteEnderman;
import com.samuel.spectrite.client.renderer.entity.layers.LayerSpectriteEndermanEyes;
import com.samuel.spectrite.client.renderer.entity.layers.LayerSpectriteEndermanHeldBlock;
import com.samuel.spectrite.entities.EntitySpectriteEnderman;
import com.samuel.spectrite.helpers.SpectriteHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class RenderSpectriteEnderman extends RenderLiving<EntitySpectriteEnderman> {

    private static final Int2ObjectMap<ResourceLocation> SPECTRITE_ENDERMAN_TEXTURE_RES_MAP = new Int2ObjectOpenHashMap<>();
    private final Random rnd = new Random();

    public RenderSpectriteEnderman(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSpectriteEnderman(0.0F), 0.5F);
        this.addLayer(new LayerSpectriteEndermanEyes(this));
        this.addLayer(new LayerSpectriteEndermanHeldBlock(this));
    }

    @Override
    public ModelSpectriteEnderman getMainModel()
    {
        return (ModelSpectriteEnderman)super.getMainModel();
    }

    @Override
    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntitySpectriteEnderman entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        IBlockState iblockstate = entity.getHeldBlockState();
        ModelSpectriteEnderman modelSpectriteEnderman = this.getMainModel();
        modelSpectriteEnderman.isCarrying = iblockstate != null;
        modelSpectriteEnderman.isAttacking = entity.isScreaming();

        if (entity.isScreaming())
        {
            x += this.rnd.nextGaussian() * 0.02D;
            z += this.rnd.nextGaussian() * 0.02D;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySpectriteEnderman entity)
    {
        int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());

        ResourceLocation resourceLocation;
        if (SPECTRITE_ENDERMAN_TEXTURE_RES_MAP.containsKey(curFrame)) {
            resourceLocation = SPECTRITE_ENDERMAN_TEXTURE_RES_MAP.get(curFrame);
        } else {
            resourceLocation = new ResourceLocation(String.format("%s:textures/entities/spectrite_enderman/spectrite_enderman/%d.png", Spectrite.MOD_ID, curFrame));
            SPECTRITE_ENDERMAN_TEXTURE_RES_MAP.put(curFrame, resourceLocation);
        }

        return resourceLocation;
    }
}
