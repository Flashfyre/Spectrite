package com.samuel.spectrite.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class SpectriteSkullData extends WorldSavedData {
	
	private static final String[] dimensions = new String[] { "Nether", "Overworld" };
	
	private List<ChunkPos>[] spawnChunks;
	private List<Integer>[] baseYCoords;
	private List<Integer>[] skullChunksGenerated;

	public SpectriteSkullData(String name) {
		super(name);
		this.spawnChunks = new List[2];
		this.baseYCoords = new List[2];
		this.skullChunksGenerated = new List[2];
		
		for (int d = 0; d <= 1; d++) {
			this.spawnChunks[d] = new ArrayList<ChunkPos>();
			this.baseYCoords[d] = new ArrayList<Integer>();
			this.skullChunksGenerated[d] = new ArrayList<Integer>();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		for (int d = 0; d < dimensions.length; d++) {
			if (nbt.hasKey("data" + dimensions[d])) {
				NBTTagList dataList = nbt.getTagList("data" + dimensions[d], Constants.NBT.TAG_INT_ARRAY);
				for (int cp = 0; cp < dataList.tagCount(); cp++) {
					int[] data = dataList.getIntArrayAt(cp);
					spawnChunks[d].add(new ChunkPos(data[0], data[1]));
					baseYCoords[d].add(data[2]);
					skullChunksGenerated[d].add(data[3]);
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		for (int d = 0; d < dimensions.length; d++) {
			NBTTagList dataList = new NBTTagList();
			for (int cp = 0; cp < spawnChunks[d].size(); cp++) {
				ChunkPos chunkPos = spawnChunks[d].get(cp);
				dataList.appendTag(new NBTTagIntArray(new int[] { chunkPos.x, chunkPos.z, baseYCoords[d].get(cp), skullChunksGenerated[d].get(cp) }));
			}
			
			compound.setTag("data" + dimensions[d], dataList);
		}
		
		return compound;
	}
	
	public List<ChunkPos> getSpawnChunks(int dimensionId) {
		return spawnChunks[dimensionId + 1];
	}

	public void setSpawnChunk(int chunkX, int chunkZ, int baseY, int dimensionId) {
		this.spawnChunks[dimensionId + 1].add(new ChunkPos(chunkX, chunkZ));
		this.baseYCoords[dimensionId + 1].add(baseY);
		this.skullChunksGenerated[dimensionId + 1].add(0);
		this.markDirty();
	}
	
	public int getBaseYCoord(int chunkX, int chunkZ, int dimensionId) {
		int i = spawnChunks[dimensionId + 1].indexOf(new ChunkPos(chunkX, chunkZ));
		return i > -1 ? baseYCoords[dimensionId + 1].get(i) : -1;
	}
	
	public int getSkullChunksGenerated(int chunkX, int chunkZ, int dimensionId) {
		int i = spawnChunks[dimensionId + 1].indexOf(new ChunkPos(chunkX, chunkZ));
		return i > -1 ? skullChunksGenerated[dimensionId + 1].get(i) : 0;
	}

	public void setSkullChunksGenerated(int chunkX, int chunkZ, int generatedChunkCount, int dimensionId) {
		int i = spawnChunks[dimensionId + 1].indexOf(new ChunkPos(chunkX, chunkZ));
		this.skullChunksGenerated[dimensionId + 1].set(i, generatedChunkCount);
		this.markDirty();
	}
}
