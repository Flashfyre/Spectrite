package com.samuel.spectrite.blocks;

import com.samuel.spectrite.entities.EntitySpectriteGolem;
import com.samuel.spectrite.init.ModBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

public class BlockMoltenSpectrite extends BlockFluidClassic {
	
	public static final PropertyBool ODD = PropertyBool.create("odd");
	
	DamageSource damageSource = new DamageSource("molten_spectrite") {
		@Override
		/**
	     * Gets the death message that is displayed when the player dies
	     */
	    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
	    {
	        EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
	        String s = "death.attack." + this.damageType;
	        String s1 = s + ".player";
	        return entitylivingbase != null && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName()}): new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName()});
	    }
	};

	public BlockMoltenSpectrite(Fluid fluid) {
		super(fluid, Material.LAVA);
		this.setHardness(100f);
		this.setResistance(500f);
		this.setLightLevel(1.0f);
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn,
		BlockPos pos) {
		return false;
	}
	
	@Override
	/**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
		if (!worldIn.isRemote) {
	    	if (state.getBlock() == ModBlocks.molten_spectrite) {
	    		if (entityIn instanceof EntityLivingBase) {
	    			float damageMultiplier = entityIn instanceof EntitySpectriteGolem ? 0.25F : 1F;
	    			if (((EntityLivingBase) entityIn).attackEntityFrom(damageSource, (32.0F - state.getValue(BlockFluidBase.LEVEL)) * damageMultiplier)) {
	    				entityIn.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, worldIn.rand.nextFloat() * 0.4F);
	    			}
	    		} else if (entityIn instanceof EntityItem) {
	    			entityIn.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, worldIn.rand.nextFloat() * 0.4F);
	    			entityIn.attackEntityFrom(damageSource, 4.0F);
	    			entityIn.setFire(15);
	    		}
	    	}
		}
    }
	
	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return super.getActualState(state, worldIn, pos).withProperty(ODD, (pos.getX() + pos.getY() + pos.getZ()) % 2 != 0);
    }
	
	@Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[] { LEVEL, ODD }, FLUID_RENDER_PROPS.toArray(new IUnlistedProperty<?>[0]));
    }
}
