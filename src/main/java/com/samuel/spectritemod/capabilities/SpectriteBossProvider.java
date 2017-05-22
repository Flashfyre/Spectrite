package com.samuel.spectritemod.capabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.samuel.spectritemod.SpectriteMod;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

public class SpectriteBossProvider implements ICapabilitySerializable<NBTTagCompound>, ICapabilityProvider {

	@CapabilityInject(ISpectriteBossCapability.class)
	public static Capability<ISpectriteBossCapability> sbc = null;
	
	public static Map<UUID, BossInfo> bossInfoMap = new HashMap<UUID, BossInfo>();
	
	private ISpectriteBossCapability capability = sbc.getDefaultInstance();
	
	public SpectriteBossProvider() { }
	
	public SpectriteBossProvider(ISpectriteBossCapability capability){
		this.capability = capability;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == sbc;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == sbc ? sbc.<T> cast(this.capability) : null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return (NBTTagCompound) sbc.getStorage().writeNBT(sbc, capability, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		sbc.getStorage().readNBT(sbc, capability, null, nbt);
	}
}
