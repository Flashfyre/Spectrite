package com.samuel.spectrite.blocks;

import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.init.ModWorldGen;
import com.samuel.spectrite.tileentity.TileEntitySpectritePortal;
import com.samuel.spectrite.world.WorldGenSpectriteDungeon;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSpectritePortal extends BlockEndPortal {

	public BlockSpectritePortal() {
		super(Material.PORTAL);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySpectritePortal();
    }
	
	@Override
	/**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss() && !worldIn.isRemote && entityIn.getEntityBoundingBox().intersects(state.getBoundingBox(worldIn, pos).offset(pos)))
        {
        	BlockPos spawnPos = SpectriteConfig.spectriteDungeon.generateSpectriteDungeon && worldIn.getWorldInfo().isMapFeaturesEnabled()
                && ModWorldGen.spectriteDungeon.getSpawnPos() != null ? ModWorldGen.spectriteDungeon.getSpawnPos()
    			: entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).getBedLocation() != null ?
                ((EntityPlayer) entityIn).getBedLocation() : worldIn.getSpawnPoint();
            entityIn.setPositionAndUpdate(spawnPos.getX(), WorldGenSpectriteDungeon.getGroundY(spawnPos.getX() >> 4, spawnPos.getZ() >> 4, worldIn) + 5,
        		spawnPos.getZ());
        }
    }
	
	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }
}
