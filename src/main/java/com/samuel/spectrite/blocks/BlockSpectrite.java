package com.samuel.spectrite.blocks;

import com.google.common.base.Predicate;
import com.samuel.spectrite.init.ModBlocks;
import net.minecraft.block.material.Material;
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

public class BlockSpectrite extends BlockSpectriteSimple {

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
		super(Material.IRON);
	}

	@Override
	public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon)
    {
        return true;
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
