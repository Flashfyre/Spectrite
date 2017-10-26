package com.samuel.spectrite.packets;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.client.particles.EnumSpectriteParticleTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Random;

public class PacketSpectriteParticles implements IMessage {
    private EnumSpectriteParticleTypes particleType;
    private float xCoord;
    private float yCoord;
    private float zCoord;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    private float particleSpeed;
    private int particleCount;
    private double hueOffset;

    public PacketSpectriteParticles()
    {
    }

    public PacketSpectriteParticles(EnumSpectriteParticleTypes particleIn, float xIn, float yIn, float zIn, float xOffsetIn, float yOffsetIn, float zOffsetIn, float speedIn, int countIn, double hueOffset)
    {
        this.particleType = particleIn;
        this.xCoord = xIn;
        this.yCoord = yIn;
        this.zCoord = zIn;
        this.xOffset = xOffsetIn;
        this.yOffset = yOffsetIn;
        this.zOffset = zOffsetIn;
        this.particleSpeed = speedIn;
        this.particleCount = countIn;
        this.hueOffset = hueOffset;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.particleType = EnumSpectriteParticleTypes.values()[buf.readInt()];

        if (this.particleType == null)
        {
            this.particleType = EnumSpectriteParticleTypes.SPECTRITE_SPELL;
        }

        this.xCoord = buf.readFloat();
        this.yCoord = buf.readFloat();
        this.zCoord = buf.readFloat();
        this.xOffset = buf.readFloat();
        this.yOffset = buf.readFloat();
        this.zOffset = buf.readFloat();
        this.particleSpeed = buf.readFloat();
        this.particleCount = buf.readInt();
        this.hueOffset = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.particleType.ordinal());
        buf.writeFloat(this.xCoord);
        buf.writeFloat(this.yCoord);
        buf.writeFloat(this.zCoord);
        buf.writeFloat(this.xOffset);
        buf.writeFloat(this.yOffset);
        buf.writeFloat(this.zOffset);
        buf.writeFloat(this.particleSpeed);
        buf.writeInt(this.particleCount);
        buf.writeDouble(this.hueOffset);
    }

    public static class Handler implements IMessageHandler<PacketSpectriteParticles, IMessage> {
        @Override
        public IMessage onMessage(
                final PacketSpectriteParticles message,
                final MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Minecraft minecraft = Minecraft.getMinecraft();
                    Random rand = new Random();

                    for (int k = 0; k < message.particleCount; ++k) {
                        double d1 = rand.nextGaussian() * (double) message.xOffset;
                        double d3 = rand.nextGaussian() * (double) message.yOffset;
                        double d5 = rand.nextGaussian() * (double) message.zOffset;
                        double d6 = rand.nextGaussian() * (double) message.particleSpeed;
                        double d7 = rand.nextGaussian() * (double) message.particleSpeed;
                        double d8 = rand.nextGaussian() * (double) message.particleSpeed;
                        if (message.particleType == EnumSpectriteParticleTypes.SPECTRITE_SPELL) {
                            Spectrite.Proxy.spawnSpectriteSpellParticle(minecraft.world, message.xCoord + d1,
                                message.yCoord + d3, message.zCoord + d5, d6, d7, d8, (float) message.hueOffset);
                        } else if (message.particleType == EnumSpectriteParticleTypes.SPECTRITE_EXPLOSION_HUGE) {
                            Spectrite.Proxy.spawnSpectriteExplosionHugeParticle(minecraft.world, message.xCoord + d1,
                                message.yCoord + d3, message.zCoord + d5, d6, d7, d8, message.hueOffset);
                        } else if (message.particleType == EnumSpectriteParticleTypes.SPECTRITE_PORTAL) {
                            Spectrite.Proxy.spawnSpectritePortalParticle(minecraft.world, message.xCoord + d1,
                                message.yCoord + d3, message.zCoord + d5, d6, d7, d8);
                        } else if (message.particleType == EnumSpectriteParticleTypes.SPECTRITE_SMOKE_NORMAL) {
                            Spectrite.Proxy.spawnSpectriteSmokeNormalParticle(minecraft.world, message.xCoord + d1,
                                message.yCoord + d3, message.zCoord + d5, d6, d7, d8);
                        } else if (message.particleType == EnumSpectriteParticleTypes.SPECTRITE_SMOKE_LARGE) {
                            Spectrite.Proxy.spawnSpectriteSmokeLargeParticle(minecraft.world, message.xCoord + d1,
                                message.yCoord + d3, message.zCoord + d5, d6, d7, d8);
                        } else {

                            Spectrite.Proxy.spawnSpectriteExplosionParticle(minecraft.world, message.particleType == EnumSpectriteParticleTypes.SPECTRITE_EXPLOSION_LARGE,
                                message.xCoord + d1, message.yCoord + d3, message.zCoord + d5, d6, d7, d8);
                        }
                    }
                }
            });
            return null;
        }
    }
}
