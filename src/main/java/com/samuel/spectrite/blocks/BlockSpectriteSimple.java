package com.samuel.spectrite.blocks;

import com.samuel.spectrite.etc.SpectriteHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSpectriteSimple extends Block {

    public static final PropertyBool ODD = PropertyBool.create("odd");

    public BlockSpectriteSimple(Material blockMaterialIn) {
        super(blockMaterialIn, MapColor.PURPLE);
        setSoundType(SoundType.METAL);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return SpectriteHelper.getSpectriteMapColour((World) worldIn, pos);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(ODD, (pos.getX() + pos.getY() + pos.getZ()) % 2 != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer.Builder(this).add(ODD).build();
    }
}
