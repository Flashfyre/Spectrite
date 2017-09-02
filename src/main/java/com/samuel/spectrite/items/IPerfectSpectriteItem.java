package com.samuel.spectrite.items;

import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

public interface IPerfectSpectriteItem {

	default String getMultiColouredDisplayName(ItemStack stack, String displayName) {
		return SpectriteHelper.getMultiColouredString(displayName,this == ModItems.spectrite_sword_2 || (this == ModItems.spectrite_wither_rod_invulnerable));
	}

    default boolean onEntitySpectriteItemUpdate(EntityItem entityItem) {
	    Field randomField = SpectriteHelper.findObfuscatedField(Entity.class, "rand", "field_70146_Z");
    	Field delayBeforeCanPickupField = SpectriteHelper.findObfuscatedField(EntityItem.class, "delayBeforeCanPickup", "field_145804_b");

        Random rand;

		try {
            rand = (Random) randomField.get(entityItem);

			int delayBeforeCanPickup = delayBeforeCanPickupField.getInt(entityItem);
			if (delayBeforeCanPickup > 0 && delayBeforeCanPickup != 32767)
	        {
	            delayBeforeCanPickupField.set(entityItem, --delayBeforeCanPickup);
	        }
		} catch (Exception e1) {
		    rand = new Random();
			e1.printStackTrace();
		}

        entityItem.prevPosX = entityItem.posX;
        entityItem.prevPosY = entityItem.posY;
        entityItem.prevPosZ = entityItem.posZ;
        double d0 = entityItem.motionX;
        double d1 = entityItem.motionY;
        double d2 = entityItem.motionZ;

        if (!entityItem.hasNoGravity())
        {
            entityItem.motionY -= 0.03999999910593033D;
        }

        if (entityItem.world.isRemote)
        {
            entityItem.noClip = false;
        }
        else
        {
        	Method pushOutOfBlocks = SpectriteHelper.findObfuscatedMethod(Entity.class, "pushOutOfBlocks", "func_145771_j", double.class, double.class, double.class);
            try {
				entityItem.noClip = (boolean) pushOutOfBlocks.invoke(entityItem, entityItem.posX, (entityItem.getEntityBoundingBox().minY + entityItem.getEntityBoundingBox().maxY) / 2.0D, entityItem.posZ);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }

        entityItem.move(MoverType.SELF, entityItem.motionX, entityItem.motionY, entityItem.motionZ);
        boolean flag = (int)entityItem.prevPosX != (int)entityItem.posX || (int)entityItem.prevPosY != (int)entityItem.posY || (int)entityItem.prevPosZ != (int)entityItem.posZ;

        if (flag || entityItem.ticksExisted % 25 == 0)
        {
            if (entityItem.world.getBlockState(new BlockPos(entityItem)).getMaterial() == Material.LAVA)
            {
                entityItem.motionY = 0.20000000298023224D;
                entityItem.motionX = (double)((rand.nextFloat() - rand.nextFloat()) * 0.2F);
                entityItem.motionZ = (double)((rand.nextFloat() - rand.nextFloat()) * 0.2F);
                entityItem.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + rand.nextFloat() * 0.4F);
            }

            if (!entityItem.world.isRemote)
            {

            	Method searchForOtherItemsNearby = SpectriteHelper.findObfuscatedMethod(EntityItem.class, "searchForOtherItemsNearby", "func_85054_d");
            	try {
					searchForOtherItemsNearby.invoke(entityItem);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }

        float f = 0.98F;

        if (entityItem.onGround)
        {
            f = entityItem.world.getBlockState(new BlockPos(MathHelper.floor(entityItem.posX), MathHelper.floor(entityItem.getEntityBoundingBox().minY) - 1, MathHelper.floor(entityItem.posZ))).getBlock().slipperiness * 0.98F;
        }

        entityItem.motionX *= f;
        entityItem.motionY *= 0.9800000190734863D;
        entityItem.motionZ *= f;

        if (entityItem.onGround)
        {
            entityItem.motionY *= -0.5D;
        }

        Field ageField = SpectriteHelper.findObfuscatedField(EntityItem.class, "age", "field_70292_b");
        int age = 0;
        try {
            age = ((int) ageField.get(entityItem));
            if (age != -32768) {
                ageField.set(entityItem, ageField.getInt(entityItem) + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        entityItem.handleWaterMovement();

        if (!entityItem.world.isRemote)
        {
            double d3 = entityItem.motionX - d0;
            double d4 = entityItem.motionY - d1;
            double d5 = entityItem.motionZ - d2;
            double d6 = d3 * d3 + d4 * d4 + d5 * d5;

            if (d6 > 0.01D)
            {
                entityItem.isAirBorne = true;
            }
        }

        ItemStack item = entityItem.getItem();

        if (!entityItem.world.isRemote && age >= entityItem.lifespan)
        {
            int hook = net.minecraftforge.event.ForgeEventFactory.onItemExpire(entityItem, item);
            if (hook < 0) entityItem.setDead();
            else          entityItem.lifespan += hook;
        }
        if (item.isEmpty())
        {
            entityItem.setDead();
        }
        return true;
    }

    default int getEntitySpectriteItemLifespan() {
        return 1728000;
    }
}
