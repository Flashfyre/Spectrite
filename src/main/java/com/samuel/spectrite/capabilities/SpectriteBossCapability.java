package com.samuel.spectrite.capabilities;

import java.util.concurrent.Callable;

import com.samuel.spectrite.Spectrite;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SpectriteBossCapability {
	
	public static class DefaultImpl implements ISpectriteBossCapability {
		
		private final EntityLivingBase entity;
		private boolean enabled;
		
		public static ResourceLocation resLoc = new ResourceLocation(Spectrite.MOD_ID, "SpectriteBoss");
	
		public DefaultImpl(EntityLivingBase entity, boolean enabled) {
			this.entity = entity;
			this.enabled = enabled;
		}
		
		public static class Storage implements IStorage<ISpectriteBossCapability> {
			
			@Override
			public NBTBase writeNBT(Capability<ISpectriteBossCapability> capability,
				ISpectriteBossCapability instance, EnumFacing side) {
				NBTTagCompound properties = new NBTTagCompound();
				
				properties.setBoolean("enabled", instance.isEnabled());
				
				return properties;
			}

			@Override
			public void readNBT(Capability<ISpectriteBossCapability> capability,
					ISpectriteBossCapability instance, EnumFacing side, NBTBase nbt) {
				NBTTagCompound properties = (NBTTagCompound) nbt;
				instance.setEnabled(properties.getBoolean("enabled"));
			}
		}
		
		public static class Factory implements Callable<ISpectriteBossCapability> {
			
			@Override
			public ISpectriteBossCapability call() throws Exception {
				return new DefaultImpl(null, false);
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
	}
}