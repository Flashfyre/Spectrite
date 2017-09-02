package com.samuel.spectrite.world;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.init.ModBlocks;
import com.samuel.spectrite.init.ModLootTables;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
		//IBlockState state = null;
		
		Template template = templateManager.getTemplate(worldIn.getMinecraftServer(), SPECTRITE_SKULL);
		PlacementSettings settings = new PlacementSettings();
		settings.setRotation(rotationIn);
		template.addBlocksToWorld(worldIn, new BlockPos(1 + chunkXOffset, baseY, 1 + chunkZOffset), settings);
		/*for (int y = baseY; y < baseY + 18; y++) {
			for (int z = -31; z < 31; z++) {
				for (int x = -31; x < 31; x++) {
					if (worldIn.getBlockState(new BlockPos(x + chunkXOffset, y, z + chunkZOffset)).getBlock() == ModBlocks.spectrite_chest) {
						x = z;
						break;
					}
				}
			}
		}*/
		
		//worldIn.setBlockState(new BlockPos(chunkXOffset + 30, baseY + 18, chunkZOffset + 30), Blocks.REDSTONE_BLOCK.getDefaultState());
		
		int chestXPos = 11, chestZPos = 12, chestXPos2 = 11, chestZPos2 = 12;
		
		/*if (rotationIn.ordinal() == Rotation.CLOCKWISE_90.ordinal()) {
			chestXPos = (-chestXPos);
			chestZPos2 = --chestZPos;
			chestXPos2 = chestXPos + 1;
		} else if (rotationIn.ordinal() == Rotation.CLOCKWISE_180.ordinal()) {
			chestXPos2 = chestXPos = (-chestXPos) + 2;
			chestZPos2 = chestZPos = (-chestZPos) + 2;
			chestZPos2--;
		} else if (rotationIn.ordinal() == Rotation.COUNTERCLOCKWISE_90.ordinal()) {
			chestXPos++;
			chestZPos2 = chestZPos = (-chestZPos) + 3;
			chestXPos2 += 2;
		} else {*/
			chestZPos2++;
		//}
		
		ResourceLocation lootTable = ModLootTables.spectrite_dungeon_high;
		TileEntity te1 = worldIn.getTileEntity(new BlockPos(chestXPos + chunkXOffset, 12 + baseY, chestZPos + chunkZOffset)),
			te2 = worldIn.getTileEntity(new BlockPos(chestXPos2 + chunkXOffset, 12 + baseY, chestZPos2 + chunkZOffset));
		if (te1 != null) {
			((TileEntityChest) te1).setLootTable(lootTable, rand.nextLong());
		}
		if (te2 != null) {
			((TileEntityChest) te2).setLootTable(lootTable, rand.nextLong());
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
	
	public static int getGroundY(World worldIn, int chunkX, int chunkZ, EnumFacing facing, Random rand) {
		int ret;
		final int x = (chunkX << 4), z = (chunkZ << 4) + 12;
		final int dimId = worldIn.provider.getDimension();
		
		ret = worldIn.getWorldType() != WorldType.FLAT ? dimId == -1 ? getGroundYNether(worldIn, x, z, facing, rand) : SpectriteConfig.spectriteSkull.spectriteSkullSurfaceRate == 0d
			|| rand.nextDouble() * 100d <= SpectriteConfig.spectriteSkull.spectriteSkullSurfaceRate ? getGroundYSurfaceGroundLevel(worldIn, x, z, facing, rand)
			: getGroundYSurfaceUnderground(worldIn, x, z, facing, rand) : 3;
		
		if (ret == 0 && dimId == -1) {
			ret = getGroundYSurfaceUnderground(worldIn, x, z, facing, rand);
		}
		
		return ret;
	}
	
	private static int getGroundYSurfaceGroundLevel(World worldIn, int x, int z, EnumFacing facing, Random rand) {
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
	
	private static int getGroundYSurfaceUnderground(World worldIn, int x, int z, EnumFacing facing, Random rand) {
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
