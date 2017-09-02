package com.samuel.spectrite.blocks;

import com.samuel.spectrite.init.ModBlocks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSpectriteBrickStairs extends BlockStairs {
	
	public static final PropertyBool ODD = PropertyBool.create("odd");

	public BlockSpectriteBrickStairs() {
		super(ModBlocks.spectrite_bricks.getDefaultState());
	}
	
	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return super.getActualState(state, worldIn, pos).withProperty(ODD, (pos.getX() + pos.getY() + pos.getZ()) % 2 != 0);
    }
	
	@Override
    protected BlockStateContainer createBlockState()
    {
		return new BlockStateContainer.Builder(this).add(FACING).add(HALF).add(SHAPE).add(ODD).build();
    }
}
