package com.samuel.spectrite.blocks;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModBlocks;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockSpectriteFire extends BlockFire {

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return SpectriteHelper.getSpectriteMapColour((World) worldIn, pos);
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(24) == 0)
        {
            worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }

        if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !ModBlocks.spectrite_fire.canCatchFire(worldIn, pos.down(), EnumFacing.UP))
        {
            if (ModBlocks.spectrite_fire.canCatchFire(worldIn, pos.west(), EnumFacing.EAST))
            {
                for (int j = 0; j < 2; ++j)
                {
                    double d3 = pos.getX() + rand.nextDouble() * 0.10000000149011612D;
                    double d8 = pos.getY() + rand.nextDouble();
                    double d13 = pos.getZ() + rand.nextDouble();
                    Spectrite.Proxy.spawnSpectriteSmokeLargeParticle(worldIn, d3, d8, d13, 0.0D, 0.0D, 0.0D);
                }
            }

            if (ModBlocks.spectrite_fire.canCatchFire(worldIn, pos.east(), EnumFacing.WEST))
            {
                for (int k = 0; k < 2; ++k)
                {
                    double d4 = pos.getX() + 1 - rand.nextDouble() * 0.10000000149011612D;
                    double d9 = pos.getY() + rand.nextDouble();
                    double d14 = pos.getZ() + rand.nextDouble();
                    Spectrite.Proxy.spawnSpectriteSmokeLargeParticle(worldIn, d4, d9, d14, 0.0D, 0.0D, 0.0D);
                }
            }

            if (ModBlocks.spectrite_fire.canCatchFire(worldIn, pos.north(), EnumFacing.SOUTH))
            {
                for (int l = 0; l < 2; ++l)
                {
                    double d5 = pos.getX() + rand.nextDouble();
                    double d10 = pos.getY() + rand.nextDouble();
                    double d15 = pos.getZ() + rand.nextDouble() * 0.10000000149011612D;
                    Spectrite.Proxy.spawnSpectriteSmokeLargeParticle(worldIn, d5, d10, d15, 0.0D, 0.0D, 0.0D);
                }
            }

            if (ModBlocks.spectrite_fire.canCatchFire(worldIn, pos.south(), EnumFacing.NORTH))
            {
                for (int i1 = 0; i1 < 2; ++i1)
                {
                    double d6 = pos.getX() + rand.nextDouble();
                    double d11 = pos.getY() + rand.nextDouble();
                    double d16 = pos.getZ() + 1 - rand.nextDouble() * 0.10000000149011612D;
                    Spectrite.Proxy.spawnSpectriteSmokeLargeParticle(worldIn, d6, d11, d16, 0.0D, 0.0D, 0.0D);
                }
            }

            if (ModBlocks.spectrite_fire.canCatchFire(worldIn, pos.up(), EnumFacing.DOWN))
            {
                for (int j1 = 0; j1 < 2; ++j1)
                {
                    double d7 = pos.getX() + rand.nextDouble();
                    double d12 = pos.getY() + 1 - rand.nextDouble() * 0.10000000149011612D;
                    double d17 = pos.getZ() + rand.nextDouble();
                    Spectrite.Proxy.spawnSpectriteSmokeLargeParticle(worldIn, d7, d12, d17, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        else
        {
            for (int i = 0; i < 3; ++i)
            {
                double d0 = pos.getX() + rand.nextDouble();
                double d1 = pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
                double d2 = pos.getZ() + rand.nextDouble();
                Spectrite.Proxy.spawnSpectriteSmokeLargeParticle(worldIn, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
