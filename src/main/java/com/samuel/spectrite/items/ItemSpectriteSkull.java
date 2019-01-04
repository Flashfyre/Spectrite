package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.blocks.BlockSpectriteSkull;
import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.init.ModBlocks;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModPotions;
import com.samuel.spectrite.tileentity.TileEntitySpectriteSkull;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
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

import javax.annotation.Nullable;

public class ItemSpectriteSkull extends ItemArmor implements ISpectriteCustomTooltipItem {

    public static final String[] SKULL_TYPES = new String[] {"wither_skeleton", "wither", "wither_invulnerable"};
    private final int skullType;

	protected ItemSpectriteSkull(int skullType) {
		super(skullType == 0 ? Spectrite.SPECTRITE_WITHER_SKELETON_SKULL : skullType == 1 ? Spectrite.SPECTRITE_WITHER_SKULL : Spectrite.SPECTRITE_WITHER_INVULNERABLE_SKULL, 4, EntityEquipmentSlot.HEAD);
        this.skullType = skullType;
	}

    public ItemSpectriteSkull() {
        this(0);
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

            if (player.canPlayerEdit(pos, facing, itemstack) && ModBlocks.spectrite_skull.canPlaceBlockAt(worldIn, pos))
            {
                if (worldIn.isRemote)
                {
                    return EnumActionResult.SUCCESS;
                }
                else
                {
                    worldIn.setBlockState(pos, ModBlocks.spectrite_skull.getDefaultState().withProperty(BlockSpectriteSkull.TYPE, this.skullType)
                        .withProperty(BlockSpectriteSkull.FACING, facing), 11);
                    int i = 0;

                    if (facing == EnumFacing.UP)
                    {
                        i = MathHelper.floor(player.rotationYaw * 16.0F / 360.0F + 0.5D) & 15;
                    }

                    TileEntity tileentity = worldIn.getTileEntity(pos);

                    if (tileentity instanceof TileEntitySpectriteSkull)
                    {
                    	TileEntitySpectriteSkull tileEntitySpectriteWitherSkeletonSkull = (TileEntitySpectriteSkull)tileentity;

                        tileEntitySpectriteWitherSkeletonSkull.setType(this.skullType);
                        tileEntitySpectriteWitherSkeletonSkull.setDamage(this.getDamage(itemstack));

                        tileEntitySpectriteWitherSkeletonSkull.setSkullRotation(i);
                        ModBlocks.spectrite_skull.checkWitherSpawn(worldIn, pos, tileEntitySpectriteWitherSkeletonSkull);
                    }

                    if (player instanceof EntityPlayerMP)
                    {
                    	CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
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
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        String domain = Spectrite.MOD_ID;
        int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());

        return String.format("%s:textures/models/armor/spectrite%s_layer_1/%d.png", domain, "_wither_"
            + (this == ModItems.spectrite_wither_skeleton_skull ? "skeleton_" :
            this == ModItems.spectrite_wither_skull ? "" : "invulnerable_") + "skull", curFrame);
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    public int getSkullType() {
	    return this.skullType;
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
    public String getTranslationKey(ItemStack stack)
    {
        return "item.spectrite_" + SKULL_TYPES[skullType] + "_skull";
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
