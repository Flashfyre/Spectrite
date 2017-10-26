package com.samuel.spectrite.entities;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.init.ModDamageSources;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySpectriteWitherSkull extends EntitySpectriteFireball implements IProjectile
{
    private static final DataParameter<Boolean> INVULNERABLE = EntityDataManager.<Boolean>createKey(EntitySpectriteWitherSkull.class, DataSerializers.BOOLEAN);

    public EntitySpectriteWitherSkull(World worldIn)
    {
        super(worldIn);
        this.setSize(0.3125F, 0.3125F);
    }

    public EntitySpectriteWitherSkull(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.3125F, 0.3125F);
    }

    @Override
    /**
     * Return the motion factor for this projectile. The factor is multiplied by the original motion.
     */
    protected float getMotionFactor()
    {
        return this.isInvulnerable() && this.shootingEntity instanceof EntitySpectriteWither ? 0.73F : super.getMotionFactor();
    }

    @SideOnly(Side.CLIENT)
    public EntitySpectriteWitherSkull(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(0.3125F, 0.3125F);
    }

    public void setAim(Entity shooter, float pitch, float yaw, float velocity)
    {
        float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float f1 = -MathHelper.sin(pitch * 0.017453292F);
        float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        this.setThrowableHeading((double)f, (double)f1, (double)f2, velocity, 0);
        this.rotationPitch = pitch;
        this.rotationYaw = yaw;
        this.motionX += shooter.motionX;
        this.motionZ += shooter.motionZ;

        if (!shooter.onGround)
        {
            this.motionY += shooter.motionY;
        }

        this.accelerationX = this.motionX * 0.1;
        this.accelerationY = this.motionY * 0.1;
        this.accelerationZ = this.motionZ * 0.1;
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    @Override
    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
        return false;
    }

    @Override
    /**
     * Explosion resistance of a block relative to this entity
     */
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn)
    {
        float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
        Block block = blockStateIn.getBlock();

        if (this.shootingEntity instanceof EntitySpectriteWither && this.isInvulnerable() && block.canEntityDestroy(blockStateIn, worldIn, pos, this)
            && EntitySpectriteWither.canDestroyBlock(block) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this.shootingEntity, pos, blockStateIn))
        {
            f = Math.min(0.8F, f);
        }

        return f;
    }

    @Override
    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            if (result.entityHit != null)
            {
                if (this.shootingEntity != null)
                {
                    if (result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 10.0F))
                    {
                        if (result.entityHit.isEntityAlive())
                        {
                            this.applyEnchantments(this.shootingEntity, result.entityHit);
                        }
                        else if (this.shootingEntity instanceof EntitySpectriteWither)
                        {
                            this.shootingEntity.heal(7.0F);
                        }
                    }
                }
                else
                {
                    result.entityHit.attackEntityFrom(ModDamageSources.SPECTRITE_DAMAGE, 7.0F);
                }

                if (result.entityHit instanceof EntityLivingBase)
                {
                    int i = 0;

                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
                    {
                        i = 10;
                    }
                    else if (this.world.getDifficulty() == EnumDifficulty.HARD)
                    {
                        i = 40;
                    }

                    if (i > 0)
                    {
                        ((EntityLivingBase)result.entityHit).addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * i, 1));
                    }
                }
            }

            int power = (this.isInvulnerable() ? 3 : 2);
            int spectriteDamagePower = power + (this.shootingEntity instanceof EntitySpectriteWither ? 1 : SpectriteConfig.items.spectriteWitherRodWeakenSpectriteDamage ? 0 : 1);
            int explosionPower = power + (this.shootingEntity instanceof EntitySpectriteWither ? 1 : SpectriteConfig.items.spectriteWitherRodWeakenExplosion ? 0 : 1);

            Spectrite.Proxy.performDispersedSpectriteDamage(this.world, spectriteDamagePower, explosionPower, result.hitVec,
                this, this.shootingEntity, this.rand);

            if (this.isInvulnerable() && this.shootingEntity instanceof EntitySpectriteWither) {
                this.spawnLingeringCloud(power);
            }

            this.setDead();
        }
    }

    @Override
    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    @Override
    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(INVULNERABLE, Boolean.valueOf(false));
    }

    /**
     * Return whether this skull comes from an invulnerable (aura) wither boss.
     */
    public boolean isInvulnerable()
    {
        return ((Boolean)this.dataManager.get(INVULNERABLE)).booleanValue();
    }

    /**
     * Set whether this skull comes from an invulnerable (aura) wither boss.
     */
    public void setInvulnerable(boolean invulnerable)
    {
        this.dataManager.set(INVULNERABLE, Boolean.valueOf(invulnerable));
    }

    @Override
    protected boolean isFireballFiery()
    {
        return false;
    }
}