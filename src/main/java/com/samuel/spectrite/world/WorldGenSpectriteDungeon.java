package com.samuel.spectrite.world;

import com.google.common.collect.Lists;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.init.ModBiomes;
import com.samuel.spectrite.init.ModBlocks;
import com.samuel.spectrite.init.ModLootTables;
import com.samuel.spectrite.init.ModWorldGen;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class WorldGenSpectriteDungeon implements IWorldGenerator {

    protected Random rand = new Random();
    protected SpectriteDungeonData savedData;
    protected BlockPos spawnPos;
    protected ChunkPos spawnChunkPos;
    protected Map<ChunkPos, Room>[] posRoomsMap = null;
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
		IChunkProvider chunkProvider) {
		if (SpectriteConfig.spectriteDungeon.generateSpectriteDungeon && spawnPos != null && !world.isRemote
			&& world.provider.isSurfaceWorld() && world.getWorldInfo().isMapFeaturesEnabled()) {
			if (((savedData != null && savedData.isDungeonGenerated()) && posRoomsMap == null) || chunkX == spawnChunkPos.x && chunkZ == spawnChunkPos.z) {
				int y = getGroundY(chunkX, chunkZ, world);
				int i = 1;
		        this.rand.setSeed(world.getSeed());
		        long j = this.rand.nextLong();
		        long k = this.rand.nextLong();
		        
		        EnumFacing facing = null;

                long j1 = spawnChunkPos.x * j;
                long k1 = spawnChunkPos.z * k;
                this.rand.setSeed(j1 ^ k1 ^ world.getSeed());
                if (facing == null) {
                	facing = EnumFacing.values()[this.rand.nextInt(4) + 2];
                }
                
                int baseY = 20;
            	
            	populateChunkBiome(spawnChunkPos.x, spawnChunkPos.z, world);

            	CoreRoom coreRoom = new CoreRoom(rand, spawnChunkPos.x, 0, spawnChunkPos.z, (y - 6) - baseY, world, facing);
            	posRoomsMap = coreRoom.chunkPosRooms;
        	
            	if (!savedData.isDungeonGenerated()) {
            		coreRoom.build();
            		savedData.setDungeonGenerated(true);
            		return;
            	}
			}
			if (posRoomsMap != null) {
				ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
				boolean biomePopulated = false;
				for (int f = 0; f <= 2; f++) {
					if (!posRoomsMap[f].isEmpty()) {
						if (posRoomsMap[f].containsKey(chunkPos)) {
							if (!biomePopulated) {
								populateChunkBiome(chunkX, chunkZ, world);
								biomePopulated = true;
							}
							Room room =  posRoomsMap[f].get(chunkPos);
							if (room instanceof ConnectableRoom && ((ConnectableRoom) room).isReserved()) {
								((ConnectableRoom) room).setReserved(false);
								room.build();
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onWorldLoad(WorldEvent.Load e) {
		World world = e.getWorld();
		if (!world.isRemote && world.provider.isSurfaceWorld() && SpectriteConfig.spectriteDungeon.generateSpectriteDungeon) {
			if (world.getWorldType() != WorldType.FLAT && world.getActualHeight() >= 30) {
				initSpawnPoint(e.getWorld());
				savedData = (SpectriteDungeonData) world.getPerWorldStorage().getOrLoadData(
					SpectriteDungeonData.class, "spectriteDungeon");
				if (savedData == null) {
					savedData = new SpectriteDungeonData("spectriteDungeon");
					world.getPerWorldStorage().setData(savedData.mapName, savedData);
				}
				if (!savedData.isDungeonGenerated()) {
					int chunkX = spawnPos.getX() >> 4, chunkZ = spawnPos.getZ() >> 4;
					if (e.getWorld().isChunkGeneratedAt(chunkX, chunkZ)) {
						generate(world.rand, chunkX, chunkZ, world, null, null);
						savedData.setDungeonGenerated(true);
					}
				}
			} else {
				this.spawnPos = null;
			}
		}
	}
	
	public void initSpawnPoint(World world) {
		this.rand.setSeed(world.getSeed());
		int spawnChunkX = 0;
		int spawnChunkZ = 0;
		
		spawnChunkX = (rand.nextInt(256) + 4) * (rand.nextBoolean() ? 1 : -1);
		spawnChunkZ = (rand.nextInt(256) + 4) * (rand.nextBoolean() ? 1 : -1);
		
		this.spawnPos = new BlockPos((spawnChunkX << 4) + 8, 0, (spawnChunkZ << 4) + 8);
		this.spawnChunkPos = new ChunkPos(spawnChunkX, spawnChunkZ);
	}
	
	private static void populateChunkBiome(int chunkX, int chunkZ, World world) {
		byte sdBiomeId = (byte) Biome.getIdForBiome(ModBiomes.spectrite_dungeon);
		BlockPos pos = new BlockPos(chunkX << 4, 4, chunkZ << 4);
		byte[] biomeArray = world.getChunk(pos).getBiomeArray();
		
		for (int p = 0; p < biomeArray.length; p++) {
			biomeArray[p] = sdBiomeId;
		}
		world.getChunk(pos).setBiomeArray(biomeArray);
	}
	
	public static int getGroundY(int chunkX, int chunkZ, World world) {
		final int x = (chunkX << 4) + 6, z = (chunkZ << 4) + 6;
		
		int y = Math.min(128, world.getActualHeight());
		
		for (; y >= 29; y--) {
			BlockPos pos = new BlockPos(x, y, z);
			if (!world.isAirBlock(pos) && !world.isAirBlock(pos.east(4)) && !world.isAirBlock(pos.south(4)) && !world.isAirBlock(pos.west(4)) && !world.isAirBlock(pos.north(4))) {
				Iterator<BlockPos> depthCheckPosIterator = BlockPos.getAllInBox(pos, pos.east(2).south(2).down(3)).iterator();
				while (depthCheckPosIterator.hasNext()) {
					BlockPos checkPos = depthCheckPosIterator.next();
					if (world.isAirBlock(checkPos)) {
						y = checkPos.getY() - 1;
						break;
					}
				}
				break;
			} else if (world.getBlockState(pos).getBlock() == Room.wallState.getBlock()) {
				y -= 3;
				break;
			}
		}
		
		if (y == 29) {
			y = 0;
		}
		
		return y;
	}
	
	public BlockPos getSpawnPos() {
		return this.spawnPos;
	}
	
	public void setSpawnPos(BlockPos spawnPos) {
		this.spawnPos = spawnPos;
	}
	
	protected abstract static class Room {
		
		protected static IBlockState airState = Blocks.AIR.getDefaultState();
		protected static IBlockState wallState = ModBlocks.spectrite_bricks.getDefaultState();
	    protected static IBlockState stairsState = ModBlocks.spectrite_brick_stairs.getDefaultState();
	    protected static IBlockState slabState = ModBlocks.spectrite_brick_slab_half.getDefaultState();
	    protected static IBlockState ladderState = ModBlocks.spectrite_ladder.getDefaultState();
	    protected static IBlockState glassState = ModBlocks.spectrite_glass.getDefaultState();
	    protected static IBlockState chestState = Blocks.CHEST.getDefaultState();
	    protected static IBlockState spectriteChestState = ModBlocks.spectrite_chest.getDefaultState();
	    protected static IBlockState mineralState = ModBlocks.spectrite_block.getDefaultState();
	    protected static IBlockState fluidState = ModBlocks.molten_spectrite.getDefaultState();
	    protected static IBlockState portalState = ModBlocks.spectrite_portal.getDefaultState();
	    protected static IBlockState fakeWallState = ModBlocks.spectrite_bricks_fake.getDefaultState();
	    protected static IBlockState fakeTrappedChestState = Blocks.TRAPPED_CHEST.getDefaultState();
	    protected static IBlockState spectriteFakeTrappedChestState = ModBlocks.spectrite_chest_trapped_fake.getDefaultState();
	    protected static IBlockState tntState = Blocks.TNT.getDefaultState();
		protected final Random rand;
		protected final World world;
		protected final int chunkX, baseY, chunkZ;
		protected final int floor;
		protected final int depth;
		protected final EnumFacing facing;
		protected final List<Room> childRooms;
				
		protected Room(Random rand, int chunkX, int floor, int chunkZ, int depth, World world, EnumFacing facing) {
			this.rand = rand;
			this.chunkX = chunkX;
			this.baseY = 20 - (8 * floor);
			this.chunkZ = chunkZ;
			this.floor = floor;
			this.depth = depth;
			this.facing = facing;
			this.world = world;
			this.childRooms = new ArrayList<Room>();
		}
		
		public abstract void build();
		
		protected abstract void buildConns();
		
		protected abstract void reserveConns();
		
		protected void setBlockState(IBlockState state, int x, int y, int z) {
			world.setBlockState(new BlockPos((chunkX << 4) + x, baseY + y, (chunkZ << 4) + z), state, 16);
		}
		
		protected void fillRange(IBlockState state, int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
				boolean mirrorX, boolean mirrorZ, boolean mirrorAxis, EnumFacing facing, boolean useAltRotation) {
			if (facing == EnumFacing.SOUTH) {
				int tempMinX = minX, tempMaxX = maxX;
				minX = minZ;
				minZ = tempMinX;
				maxX = maxZ;
				maxZ = tempMaxX;
				if (mirrorX != mirrorZ) {
					boolean tempMirrorX = mirrorX;
					mirrorX = mirrorZ;
					mirrorZ = tempMirrorX;
				}
			} else if (facing == EnumFacing.WEST) {
				minX = 15 - minX;
				maxX = 15 - maxX;
			} else if (facing == EnumFacing.NORTH) {
				int tempMinX = minX, tempMaxX = maxX;
				minX = 15 - minZ;
				minZ = 15 - tempMinX;
				maxX = 15 - maxZ;
				maxZ = 15 - tempMaxX;
				if (mirrorX != mirrorZ) {
					boolean tempMirrorX = mirrorX;
					mirrorX = mirrorZ;
					mirrorZ = tempMirrorX;
				}
			}
			
			fillRange(state, minX, minY, minZ, maxX, maxY, maxZ, facing);
			
			if (mirrorX) {
				IBlockState rotatedState;
				if (state.getBlock() instanceof BlockStairs && facing.getAxis() == Axis.X) {
					rotatedState = state.withProperty(BlockStairs.FACING, state.getValue(BlockStairs.FACING).getOpposite());
				} else {
					rotatedState = state;
				}
				fillRange(rotatedState, 15 - minX, minY, minZ, 15 - maxX, maxY, maxZ, facing);
			}
			
			if (mirrorZ) {
				IBlockState rotatedState;
				if (state.getBlock() instanceof BlockStairs && facing.getAxis() == Axis.Z) {
					rotatedState = state.withProperty(BlockStairs.FACING, state.getValue(BlockStairs.FACING).getOpposite());
				} else {
					rotatedState = state;
				}
				fillRange(rotatedState, minX, minY, 15 - minZ, maxX, maxY, 15 - maxZ, facing);
			}
			
			if (mirrorX && mirrorZ) {
				IBlockState rotatedState;
				if (state.getBlock() instanceof BlockStairs) {
					rotatedState = state.withProperty(BlockStairs.FACING, state.getValue(BlockStairs.FACING).getOpposite());
				} else {
					rotatedState = state;
				}
				fillRange(rotatedState, 15 - minX, minY, 15 - minZ, 15 - maxX, maxY, 15 - maxZ, facing);
			}
			
			if (mirrorAxis) {
				IBlockState rotatedState;
				boolean isXAxis = facing.getAxis() == Axis.X;
				boolean isPosDir = facing.getAxisDirection() == AxisDirection.POSITIVE;
				if (state.getBlock() instanceof BlockStairs) {
					rotatedState = state.withProperty(BlockStairs.FACING, isPosDir ? facing.rotateY() : facing.rotateYCCW());
				} else {
					rotatedState = state;
				}
				fillRange(rotatedState, isXAxis ? minX : minZ, minY, isXAxis ? minZ : minX, isXAxis ? maxX : maxZ, maxY, isXAxis ? maxZ : maxX,
						isXAxis ? mirrorX : mirrorZ, isXAxis ? mirrorZ : mirrorX, false, !useAltRotation ? facing.rotateY() : facing.rotateYCCW());
			}
		}
		
		protected void fillRange(IBlockState state, int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
				boolean mirrorX, boolean mirrorZ, boolean mirrorAxis, EnumFacing facing) {
			fillRange(state, minX, minY, minZ, maxX, maxY, maxZ, mirrorX, mirrorZ, mirrorAxis, facing, false);
		}
		
		private void fillRange(IBlockState state, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, EnumFacing facing) {
			Iterator<BlockPos> posIterator = BlockPos.getAllInBox(new BlockPos((chunkX << 4) + minX, baseY + minY, (chunkZ << 4) + minZ),
				new BlockPos((chunkX << 4) + maxX, baseY + maxY, (chunkZ << 4) + maxZ)).iterator();
			
			while (posIterator.hasNext()) {
				world.setBlockState(posIterator.next(), state, 16);
			}
		}
		
		protected IBlockState getBlockState(int x, int y, int z, EnumFacing facing) {
			if (facing == EnumFacing.SOUTH) {
				int tempX = x;
				x = z;
				z = tempX;
			} else if (facing == EnumFacing.WEST) {
				x = 15 - x;
				z = 15 - z;
			} else if (facing == EnumFacing.NORTH) {
				int tempX = x;
				x = 15 - z;
				z = 15 - tempX;
			}
			
			return world.getBlockState(new BlockPos((chunkX << 4) + x, baseY + y, (chunkZ << 4) + z));
		}
		
		protected TileEntity getTileEntity(int x, int y, int z, EnumFacing facing) {
			if (facing == EnumFacing.SOUTH) {
				int tempX = x;
				x = z;
				z = tempX;
			} else if (facing == EnumFacing.WEST) {
				x = 15 - x;
				z = 15 - z;
			} else if (facing == EnumFacing.NORTH) {
				int tempX = x;
				x = 15 - z;
				z = 15 - tempX;
			}
			
			return world.getTileEntity(new BlockPos((chunkX << 4) + x, baseY + y, (chunkZ << 4) + z));
		}
		
		protected abstract EnumFacing[] getRandomSortedDirs();
		
		public int getDepth() {
			return this.depth;
		}
		
		public List<Room> getChildRooms() {
			return this.childRooms;
		}
	}
	
	protected static class ConnectableRoom extends Room {
		
		protected CoreRoom coreRoom;
		protected Room floorStartRoom;
		protected ConnectableRoom adoptedRoom;
		protected final Room prevRoom;
		protected int size;
		protected final int connsLeft;
		protected boolean mainRoute;
		protected EnumFacing baseDir;
		protected boolean sidePath;
		protected boolean deadEnd = false;
		protected boolean reserved = false;
		protected boolean hasGlassFloor = false;
		protected Map<EnumFacing, ConnectableRoom> reservedConnsMap;
		
		protected ConnectableRoom(Random rand, int chunkX, int floor, int chunkZ, Room prevRoom, int connsLeft, boolean sidePath, World world, EnumFacing facing) {
			super(rand, chunkX, floor, chunkZ, prevRoom.depth + 1, world, facing);
			this.prevRoom = prevRoom;
			this.connsLeft = connsLeft;
			if (prevRoom instanceof IStartRoom) {
				this.mainRoute = ((IStartRoom) prevRoom).getBaseDirs().indexOf(facing) == 3;
				this.floorStartRoom = prevRoom;
			} else {
				this.mainRoute = ((ConnectableRoom) prevRoom).isMainRoute();
				this.floorStartRoom = ((ConnectableRoom) prevRoom).getFloorStartRoom();
			}
			this.sidePath = sidePath;
			
			resetSeed();
			
			if (sidePath || connsLeft > 1) {
				if (!(this instanceof NewFloorRoom)) {
					this.size = (rand.nextInt(5) + 1) << 1;
				} else {
					this.size = ((ConnectableRoom) prevRoom).getSize();
				}
			} else {
				if (floor < 2) {
					this.size = (rand.nextInt(3) + 3) << 1;
				} else {
					this.size = 10;
				}
			}
			this.hasGlassFloor = (sidePath || !mainRoute || floor < 2) && rand.nextInt(Math.max(3, 16 - depth)) == 0;
			
			resetSeed();
		}
		
		protected ConnectableRoom(Random rand, int chunkX, int floor, int chunkZ, CoreRoom prevRoom, int connsLeft, World world, EnumFacing facing) {
			this(rand, chunkX, floor, chunkZ, prevRoom, connsLeft, false, world, facing);
			this.baseDir = facing;
			this.coreRoom = prevRoom;
			this.coreRoom.chunkPosRooms[floor].put(new ChunkPos(chunkX, chunkZ), this);
			ModWorldGen.spectriteDungeon.posRoomsMap[floor].put(new ChunkPos(chunkX, chunkZ), this);
		}
		
		protected ConnectableRoom(Random rand, int chunkX, int floor, int chunkZ, ConnectableRoom prevRoom, int connsLeft, boolean sidePath, World world, EnumFacing facing) {
			this(rand, chunkX, floor, chunkZ, (Room) prevRoom, connsLeft, sidePath, world, facing);
			this.coreRoom = prevRoom.coreRoom;
			if (this.getClass() != NewFloorRoom.class) {
				if (prevRoom.getClass() != NewFloorRoom.class) {
					this.baseDir = prevRoom.facing;
				} else {
					this.baseDir = facing;
				}
			} else {
				this.baseDir = null;
			}
			this.coreRoom.chunkPosRooms[floor].put(new ChunkPos(chunkX, chunkZ), this);
			ModWorldGen.spectriteDungeon.posRoomsMap[floor].put(new ChunkPos(chunkX, chunkZ), this);
		}
		
		@Override
		public void build() {
			int halfSize = size >> 1;
			
			Room.stairsState = stairsState.withProperty(BlockStairs.FACING, facing);
			
			fillRange(wallState, 0, 1, 6, 8 - halfSize, 5, 6, false, true, false, facing);
			fillRange(wallState, 0, 0, 6, 8 - halfSize, 0, 9, false, false, false, facing);
			fillRange(wallState, 0, 5, 6, 8 - halfSize, 6, 9, false, false, false, facing);
			fillRange(airState, 8 - halfSize, 1, 8 - halfSize, 7 + halfSize, (halfSize > 1 ? 5 : 6), 7 + halfSize, false, false, false, facing);
			fillRange(slabState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP), 0, 4, 7, (halfSize > 1 ? 7 : 6) - halfSize, 4, 8, false, false, false, facing);
			fillRange(airState, 0, 1, 7, 8 - halfSize, 3, 8, false, false, false, facing);
			if (halfSize > 1) {
				fillRange(stairsState.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP), 8 - halfSize, 5, 8 - halfSize, 8 - halfSize, 5, 7 + halfSize,
					true, false, true, facing.getOpposite(), true);
			} else {
				fillRange(stairsState.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, facing.getOpposite()),
					7 - halfSize, 5, 7, 7 - halfSize, 5, 8, false, false, false, facing);
				fillRange(airState, 7 - halfSize, 4, 7, 7 - halfSize, 4, 8, false, false, false, facing);
			}
			fillRange(wallState, 7 - halfSize, 1, 7 - halfSize, 7 - halfSize, 5, (7 - halfSize) + (halfSize - 1), true, true, true, facing);
			fillRange(stairsState, 7 - halfSize, 6, 7 - halfSize, 7 - halfSize, 6, 8 + halfSize, true, false, true, facing);
			fillRange(wallState, 0, 0, 6 - halfSize, 9 + halfSize, 0, 9 + halfSize, false, false, false, facing);
			fillRange(wallState, 8 - halfSize, 6, 8 - halfSize, 7 + halfSize, 6, 7 + halfSize, false, false, false, facing);
			
			fillRange(wallState, 5 - halfSize, -2, 5 - halfSize, 10 + halfSize, -2, 10 + halfSize, false, false, false, facing);
			fillRange(fluidState, 0, -1, 6, 5 - halfSize, -1, 9, false, false, false, facing);
			fillRange(fluidState, 6 - halfSize, -1, 6 - halfSize, 9 + halfSize, -1, 9 + halfSize, false, false, false, facing);
			fillRange(wallState, 5 - halfSize, -2, 5 - halfSize, 5, 7, 5 - halfSize, true, true, true, facing);
			fillRange(wallState, 0, -2, 4, 6 - halfSize, -2, 10, false, false, false, facing);
			fillRange(fluidState, 0, -1, 5, 6 - halfSize, 7, 5, false, true, false, facing);
			fillRange(wallState, 0, -2, 4, 6 - halfSize, 7, 4, false, true, false, facing);
			fillRange(fluidState, 0, 7, 5, 6 - halfSize, 7, 9, false, false, false, facing);
			fillRange(fluidState, 6 - halfSize, -1, 6 - halfSize, 5, 7, 6 - halfSize, true, true, true, facing);
			fillRange(wallState, 0, 8, 4, 6 - halfSize, 8, 11, false, false, false, facing);
			fillRange(fluidState, 6 - halfSize, 7, 6 - halfSize, 9 + halfSize, 7, 9 + halfSize, false, false, false, facing);
			fillRange(wallState, 5 - halfSize, 8, 5 - halfSize, 10 + halfSize, 8, 10 + halfSize, false, false, true, facing);
			
			if (reserved) {
				setReserved(false);
				buildConns();
			} else {
				buildConns();
				populateChunkBiome(chunkX, chunkZ, world);
			}
			
			if (hasGlassFloor) {
				buildGlassFloor();
			}
		}
		
		public void updateSize() {
			if (!sidePath) {
				if (floor < 2) {
					this.size = (rand.nextInt(3) + 3) << 1;
				} else {
					this.size = 10;
				}
			}
		}
		
		public Room getPrevRoom() {
			return this.prevRoom;
		}
		
		public Room getFloorStartRoom() {
			return this.floorStartRoom;
		}
		
		@Override
		protected EnumFacing[] getRandomSortedDirs() {
			List<EnumFacing> facingList = Lists.newArrayList(EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.NORTH);
			List<EnumFacing> ret = new ArrayList();
			
			facingList.remove(facing.getOpposite());
			
			while (ret.size() < 3) {
				int randDir = rand.nextInt(facingList.size());
				ret.add(facingList.get(randDir));
				facingList.remove(randDir);
			}
			
			return ret.toArray(new EnumFacing[3]);
		}
		
		public boolean isDeadEnd() {
			return this.deadEnd;
		}
		
		public void setDeadEnd(boolean deadEnd) {
			this.deadEnd = deadEnd;
		}
		
		public boolean isReserved() {
			return this.reserved;
		}
		
		public void setReserved(boolean reserved) {
			this.reserved = reserved;
			if (reserved) {
				this.reservedConnsMap = new HashMap<EnumFacing, ConnectableRoom>();
			} else {
				this.reservedConnsMap = null;
			}
		}
		
		public Map<EnumFacing, ConnectableRoom> getReservedConnsMap() {
			return reservedConnsMap;
		}
		
		public boolean shouldRoomsConnect(ConnectableRoom otherRoom) {
			Random rand = new Random();
			rand.setSeed(this.world.getSeed());
	        long j = rand.nextLong();
	        long k = rand.nextLong();
	        long j1 = chunkX * otherRoom.chunkX * j;
            long k1 = chunkZ * otherRoom.chunkZ * k;
			rand.setSeed(j1 ^ k1 ^ this.world.getSeed());
			
			return rand.nextInt(4) == 0;
		}
		
		@Override
		protected void reserveConns() {
			int connsLeft = !deadEnd ? this.connsLeft - 1 : 0;
			int halfSize = size >> 1;
			boolean sidePath = this.sidePath;
			
			resetSeed();
			
			EnumFacing[] dirs = !(this instanceof NewFloorRoom) ? getRandomSortedDirs() : ((NewFloorRoom) this).baseDirs.toArray(new EnumFacing[4]);
			BlockPos chunkBlockPos = new BlockPos(chunkX << 4, baseY, chunkZ << 4);
		
			for (int d = dirs.length; d > 0; d--) {
				EnumFacing dir = dirs[d - 1];
				BlockPos pos = chunkBlockPos.offset(dir, 16);
				ChunkPos chunkPos = new ChunkPos(pos);
				Room connRoom = coreRoom.chunkPosRooms[floor].get(chunkPos);
				boolean dirHasConn = connsLeft > 0;
				
				if (dirHasConn || connRoom != null) {
					if (connRoom == null) {
						if (((!(chunkPos.x == this.floorStartRoom.chunkX && (chunkPos.z == this.floorStartRoom.chunkZ - 1
							|| chunkPos.z == this.floorStartRoom.chunkZ + 1)) && !(chunkPos.z == this.floorStartRoom.chunkZ
							&& (chunkPos.x == this.floorStartRoom.chunkX - 1 || chunkPos.x == this.floorStartRoom.chunkX + 1))))
							|| this == this.floorStartRoom) {
							ConnectableRoom newRoom = new ConnectableRoom(new Random(), chunkPos.x, floor, chunkPos.z, this, connsLeft, sidePath, world, dir);
							if (connsLeft <= 1) {
								newRoom.setDeadEnd(true);
							} else {
								boolean newRoomHasConn = false;
								BlockPos newRoomChunkBlockPos = new BlockPos(chunkPos.x << 4, baseY, chunkPos.z << 4);
								EnumFacing[] newRoomDirs = newRoom.getRandomSortedDirs();
								for (int d2 = newRoomDirs.length; d2 > 0; d2--) {
									EnumFacing newRoomDir = newRoomDirs[d2 - 1];
									BlockPos newRoomPos = newRoomChunkBlockPos.offset(newRoomDir, 16);
									ChunkPos newRoomChunkPos = new ChunkPos(newRoomPos);
									Room newRoomConnRoom = coreRoom.chunkPosRooms[floor].get(newRoomChunkPos);
									if (newRoomConnRoom == null || ((newRoomConnRoom.getClass() == ConnectableRoom.class
										&& !((ConnectableRoom) newRoomConnRoom).isDeadEnd() && (sidePath || (((ConnectableRoom) newRoomConnRoom).isSidePath()))
										&& newRoom.shouldRoomsConnect((ConnectableRoom) newRoomConnRoom)))) {
										newRoomHasConn = true;
										break;
									}
								}
								if (!newRoomHasConn) {
									newRoom.setDeadEnd(true);
									newRoom.updateSize();
								}
							}
							childRooms.add(newRoom);
							if (!world.isChunkGeneratedAt(chunkPos.x, chunkPos.z)) {
								newRoom.setReserved(true);
							}
							newRoom.reserveConns();
	
							if (!sidePath && this != floorStartRoom) {
								sidePath = !newRoom.isSidePath();
							}
						} else {
							dirHasConn = false;
						}
					} else if (connRoom instanceof ConnectableRoom && (((ConnectableRoom) connRoom).isDeadEnd() || (!sidePath && !((ConnectableRoom) connRoom).isSidePath())
						|| (((ConnectableRoom) connRoom).getAdoptedRoom() != this && !shouldRoomsConnect((ConnectableRoom) connRoom)))) {
						dirHasConn = false;
					} else if (connRoom instanceof ConnectableRoom && !sidePath && ((ConnectableRoom) connRoom).isSidePath() && shouldRoomsConnect((ConnectableRoom) connRoom)) {
						this.adoptedRoom = ((ConnectableRoom) connRoom);
						adoptPath((ConnectableRoom) connRoom);
						sidePath = true;
					}
					
					if (dirHasConn && connsLeft > 0) {
						connsLeft = Math.min(rand.nextInt(connsLeft) + (d << 1), connsLeft - 1);
					}
				}
			}
			
			if (!sidePath && !deadEnd && childRooms.isEmpty() && prevRoom != floorStartRoom) {
				setSidePath(true);
			} else if (deadEnd && mainRoute && !sidePath && floor < 2) {
				NewFloorRoom newRoom = new NewFloorRoom(new Random(), chunkX, floor + 1, chunkZ, this, rand.nextInt(8) + 4 + (4 * floor), false, world, facing);
				childRooms.add(newRoom);
				newRoom.reserveConns();
			}
		}
		
		protected void adoptPath(ConnectableRoom otherRoom) {
			otherRoom.setSidePath(false);
			otherRoom.setMainRoute(mainRoute);
			otherRoom.setBaseDir(baseDir);
			
			if (!otherRoom.childRooms.isEmpty()) {
				otherRoom.adoptPath((ConnectableRoom) otherRoom.getChildRooms().get(0));
			} else {
				otherRoom.updateSize();
			}
		}
		
		protected void buildGlassFloor() {
			if (this.rand.nextBoolean()) {
				fillRange(glassState, 7, 0, 7, 8, 0, 8, false, false, false, facing);
			}
			
			if (size >= 4 && rand.nextBoolean()) {
				switch(rand.nextInt(3)) {
					case 0:
						fillRange(glassState, 6, 0, 6, 6, 0, 6, true, true, true, facing);
						break;
					case 1:
						fillRange(glassState, 6, 0, 7, 6, 0, 8, true, false, true, facing);
						break;
					case 2:
						fillRange(glassState, 6, 0, 6, 6, 0, 9, true, false, true, facing);
						break;
				}
			}
			
			if (size >= 6 && rand.nextBoolean()) {
				switch(rand.nextInt(6)) {
					case 0:
						fillRange(glassState, 5, 0, 5, 5, 0, 5, true, true, true, facing);
						break;
					case 1:
						fillRange(glassState, 5, 0, 5, 5, 0, 6, true, true, true, facing);
						break;
					case 2:
						fillRange(glassState, 5, 0, 6, 5, 0, 6, true, true, true, facing);
						break;
					case 3:
						fillRange(glassState, 5, 0, 5, 5, 0, 5, true, true, true, facing);
						fillRange(glassState, 5, 0, 7, 5, 0, 8, true, false, true, facing);
						break;
					case 4:
						fillRange(glassState, 5, 0, 7, 5, 0, 8, true, false, true, facing);
						break;
					case 5:
						fillRange(glassState, 5, 0, 6, 5, 0, 9, true, false, true, facing);
						break;
				}
			}
			
			if (size >= 8 && rand.nextBoolean()) {
				switch (rand.nextInt(10)) {
					case 0:
						fillRange(glassState, 4, 0, 4, 4, 0, 4, true, true, true, facing);
						break;
					case 1:
						fillRange(glassState, 4, 0, 4, 4, 0, 5, true, true, true, facing);
						break;
					case 2:
						fillRange(glassState, 4, 0, 5, 4, 0, 5, true, true, true, facing);
						break;
					case 3:
						fillRange(glassState, 4, 0, 4, 4, 0, 6, true, true, true, facing);
						break;
					case 4:
						fillRange(glassState, 4, 0, 4, 4, 0, 4, true, true, true, facing);
						fillRange(glassState, 4, 0, 6, 4, 0, 6, true, true, true, facing);
						break;
					case 5:
						fillRange(glassState, 4, 0, 4, 4, 0, 4, true, true, true, facing);
						fillRange(glassState, 4, 0, 6, 4, 0, 9, true, false, true, facing);
						break;
					case 6:
						fillRange(glassState, 4, 0, 6, 4, 0, 6, true, true, true, facing);
						break;
					case 7:
						fillRange(glassState, 4, 0, 6, 4, 0, 9, true, false, true, facing);
						break;
					case 8:
						fillRange(glassState, 4, 0, 7, 4, 0, 8, true, false, true, facing);
						break;
					case 9:
						fillRange(glassState, 4, 0, 4, 4, 0, 11, true, false, true, facing);
						break;
				}
			}
			
			if (size >= 10 && rand.nextBoolean()) {
				switch (rand.nextInt(21)) {
					case 0:
						fillRange(glassState, 3, 0, 3, 3, 0, 3, true, true, true, facing);
						break;
					case 1:
						fillRange(glassState, 3, 0, 3, 3, 0, 4, true, true, true, facing);
						break;
					case 2:
						fillRange(glassState, 3, 0, 4, 3, 0, 4, true, true, true, facing);
						break;
					case 3:
						fillRange(glassState, 3, 0, 3, 3, 0, 5, true, true, true, facing);
						break;
					case 4:
						fillRange(glassState, 3, 0, 4, 3, 0, 5, true, true, true, facing);
						break;
					case 5:
						fillRange(glassState, 3, 0, 3, 3, 0, 3, true, true, true, facing);
						fillRange(glassState, 3, 0, 5, 3, 0, 5, true, true, true, facing);
						break;
					case 6:
						fillRange(glassState, 3, 0, 5, 3, 0, 5, true, true, true, facing);
						break;
					case 7:
						fillRange(glassState, 3, 0, 3, 3, 0, 6, true, true, true, facing);
						break;
					case 8:
						fillRange(glassState, 3, 0, 4, 3, 0, 6, true, true, true, facing);
						break;
					case 9:
						fillRange(glassState, 3, 0, 4, 3, 0, 4, true, true, true, facing);
						fillRange(glassState, 3, 0, 6, 3, 0, 6, true, true, true, facing);
						break;
					case 10:
						fillRange(glassState, 3, 0, 5, 3, 0, 6, true, true, true, facing);
						break;
					case 11:
						fillRange(glassState, 3, 0, 6, 3, 0, 6, true, true, true, facing);
						break;
					case 12:
						fillRange(glassState, 3, 0, 3, 3, 0, 12, true, false, true, facing);
						break;
					case 13:
						fillRange(glassState, 3, 0, 3, 3, 0, 3, true, true, true, facing);
						fillRange(glassState, 3, 0, 5, 3, 0, 5, true, true, true, facing);
						fillRange(glassState, 3, 0, 7, 3, 0, 7, true, true, true, facing);
						break;
					case 14:
						fillRange(glassState, 3, 0, 3, 3, 0, 3, true, true, true, facing);
						fillRange(glassState, 3, 0, 5, 3, 0, 7, true, true, true, facing);
						break;
					case 15:
						fillRange(glassState, 3, 0, 4, 3, 0, 7, true, true, true, facing);
						break;
					case 16:
						fillRange(glassState, 3, 0, 5, 3, 0, 7, true, true, true, facing);
						break;
					case 17:
						fillRange(glassState, 3, 0, 6, 3, 0, 7, true, true, true, facing);
						break;
					case 18:
						fillRange(glassState, 3, 0, 3, 3, 0, 4, true, true, true, facing);
						break;
					case 19:
						fillRange(glassState, 3, 0, 7, 3, 0, 8, true, false, true, facing);
						break;
					case 20:
						fillRange(glassState, 3, 0, 3, 3, 0, 5, true, true, true, facing);
						fillRange(glassState, 3, 0, 7, 3, 0, 8, true, false, true, facing);
						break;
					case 22:
						fillRange(glassState, 3, 0, 7, 3, 0, 8, true, false, true, facing);
						break;
				}
			}
		}
		
		@Override
		protected void buildConns() {
			int connsLeft = this.connsLeft - 1;
			boolean sidePath = this.sidePath;
			boolean fakeChest = false;
			int halfSize = size >> 1;
			
			resetSeed();
			
			EnumFacing[] dirs = !(this instanceof NewFloorRoom) ? getRandomSortedDirs() : ((NewFloorRoom) this).baseDirs.toArray(new EnumFacing[4]);
			BlockPos chunkBlockPos = new BlockPos(chunkX << 4, baseY, chunkZ << 4);
			
			for (int d = dirs.length; d > 0; d--) {
				boolean deadEndConn = false;
				EnumFacing dir = dirs[d - 1];
				BlockPos pos = chunkBlockPos.offset(dir, 16);
				ChunkPos chunkPos = new ChunkPos(pos);
				Room connRoom = coreRoom.chunkPosRooms[floor].get(chunkPos);
				boolean dirHasConn = connsLeft > 0;
				boolean hiddenConn = false;
				boolean shouldRoomsConnect = false;
				
				if (dirHasConn || connRoom != null) {
					if (childRooms.contains(connRoom) || (adoptedRoom != null && adoptedRoom == connRoom)) {
						if (!((ConnectableRoom) connRoom).isReserved()) {
							connRoom.build();
						}
						dirHasConn = true;
						shouldRoomsConnect = true;
					} else if (connRoom instanceof ConnectableRoom && !deadEnd && !(deadEndConn = ((ConnectableRoom) connRoom).isDeadEnd())
						&& (shouldRoomsConnect = ((ConnectableRoom) connRoom).getAdoptedRoom() == this || ((sidePath || ((ConnectableRoom) connRoom).isSidePath())
						&& shouldRoomsConnect((ConnectableRoom) connRoom)))) {
						EnumFacing connFacing = dir.getOpposite();
						int connRoomDepth = connRoom.getDepth();
						if (!world.isChunkGeneratedAt(connRoom.chunkX, connRoom.chunkZ)) {
							((ConnectableRoom) connRoom).getReservedConnsMap().put(connFacing, this);
						} else {
							int connHalfSize = ((ConnectableRoom) connRoom).getSize() >> 1;
							
							if (Math.abs(depth - connRoomDepth) > (connRoomDepth > 1 && depth > 1 ? 3 : 7)) {
								if (connRoomDepth < depth) {
									connRoom.fillRange(wallState, 8 + connHalfSize, 1, 7, 8 + connHalfSize, 4, 8, false, false, false, connFacing);
								} else {
									hiddenConn = true;
								}
							}
			
							connRoom.fillRange(wallState, 8 + connHalfSize, 1, 6, 15, 4, 6, false, true, false, connFacing);
							connRoom.fillRange(wallState, (connHalfSize > 1 ? 8 : 9) + connHalfSize, 5, 6, 15, 6, 9, false, false, false, connFacing);
							if (connHalfSize == 1) {
								connRoom.fillRange(Room.stairsState.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, connFacing),
									8 + connHalfSize, 5, 7, 8 + connHalfSize, 5, 8, false, false, false, connFacing);
								connRoom.fillRange(airState, 8 + connHalfSize, 4, 7, 8 + connHalfSize, 4, 8, false, false, false, connFacing);
							}
							connRoom.fillRange(wallState, 8 + connHalfSize, 0, 6, 15, 0, 9, false, false, false, connFacing);
							connRoom.fillRange(slabState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP),
								(connHalfSize > 1 ? 8 : 9) + connHalfSize, 4, 7, 15, 4, 8, false, false, false, connFacing);
							connRoom.fillRange(airState, 7 + connHalfSize, 1, 7, 15, 3, 8, false, false, false, connFacing);
							
							connRoom.fillRange(wallState, 9 + connHalfSize, -2, 5, 15, -2, 10, false, false, false, connFacing);
							connRoom.fillRange(fluidState, 9 + connHalfSize, -1, 5, 15, -1, 10, false, false, false, connFacing);
							connRoom.fillRange(wallState, 10 + connHalfSize, 1, 4, 15, 6, 4, false, true, false, connFacing);
							connRoom.fillRange(fluidState, 9 + connHalfSize, 0, 5, 15, 7, 5, false, true, false, connFacing);
							connRoom.fillRange(wallState, 9 + connHalfSize, 8, 4, 15, 8, 11, false, false, false, connFacing);
							connRoom.fillRange(fluidState, 9 + connHalfSize, 7, 6, 15, 7, 9, false, false, false, connFacing);
							connRoom.fillRange(fluidState, 9 + connHalfSize, 0, 5, 15, 7, 5, false, true, false, connFacing);
						}
						if (Math.abs(connRoomDepth - depth) > (depth > 1 && ((ConnectableRoom) connRoom).getDepth() > 1 ? 3 : 7)) {
							if (depth < connRoomDepth) {
								hiddenConn = true;
							}
						}
						dirHasConn = true;
					}
					
					if (dirHasConn) {
	 					if (!deadEndConn && shouldRoomsConnect) {
							fillRange(wallState, 8 + halfSize, 1, 6, 15, 4, 6, false, true, false, dir);
							fillRange(wallState, (halfSize > 1 ? 8 : 9) + halfSize, 5, 6, 15, 6, 9, false, false, false, dir);
							if (halfSize == 1) {
								fillRange(stairsState.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, dir),
									8 + halfSize, 5, 7, 8 + halfSize, 5, 8, false, false, false, dir);
								fillRange(airState, 8 + halfSize, 4, 7, 8 + halfSize, 4, 8, false, false, false, dir);
							}
							fillRange(wallState, 8 + halfSize, 0, 6, 15, 0, 9, false, false, false, dir);
							fillRange(slabState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP),
								(halfSize > 1 ? 8 : 9) + halfSize, 4, 7, 15, 4, 8, false, false, false, dir);
							fillRange(airState, 8 + halfSize, 1, 7, 15, 3, 8, false, false, false, dir);
							
							if (hiddenConn) {
								fillRange(wallState, 8 + halfSize, 1, 7, 8 + halfSize, 5, 8, false, false, false, dir);
							}
							
							fillRange(wallState, 9 + halfSize, -2, 5, 15, -2, 10, false, false, false, dir);
							fillRange(fluidState, 9 + halfSize, -1, 5, 15, -1, 10, false, false, false, dir);
							fillRange(wallState, 10 + halfSize, -2, 4, 15, 7, 4, false, true, false, dir);
							fillRange(fluidState, 9 + halfSize, 0, 5, 15, 7, 5, false, true, false, dir);
							fillRange(wallState, 9 + halfSize, 8, 4, 15, 8, 11, false, false, false, dir);
							fillRange(fluidState, 9 + halfSize, 7, 6, 15, 7, 9, false, false, false, dir);
							fillRange(fluidState, 9 + halfSize, 0, 5, 15, 7, 5, false, true, false, dir);
						} else {
							dirHasConn = false;
						}
					}
					
					if (dirHasConn && connsLeft > 0) {
						connsLeft = Math.min(rand.nextInt(connsLeft) + (d << 1), connsLeft - 1);
					}
				}
				
				if (!dirHasConn) {
					fillRange(wallState, 8 + halfSize, 1, 7, 8 + halfSize, 5, 8, false, false, false, dir);
					
					fillRange(wallState, 10 + halfSize, -2, 6, 10 + halfSize, 7, 9, false, false, false, dir);
					fillRange(fluidState, 9 + halfSize, 0, 6, 9 + halfSize, 6, 9, false, false, false, dir);
				}
			}
			
			if (deadEnd && (!sidePath || depth >= 32 || rand.nextInt(Math.max(9 - (depth >> 2), 2)) == 0)) {
				fakeChest = size == 2 && !hasGlassFloor && rand.nextInt(4) == 0;
				boolean mainEnd = !sidePath && mainRoute;
				boolean highChest = !sidePath && floor == 2;
				if (!mainEnd || floor == 2) {
					if (mainEnd || prevRoom != floorStartRoom) {
						int xDist = !highChest ? (halfSize > 1 ? 6 : 7) + halfSize : 8;
						if (floor < 2 || !mainEnd) {
							boolean midChest = !highChest;
							int chestY = !highChest ? 1 : 2;
							ResourceLocation lootTable = null;
							if (highChest) {
								fillRange(stairsState.withProperty(BlockStairs.FACING, facing), xDist + 1, 1, 6, xDist + 1, 1, 9, true, false, true, facing.getOpposite(), true);
								fillRange(wallState, xDist - 1, 1, 7, xDist, 1, 8, false, false, false, facing);
								lootTable = ModLootTables.spectrite_dungeon_high;
							} else if (sidePath || floor < 2) {
								int midChestChance = (4 - (!sidePath ? 2 : 0)) - floor;
								midChest = midChestChance == 1 || rand.nextInt(midChestChance) == 0;
								lootTable = midChest ? ModLootTables.spectrite_dungeon_mid : ModLootTables.spectrite_dungeon_low;
							}
							if (!fakeChest) {
								if (!(getBlockState(xDist, chestY, 7, facing).getBlock() instanceof BlockChest)) {
									fillRange((SpectriteConfig.spectriteDungeon.spectriteDungeonChestMode.shouldChestTierUseSpectriteChest(highChest ? 2 : midChest ? 1 : 0) ?
										spectriteChestState : chestState).withProperty(BlockChest.FACING, facing.getOpposite()),
										xDist, chestY, 7, xDist, chestY, 8, false, false, false, facing);
									((TileEntityChest) getTileEntity(xDist, chestY, 7, facing)).setLootTable(lootTable, rand.nextLong());
									((TileEntityChest) getTileEntity(xDist, chestY, 8, facing)).setLootTable(lootTable, rand.nextLong());
								}
							} else {
								fillRange((SpectriteConfig.spectriteDungeon.spectriteDungeonChestMode.shouldChestTierUseSpectriteChest(midChest ? 1 : 0) ?
									spectriteFakeTrappedChestState : fakeTrappedChestState), xDist, chestY, 7, xDist, chestY, 8, false, false, false, facing);
								fillRange(tntState, xDist, chestY - 1, 7, xDist, chestY - 1, 8, false, false, false, facing);
								fillRange(fakeWallState, 0, chestY - 1, 7, xDist - 1, chestY - 1, 8, false, false, false, facing);
								fillRange(tntState, 0, chestY - 2, 7, xDist, chestY - 2, 8, false, false, false, facing);
							}
						} else {
							fillRange(stairsState.withProperty(BlockStairs.FACING, facing.getOpposite()), xDist + 3, 1, 4, xDist + 3, 1, 11, true, false, true, facing, true);
							fillRange(stairsState, xDist + 2, 1, 5, xDist + 2, 1, 10, true, false, true, facing);
							fillRange(airState, xDist - 2, 0, 6, xDist + 1, 0, 9, false, false, false, facing);
							fillRange(portalState, xDist - 2, 0, 6, xDist + 1, 0, 9, false, false, false, facing);
							fillRange(stairsState.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP), 7 - halfSize, 4, 7, 7 - halfSize, 4, 8, false, false, false, facing);
							fillRange(stairsState, 8 - halfSize, 4, 8 - halfSize, 8 - halfSize, 4, 7 + halfSize, true, false, true, facing.getOpposite(), true);
							fillRange(stairsState, 9 - halfSize, 1, 8 - halfSize, 9 - halfSize, 1, 8 - halfSize, true, true, true, facing.getOpposite(), true);
							fillRange(wallState, 8 - halfSize, 1, 8 - halfSize, 8 - halfSize, 3, 8 - halfSize, true, true, true, facing);
							fillRange(wallState, 8 - halfSize, 1, 11 - halfSize, 8 - halfSize, 3, 11 - halfSize, true, true, true, facing);
							fillRange(stairsState, 5 + halfSize, 1, 7 + halfSize, 5 + halfSize, 1, 7 + halfSize, true, true, true, facing.getOpposite(), true);
							fillRange(stairsState.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP),
								5 + halfSize, 3, 7 + halfSize, 5 + halfSize, 3, 7 + halfSize, true, true, true, facing.getOpposite(), true);
							fillRange(stairsState.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP),
								6 + halfSize, 3, 7 + halfSize, 6 + halfSize, 3, 7 + halfSize, true, true, true, facing);
							fillRange(stairsState, 2 + halfSize, 1, 7 + halfSize, 2 + halfSize, 1, 7 + halfSize, true, true, true, facing.getOpposite(), true);
							fillRange(stairsState.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP),
								2 + halfSize, 3, 7 + halfSize, 2 + halfSize, 3, 7 + halfSize, true, true, true, facing.getOpposite(), true);
							fillRange(airState, 8 - halfSize, 1, 2 + halfSize, 8 - halfSize, 3, 3 + halfSize, false, false, false, facing);
						}
					}
				} else if (mainEnd && floor < 2) {
					childRooms.get(childRooms.size() - 1).build();
				}
			}
				
			if (reserved && !reservedConnsMap.isEmpty()) {
				for (EnumFacing dir : reservedConnsMap.keySet()) {
					ConnectableRoom connRoom = reservedConnsMap.get(dir);
					int connHalfSize = connRoom.getSize() >> 1;
					int connRoomDepth = connRoom.getDepth();
					EnumFacing connFacing = dir.getOpposite();
					connRoom.fillRange(wallState, 8 + connHalfSize, 1, 6, 15, 4, 6, false, true, false, connFacing);
					connRoom.fillRange(wallState, (connHalfSize > 1 ? 8 : 9), 5, 6, 15, 5, 9, false, false, false, connFacing);
					if (connHalfSize == 1) {
						connRoom.fillRange(Room.stairsState.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, connFacing),
							8 + connHalfSize, 5, 7, 8 + connHalfSize, 5, 8, false, false, false, connFacing);
						connRoom.fillRange(airState, 8 + connHalfSize, 4, 7, 8 + connHalfSize, 4, 8, false, false, false, connFacing);
					}
					connRoom.fillRange(wallState, 8 + connHalfSize, 0, 6, 15, 0, 9, false, false, false, connFacing);
					connRoom.fillRange(slabState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP),
						(connHalfSize > 1 ? 8 : 9) + connHalfSize, 4, 7, 15, 4, 8, false, false, false, connFacing);
					connRoom.fillRange(airState, 7 + connHalfSize, 1, 7, 15, 3, 8, false, false, false, connFacing);
					
					connRoom.fillRange(wallState, 9 + connHalfSize, -2, 5, 15, -2, 10, false, false, false, connFacing);
					connRoom.fillRange(fluidState, 9 + connHalfSize, -1, 5, 15, -1, 10, false, false, false, connFacing);
					connRoom.fillRange(wallState, 10 + connHalfSize, 1, 4, 15, 6, 4, false, true, false, connFacing);
					connRoom.fillRange(fluidState, 9 + connHalfSize, 0, 5, 15, 7, 5, false, true, false, connFacing);
					connRoom.fillRange(wallState, 9 + connHalfSize, 8, 4, 15, 8, 11, false, false, false, connFacing);
					connRoom.fillRange(fluidState, 9 + connHalfSize, 7, 6, 15, 7, 9, false, false, false, connFacing);
					connRoom.fillRange(fluidState, 9 + connHalfSize, 0, 5, 15, 7, 5, false, true, false, connFacing);
					
					if (Math.abs(connRoomDepth - depth) > (depth > 1 && connRoom.getDepth() > 1 ? 3 : 7) && depth < connRoomDepth) {
						fillRange(wallState, 8 + halfSize, 1, 7, 8 + halfSize, 4, 8, false, false, false, dir);
					}
				}
			}
			
			if (this instanceof NewFloorRoom) {
				fillRange(wallState, 6, 4, 6, 9, 7, 6, false, true, true, facing);
				fillRange(wallState, 6, 1, 6, 9, 3, 6, false, true, false, facing);
				fillRange(wallState, 6, 1, 6, 6, 3, 9, false, false, false, facing);
				fillRange(slabState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP), 9, 3, 7, 9, 3, 8, false, false, false, facing);
				fillRange(ladderState.withProperty(BlockLadder.FACING, facing), 7, 1, 7, 7, 8, 8, false, false, false, facing);
				fillRange(airState, 8, 1, 7, 8, 8, 8, false, false, false, facing);
			}
		}
		
		public void resetSeed() {
			this.rand.setSeed(world.getSeed());
	        long j = this.rand.nextLong();
	        long k = this.rand.nextLong();
	        long j1 = chunkX * (j ^ (floor + 1));
            long k1 = chunkZ * (k ^ (floor + 1));
			this.rand.setSeed(j1 ^ k1 ^ world.getSeed());
		}
		
		public CoreRoom getCoreRoom() {
			return this.coreRoom;
		}
		
		public ConnectableRoom getAdoptedRoom() {
			return this.adoptedRoom;
		}
		
		public int getSize() {
			return this.size;
		}
		
		public boolean isMainRoute() {
			return this.mainRoute;
		}
		
		public void setMainRoute(boolean mainRoute) {
			this.mainRoute = mainRoute;
		}
		
		public boolean isSidePath() {
			return this.sidePath;
		}
		
		public void setSidePath(boolean sidePath) {
			this.sidePath = sidePath;
		}
		
		public EnumFacing getBaseDir() {
			return this.baseDir;
		}
		
		public void setBaseDir(EnumFacing baseDir) {
			this.baseDir = baseDir;
		}
	}
	
	protected static class NewFloorRoom extends ConnectableRoom implements IStartRoom {
		
		private final List<EnumFacing> baseDirs;
		
		protected NewFloorRoom(Random rand, int chunkX, int floor, int chunkZ, ConnectableRoom prevRoom, int connsLeft, boolean sidePath, World world, EnumFacing facing) {
			super(rand, chunkX, floor, chunkZ, (Room) prevRoom, connsLeft, sidePath, world, facing);
			this.baseDirs = Lists.newArrayList(getRandomSortedDirs());
			this.baseDir = null;
			this.coreRoom = prevRoom.coreRoom;
			this.coreRoom.chunkPosRooms[floor].put(new ChunkPos(chunkX, chunkZ), this);
			this.floorStartRoom = this;
			ModWorldGen.spectriteDungeon.posRoomsMap[floor].put(new ChunkPos(chunkX, chunkZ), this);
		}
		
		@Override
		protected EnumFacing[] getRandomSortedDirs() {
			List<EnumFacing> facingList = Lists.newArrayList(EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.NORTH);
			List<EnumFacing> ret = new ArrayList();
			
			while (ret.size() < 4) {
				int randDir = rand.nextInt(facingList.size());
				ret.add(facingList.get(randDir));
				facingList.remove(randDir);
			}
			
			return ret.toArray(new EnumFacing[4]);
		}
		
		@Override
		public List<EnumFacing> getBaseDirs() {
			return this.baseDirs;
		}
	}
	
	protected static class CoreRoom extends Room implements IStartRoom {
		
		private final int height;
		private final Map<ChunkPos, Room>[] chunkPosRooms;
		private final List<EnumFacing> baseDirs;
		
		protected CoreRoom(Random rand, int chunkX, int floor, int chunkZ, int height, World world, EnumFacing facing) {
			super(rand, chunkX, floor, chunkZ, 0, world, facing);
			this.height = height;
			baseDirs = Lists.newArrayList(getRandomSortedDirs());
			stairsState = stairsState.withProperty(BlockStairs.FACING, facing);
			chunkPosRooms = new Map[3];
			for (int f = 0; f <= 2; f++) {
				chunkPosRooms[f] = new HashMap<ChunkPos, Room>();
			}
			chunkPosRooms[0].put(new ChunkPos(chunkX, chunkZ), this);
			ModWorldGen.spectriteDungeon.posRoomsMap = chunkPosRooms;
		}
		
		@Override
		public void build() {
			fillRange(wallState, 6, 1, 6, 8, 9 + height, 6, false, true, false, facing);
			fillRange(wallState, 9, 4, 6, 9, 9 + height, 9, false, false, false, facing);
			fillRange(wallState, 6, 1, 6, 7, 6 + height, 8, false, false, false, facing);
			fillRange(wallState, 7, 10 + height, 7, 8, 10 + height, 8, false, false, false, facing);
			fillRange(slabState, 7, 11 + height, 7, 8, 11 + height, 8, false, false, false, facing);
			fillRange(stairsState, 6, 10 + height, 6, 6, 10 + height, 9, true, false, true, facing);
			fillRange(airState, 7, 12 + height, 7, 8, 13 + height, 8, false, false, false, facing);
			fillRange(wallState, 5, 7 + height, 6, 5, 8 + height, 6, false, true, false, facing);
			fillRange(stairsState, 5, 9 + height, 6, 5, 9 + height, 9, false, false, false, facing);
			fillRange(wallState, 4, 7 + height, 6, 4, 7 + height, 6, false, true, false, facing);
			fillRange(stairsState, 4, 8 + height, 6, 4, 8 + height, 6, false, true, false, facing);
			fillRange(stairsState, 3, 7 + height, 6, 3, 7 + height, 6, false, true, false, facing);
			fillRange(wallState, 3, 6 + height, 6, 3, 6 + height, 6, false, true, false, facing);
			fillRange(wallState, 4, 6 + height, 6, 5, 6 + height, 9, false, false, false, facing);
			fillRange(airState, 7, 1, 7, 8, 9 + height, 8, false, false, false, facing);
			fillRange(ladderState.withProperty(BlockLadder.FACING, facing), 7, 1, 7, 7, 6 + height, 8, false, false, false, facing);
			fillRange(slabState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP), 9, 3, 7, 9, 3, 8, false, false, false, facing);
			fillRange(wallState, 9, 1, 6, 9, 3, 6, false, true, false, facing);
			fillRange(airState, 9, 1, 7, 9, 2, 8, false, false, false, facing);
			fillRange(airState, 4, 1, 4, 11, 4, 5, false, true, true, facing);
			fillRange(stairsState.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP), 4, 4, 4, 4, 4, 11, true, false, true, facing.getOpposite(), true);
			fillRange(wallState, 3, 5, 3, 12, 6, 5, false, true, true, facing);
			fillRange(wallState, 3, 0, 3, 12, 0, 12, false, false, false, facing);
			fillRange(wallState, 3, 1, 3, 6, 4, 3, true, true, true, facing);
			fillRange(wallState, 7, 4, 3, 8, 4, 3, false, true, true, facing);
			fillRange(slabState.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP), 7, 3, 3, 8, 3, 3, false, true, true, facing);
			fillRange(wallState, 6, 1, 0, 6, 6, 2, true, true, true, facing);
			fillRange(wallState, 6, 0, 0, 9, 0, 3, false, true, true, facing);
			fillRange(airState, 7, 1, 3, 8, 2, 3, false, true, true, facing);
			fillRange(wallState, 7, 5, 0, 8, 6, 2, false, true, true, facing);
			fillRange(airState, 7, 1, 0, 8, 4, 2, false, true, true, facing);
			fillRange(stairsState.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP), 2, 4, 7, 2, 4, 8, true, false, true, facing);
			
			fillRange(wallState, 1, -2, 1, 14, -2, 14, false, false, false, facing);
			fillRange(fluidState, 2, -1, 2, 13, -1, 13, false, false, false, facing);
			fillRange(fluidState, 0, -1, 6, 1, -1, 9, false, true, true, facing);
			fillRange(wallState, 0, -1, 5, 1, -1, 5, true, true, true, facing);
			fillRange(wallState, 0, -2, 5, 1, -2, 9, false, true, true, facing);
			fillRange(wallState, 1, -2, 1, 5, 8, 1, true, true, true, facing);
			fillRange(fluidState, 2, 0, 2, 5, 7, 2, true, true, true, facing);
			fillRange(wallState, 0, -2, 4, 1, -2, 10, true, false, true, facing);
			fillRange(fluidState, 0, -1, 5, 1, -1, 9, true, false, true, facing);
			fillRange(fluidState, 0, -1, 5, 1, 7, 5, true, true, true, facing);
			fillRange(wallState, 0, -2, 4, 1, 8, 4, true, true, true, facing);
			fillRange(wallState, 0, 8, 5, 1, 8, 7, true, true, true, facing);
			fillRange(wallState, 5, 8, 1, 8, 8, 1, true, true, true, facing);
			fillRange(fluidState, 2, 7, 2, 5, 7, 13, true, true, true, facing);
			fillRange(wallState, 1, 8, 1, 14, 8, 5, false, true, true, facing);
			fillRange(fluidState, 0, 7, 6, 1, 7, 9, true, false, true, facing);
			
			reserveConns();
			buildConns();
		}
		
		@Override
		public List<EnumFacing> getBaseDirs() {
			return this.baseDirs;
		}
		
		@Override
		protected EnumFacing[] getRandomSortedDirs() {
			List<EnumFacing> facingList = Lists.newArrayList(EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.NORTH);
			List<EnumFacing> ret = new ArrayList();
			
			while (ret.size() < 4) {
				int randDir = rand.nextInt(facingList.size());
				ret.add(facingList.get(randDir));
				facingList.remove(randDir);
			}
			
			return ret.toArray(new EnumFacing[4]);
		}

		@Override
		protected void reserveConns() {
			int connsLeft = rand.nextInt(8) + 4;
			BlockPos chunkBlockPos = new BlockPos(chunkX << 4, baseY, chunkZ << 4);
			
			for (int d = baseDirs.size(); d > 0; d--) {
				EnumFacing dir = baseDirs.get(d - 1);
				BlockPos pos = chunkBlockPos.offset(dir, 16);
				ChunkPos chunkPos = new ChunkPos(pos);
				ConnectableRoom newRoom = new ConnectableRoom(new Random(), chunkPos.x, floor, chunkPos.z, this, connsLeft, world, dir);
				childRooms.add(newRoom);
				newRoom.reserveConns();
				connsLeft = Math.min(rand.nextInt(connsLeft) + (d << 1), connsLeft - 1);
			}
		}
		
		@Override
		protected void buildConns() {
			for (Room connRoom : childRooms) {
				if (world.isChunkGeneratedAt(connRoom.chunkX, connRoom.chunkZ)) {
					connRoom.build();
				} else {
					((ConnectableRoom) connRoom).setReserved(true);
				}
			}
		}
	}
	
	protected interface IStartRoom {
		List<EnumFacing> getBaseDirs();
	}
}
