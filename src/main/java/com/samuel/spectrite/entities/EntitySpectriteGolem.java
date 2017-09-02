package com.samuel.spectrite.entities;

import com.google.common.base.Predicate;
import com.samuel.spectrite.init.ModBiomes;
import com.samuel.spectrite.init.ModLootTables;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntitySpectriteGolem extends EntityIronGolem implements ISpectriteMob {
	
	private int healTimer;

	public EntitySpectriteGolem(World worldIn) {
		super(worldIn);
		this.initEntityAIAttackableTarget(false);
		this.experienceValue = 77;
	}
	
	public EntitySpectriteGolem(World worldIn, boolean playerCreated) {
		super(worldIn);
		this.setPlayerCreated(playerCreated);
		this.initEntityAIAttackableTarget(playerCreated);
		if (!playerCreated) {
			this.experienceValue = 77;
		}
	}
	
	@Override
	protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[] { EntitySpectriteGolem.class }));
    }
	
	protected void initEntityAIAttackableTarget(boolean playerCreated) {
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, false, true, !playerCreated ?
			new Predicate<EntityLiving>()
		    {
		        @Override
				public boolean apply(@Nullable EntityLiving p_apply_1_)
		        {
		            return p_apply_1_ != null && IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) && !(p_apply_1_ instanceof ISpectriteMob) && !(p_apply_1_ instanceof EntityCreeper);
		        }
		    } :
		    new Predicate<EntityLiving>()
		    {
		        @Override
				public boolean apply(@Nullable EntityLiving p_apply_1_)
		        {
		            return p_apply_1_ != null && IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) && !(p_apply_1_ instanceof EntityCreeper);
		        }
		    }));
	}
	
	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.175D);
    }
	
	@Override
	protected void collideWithEntity(Entity entityIn)
    {
        if (!this.isPlayerCreated() && entityIn instanceof EntityPlayer && !((EntityPlayer) entityIn).isCreative() && this.getRNG().nextInt(20) == 0)
        {
            this.setAttackTarget((EntityLivingBase)entityIn);
        }

        super.collideWithEntity(entityIn);
    }
	
	@Override
	public boolean canAttackClass(Class <? extends EntityLivingBase > cls)
    {
        if (this.isPlayerCreated() && EntityPlayer.class.isAssignableFrom(cls))
        {
            return false;
        }
        else
        {
            return cls == EntitySpectriteCreeper.class ? false : super.canAttackClass(cls);
        }
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn)
    {
        this.world.setEntityState(this, (byte)4);
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 6 + this.rand.nextInt(10));

        if (flag)
        {
            entityIn.motionY += 0.4000000059604645D;
            this.applyEnchantments(this, entityIn);
        }

        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }
	
	@Override
	public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!this.world.isRemote) {
	        if (this.healTimer > 0) {
	        	this.healTimer--;
	        } else {
	        	this.heal(1.0f);
	        	this.healTimer = 50;
	        }
        }
    }
	
	@Override
	/**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
		BlockPos pos = new BlockPos(this);

		if (this.world.getBiome(pos) != ModBiomes.spectrite_dungeon)
			return true;

		int spawnChance = (pos.getY() + 8) >> 3;
		boolean shouldSpawn = spawnChance == 1 || (spawnChance == 2 && rand.nextBoolean()) || (spawnChance == 3 && rand.nextInt(3) == 0);
        return shouldSpawn && pos.down().getY() < 28 && world.getEntitiesWithinAABB(EntitySpectriteGolem.class, new AxisAlignedBB(pos.east(8).south(8).down(), pos.west(8).north(8).up(8)),
    		EntitySelectors.IS_ALIVE).size() < 1 && super.getCanSpawnHere();
    }
	
	@Override
	public boolean isOnSameTeam(Entity entityIn) {
		return (entityIn instanceof ISpectriteMob && (entityIn.getClass() != EntitySpectriteGolem.class
			|| ((EntitySpectriteGolem) entityIn).isPlayerCreated() == this.isPlayerCreated())) || super.isOnSameTeam(entityIn);
	}
	
	@Override
	@Nullable
    protected ResourceLocation getLootTable()
    {
        return ModLootTables.spectrite_golem;
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.initEntityAIAttackableTarget(this.isPlayerCreated());
    }
}
