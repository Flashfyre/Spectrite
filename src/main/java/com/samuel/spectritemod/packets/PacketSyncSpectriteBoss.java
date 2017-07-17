package com.samuel.spectritemod.packets;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.samuel.spectritemod.capabilities.ISpectriteBossCapability;
import com.samuel.spectritemod.capabilities.SpectriteBossProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncSpectriteBoss implements IMessage {

	public UUID uuid;
	public boolean enabled;

	public PacketSyncSpectriteBoss() {
		this.uuid = null;
		this.enabled = false;
	}

	public PacketSyncSpectriteBoss(UUID uuid, boolean enabled) {
		this.uuid = uuid;
		this.enabled = enabled;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		uuid = new PacketBuffer(buf).readUniqueId();
		enabled = buf.getBoolean(0);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeUniqueId(uuid);
		buf.writeBoolean(enabled);
	}

	public static class Handler implements
		IMessageHandler<PacketSyncSpectriteBoss, IMessage> {
		@Override
		public IMessage onMessage(
			final PacketSyncSpectriteBoss message,
			final MessageContext ctx) {
			IThreadListener mainThread = Minecraft
				.getMinecraft();
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					List<Entity> entityResults = Minecraft.getMinecraft().world.getLoadedEntityList().stream().filter(e -> e.getUniqueID().equals(message.uuid)).collect(Collectors.toList());
					if (!entityResults.isEmpty()) {
						EntityLivingBase entity = (EntityLivingBase) entityResults.get(0);
						ISpectriteBossCapability props =
							entity.getCapability(SpectriteBossProvider.sbc, null);
						if (props != null) {
							props.setEnabled(message.enabled);
						}
					}
				}
			});
			return null;
		}
	}
}