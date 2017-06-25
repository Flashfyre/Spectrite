package com.samuel.spectritemod.blocks;

import net.minecraft.block.BlockLadder;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSpectriteLadder extends BlockLadder {

	public static final PropertyBool ODD = PropertyBool.create("odd");

	public BlockSpectriteLadder() {
		super();
		setSoundType(SoundType.METAL);
	}
	
	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return super.getActualState(state, worldIn, pos).withProperty(ODD, (pos.getX() + pos.getY() + pos.getZ()) % 2 != 0);
    }
	
	@Override
    protected BlockStateContainer createBlockState()
    {
		return new BlockStateContainer.Builder(this).add(FACING).add(ODD).build();
    }
}
