package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.init.ModBlocks;
import com.samuel.spectrite.init.ModPotions;
import com.samuel.spectrite.tileentity.TileEntitySpectriteWitherSkeletonSkull;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemSpectriteWitherSkeletonSkull extends ItemArmor {

	public ItemSpectriteWitherSkeletonSkull() {
		super(Spectrite.SPECTRITE_WITHER_SKELETON_SKULL, 4, EntityEquipmentSlot.HEAD);
		
		this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setMaxDamage(0);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (facing == EnumFacing.DOWN)
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
            {
                facing = EnumFacing.UP;
                pos = pos.down();
            }
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            boolean flag = block.isReplaceable(worldIn, pos);

            if (!flag)
            {
                if (!worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.isSideSolid(pos, facing, true))
                {
                    return EnumActionResult.FAIL;
                }

                pos = pos.offset(facing);
            }

            ItemStack itemstack = player.getHeldItem(hand);

            if (player.canPlayerEdit(pos, facing, itemstack) && ModBlocks.spectrite_wither_skeleton_skull.canPlaceBlockAt(worldIn, pos))
            {
                if (worldIn.isRemote)
                {
                    return EnumActionResult.SUCCESS;
                }
                else
                {
                    worldIn.setBlockState(pos, ModBlocks.spectrite_wither_skeleton_skull.getDefaultState().withProperty(BlockSkull.FACING, facing), 11);
                    int i = 0;

                    if (facing == EnumFacing.UP)
                    {
                        i = MathHelper.floor(player.rotationYaw * 16.0F / 360.0F + 0.5D) & 15;
                    }

                    TileEntity tileentity = worldIn.getTileEntity(pos);

                    if (tileentity instanceof TileEntitySpectriteWitherSkeletonSkull)
                    {
                    	TileEntitySpectriteWitherSkeletonSkull tileentityskull = (TileEntitySpectriteWitherSkeletonSkull)tileentity;

                        tileentityskull.setSkullRotation(i);
                        //ModBlocks.spectrite_wither_skeleton_skull.checkWitherSpawn(worldIn, pos, tileentityskull);
                    }

                    if (player instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)player, pos, itemstack);
                    }

                    itemstack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            }
            else
            {
                return EnumActionResult.FAIL;
            }
        }
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return "item.spectrite_wither_skeleton_skull";
    }
	
	@Override
    public boolean updateItemStackNBT(NBTTagCompound nbt) {
    	super.updateItemStackNBT(nbt);
        return false;
    }
	
	@Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote) {
			if (player.getActivePotionEffect(ModPotions.SPECTRITE_RESISTANCE) == null) {
				player.addPotionEffect(new PotionEffect(ModPotions.SPECTRITE_RESISTANCE, 16, 0, true, true));
			}
		}
    }
}
