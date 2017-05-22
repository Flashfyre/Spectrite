package com.samuel.spectritemod.capabilities;

import java.util.concurrent.Callable;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.etc.SpectriteHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants.NBT;

public class SpectriteBossCapability {
	
	public static class DefaultImpl implements ISpectriteBossCapability {
		
		private final EntityLivingBase entity;
		private boolean enabled;
		private boolean perfectSword;
		private BossInfo bossInfo;
		
		public static ResourceLocation resLoc = new ResourceLocation(SpectriteMod.MOD_ID, "SpectriteBoss");
	
		public DefaultImpl(EntityLivingBase entity, boolean enabled, boolean hasPerfectSword) {
			this.entity = entity;
			this.enabled = enabled;
			this.perfectSword = hasPerfectSword;
			if (entity != null && !entity.getEntityWorld().isRemote) {
				BossInfo bossInfo = (BossInfoServer)(new BossInfoServer(new TextComponentString(SpectriteHelper.getMultiColouredString(entity.getDisplayName().getUnformattedText(),
						Math.abs((int) entity.getUniqueID().getLeastSignificantBits() % 7))), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
				SpectriteBossProvider.bossInfoMap.put(entity.getUniqueID(), bossInfo);
				this.bossInfo = bossInfo;
			}
		}
		
		public static class Storage implements IStorage<ISpectriteBossCapability> {
			
			@Override
			public NBTBase writeNBT(Capability<ISpectriteBossCapability> capability,
				ISpectriteBossCapability instance, EnumFacing side) {
				NBTTagCompound properties = new NBTTagCompound();
				
				properties.setBoolean("enabled", instance.isEnabled());
				properties.setBoolean("perfectSword", instance.isPerfectSword());
				
				return properties;
			}

			@Override
			public void readNBT(Capability<ISpectriteBossCapability> capability,
					ISpectriteBossCapability instance, EnumFacing side, NBTBase nbt) {
				NBTTagCompound properties = (NBTTagCompound) nbt;
				instance.setEnabled(properties.getBoolean("enabled"));
				instance.setPerfectSword(properties.getBoolean("perfectSword"));
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
		public boolean isPerfectSword() {
			return perfectSword;
		}
		
		@Override
		public void setPerfectSword(boolean hasPerfectSword) {
			this.perfectSword = hasPerfectSword;
		}

		@Override
		public BossInfo getBossInfo() {
			return bossInfo;
		}
	}
}