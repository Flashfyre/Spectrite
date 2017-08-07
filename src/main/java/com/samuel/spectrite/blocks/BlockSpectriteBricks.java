package com.samuel.spectrite.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSpectriteBricks extends Block {
	
	public static final PropertyBool ODD = PropertyBool.create("odd");

	public BlockSpectriteBricks() {
		super(Material.IRON, MapColor.PURPLE);
		setSoundType(SoundType.METAL);
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
