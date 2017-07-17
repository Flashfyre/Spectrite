package com.samuel.spectritemod.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class SpectriteDungeonData extends WorldSavedData {
	
	private boolean dungeonGenerated = false;

	public SpectriteDungeonData() {
		super("spectriteDungeon");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("dungeonGenerated")) {
			this.dungeonGenerated = nbt.getBoolean("dungeonGenerated");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("dungeonGenerated", dungeonGenerated);
		
		return compound;
	}
	
	public boolean isDungeonGenerated() {
		return dungeonGenerated;
	}

	public void setDungeonGenerated(boolean dungeonGenerated) {
		this.dungeonGenerated = dungeonGenerated;
		this.markDirty();
	}
}
