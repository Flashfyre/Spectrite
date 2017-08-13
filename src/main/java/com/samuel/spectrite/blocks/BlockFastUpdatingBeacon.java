package com.samuel.spectrite.blocks;

import java.util.Random;

import com.samuel.spectrite.tileentity.TileEntityFastUpdatingBeacon;

import net.minecraft.block.BlockBeacon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFastUpdatingBeacon extends BlockBeacon {

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityFastUpdatingBeacon();
    }
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.BEACON);
    }
}
