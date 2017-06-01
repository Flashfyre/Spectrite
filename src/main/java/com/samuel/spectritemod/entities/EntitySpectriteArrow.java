package com.samuel.spectritemod.entities;

import java.util.List;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.SpectriteModConfig;
import com.samuel.spectritemod.init.ModSounds;
import com.samuel.spectritemod.items.ItemSpectriteShield;
import com.samuel.spectritemod.items.ItemSpectriteShieldSpecial;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntitySpectriteArrow extends EntityArrow {

	public EntitySpectriteArrow(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}
	
	public EntitySpectriteArrow(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }
	
	public EntitySpectriteArrow(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void arrowHit(EntityLivingBase living)
    {
		if (!world.isRemote) {
			WorldServer worldServer = (WorldServer) world;
			BlockPos pos = living.getPosition();
			int power = this.getIsCritical() ? 3 : 2;
			
			List<Entity> surrounding = world.getEntitiesWithinAABBExcludingEntity(this,
				new AxisAlignedBB(pos.north(power).west(power).down(power),
				pos.south(power).east(power).up(power)));
		
			if (living.getMaxHealth() >= 200.0F && SpectriteMod.Config.spectriteArrowDamageMode != SpectriteModConfig.EnumSpectriteArrowDamageMode.EXPLOSION) {
				living.attackEntityFrom(DamageSource.causeThornsDamage(living), 5 + (9 * power));
			}
		}
    }
	
	@Override
	protected void onHit(RayTraceResult raytraceResultIn)
    {
		if (!world.isRemote) {
			
			WorldServer worldServer = (WorldServer) world;
			BlockPos hitPos = this.getPosition();
			
			if (SpectriteMod.Config.spectriteArrowDamageMode != SpectriteModConfig.EnumSpectriteArrowDamageMode.INSTANT_DAMAGE) {
				this.world.newExplosion(
					this, this.posX, this.posY + this.height / 2.0F,
					this.posZ, 2.0F, false, true);
			}
				
			int power = this.getIsCritical() ? 3 : 2;
			List<Entity> surrounding = world.getEntitiesWithinAABBExcludingEntity(this,
				new AxisAlignedBB(hitPos.north(power).west(power).down(power),
				hitPos.south(power).east(power).up(power)));
			
			for (int s = 0; s < surrounding.size(); s++) {
				if (surrounding.get(s) instanceof EntityLivingBase &&
					(this.shootingEntity == null || (!((EntityLivingBase) surrounding.get(s)).isOnSameTeam(this.shootingEntity))
					&& !surrounding.get(s).equals(this.shootingEntity))) {
					EntityLivingBase curEntity = ((EntityLivingBase) surrounding.get(s));
					double distance = curEntity.getDistanceSq(new BlockPos(raytraceResultIn.hitVec));
					int relPower = (int) Math.ceil(power - distance);
					int damageLevel = relPower;
					if (!curEntity.getActiveItemStack().isEmpty() && curEntity.isActiveItemStackBlocking() && curEntity.getActiveItemStack().getItem() instanceof ItemShield) {
						damageLevel = Math.max(relPower - (curEntity.getActiveItemStack().getItem() instanceof ItemSpectriteShield ?
							curEntity.getActiveItemStack().getItem() instanceof ItemSpectriteShieldSpecial ? 2 : 1 : 0), 0);
						relPower = Math.max(relPower - (relPower - damageLevel), 0);
						int shieldDamage = 0;
						switch (damageLevel) {
							case 1:
								shieldDamage = 8;
								break;
							case 2:
								shieldDamage = 32;
								break;
							case 3:
								shieldDamage = 128;
								break;
						}
						if (shieldDamage > 0) {
							curEntity.getActiveItemStack().damageItem(shieldDamage, curEntity);
						}
					}
					if (SpectriteMod.Config.spectriteArrowDamageMode != SpectriteModConfig.EnumSpectriteArrowDamageMode.EXPLOSION) {
						curEntity.addPotionEffect(new PotionEffect(!curEntity.isEntityUndead() ? MobEffects.INSTANT_DAMAGE :
							MobEffects.INSTANT_HEALTH, 5, relPower - (curEntity instanceof EntityPlayer ? 1 : 0)));
					}
				}
			}
			
			try {
				if (!this.getIsCritical()) {
					if (SpectriteMod.Config.spectriteArrowDamageMode != SpectriteModConfig.EnumSpectriteArrowDamageMode.INSTANT_DAMAGE_EXPLOSION) {
						world.playSound(null, hitPos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.75F,
							1.0F + (world.rand.nextFloat()) * 0.4F);
					}
				} else {
					world.playSound(null, hitPos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F,
						1.0F + (world.rand.nextFloat()) * 0.4F);
				}
				
				if (SpectriteMod.Config.spectriteArrowDamageMode != SpectriteModConfig.EnumSpectriteArrowDamageMode.INSTANT_DAMAGE_EXPLOSION) {
					worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE,
						EnumParticleTypes.EXPLOSION_LARGE.getShouldIgnoreRange(), hitPos.getX(),
						this.getEntityBoundingBox().minY, hitPos.getZ(), !this.getIsCritical() ? 1 : 7,
						world.rand.nextGaussian() * 0.25D, world.rand.nextGaussian() * 0.25D,
						world.rand.nextGaussian() * 0.25D, 0.0D, new int[0]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		super.onHit(raytraceResultIn);
		
		if (!world.isRemote) {
			this.setDead();
		}
    }

	@Override
	protected ItemStack getArrowStack()
    {
        return new ItemStack(SpectriteMod.ItemSpectriteArrow);
    }
}
