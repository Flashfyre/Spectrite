package com.samuel.spectritemod.world;

import java.util.Random;

import com.google.common.base.Predicate;
import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.init.ModBiomes;
import com.samuel.spectritemod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenSpectrite implements IWorldGenerator {
	
	private final WorldGenerator spectriteMinable;
	
	public WorldGenSpectrite() {
		super();
		spectriteMinable = new WorldGenSpectriteMinable();
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
		IChunkProvider chunkProvider) {
		
		switch(world.provider.getDimension()) {
			case 0:
				generateSurface(world, random, chunkX, chunkZ);
				break;
			case -1:
				generateNether(world, random, chunkX, chunkZ);
				break;
			case 1:
				generateEnd(world, random, chunkX, chunkZ);
				break;
			default:
		}	
		
	}
	
	private void generateOre(Block block, World world, Random rand, int chunkX, int chunkZ,
		int chancesToSpawn, int minHeight, int maxHeight) {
		if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
	        throw new IllegalArgumentException("Illegal Height Arguments for WorldGenSpectrite");

	    int heightDiff = maxHeight - minHeight + 1;
	    for (int i = 0; i < chancesToSpawn; i ++) {
	        int x = chunkX * 16 + rand.nextInt(16);
	        int y = minHeight + rand.nextInt(heightDiff);
	        int z = chunkZ * 16 + rand.nextInt(16);
	        spectriteMinable.generate(world, rand, new BlockPos(x, y, z));
	    }
	}
	
	private void generateEnd(World world, Random random, int chunkX, int chunkZ) {
		generateOre(ModBlocks.spectrite_ore, world, random, chunkX, chunkZ, SpectriteMod.Config.spectriteCountEnd, SpectriteMod.Config.spectriteMinYEnd,
			SpectriteMod.Config.spectriteMaxYEnd);
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ) {
		final int chancesToSpawn = SpectriteMod.Config.spectriteCountSurface * (world.getBiome(new BlockPos(chunkX << 4, 0, chunkZ << 4)) == ModBiomes.spectrite_dungeon ? 3 : 1);
		generateOre(ModBlocks.spectrite_ore, world, random, chunkX, chunkZ, chancesToSpawn, SpectriteMod.Config.spectriteMinYSurface,
			SpectriteMod.Config.spectriteMaxYEnd);
	}

	private void generateNether(World world, Random random, int chunkX, int chunkZ) {
		generateOre(ModBlocks.spectrite_ore, world, random, chunkX, chunkZ, SpectriteMod.Config.spectriteCountNether, SpectriteMod.Config.spectriteMinYNether,
			SpectriteMod.Config.spectriteMaxYNether);
	}
	
	public class WorldGenSpectriteMinable extends WorldGenerator {

		private final IBlockState stateSurface = ModBlocks.spectrite_ore.getDefaultState(),
		stateNether = ModBlocks.spectrite_ore.getStateFromMeta(1),
		stateEnd = ModBlocks.spectrite_ore.getStateFromMeta(2);
	    private final Predicate<IBlockState> targetSurface = BlockStateMatcher.forBlock(Blocks.STONE),
	    targetNether = BlockStateMatcher.forBlock(Blocks.NETHERRACK),
	    targetEnd = BlockStateMatcher.forBlock(Blocks.END_STONE);

	    public WorldGenSpectriteMinable() { }

	    @Override
	    public boolean generate(World world, Random rand, BlockPos pos) {
	    	final boolean isSurface = world.provider.getDimension() == 0,
	    	isNether = world.provider.getDimension() == -1,
	    	isEnd = world.provider.getDimension() == 1;
	    	final Block matchBlock = isSurface ? Blocks.STONE : isNether ? Blocks.NETHERRACK : Blocks.END_STONE;
	    	final IBlockState oreState = isSurface ? stateSurface : isNether ? stateNether : stateEnd;
    		final int veinSize = rand.nextInt(rand.nextInt(rand.nextInt((isSurface ? SpectriteMod.Config.spectriteMaxSizeSurface :
    			isNether ? SpectriteMod.Config.spectriteMaxSizeNether : SpectriteMod.Config.spectriteMaxSizeEnd) + 1) + 1) + 1)
				+ (isSurface ? SpectriteMod.Config.spectriteMaxSizeSurface : isNether ? SpectriteMod.Config.spectriteMaxSizeNether :
				SpectriteMod.Config.spectriteMaxSizeEnd);
    		new WorldGenMinable(oreState, veinSize, BlockMatcher.forBlock(matchBlock)).generate(world, rand, pos);
    	    return true;
	    }
	}
}