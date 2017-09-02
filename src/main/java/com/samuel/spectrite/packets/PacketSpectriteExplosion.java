package com.samuel.spectrite.packets;

import com.google.common.collect.Lists;
import com.samuel.spectrite.etc.SpectriteExplosion;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketSpectriteExplosion implements IMessage {

    private double posX;
    private double posY;
    private double posZ;
    private float strength;
    private boolean damagesTerrain = false;
    private List<BlockPos> affectedBlockPositions;
    private float motionX;
    private float motionY;
    private float motionZ;

    public PacketSpectriteExplosion() { }

    public PacketSpectriteExplosion(double posX, double posY, double posZ, float strength, boolean damagesTerrain,
        List<BlockPos> affectedBlockPositions, Vec3d motion) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.strength = strength;
        this.damagesTerrain = damagesTerrain;
        this.affectedBlockPositions = affectedBlockPositions;
        if (motion != null) {
            this.motionX = (float) motion.x;
            this.motionY = (float) motion.y;
            this.motionZ = (float) motion.z;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
        this.strength = buf.readFloat();
        this.damagesTerrain = buf.readBoolean();
        int numAffectedPositions = buf.readInt();
        this.affectedBlockPositions = Lists.<BlockPos>newArrayListWithCapacity(numAffectedPositions);
        for (int p = 0; p < numAffectedPositions; p++) {
            this.affectedBlockPositions.add(new PacketBuffer(buf).readBlockPos());
        }
        this.motionX = buf.readFloat();
        this.motionY = buf.readFloat();
        this.motionZ = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
        buf.writeFloat(this.strength);
        buf.writeBoolean(this.damagesTerrain);
        int numAffectedPositions = this.affectedBlockPositions.size();
        buf.writeInt(numAffectedPositions);
        for (int p = 0; p < numAffectedPositions; p++) {
            new PacketBuffer(buf).writeBlockPos(this.affectedBlockPositions.get(p));
        }
        buf.writeFloat(this.motionX);
        buf.writeFloat(this.motionY);
        buf.writeFloat(this.motionZ);
    }

    public static class Handler implements IMessageHandler<PacketSpectriteExplosion, IMessage> {
        @Override
        public IMessage onMessage(
                final PacketSpectriteExplosion message,
                final MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Minecraft minecraft = Minecraft.getMinecraft();
                    Explosion explosion = new SpectriteExplosion(minecraft.world, null, message.posX, message.posY, message.posZ,
                        message.strength, false, message.damagesTerrain,false, message.affectedBlockPositions);
                    explosion.doExplosionB(true);
                    minecraft.player.motionX += (double) message.motionX;
                    minecraft.player.motionY += (double) message.motionY;
                    minecraft.player.motionZ += (double) message.motionZ;
                }
            });
            return null;
        }
    }
}
