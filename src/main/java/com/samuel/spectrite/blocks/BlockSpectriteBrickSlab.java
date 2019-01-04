package com.samuel.spectrite.blocks;

import com.samuel.spectrite.init.ModBlocks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public abstract class BlockSpectriteBrickSlab extends BlockSlab {

	public static final PropertyBool ODD = PropertyBool.create("odd");
	
	public BlockSpectriteBrickSlab() {
		super(Material.IRON, MapColor.PURPLE);
		IBlockState blockState = this.blockState.getBaseState();
        if (!this.isDouble()) {
            blockState = blockState.withProperty(HALF, EnumBlockHalf.BOTTOM);
        }
        
        this.useNeighborBrightness = !this.isDouble();
        setDefaultState(blockState);
	}

	@Override
	public String getTranslationKey(int meta) {
		return this.getTranslationKey();
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return HALF;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return EnumBlockHalf.BOTTOM;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}
	
	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(ODD, (pos.getX() + pos.getY() + pos.getZ()) % 2 != 0);
    }

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (!this.isDouble())
			return this.getDefaultState().withProperty(HALF,
					EnumBlockHalf.values()[meta % 2]).withProperty(ODD, (meta & 2) == 0);
		return this.getDefaultState().withProperty(ODD, (meta & 2) == 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if (this.isDouble())
			return state.getValue(ODD) ? 1 : 0;
		return state.getValue(HALF).ordinal() + 1 + (state.getValue(ODD) ? 2 : 0);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(ModBlocks.spectrite_brick_slab_half);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { HALF, ODD });
	}
}
