package com.samuel.spectrite.client.renderer.entity;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.client.model.ModelSpectriteBlaze;
import com.samuel.spectrite.entities.EntitySpectriteBlaze;
import com.samuel.spectrite.helpers.SpectriteHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Method;

@SideOnly(Side.CLIENT)
public class RenderSpectriteBlaze extends RenderLiving<EntitySpectriteBlaze> {

	private static final Int2ObjectMap<ResourceLocation> SPECTRITE_BLAZE_TEXTURE_RES_MAP = new Int2ObjectOpenHashMap<>();
	private static Method renderShadow = null;

    public RenderSpectriteBlaze(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSpectriteBlaze(), 0.5F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(EntitySpectriteBlaze entity)
    {
    	int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());
		ResourceLocation resourceLocation;

		if (SPECTRITE_BLAZE_TEXTURE_RES_MAP.containsKey(curFrame)) {
		    resourceLocation = SPECTRITE_BLAZE_TEXTURE_RES_MAP.get(curFrame);
        } else {
		    resourceLocation = new ResourceLocation(String.format("%s:textures/entities/spectrite_blaze/%d.png", Spectrite.MOD_ID, curFrame));
		    SPECTRITE_BLAZE_TEXTURE_RES_MAP.put(curFrame, resourceLocation);
        }
		
		return resourceLocation;
    }
    
    /**
     * Renders the entity's shadow and fire (if its on fire). Args: entity, x, y, z, yaw, partialTickTime
     */
    @Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks)
    {
        if (this.renderManager.options != null)
        {
            if (this.renderManager.options.entityShadows && this.shadowSize > 0.0F && !entityIn.isInvisible() && this.renderManager.isRenderShadow())
            {
                double d0 = this.renderManager.getDistanceToCamera(entityIn.posX, entityIn.posY, entityIn.posZ);
                float f = (float)((1.0D - d0 / 256.0D) * this.shadowOpaque);

                if (f > 0.0F)
                {
                	if (renderShadow == null) {
                		renderShadow = ObfuscationReflectionHelper.findMethod(Render.class, "func_76975_c", void.class, Entity.class, double.class, double.class, double.class, float.class, float.class);
                	}
                    try {
						renderShadow.invoke(this, entityIn, x, y, z, f, partialTicks);
					} catch (Exception e) {
						e.printStackTrace();
					}
                }
            }

            if (entityIn.canRenderOnFire() && (!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).isSpectator()))
            {
                this.renderEntityOnFire(entityIn, x, y, z, partialTicks);
            }
        }
    }
    
    /**
     * Renders a layer of fire on top of an entity.
     */
    private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks)
    {
        GlStateManager.disableLighting();
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("spectrite:blocks/spectrite_fire_layer_0");
        TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("spectrite:blocks/spectrite_fire_layer_1");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        float f = entity.width * 1.4F;
        GlStateManager.scale(f, f, f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = entity.height / f;
        float f4 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, -0.3F + ((int)f3) * 0.02F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f5 = 0.0F;
        int i = 0;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        while (f3 > 0.0F)
        {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f6 = textureatlassprite2.getMinU();
            float f7 = textureatlassprite2.getMinV();
            float f8 = textureatlassprite2.getMaxU();
            float f9 = textureatlassprite2.getMaxV();

            if (i / 2 % 2 == 0)
            {
                float f10 = f8;
                f8 = f6;
                f6 = f10;
            }

            bufferbuilder.pos(f1 - 0.0F, 0.0F - f4, f5).tex(f8, f9).endVertex();
            bufferbuilder.pos(-f1 - 0.0F, 0.0F - f4, f5).tex(f6, f9).endVertex();
            bufferbuilder.pos(-f1 - 0.0F, 1.4F - f4, f5).tex(f6, f7).endVertex();
            bufferbuilder.pos(f1 - 0.0F, 1.4F - f4, f5).tex(f8, f7).endVertex();
            f3 -= 0.45F;
            f4 -= 0.45F;
            f1 *= 0.9F;
            f5 += 0.03F;
            ++i;
        }

        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }
}
