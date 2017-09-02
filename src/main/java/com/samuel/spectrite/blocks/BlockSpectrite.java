package com.samuel.spectrite.blocks;

import com.google.common.base.Predicate;
import com.samuel.spectrite.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class BlockSpectrite extends Block {

	public static final PropertyBool ODD = PropertyBool.create("odd");
    private static BlockPattern golemPattern;
    private static final Predicate<IBlockState> IS_PUMPKIN = new Predicate<IBlockState>()
    {
        @Override
		public boolean apply(@Nullable IBlockState p_apply_1_)
        {
            return p_apply_1_ != null && (p_apply_1_.getBlock() == Blocks.PUMPKIN || p_apply_1_.getBlock() == Blocks.LIT_PUMPKIN);
        }
    };
	
	public BlockSpectrite() {
		super(Material.IRON, MapColor.PURPLE);
		setSoundType(SoundType.METAL);
	}

	@Override
	public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon)
    {
        return true;
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
	
    public static BlockPattern getGolemPattern()
    {
        if (golemPattern == null)
        {
            golemPattern = FactoryBlockPattern.start().aisle("~^~", "###", "~#~").where('^', BlockWorldState.hasState(IS_PUMPKIN)).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(ModBlocks.spectrite_block))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return golemPattern;
    }
}
