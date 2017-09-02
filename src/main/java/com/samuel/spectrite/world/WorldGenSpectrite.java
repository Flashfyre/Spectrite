package com.samuel.spectrite.world;

import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.init.ModBiomes;
import com.samuel.spectrite.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

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
		generateOre(ModBlocks.spectrite_ore, world, random, chunkX, chunkZ, SpectriteConfig.spectriteOre.spectriteCountEnd, SpectriteConfig.spectriteOre.spectriteMinYEnd,
			SpectriteConfig.spectriteOre.spectriteMaxYEnd);
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ) {
		final int chancesToSpawn = SpectriteConfig.spectriteOre.spectriteCountSurface * (world.getBiome(new BlockPos(chunkX << 4, 0, chunkZ << 4)) == ModBiomes.spectrite_dungeon ? 3 : 1);
		generateOre(ModBlocks.spectrite_ore, world, random, chunkX, chunkZ, chancesToSpawn, SpectriteConfig.spectriteOre.spectriteMinYSurface,
			SpectriteConfig.spectriteOre.spectriteMaxYSurface);
	}

	private void generateNether(World world, Random random, int chunkX, int chunkZ) {
		generateOre(ModBlocks.spectrite_ore, world, random, chunkX, chunkZ, SpectriteConfig.spectriteOre.spectriteCountNether, SpectriteConfig.spectriteOre.spectriteMinYNether,
			SpectriteConfig.spectriteOre.spectriteMaxYNether);
	}
	
	public class WorldGenSpectriteMinable extends WorldGenerator {

		private final IBlockState stateSurface = ModBlocks.spectrite_ore.getDefaultState(),
		stateNether = ModBlocks.spectrite_ore.getStateFromMeta(1),
		stateEnd = ModBlocks.spectrite_ore.getStateFromMeta(2);

	    public WorldGenSpectriteMinable() { }

	    @Override
	    public boolean generate(World world, Random rand, BlockPos pos) {
	    	final boolean isSurface = world.provider.getDimension() == 0,
	    	isNether = world.provider.getDimension() == -1;
	    	final Block matchBlock = isSurface ? Blocks.STONE : isNether ? Blocks.NETHERRACK : Blocks.END_STONE;
	    	final IBlockState oreState = isSurface ? stateSurface : isNether ? stateNether : stateEnd;
    		final int veinSize = rand.nextInt(rand.nextInt(rand.nextInt((isSurface ? SpectriteConfig.spectriteOre.spectriteMaxSizeSurface :
    			isNether ? SpectriteConfig.spectriteOre.spectriteMaxSizeNether : SpectriteConfig.spectriteOre.spectriteMaxSizeEnd) + 1) + 1) + 1)
				+ (isSurface ? SpectriteConfig.spectriteOre.spectriteMaxSizeSurface : isNether ? SpectriteConfig.spectriteOre.spectriteMaxSizeNether :
				SpectriteConfig.spectriteOre.spectriteMaxSizeEnd);
    		new WorldGenMinable(oreState, veinSize, BlockMatcher.forBlock(matchBlock)).generate(world, rand, pos);
    	    return true;
	    }
	}
}