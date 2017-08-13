package com.samuel.spectrite.blocks;

import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.tileentity.TileEntitySpectriteWitherSkeletonSkull;

import net.minecraft.block.BlockSkull;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpectriteWitherSkeletonSkull extends BlockSkull {
	
	public BlockSpectriteWitherSkeletonSkull() {
		super();
		this.setSoundType(SoundType.METAL);
	}

	@Override
	/**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySpectriteWitherSkeletonSkull();
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ModItems.spectrite_wither_skeleton_skull);
    }
	
	@Override
	/**
     * Gets the localized name of this block. Used for the statistics page.
     */
    public String getLocalizedName()
    {
        return I18n.translateToLocal(this.getUnlocalizedName() + ".name");
    }
	
	@Override
	public java.util.List<ItemStack> getDrops(IBlockAccess worldIn, BlockPos pos,
		 IBlockState state, int fortune) {
        java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        {
            if (!state.getValue(NODROP).booleanValue())
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TileEntitySpectriteWitherSkeletonSkull) {
                    TileEntitySpectriteWitherSkeletonSkull tileentityskull =
                    	(TileEntitySpectriteWitherSkeletonSkull)tileentity;
                    ItemStack itemstack = new ItemStack(ModItems.spectrite_wither_skeleton_skull, 1, tileentityskull.getSkullType());

                    ret.add(itemstack);
                }
            }
        }
        return ret;
    }
}
