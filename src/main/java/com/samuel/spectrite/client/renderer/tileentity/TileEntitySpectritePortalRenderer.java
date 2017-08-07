package com.samuel.spectrite.client.renderer.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;

import com.samuel.spectrite.tileentity.TileEntitySpectritePortal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntitySpectritePortalRenderer extends TileEntitySpecialRenderer<TileEntitySpectritePortal> {

	private static final ResourceLocation SPECTRITE_SKY_TEXTURE = new ResourceLocation("spectrite:textures/environment/spectrite_sky.png");
    private static final ResourceLocation SPECTRITE_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
    
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    private FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

    @Override
    public void func_192841_a(TileEntitySpectritePortal te, double x, double y, double z, float partialTick, int breakStage, float partial) {
        GlStateManager.disableLighting();
        RANDOM.setSeed(31100L);
        GlStateManager.getFloat(2982, MODELVIEW);
        GlStateManager.getFloat(2983, PROJECTION);
        double d0 = x * x + y * y + z * z;
        int i = this.getPasses(d0);
        float f = this.getOffset();
        boolean flag = false;

        for (int j = 0; j < i; ++j)
        {
        	
            GlStateManager.pushMatrix();
            float f1 = 2.0F / (18 - j);

            if (j == 0)
            {
                this.bindTexture(SPECTRITE_SKY_TEXTURE);
                f1 = 0.625F;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
               
            }

            if (j >= 1)
            {
                this.bindTexture(SPECTRITE_PORTAL_TEXTURE);
                flag = true;
                Minecraft.getMinecraft().entityRenderer.func_191514_d(true);
            }

            if (j == 1)
            {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            }

            GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GlStateManager.texGen(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.5F, 0.5F, 0.0F);
            GlStateManager.scale(0.5F, 0.5F, 1.0F);
            float f2 = j + 1;
            GlStateManager.translate(17.0F / f2, (2.0F + f2 / 1.5F) * (Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
            GlStateManager.rotate((f2 * f2 * 4321.0F + f2 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.scale(4.5F - f2 / 4.0F, 4.5F - f2 / 4.0F, 1.0F);
            GlStateManager.multMatrix(PROJECTION);
            GlStateManager.multMatrix(MODELVIEW);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            float f3 = (RANDOM.nextFloat() * 0.5F + 0.1F) * f1;
            float f4 = (RANDOM.nextFloat() * 0.5F + 0.4F) * f1;
            float f5 = (RANDOM.nextFloat() * 0.5F + 0.5F) * f1;
            
            int hueFrame = ((int) te.getWorld().getWorldTime()) % 720;
			float r = hueFrame >= 480 && hueFrame < 600 ? (1f / 120) * (hueFrame - 480) : hueFrame < 120 || hueFrame >= 600 ? 1f : hueFrame < 240 ? (1f / 120) * (120 - (hueFrame - 120)) : 0f,
				g = hueFrame < 120 ? (1f / 120) * hueFrame : hueFrame < 360 ? 1f : hueFrame < 480 ? (1f / 120) * (120 - (hueFrame - 360)) : 0f,
				b = hueFrame >= 240 && hueFrame < 360 ? (1f / 120) * (hueFrame - 240) : hueFrame >= 360 && hueFrame < 600 ? 1f : hueFrame >= 600 ? (1f / 120) * (120 - (hueFrame - 600)) : 0f;
				f3 = r * 0.5f;
				f4 = g * 0.5f;
				f5 = b * 0.5f;

            if (te.shouldRenderFace(EnumFacing.SOUTH))
            {
                bufferBuilder.pos(x, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x + 1.0D, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x + 1.0D, y + 1.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x, y + 1.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
            }

            if (te.shouldRenderFace(EnumFacing.NORTH))
            {
                bufferBuilder.pos(x, y + 1.0D, z).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x + 1.0D, y + 1.0D, z).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x + 1.0D, y, z).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x, y, z).color(f3, f4, f5, 1.0F).endVertex();
            }

            if (te.shouldRenderFace(EnumFacing.EAST))
            {
                bufferBuilder.pos(x + 1.0D, y + 1.0D, z).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x + 1.0D, y + 1.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x + 1.0D, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x + 1.0D, y, z).color(f3, f4, f5, 1.0F).endVertex();
            }

            if (te.shouldRenderFace(EnumFacing.WEST))
            {
                bufferBuilder.pos(x, y, z).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x, y + 1.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x, y + 1.0D, z).color(f3, f4, f5, 1.0F).endVertex();
            }

            if (te.shouldRenderFace(EnumFacing.DOWN))
            {
                bufferBuilder.pos(x, y, z).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x + 1.0D, y, z).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x + 1.0D, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
            }

            if (te.shouldRenderFace(EnumFacing.UP))
            {
                bufferBuilder.pos(x, y + f, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x + 1.0D, y + f, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x + 1.0D, y + f, z).color(f3, f4, f5, 1.0F).endVertex();
                bufferBuilder.pos(x, y + f, z).color(f3, f4, f5, 1.0F).endVertex();
            }

            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            this.bindTexture(SPECTRITE_SKY_TEXTURE);
        }

        GlStateManager.disableBlend();
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
        GlStateManager.enableLighting();

        if (flag)
        {
            Minecraft.getMinecraft().entityRenderer.func_191514_d(false);
        }
    }
    
    protected int getPasses(double p_191286_1_)
    {
        int i;

        if (p_191286_1_ > 36864.0D)
        {
            i = 1;
        }
        else if (p_191286_1_ > 25600.0D)
        {
            i = 3;
        }
        else if (p_191286_1_ > 16384.0D)
        {
            i = 5;
        }
        else if (p_191286_1_ > 9216.0D)
        {
            i = 7;
        }
        else if (p_191286_1_ > 4096.0D)
        {
            i = 9;
        }
        else if (p_191286_1_ > 1024.0D)
        {
            i = 11;
        }
        else if (p_191286_1_ > 576.0D)
        {
            i = 13;
        }
        else if (p_191286_1_ > 256.0D)
        {
            i = 14;
        }
        else
        {
            i = 15;
        }

        return i;
    }

    protected float getOffset()
    {
        return 0.75F;
    }

    
    private FloatBuffer getBuffer(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
    {
        this.buffer.clear();
        this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.buffer.flip();
        return this.buffer;
    }
}
