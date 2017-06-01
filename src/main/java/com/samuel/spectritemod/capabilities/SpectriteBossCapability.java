package com.samuel.spectritemod.capabilities;

import java.util.concurrent.Callable;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.etc.SpectriteHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SpectriteBossCapability {
	
	public static class DefaultImpl implements ISpectriteBossCapability {
		
		private final EntityLivingBase entity;
		private boolean enabled;
		private boolean perfectWeapon;
		private BossInfo bossInfo;
		
		public static ResourceLocation resLoc = new ResourceLocation(SpectriteMod.MOD_ID, "SpectriteBoss");
	
		public DefaultImpl(EntityLivingBase entity, boolean enabled, boolean hasPerfectWeapon) {
			this.entity = entity;
			this.enabled = enabled;
			this.perfectWeapon = hasPerfectWeapon;
			if (entity != null && !entity.getEntityWorld().isRemote) {
				BossInfo bossInfo = (BossInfoServer)(new BossInfoServer(new TextComponentString(SpectriteHelper.getMultiColouredString(entity.getDisplayName().getUnformattedText(),
						Math.abs((int) entity.getUniqueID().getLeastSignificantBits() % 7))), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
				this.bossInfo = bossInfo;
			}
		}
		
		public static class Storage implements IStorage<ISpectriteBossCapability> {
			
			@Override
			public NBTBase writeNBT(Capability<ISpectriteBossCapability> capability,
				ISpectriteBossCapability instance, EnumFacing side) {
				NBTTagCompound properties = new NBTTagCompound();
				
				properties.setBoolean("enabled", instance.isEnabled());
				properties.setBoolean("perfectWeapon", instance.isPerfectWeapon());
				
				return properties;
			}

			@Override
			public void readNBT(Capability<ISpectriteBossCapability> capability,
					ISpectriteBossCapability instance, EnumFacing side, NBTBase nbt) {
				NBTTagCompound properties = (NBTTagCompound) nbt;
				instance.setEnabled(properties.getBoolean("enabled"));
				instance.setPerfectWeapon(properties.getBoolean("perfectWeapon"));
			}
		}
		
		public static class Factory implements Callable<ISpectriteBossCapability> {
			
			@Override
			public ISpectriteBossCapability call() throws Exception {
				return new DefaultImpl(null, false, false);
			}
		}
		
		@Override
		public boolean isEnabled() {
			return enabled;
		}
		
		@Override
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public boolean isPerfectWeapon() {
			return perfectWeapon;
		}
		
		@Override
		public void setPerfectWeapon(boolean perfectWeapon) {
			this.perfectWeapon = perfectWeapon;
		}

		@Override
		public BossInfo getBossInfo() {
			return bossInfo;
		}
	}
}