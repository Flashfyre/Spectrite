package com.samuel.spectrite.client.renderer.sky;

import com.samuel.spectrite.Spectrite;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IRenderHandler;

public class SpectriteSkyRenderer extends IRenderHandler {

	private static final ResourceLocation SKY_TEXTURES = new ResourceLocation(Spectrite.MOD_ID, "textures/environment/spectrite_sky.png");

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		GlStateManager.disableFog();
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.depthMask(false);
		mc.renderEngine.bindTexture(SKY_TEXTURES);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();

		float dim = 8;

		for (int i = 0; i < 6; ++i) {

			GlStateManager.pushMatrix();

			if (i == 1) {
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (i == 2) {
				GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (i == 3) {
				GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			}

			if (i == 4) {
				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
			}

			if (i == 5) {
				GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
			}

			int val = 30;
			
			vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			vertexbuffer.pos(-100.0D, -100.0D, -100.0D).tex(0, 0).color(255, 255, 255, val).endVertex();
			vertexbuffer.pos(-100.0D, -100.0D, 100.0D).tex(0, dim).color(255, 255, 255, val).endVertex();
			vertexbuffer.pos(100.0D, -100.0D, 100.0D).tex(dim, dim).color(255, 255, 255, val).endVertex();
			vertexbuffer.pos(100.0D, -100.0D, -100.0D).tex(dim, 0).color(255, 255, 255, val).endVertex();
			tessellator.draw();

			GlStateManager.popMatrix();
		}

		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
	}
}
