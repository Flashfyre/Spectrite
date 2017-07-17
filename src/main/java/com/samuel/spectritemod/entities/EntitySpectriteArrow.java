package com.samuel.spectritemod.entities;

import java.util.List;

import com.samuel.spectritemod.SpectriteModConfig;
import com.samuel.spectritemod.init.ModItems;
import com.samuel.spectritemod.init.ModPotions;
import com.samuel.spectritemod.init.ModSounds;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntitySpectriteArrow extends EntityArrow {
	
	boolean enhancedBow = false;

	public EntitySpectriteArrow(World worldIn, EntityLivingBase shooter, boolean enhancedBow) {
		super(worldIn, shooter);
		
		this.enhancedBow = enhancedBow;
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
		}
    }
	
	@Override
	protected void onHit(RayTraceResult raytraceResultIn)
    {
		if (!world.isRemote) {
			
			WorldServer worldServer = (WorldServer) world;
			BlockPos hitPos = new BlockPos(raytraceResultIn.hitVec);
				
			int power = (this.getIsCritical() ? 2 : 1) + (this.enhancedBow ? 1 : 0);
			List<Entity> surrounding = world.getEntitiesWithinAABBExcludingEntity(this,
				new AxisAlignedBB(hitPos.north(power).west(power).down(power),
				hitPos.south(power).east(power).up(power)));
			
			if (SpectriteModConfig.spectriteArrowDamageMode != SpectriteModConfig.EnumSpectriteArrowDamageMode.SPECTRITE_DAMAGE) {
				this.world.newExplosion(
					this, this.posX, this.posY + this.height / 2.0F,
					this.posZ, new Integer(power).floatValue(), false, this.shootingEntity instanceof EntityPlayer || world.getGameRules().getBoolean("mobGriefing"));
			}
			
			for (int s = 0; s < surrounding.size(); s++) {
				if (surrounding.get(s) instanceof EntityLivingBase &&
					(this.shootingEntity == null || (!((EntityLivingBase) surrounding.get(s)).isOnSameTeam(this.shootingEntity))
					&& !surrounding.get(s).equals(this.shootingEntity))) {
					EntityLivingBase curEntity = ((EntityLivingBase) surrounding.get(s));
					double distance = curEntity.getDistanceSq(hitPos);
					double health = distance >= 1 ? 1.0D - Math.sqrt(distance) / (power + 2) : 1.0D;
					if (health > 0.0D) {
						if (SpectriteModConfig.spectriteArrowDamageMode != SpectriteModConfig.EnumSpectriteArrowDamageMode.EXPLOSION) {
							ModPotions.SPECTRITE_DAMAGE.affectEntity(this, this.shootingEntity, curEntity, power, health);
						}
					}
				}
			}
			
			EnumParticleTypes particle = null;
			
			try {
				if (power == 1) {
					particle = EnumParticleTypes.EXPLOSION_NORMAL;
					
					world.playSound(null, hitPos, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.PLAYERS, 1.0F,
						1.0F + (rand.nextFloat()) * 0.4F);
				} else if (power == 2) {
					if (SpectriteModConfig.spectriteArrowDamageMode != SpectriteModConfig.EnumSpectriteArrowDamageMode.SPECTRITE_DAMAGE_EXPLOSION) {
						world.playSound(null, hitPos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.75F,
							1.0F + (rand.nextFloat()) * 0.4F);
					}
					particle = EnumParticleTypes.EXPLOSION_LARGE;
				} else if (power == 3) {
					world.playSound(null, hitPos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F,
						1.0F + (rand.nextFloat()) * 0.4F);
					particle = EnumParticleTypes.EXPLOSION_LARGE;
				}
				
				if (SpectriteModConfig.spectriteArrowDamageMode != SpectriteModConfig.EnumSpectriteArrowDamageMode.SPECTRITE_DAMAGE_EXPLOSION) {
					if (power != 3) {
						worldServer.spawnParticle(particle, particle.getShouldIgnoreRange(), hitPos.getX(),
							this.getEntityBoundingBox().minY, hitPos.getZ(), 1,
							rand.nextGaussian() * 0.25D, rand.nextGaussian() * 0.25D,
							rand.nextGaussian() * 0.25D, 0.0D, new int[0]);
					} else {
						double offsetX = rand.nextGaussian() * 0.25D, offsetY = rand.nextGaussian() * 0.25D, offsetZ = rand.nextGaussian() * 0.25D;
						
						for (int f = 0; f <= EnumFacing.values().length; f++) {
							BlockPos particleOffsetPos = f == 0 ? hitPos : hitPos.offset(EnumFacing.values()[f - 1]);
							particleOffsetPos.add((particleOffsetPos.getX() - hitPos.getX()) * 0.5d, (particleOffsetPos.getY() - hitPos.getY()) * 0.5d,
								(particleOffsetPos.getZ() - hitPos.getZ()) * 0.5d);
							worldServer.spawnParticle(particle, particle.getShouldIgnoreRange(), particleOffsetPos.getX(),
								 particleOffsetPos.getY(), particleOffsetPos.getZ(), 1, offsetX, offsetY, offsetZ, 0.0D, new int[0]);
						}
					}
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
        return new ItemStack(ModItems.spectrite_arrow);
    }
}
