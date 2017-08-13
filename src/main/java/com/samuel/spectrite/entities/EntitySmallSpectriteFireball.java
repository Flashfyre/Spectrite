package com.samuel.spectrite.entities;

import java.util.List;

import com.samuel.spectrite.init.ModBlocks;
import com.samuel.spectrite.init.ModPotions;
import com.samuel.spectrite.init.ModSounds;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntitySmallSpectriteFireball extends EntitySpectriteFireball {

	public EntitySmallSpectriteFireball(World worldIn) {
		super(worldIn);
		this.setSize(0.3125F, 0.3125F);
	}

	public EntitySmallSpectriteFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.3125F, 0.3125F);
    }

    public EntitySmallSpectriteFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(0.3125F, 0.3125F);
    }
    
    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            if (result.entityHit != null)
            {
                if (!result.entityHit.isImmuneToFire())
                {
                	DamageSource damageSource = (new EntityDamageSourceIndirect("fireball", this, this.shootingEntity)).setFireDamage().setProjectile();
                    boolean flag = result.entityHit.attackEntityFrom(damageSource, 5.0F);

                    if (flag)
                    {
                        this.applyEnchantments(this.shootingEntity, result.entityHit);
                        result.entityHit.setFire(5);
                    }
                }
            }
            else
            {
                boolean flag1 = true;

                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving)
                {
                    flag1 = this.world.getGameRules().getBoolean("mobGriefing");
                }

                if (flag1)
                {
                    BlockPos blockpos = result.getBlockPos().offset(result.sideHit);

                    if (this.world.isAirBlock(blockpos))
                    {
                        this.world.setBlockState(blockpos, ModBlocks.spectrite_fire.getDefaultState());
                    }
                }
            }
            
            BlockPos hitPos = new BlockPos(result.hitVec);
            
            int power = 3;
            List<Entity> surrounding = world.getEntitiesWithinAABBExcludingEntity(this,
				new AxisAlignedBB(hitPos.north(power).west(power).down(power),
				hitPos.south(power).east(power).up(power)));
			
			for (int s = 0; s < surrounding.size(); s++) {
				if (surrounding.get(s) instanceof EntityLivingBase &&
					(this.shootingEntity == null || (!((EntityLivingBase) surrounding.get(s)).isOnSameTeam(this.shootingEntity))
					&& !surrounding.get(s).equals(this.shootingEntity))) {
					EntityLivingBase curEntity = ((EntityLivingBase) surrounding.get(s));
					double distance = curEntity.getDistanceSq(hitPos);
					double health = distance >= 1 ? 1.0D - Math.sqrt(distance) / (power + 2) : 1.0D;
					if (health > 0.0D) {
						ModPotions.SPECTRITE_DAMAGE.affectEntity(this, this.shootingEntity, curEntity, power, health);
					}
				}
			}
			
			WorldServer worldServer = (WorldServer) this.world;
			EnumParticleTypes particle = null;
		
			world.playSound(null, hitPos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F,
					1.0F + (rand.nextFloat()) * 0.4F);
			double offsetX = rand.nextGaussian() * 0.25D, offsetY = rand.nextGaussian() * 0.25D, offsetZ = rand.nextGaussian() * 0.25D;
			
			for (int f = 0; f <= EnumFacing.values().length; f++) {
				BlockPos particleOffsetPos = f == 0 ? hitPos : hitPos.offset(EnumFacing.values()[f - 1]);
				particleOffsetPos.add((particleOffsetPos.getX() - hitPos.getX()) * 0.5d, (particleOffsetPos.getY() - hitPos.getY()) * 0.5d,
					(particleOffsetPos.getZ() - hitPos.getZ()) * 0.5d);
				worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, EnumParticleTypes.EXPLOSION_LARGE.getShouldIgnoreRange(), particleOffsetPos.getX(),
					 particleOffsetPos.getY(), particleOffsetPos.getZ(), 1, offsetX, offsetY, offsetZ, 0.0D, new int[0]);
			}

            this.setDead();
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith()
    {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }
}
