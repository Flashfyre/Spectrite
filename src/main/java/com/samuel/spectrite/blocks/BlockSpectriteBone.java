package com.samuel.spectrite.blocks;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSpectriteBone extends BlockRotatedPillar {
	
	public static final PropertyBool ODD = PropertyBool.create("odd");

	public BlockSpectriteBone() {
        super(Material.IRON, MapColor.PURPLE);
        this.setSoundType(SoundType.METAL);
    }
	
	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(ODD, (pos.getX() + pos.getY() + pos.getZ()) % 2 != 0);
    }
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { AXIS, ODD });
    }
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
    {
		boolean canBreak = false;
		Item heldItem = null;
		if (!player.getHeldItemMainhand().isEmpty()) {
			heldItem = player.getHeldItemMainhand().getItem();
			canBreak = heldItem instanceof ItemTool && ((ItemTool) heldItem).getStrVsBlock(player.getHeldItemMainhand(), state) >= 10.0f;
		}
        return canBreak ? net.minecraftforge.common.ForgeHooks.blockStrength(state, player, worldIn, pos) : -1.0f;
    }
}
