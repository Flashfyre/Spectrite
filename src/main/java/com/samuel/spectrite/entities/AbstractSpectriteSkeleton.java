package com.samuel.spectrite.entities;

import com.samuel.spectrite.init.ModBiomes;
import com.samuel.spectrite.items.ItemSpectriteBow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public abstract class AbstractSpectriteSkeleton extends AbstractSkeleton implements ISpectriteMob, ISpectriteBipedMob {

    protected boolean hasSpectriteResistance;
    protected Integer equipmentSet;

    public static final int EQUIPMENT_BOW = 1, EQUIPMENT_SPECIAL_WEAPON = 2, EQUIPMENT_HELMET = 4, EQUIPMENT_CHESTPLATE = 8, EQUIPMENT_LEGGINGS = 16, EQUIPMENT_BOOTS = 32;
    public static final int[] EQUIPMENT_TYPES = new int[] { EQUIPMENT_BOW, EQUIPMENT_SPECIAL_WEAPON, EQUIPMENT_HELMET, EQUIPMENT_CHESTPLATE, EQUIPMENT_LEGGINGS, EQUIPMENT_BOOTS };

    protected final EntityAIAttackRangedBow aiArrowAttack = new EntityAIAttackRangedBow(this, 1.0D, 20, 15.0F) {

        private Field entity = null;

        @Override
        protected boolean isBowInMainhand()
        {
            if (entity == null) {
                entity = ObfuscationReflectionHelper.findField(EntityAIAttackRangedBow.class, "field_188499_a" );
            }
            AbstractSkeleton entityInstance;
            try {
                entityInstance = (AbstractSkeleton) entity.get(this);
                return !entityInstance.getHeldItemMainhand().isEmpty() && entityInstance.getHeldItemMainhand().getItem() instanceof ItemBow;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    };

    public AbstractSpectriteSkeleton(World worldIn) {
        super(worldIn);

        setHasSpectriteResistance(isArmorFullEnhanced());
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }

    @Override
    public void setCombatTask()
    {
        if (this.world != null && !this.world.isRemote)
        {
            ItemStack itemstack = this.getHeldItemMainhand();

            if (itemstack.getItem() instanceof ItemSpectriteBow)
            {
                this.tasks.removeTask(this.aiArrowAttack);
                Field aiAttackOnCollide = ObfuscationReflectionHelper.findField(AbstractSkeleton.class, "field_85038_e" );
                try {
                    this.tasks.removeTask((EntityAIAttackMelee) aiAttackOnCollide.get(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int i = 20;

                if (this.world.getDifficulty() != EnumDifficulty.HARD)
                {
                    i = 40;
                }

                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(4, this.aiArrowAttack);
            }
            else
            {
                super.setCombatTask();
            }
        }
    }

    @Override
    public boolean isHasSpectriteResistance() {
        return this.hasSpectriteResistance;
    }

    @Override
    public void setHasSpectriteResistance(boolean hasSpectriteResistance) {
        this.hasSpectriteResistance = hasSpectriteResistance;
    }

    public Integer getEquipmentSet() {
        return this.equipmentSet;
    }

    public void setEquipmentSet(Integer equipmentSet) {
        this.equipmentSet = equipmentSet;
    }

    @Override
    /**
     * drops the loot of this entity upon death
     */
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        super.dropLoot(wasRecentlyHit, lootingModifier, source);

        this.dropFewItems(wasRecentlyHit, lootingModifier);

        Entity damageSourceEntity = source.getTrueSource() != null ? source.getTrueSource() : source.getImmediateSource();
        if (damageSourceEntity == null || (!(damageSourceEntity instanceof EntityPlayer)
            && (!(damageSourceEntity instanceof EntityIronGolem) || !((EntityIronGolem) damageSourceEntity).isPlayerCreated()))) {
            lootingModifier = -1;
        }
        if (lootingModifier > -1) {
            this.dropEquipment(wasRecentlyHit, lootingModifier);
        }
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        EntityArrow entityarrow = this.getArrow(distanceFactor);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entityarrow.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entityarrow);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        setHasSpectriteResistance(isArmorFullEnhanced());
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
        return shouldSpawn && pos.down().getY() < 28;
    }

    @Override
    public boolean isOnSameTeam(Entity entityIn) {
        return (entityIn instanceof ISpectriteMob && (entityIn.getClass() != EntitySpectriteGolem.class
            || !((EntitySpectriteGolem) entityIn).isPlayerCreated())) || super.isOnSameTeam(entityIn);
    }
}
