package com.samuel.spectrite.packets;

import com.samuel.spectrite.init.ModWorldGen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncSpectriteDungeonSpawnPos implements IMessage {

	public BlockPos spawnPos;

	public PacketSyncSpectriteDungeonSpawnPos() {
		this.spawnPos = null;
	}

	public PacketSyncSpectriteDungeonSpawnPos(BlockPos spawnPos) {
		this.spawnPos = spawnPos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.spawnPos = new PacketBuffer(buf).readBlockPos();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeBlockPos(this.spawnPos);
	}

	public static class Handler implements
		IMessageHandler<PacketSyncSpectriteDungeonSpawnPos, IMessage> {
		@Override
		public IMessage onMessage(
			final PacketSyncSpectriteDungeonSpawnPos message,
			final MessageContext ctx) {
			IThreadListener mainThread = Minecraft
				.getMinecraft();
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					ModWorldGen.spectriteDungeon.setSpawnPos(message.spawnPos);
				}
			});
			return null;
		}
	}
}