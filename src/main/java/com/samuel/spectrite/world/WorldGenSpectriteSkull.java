package com.samuel.spectrite.world;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.entities.AbstractSpectriteSkeleton;
import com.samuel.spectrite.entities.EntitySpectriteCreeper;
import com.samuel.spectrite.entities.EntitySpectriteSkeleton;
import com.samuel.spectrite.entities.EntitySpectriteWitherSkeleton;
import com.samuel.spectrite.init.ModBlocks;
import com.samuel.spectrite.init.ModLootTables;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class WorldGenSpectriteSkull implements IWorldGenerator {
	
	protected Random rand = new Random();
	protected SpectriteSkullData savedData;
    protected World world;
    protected TemplateManager templateManager;
    
    private static final List<AxisAlignedBB>[] skullBounds = new List[2];
    private static final ResourceLocation SPECTRITE_SKULL;
    private static final int OFFSET_X = 10, OFFSET_Z = 12;
    private static final IBlockState wallState = ModBlocks.spectrite_bricks.getDefaultState();
    private static final IBlockState stairsState = ModBlocks.spectrite_brick_stairs.getDefaultState();
    
    static {
		SPECTRITE_SKULL = new ResourceLocation(Spectrite.MOD_ID, "spectrite_skull");
		for (int d = 0; d < skullBounds.length; d++) {
			skullBounds[d] = new ArrayList<AxisAlignedBB>();
		}
    }
    
    private void initSkullStructure(World worldIn) {
		this.world = worldIn;
		this.templateManager = world.getSaveHandler().getStructureTemplateManager();
		savedData = (SpectriteSkullData) this.world.getPerWorldStorage().getOrLoadData(
			SpectriteSkullData.class, "spectriteSkull");
		if (savedData == null) {
			savedData = new SpectriteSkullData("spectriteSkull");
			world.getPerWorldStorage().setData(savedData.mapName, savedData);
		} else {
			for (int d = -1; d < 1; d++) {
				for (ChunkPos cp : savedData.getSpawnChunks(d)) {
					int baseY = savedData.getBaseYCoord(cp.x, cp.z, d);
					skullBounds[d + 1].add(new AxisAlignedBB(new BlockPos(cp.getXEnd() - OFFSET_X, baseY, cp.getZEnd() - OFFSET_Z),
						new BlockPos(cp.getXStart() + 16 + OFFSET_X, baseY + 18, cp.getZStart() + 16 + OFFSET_Z)));
				}
			}
		}
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World worldIn, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		int dimId;
		if (SpectriteConfig.spectriteSkull.generateSpectriteSkull && worldIn.getWorldInfo().isMapFeaturesEnabled()
			&& (((dimId = worldIn.provider.getDimension()) + 1) >> 1) == 0) {
			if (this.world == null) {
				initSkullStructure(worldIn);
			}
			random.setSeed(worldIn.getSeed());
	        long j = random.nextLong();
	        long k = random.nextLong();
	
	        long j1 = (chunkX >> 1) * j;
	        long k1 = (chunkZ >> 1) * k;
	        random.setSeed(j1 ^ k1 ^ worldIn.getSeed());
	        if (SpectriteConfig.spectriteSkull.spectriteSkullSpawnRate > 0d
				&& random.nextDouble() * 100d < SpectriteConfig.spectriteSkull.spectriteSkullSpawnRate) {
	        	boolean isOrigChunkX = chunkX % 2 != 0, isOrigChunkZ = chunkZ % 2 != 0;
	        	int origChunkX = chunkX + (isOrigChunkX ? 0 : 1), origChunkZ = chunkZ + (isOrigChunkZ ? 0 : 1);
				int baseY = savedData.getBaseYCoord(origChunkX, origChunkZ, dimId);
				if (baseY == -1) {
					baseY = getGroundY(worldIn, chunkX + 1, chunkZ + 1, EnumFacing.SOUTH, random);
					if (baseY == 0) {
						return;
					}
				} else if (baseY == 0) {
					return;
				}
				int chunksGenerated = savedData.getSkullChunksGenerated(origChunkX, origChunkZ, dimId);
				if (chunksGenerated == 0) {
					savedData.setSpawnChunk(origChunkX, origChunkZ, baseY, dimId);
					skullBounds[dimId + 1].add(new AxisAlignedBB(new BlockPos((origChunkX << 4) + (16 - OFFSET_X), baseY,
						(origChunkZ << 4) + (16 - OFFSET_Z)), new BlockPos((origChunkX << 4) + 16 + OFFSET_X, baseY + 18, (origChunkZ << 4) + 16 + OFFSET_Z)));
				}
				savedData.setSkullChunksGenerated(origChunkX, origChunkZ, ++chunksGenerated, dimId);
				world.getPerWorldStorage().setData(savedData.mapName, savedData);
				if (chunksGenerated == 4) {
					generateSkull(worldIn, origChunkX, origChunkZ, baseY, Rotation.NONE/*.values()[random.nextInt(4)]*/);
				}
	        }
		}
	}
	
	private void generateSkull(World worldIn, int chunkX, int chunkZ, int baseY, Rotation rotationIn) {
		int chunkXOffset = (chunkX << 4), chunkZOffset = (chunkZ << 4);

		Template template = templateManager.getTemplate(worldIn.getMinecraftServer(), SPECTRITE_SKULL);
		BlockPos skullPos = new BlockPos(1 + chunkXOffset, baseY, 1 + chunkZOffset);
		PlacementSettings settings = new PlacementSettings();
		settings.setRotation(rotationIn);
		template.addBlocksToWorldChunk(worldIn, skullPos, settings);

		boolean highTierChest = SpectriteConfig.spectriteSkull.spectriteSkullHighTierChestRate > 0F
			&& worldIn.rand.nextFloat() * 100F < SpectriteConfig.spectriteSkull.spectriteSkullHighTierChestRate;

		SpectriteConfig.EnumSpectriteSkullChestMode chestMode = SpectriteConfig.spectriteSkull.spectriteSkullChestMode;
		ResourceLocation lootTable = highTierChest ? ModLootTables.spectrite_dungeon_high : ModLootTables.spectrite_dungeon_mid;
		BlockPos chestPos1 = new BlockPos(11 + chunkXOffset, 13 + baseY, 12 + chunkZOffset),
			chestPos2 =  new BlockPos(11 + chunkXOffset, 13 + baseY, 13 + chunkZOffset);

		int[] equipmentTypes = AbstractSpectriteSkeleton.EQUIPMENT_TYPES;
		Map<BlockPos, String> entityPositions = template.getDataBlocks(skullPos, settings);
		for (Map.Entry<BlockPos, String> e : entityPositions.entrySet()) {
			BlockPos pos = e.getKey();
			String rawData = e.getValue();
			int[] data = new int[rawData.charAt(0) == '2' ? 2 : equipmentTypes.length + 2];
			for (int d = 0; d < data.length; d++) {
				int val = rawData.charAt(d) - 48;
				data[d] = val;
			}
			EntityLiving entity = null;
			switch (data[0]) {
				case 0:
					entity = new EntitySpectriteSkeleton(world);;
				case 1:
					if (entity == null)
						entity = new EntitySpectriteWitherSkeleton(world);
					int equipmentSet = 0;
					for (int t = 0; t < equipmentTypes.length; t++) {
						if (data[t + 2] == 1)
							equipmentSet |= equipmentTypes[t];
					}
					((AbstractSpectriteSkeleton) entity).setEquipmentSet(equipmentSet);
					break;
				case 2:
					entity = new EntitySpectriteCreeper(world);
					break;
			}
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			if (entity != null) {
				EnumFacing facing = EnumFacing.values()[data[1] + 2];
				float angle = facing.getHorizontalIndex() * 90f;
				if (angle > 180f)
					angle -= 360f;
				entity.setPositionAndRotation(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, angle, 0);
				entity.enablePersistence();
				world.spawnEntity(entity);
				entity.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			}
		}

		/*for (int z = -15; z < 15; z++) {
			for (int x = -15; x < 15; x++) {
				BlockPos chestPos = new BlockPos(x + chunkXOffset, baseY + 12, z + chunkZOffset);
				IBlockState chestState = worldIn.getBlockState(chestPos);
				if (chestState.getBlock() == ModBlocks.spectrite_chest) {
					if (chestPos1 == null) {
						chestPos1 = chestPos;
					} else {
						chestPos2 = chestPos;
					}
					if (chestMode == SpectriteConfig.EnumSpectriteSkullChestMode.NONE
						|| (!highTierChest && chestMode == SpectriteConfig.EnumSpectriteSkullChestMode.HIGH_TIER_ONLY)) {
						world.setTileEntity(chestPos, null);
					}
				}
			}
		}*/
		if (chestMode == SpectriteConfig.EnumSpectriteSkullChestMode.NONE
			|| (!highTierChest && chestMode == SpectriteConfig.EnumSpectriteSkullChestMode.HIGH_TIER_ONLY)) {
			world.setBlockState(chestPos1, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.EAST));
			world.setBlockState(chestPos2, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.EAST));
		}
		TileEntity te = worldIn.getTileEntity(chestPos1);
		if (te != null) {
			((TileEntityChest) te).setLootTable(lootTable, rand.nextLong());
		}
		te = worldIn.getTileEntity(chestPos2);
		if (te != null) {
			((TileEntityChest) te).setLootTable(lootTable, rand.nextLong());
		}

		buildSupport(worldIn, new BlockPos(chunkXOffset + (16 - (OFFSET_X - 6)), baseY, chunkZOffset + (16 - (OFFSET_Z - 4))));
		buildSupport(worldIn, new BlockPos(chunkXOffset + (16 + (OFFSET_X - 8)), baseY, chunkZOffset + (16 - (OFFSET_Z - 4))));
		buildSupport(worldIn, new BlockPos(chunkXOffset + (16 - (OFFSET_X - 6)), baseY, chunkZOffset + (16 + (OFFSET_Z - 4))));
		buildSupport(worldIn, new BlockPos(chunkXOffset + (16 + (OFFSET_X - 8)), baseY, chunkZOffset + (16 + (OFFSET_Z - 4))));
	}

	private void buildSupport(World worldIn, BlockPos pos) {
		int y = pos.getY();
		
		while (pos.getY() > 0 && !worldIn.isBlockFullCube(pos.down())) {
			pos = pos.down();
			worldIn.setBlockState(pos, wallState);
		}
		
		if (pos.getY() == y || y - pos.getY() == 1) {
			return;
		}
		
		BlockPos stairPos = pos.east().north(2);
		int s = 0;
		
		for (; s < 3; s++) {
			if (!worldIn.isBlockFullCube((stairPos = stairPos.south())) || !worldIn.isBlockFullCube(stairPos.up())) {
				worldIn.setBlockState(stairPos, stairsState.withProperty(BlockStairs.FACING, EnumFacing.WEST));
			}
		}
		
		stairPos = pos.west().north(2);
		s = 0;
		
		for (; s < 3; s++) {
			if (!worldIn.isBlockFullCube((stairPos = stairPos.south())) || !worldIn.isBlockFullCube(stairPos.up())) {
				worldIn.setBlockState(stairPos, stairsState.withProperty(BlockStairs.FACING, EnumFacing.EAST));
			}
		}
		
		if (!worldIn.isBlockFullCube((stairPos = pos.south())) || !worldIn.isBlockFullCube(stairPos.up())) {
			worldIn.setBlockState(stairPos, stairsState.withProperty(BlockStairs.FACING, EnumFacing.NORTH));
		}
		
		if (!worldIn.isBlockFullCube((stairPos = pos.north())) || !worldIn.isBlockFullCube(stairPos.up())) {
			worldIn.setBlockState(stairPos, stairsState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onWorldLoad(WorldEvent.Load e) {
		if (this.world == null && !e.getWorld().isRemote && SpectriteConfig.spectriteSkull.generateSpectriteSkull) {
			initSkullStructure(e.getWorld());
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onWorldUnload(WorldEvent.Unload e) {
		this.world = null;
		for (int d = 0; d <= 1; d++) {
			WorldGenSpectriteSkull.skullBounds[d].clear();
		}
	}

	public boolean isPosInSkullBounds(BlockPos pos, int dimId) {
		boolean ret = false;
		Vec3d vec = new Vec3d(pos);

		if ((dimId + 1) >> 1 == 0) {
			for (AxisAlignedBB b : skullBounds[dimId + 1]) {
				if (b.contains(vec)) {
					ret = true;
					break;
				}
			}
		}

		return ret;
	}
	
	public static int getGroundY(World worldIn, int chunkX, int chunkZ, EnumFacing facing, Random rand) {
		int ret;
		final int x = (chunkX << 4), z = (chunkZ << 4) + 12;
		final int dimId = worldIn.provider.getDimension();
		
		ret = worldIn.getWorldType() != WorldType.FLAT ? dimId == -1 ? getGroundYNether(worldIn, x, z, facing, rand) : (SpectriteConfig.spectriteSkull.spectriteSkullSurfaceRate > 0d
			&& rand.nextDouble() * 100d <= SpectriteConfig.spectriteSkull.spectriteSkullSurfaceRate) || worldIn.getBiome(new BlockPos(x, 1, z)).getBiomeClass() == BiomeSpectriteDungeon.class
			? getGroundYSurfaceGroundLevel(worldIn, x, z, facing) : getGroundYSurfaceUnderground(rand) : 3;
		
		if (ret == 0 && dimId == -1) {
			ret = getGroundYSurfaceUnderground(rand);
		}
		
		return ret;
	}
	
	private static int getGroundYSurfaceGroundLevel(World worldIn, int x, int z, EnumFacing facing) {
		int y = Math.min(128, worldIn.getActualHeight());
		
		for (; y >= 30; y--) {
			BlockPos pos = new BlockPos(x, y, z);
			if (!worldIn.isAirBlock(pos) && worldIn.isBlockFullCube(pos)) {
				Iterator<BlockPos> checkPosIterator = BlockPos.getAllInBox(pos, pos.down(3)).iterator();
				boolean sufficientDepth = true;
				while (checkPosIterator.hasNext()) {
					BlockPos checkPos = checkPosIterator.next();
					if (worldIn.isAirBlock(checkPos) || !worldIn.isBlockFullCube(checkPos)) {
						sufficientDepth = false;
						break;
					}
				}
				if (sufficientDepth) {
					pos = pos.up();
					boolean sufficientEntranceSpace = true;
					checkPosIterator = BlockPos.getAllInBox(pos, pos.offset(facing, 3)).iterator();
					while (checkPosIterator.hasNext()) {
						BlockPos checkPos = checkPosIterator.next();
						if (worldIn.isBlockFullCube(checkPos)) {
							sufficientEntranceSpace = false;
							break;
						}
					}
					if (sufficientEntranceSpace) {
						break;
					}
				}
			}
		}

		if (y == 29) {
			y = 0;
		}
		
		return y;
	}

	private static int getGroundYSurfaceUnderground(Random rand) {
		return rand.nextInt(6) + 5;
	}
	
	private static int getGroundYNether(World worldIn, int x, int z, EnumFacing facing, Random rand) {
		int y = Math.min(96, worldIn.getActualHeight()), openY = 0;
		
		for (; y >= 4; y--) {
			BlockPos pos = new BlockPos(x, y, z);
			if (!worldIn.isAirBlock(pos) && worldIn.isBlockFullCube(pos)) {
				if (openY >= 8) {
					Iterator<BlockPos> checkPosIterator = BlockPos.getAllInBox(pos, pos.down(3)).iterator();
					boolean sufficientDepth = true;
					while (checkPosIterator.hasNext()) {
						BlockPos checkPos = checkPosIterator.next();
						if (worldIn.isAirBlock(checkPos) || !worldIn.isBlockFullCube(checkPos)) {
							sufficientDepth = false;
							break;
						}
					}
					if (sufficientDepth) {
						pos = pos.up(3);
						boolean sufficientEntranceSpace = true;
						checkPosIterator = BlockPos.getAllInBox(pos, pos.offset(facing, 3)).iterator();
						while (checkPosIterator.hasNext()) {
							BlockPos checkPos = checkPosIterator.next();
							if (worldIn.isBlockFullCube(checkPos)) {
								sufficientEntranceSpace = false;
								break;
							}
						}
						if (sufficientEntranceSpace) {
							break;
						}
					}
				}
				openY = 0;
			} else {
				openY++;
			}
		}
		
		if (y == 4) {
			y = 0;
		} else if (y <= 30 && worldIn.getBlockState(new BlockPos(x, 30, z)).getBlock() == Blocks.LAVA) {
			y = 31;
		}
		
		return y;
	}
}
