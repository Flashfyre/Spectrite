package com.samuel.spectritemod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpectriteSand extends BlockFalling {

	public static final PropertyBool ODD = PropertyBool.create("odd");

	public BlockSpectriteSand() {
		super();
		setSoundType(SoundType.SAND);
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
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_)
    {
        return MapColor.PURPLE;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getDustColor(IBlockState p_189876_1_)
    {
		int hueFrame = Block.RANDOM.nextInt(720);
		float r = hueFrame >= 480 && hueFrame < 600 ? (1f / 120) * (hueFrame - 480) : hueFrame < 120 || hueFrame >= 600 ? 1f : hueFrame < 240 ? (1f / 120) * (120 - (hueFrame - 120)) : 0f,
			g = hueFrame < 120 ? (1f / 120) * hueFrame : hueFrame < 360 ? 1f : hueFrame < 480 ? (1f / 120) * (120 - (hueFrame - 360)) : 0f,
			b = hueFrame >= 240 && hueFrame < 360 ? (1f / 120) * (hueFrame - 240) : hueFrame >= 360 && hueFrame < 600 ? 1f : hueFrame >= 600 ? (1f / 120) * (120 - (hueFrame - 600)) : 0f;
        return 0xFF000000 | MathHelper.floor(r * 255F) << 16 | MathHelper.floor(g * 255F) << 8 | MathHelper.floor(b * 255F);
    }
}
