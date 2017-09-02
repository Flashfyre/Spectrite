package com.samuel.spectrite.client.renderer;

import com.samuel.spectrite.etc.SpectriteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
/**
 * This class has been adapted from Embers' source code under GNU Lesser General Public License
 *
 * Dissolution, which also adapted code from Embers, was also referenced with direct permission
 *
 * https://github.com/RootsTeam/Embers
 * https://github.com/Pyrofab/Dissolution
 *
 * @author Elucent
 * @author Pyrofab
 *
 */
public class SpectriteParticleManager {

    public static final SpectriteParticleManager INSTANCE = new SpectriteParticleManager();
    private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
    private static Method calculateParticleLevel = null;

    private List<Particle> particles = new ArrayList<>();

    public void updateParticles() {
        for (int p = 0; p < particles.size(); p++) {
            Particle particle = particles.get(p);
            if (particle == null || !particle.isAlive()) {
                particles.remove(p--);
            } else {
                particle.onUpdate();
            }
        }
    }

    public void renderParticles(float partialTicks) {
        float f = ActiveRenderInfo.getRotationX();
        float f1 = ActiveRenderInfo.getRotationZ();
        float f2 = ActiveRenderInfo.getRotationYZ();
        float f3 = ActiveRenderInfo.getRotationXY();
        float f4 = ActiveRenderInfo.getRotationXZ();
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            Particle.interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            Particle.interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            Particle.interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
            Particle.cameraViewDir = player.getLook(partialTicks);

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            GlStateManager.alphaFunc(516, 0.003921569F);
            GlStateManager.depthMask(false);

            Minecraft.getMinecraft().renderEngine.bindTexture(PARTICLE_TEXTURES);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();

            buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
            for (int p = 0; p < particles.size(); p++) {
                Particle particle = particles.get(p);
                if (particle != null) {
                    particle.renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3);
                }
            }
            tess.draw();

            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
        }
    }

    public void addParticle(Particle p) {
        if (calculateParticleLevel == null) {
            calculateParticleLevel = SpectriteHelper.findObfuscatedMethod(RenderGlobal.class,
                "calculateParticleLevel", "func_190572_a", boolean.class);
        }
        int particleLevel = 0;

        try {
            particleLevel = (int) calculateParticleLevel.invoke(Minecraft.getMinecraft().renderGlobal, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (particleLevel <= 1) {
            particles.add(p);
        }
    }

}