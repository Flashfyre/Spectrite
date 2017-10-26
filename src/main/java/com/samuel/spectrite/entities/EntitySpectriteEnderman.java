package com.samuel.spectrite.entities;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.damagesources.DamageSourceSpectriteIndirectPlayer;
import com.samuel.spectrite.init.*;
import com.samuel.spectrite.items.ItemSpectriteOrb;
import com.samuel.spectrite.items.ItemSpectriteSkull;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class EntitySpectriteEnderman extends EntityMob implements ISpectriteMob {

    private static final UUID ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier ATTACKING_SPEED_BOOST = (new AttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.24270000964D, 0)).setSaved(false);
    private static final Set<Block> CARRIABLE_BLOCKS = Sets.<Block>newIdentityHashSet();
    private static final DataParameter<Optional<IBlockState>> CARRIED_BLOCK = EntityDataManager.<Optional<IBlockState>>createKey(EntityEnderman.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Boolean> SCREAMING = EntityDataManager.<Boolean>createKey(EntityEnderman.class, DataSerializers.BOOLEAN);
    private int lastCreepySound;
    private int targetChangeTime;

    public EntitySpectriteEnderman(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 2.9F);
        this.stepHeight = 1.0F;
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.experienceValue = 70;
    }

    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(10, new EntitySpectriteEnderman.AIPlaceBlock(this));
        this.tasks.addTask(11, new EntitySpectriteEnderman.AITakeBlock(this));
        this.targetTasks.addTask(1, new EntitySpectriteEnderman.AIFindPlayer(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate<EntityEndermite>()
        {
            public boolean apply(@Nullable EntityEndermite p_apply_1_)
            {
                return p_apply_1_.isSpawnedByPlayer();
            }
        }));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.48540001928D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(96.0D);
    }

    @Override
    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
        super.setAttackTarget(entitylivingbaseIn);
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (entitylivingbaseIn == null)
        {
            this.targetChangeTime = 0;
            this.dataManager.set(SCREAMING, Boolean.valueOf(false));
            iattributeinstance.removeModifier(ATTACKING_SPEED_BOOST);
        }
        else
        {
            this.targetChangeTime = this.ticksExisted;
            this.dataManager.set(SCREAMING, Boolean.valueOf(true));

            if (!iattributeinstance.hasModifier(ATTACKING_SPEED_BOOST))
            {
                iattributeinstance.applyModifier(ATTACKING_SPEED_BOOST);
            }
        }
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(CARRIED_BLOCK, Optional.absent());
        this.dataManager.register(SCREAMING, Boolean.valueOf(false));
    }

    public void playEndermanSound()
    {
        if (this.ticksExisted >= this.lastCreepySound + 400)
        {
            this.lastCreepySound = this.ticksExisted;

            if (!this.isSilent())
            {
                this.world.playSound(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, ModSounds.spectrite_enderman_stare, this.getSoundCategory(), 2.5F, 1.0F, false);
            }
        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (SCREAMING.equals(key) && this.isScreaming() && this.world.isRemote)
        {
            this.playEndermanSound();
        }

        super.notifyDataManagerChange(key);
    }

    @Override
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        IBlockState iblockstate = this.getHeldBlockState();

        if (iblockstate != null)
        {
            compound.setShort("carried", (short) Block.getIdFromBlock(iblockstate.getBlock()));
            compound.setShort("carriedData", (short)iblockstate.getBlock().getMetaFromState(iblockstate));
        }
    }

    @Override
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        IBlockState iblockstate;

        if (compound.hasKey("carried", 8))
        {
            iblockstate = Block.getBlockFromName(compound.getString("carried")).getStateFromMeta(compound.getShort("carriedData") & 65535);
        }
        else
        {
            iblockstate = Block.getBlockById(compound.getShort("carried")).getStateFromMeta(compound.getShort("carriedData") & 65535);
        }

        if (iblockstate == null || iblockstate.getBlock() == null || iblockstate.getMaterial() == Material.AIR)
        {
            iblockstate = null;
        }

        this.setHeldBlockState(iblockstate);
    }

    /**
     * Checks to see if this spectriteEnderman should be attacking this player
     */
    private boolean shouldAttackPlayer(EntityPlayer player)
    {
        ItemStack itemstack = player.inventory.armorInventory.get(3);

        if (itemstack.getItem() instanceof ItemSpectriteSkull)
        {
            return false;
        }
        else
        {
            Vec3d vec3d = player.getLook(1.0F).normalize();
            Vec3d vec3d1 = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY + (double)this.getEyeHeight() - (player.posY + (double)player.getEyeHeight()), this.posZ - player.posZ);
            double d0 = vec3d1.lengthVector();
            vec3d1 = vec3d1.normalize();
            double d1 = vec3d.dotProduct(vec3d1);
            return d1 > 1.0D - 0.025D / d0 ? player.canEntityBeSeen(this) : false;
        }
    }

    @Override
    public float getEyeHeight()
    {
        return 2.55F;
    }

    @Override
    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (this.world.isRemote)
        {
            for (int i = 0; i < 2; ++i)
            {
                Spectrite.Proxy.spawnSpectritePortalParticle(this.world,this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width,
                    this.posY + this.rand.nextDouble() * (double)this.height - 0.25D,
                    this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width,
                    (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }

        this.isJumping = false;
        super.onLivingUpdate();
    }

    @Override
    protected void updateAITasks()
    {
        if (this.isWet())
        {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }

        if (this.world.isDaytime() && this.ticksExisted >= this.targetChangeTime + 600)
        {
            float f = this.getBrightness();

            if (f > 0.5F && this.world.canSeeSky(new BlockPos(this)) && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F)
            {
                this.setAttackTarget((EntityLivingBase)null);
                this.teleportRandomly();
            }
        }

        super.updateAITasks();
    }

    /**
     * Teleport the spectriteEnderman to a random nearby position
     */
    protected boolean teleportRandomly()
    {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(d0, d1, d2);
    }

    /**
     * Teleport the spectriteEnderman to another entity
     */
    protected boolean teleportToEntity(Entity p_70816_1_)
    {
        Vec3d vec3d = new Vec3d(this.posX - p_70816_1_.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0F) - p_70816_1_.posY + (double)p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
        vec3d = vec3d.normalize();
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
        double d2 = this.posY + (double)(this.rand.nextInt(16) - 8) - vec3d.y * 16.0D;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;
        return this.teleportTo(d1, d2, d3);
    }

    /**
     * Teleport the spectriteEnderman
     */
    private boolean teleportTo(double x, double y, double z)
    {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag)
        {
            this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, ModSounds.spectrite_enderman_teleport, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(ModSounds.spectrite_enderman_teleport, 1.0F, 1.0F);
        }

        return flag;
    }

    @Override
    public boolean attemptTeleport(double x, double y, double z)
    {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(this);
        World world = this.world;
        Random random = this.getRNG();

        if (world.isBlockLoaded(blockpos))
        {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0)
            {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate = world.getBlockState(blockpos1);

                if (iblockstate.getMaterial().blocksMovement())
                {
                    flag1 = true;
                }
                else
                {
                    --this.posY;
                    blockpos = blockpos1;
                }
            }

            if (flag1)
            {
                this.setPositionAndUpdate(this.posX, this.posY, this.posZ);

                if (world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(this.getEntityBoundingBox()))
                {
                    flag = true;
                }
            }
        }

        if (!flag)
        {
            this.setPositionAndUpdate(d0, d1, d2);
            return false;
        }
        else
        {
            for (int j = 0; j < 128; ++j)
            {
                double d6 = (double)j / 127.0D;
                float f = (random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (random.nextFloat() - 0.5F) * 0.2F;
                double d3 = d0 + (this.posX - d0) * d6 + (random.nextDouble() - 0.5D) * (double)this.width * 2.0D;
                double d4 = d1 + (this.posY - d1) * d6 + random.nextDouble() * (double)this.height;
                double d5 = d2 + (this.posZ - d2) * d6 + (random.nextDouble() - 0.5D) * (double)this.width * 2.0D;
                Spectrite.Proxy.spawnSpectritePortalParticle(this.world, d3, d4, d5, (double)f, (double)f1, (double)f2);
            }

            this.getNavigator().clearPathEntity();

            return true;
        }
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return this.isScreaming() ? ModSounds.spectrite_enderman_scream : ModSounds.spectrite_enderman_ambient;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return ModSounds.spectrite_enderman_hurt;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return ModSounds.spectrite_enderman_death;
    }

    @Override
    /**
     * Drop the equipment for this entity.
     */
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier)
    {
        super.dropEquipment(wasRecentlyHit, lootingModifier);
        IBlockState iblockstate = this.getHeldBlockState();

        if (iblockstate != null)
        {
            Item item = Item.getItemFromBlock(iblockstate.getBlock());
            int i = item.getHasSubtypes() ? iblockstate.getBlock().getMetaFromState(iblockstate) : 0;
            this.entityDropItem(new ItemStack(item, 1, i), 0.0F);
        }
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        Entity damageSourceEntity;
        if (source instanceof DamageSourceSpectriteIndirectPlayer) {
            damageSourceEntity = ((DamageSourceSpectriteIndirectPlayer) source).getPlayer();
        } else {
            damageSourceEntity = source.getTrueSource() != null ? source.getTrueSource() : source.getImmediateSource();
        }
        if (damageSourceEntity == null || (!(damageSourceEntity instanceof EntityPlayer)
            && (!(damageSourceEntity instanceof EntityIronGolem) || !((EntityIronGolem) damageSourceEntity).isPlayerCreated()))) {
            lootingModifier = -1;
        } else if (((EntityLivingBase) damageSourceEntity).getActivePotionEffect(ModPotions.PROSPERITY) != null) {
            lootingModifier = ((EntityLivingBase) damageSourceEntity).getActivePotionEffect(ModPotions.PROSPERITY).getAmplifier() + 1;
        } else {
            lootingModifier = 0;
        }

        if (lootingModifier >= 0 && (SpectriteConfig.mobs.spectriteEndermanOrbDropRate > 0f &&
            rand.nextFloat() * 100f < SpectriteConfig.mobs.spectriteEndermanOrbDropRate)) {
            this.dropFewItems(wasRecentlyHit, lootingModifier);
        }
    }

    /**
     * Drop 0-2 items of this living's type
     */
    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        int numDrops = lootingModifier > 0 ? lootingModifier == 1 ? rand.nextInt(7) >= 2 ? 1 : 2 : 2 : 1;
        for (int d = 0; d < numDrops; d++) {
            EntityItem entityitem = this.entityDropItem(new ItemStack(ModItems.spectrite_orb,1,
                ItemSpectriteOrb.ORB_COLOURS[rand.nextInt(ItemSpectriteOrb.ORB_COLOURS.length)]), 0.0f);

            if (entityitem != null) {
                entityitem.setNoDespawn();
            }
        }
    }

    /**
     * Sets this spectriteEnderman's held block state
     */
    public void setHeldBlockState(@Nullable IBlockState state)
    {
        this.dataManager.set(CARRIED_BLOCK, Optional.fromNullable(state));
    }

    /**
     * Gets this spectriteEnderman's held block state
     */
    @Nullable
    public IBlockState getHeldBlockState()
    {
        return (IBlockState)((Optional)this.dataManager.get(CARRIED_BLOCK)).orNull();
    }

    @Override
    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else if (source instanceof EntityDamageSourceIndirect && !source.getDamageType().equals("spectrite"))
        {
            for (int i = 0; i < 64; ++i)
            {
                if (this.teleportRandomly())
                {
                    return true;
                }
            }

            return false;
        }
        else
        {
            boolean weakAttack = amount < 10F;
            boolean flag = !weakAttack && super.attackEntityFrom(source, amount);

            if (weakAttack || (source.isUnblockable() && (this.rand.nextInt(10) != 0)))
            {
                this.teleportRandomly();
            }

            return flag;
        }
    }

    public static void setCarriable(Block block, boolean canCarry)
    {
        if (canCarry) CARRIABLE_BLOCKS.add(block);
        else          CARRIABLE_BLOCKS.remove(block);
    }

    public static boolean getCarriable(Block block)
    {
        return CARRIABLE_BLOCKS.contains(block);
    }

    public static void initCarriableBlocks() {
        Field endermanCarriableBlocksField = ReflectionHelper.findField(EntityEnderman.class, "CARRIABLE_BLOCKS", "field_70827_d");
        Set<Block> endermanCarriableBlocks = null;
        try {
            endermanCarriableBlocks = (Set<Block>) endermanCarriableBlocksField.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (endermanCarriableBlocks != null) {
            CARRIABLE_BLOCKS.addAll(endermanCarriableBlocks);
        }

        CARRIABLE_BLOCKS.add(ModBlocks.spectrite_sand);
    }

    public boolean isScreaming()
    {
        return ((Boolean)this.dataManager.get(SCREAMING)).booleanValue();
    }

    static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        private final EntitySpectriteEnderman spectriteEnderman;
        /** The player */
        private EntityPlayer player;
        private int aggroTime;
        private int teleportTime;

        public AIFindPlayer(EntitySpectriteEnderman p_i45842_1_)
        {
            super(p_i45842_1_, EntityPlayer.class, false);
            this.spectriteEnderman = p_i45842_1_;
        }

        @Override
        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            double d0 = this.getTargetDistance();
            this.player = this.spectriteEnderman.world.getNearestAttackablePlayer(this.spectriteEnderman.posX, this.spectriteEnderman.posY, this.spectriteEnderman.posZ, d0, d0, (Function)null, new Predicate<EntityPlayer>()
            {
                public boolean apply(@Nullable EntityPlayer p_apply_1_)
                {
                    return p_apply_1_ != null && EntitySpectriteEnderman.AIFindPlayer.this.spectriteEnderman.shouldAttackPlayer(p_apply_1_);
                }
            });
            return this.player != null;
        }

        @Override
        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            this.aggroTime = 5;
            this.teleportTime = 0;
        }

        @Override
        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
            this.player = null;
            super.resetTask();
        }

        @Override
        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            if (this.player != null)
            {
                if (!this.spectriteEnderman.shouldAttackPlayer(this.player))
                {
                    return false;
                }
                else
                {
                    this.spectriteEnderman.faceEntity(this.player, 10.0F, 10.0F);
                    return true;
                }
            }
            else
            {
                return this.targetEntity != null && ((EntityPlayer)this.targetEntity).isEntityAlive() ? true : super.shouldContinueExecuting();
            }
        }

        @Override
        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            if (this.player != null)
            {
                if (--this.aggroTime <= 0)
                {
                    this.targetEntity = this.player;
                    this.player = null;
                    super.startExecuting();
                }
            }
            else
            {
                if (this.targetEntity != null)
                {
                    if (this.spectriteEnderman.shouldAttackPlayer((EntityPlayer)this.targetEntity))
                    {
                        if (((EntityPlayer)this.targetEntity).getDistanceSqToEntity(this.spectriteEnderman) < 16.0D)
                        {
                            this.spectriteEnderman.teleportRandomly();
                        }

                        this.teleportTime = 0;
                    }
                    else if (((EntityPlayer)this.targetEntity).getDistanceSqToEntity(this.spectriteEnderman) > 256.0D && this.teleportTime++ >= 30 && this.spectriteEnderman.teleportToEntity(this.targetEntity))
                    {
                        this.teleportTime = 0;
                    }
                }

                super.updateTask();
            }
        }
    }

    static class AIPlaceBlock extends EntityAIBase
    {
        private final EntitySpectriteEnderman spectriteEnderman;

        public AIPlaceBlock(EntitySpectriteEnderman p_i45843_1_)
        {
            this.spectriteEnderman = p_i45843_1_;
        }

        @Override
        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (this.spectriteEnderman.getHeldBlockState() == null)
            {
                return false;
            }
            else if (!this.spectriteEnderman.world.getGameRules().getBoolean("mobGriefing"))
            {
                return false;
            }
            else
            {
                return this.spectriteEnderman.getRNG().nextInt(2000) == 0;
            }
        }

        @Override
        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            Random random = this.spectriteEnderman.getRNG();
            World world = this.spectriteEnderman.world;
            int i = MathHelper.floor(this.spectriteEnderman.posX - 1.0D + random.nextDouble() * 2.0D);
            int j = MathHelper.floor(this.spectriteEnderman.posY + random.nextDouble() * 2.0D);
            int k = MathHelper.floor(this.spectriteEnderman.posZ - 1.0D + random.nextDouble() * 2.0D);
            BlockPos blockpos = new BlockPos(i, j, k);
            IBlockState iblockstate = world.getBlockState(blockpos);
            IBlockState iblockstate1 = world.getBlockState(blockpos.down());
            IBlockState iblockstate2 = this.spectriteEnderman.getHeldBlockState();

            if (iblockstate2 != null && this.canPlaceBlock(world, blockpos, iblockstate2.getBlock(), iblockstate, iblockstate1))
            {
                world.setBlockState(blockpos, iblockstate2, 3);
                this.spectriteEnderman.setHeldBlockState((IBlockState)null);
            }
        }

        private boolean canPlaceBlock(World p_188518_1_, BlockPos p_188518_2_, Block p_188518_3_, IBlockState p_188518_4_, IBlockState p_188518_5_)
        {
            if (!p_188518_3_.canPlaceBlockAt(p_188518_1_, p_188518_2_))
            {
                return false;
            }
            else if (p_188518_4_.getMaterial() != Material.AIR)
            {
                return false;
            }
            else if (p_188518_5_.getMaterial() == Material.AIR)
            {
                return false;
            }
            else
            {
                return p_188518_5_.isFullCube();
            }
        }
    }

    static class AITakeBlock extends EntityAIBase
    {
        private final EntitySpectriteEnderman spectriteEnderman;

        public AITakeBlock(EntitySpectriteEnderman p_i45841_1_)
        {
            this.spectriteEnderman = p_i45841_1_;
        }

        @Override
        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (this.spectriteEnderman.getHeldBlockState() != null)
            {
                return false;
            }
            else if (!this.spectriteEnderman.world.getGameRules().getBoolean("mobGriefing"))
            {
                return false;
            }
            else
            {
                return this.spectriteEnderman.getRNG().nextInt(20) == 0;
            }
        }

        @Override
        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            Random random = this.spectriteEnderman.getRNG();
            World world = this.spectriteEnderman.world;
            int i = MathHelper.floor(this.spectriteEnderman.posX - 2.0D + random.nextDouble() * 4.0D);
            int j = MathHelper.floor(this.spectriteEnderman.posY + random.nextDouble() * 3.0D);
            int k = MathHelper.floor(this.spectriteEnderman.posZ - 2.0D + random.nextDouble() * 4.0D);
            BlockPos blockpos = new BlockPos(i, j, k);
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            RayTraceResult raytraceresult = world.rayTraceBlocks(new Vec3d((double)((float)MathHelper.floor(this.spectriteEnderman.posX) + 0.5F), (double)((float)j + 0.5F), (double)((float)MathHelper.floor(this.spectriteEnderman.posZ) + 0.5F)), new Vec3d((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F)), false, true, false);
            boolean flag = raytraceresult != null && raytraceresult.getBlockPos().equals(blockpos);

            if (EntitySpectriteEnderman.CARRIABLE_BLOCKS.contains(block) && flag)
            {
                this.spectriteEnderman.setHeldBlockState(iblockstate);
                world.setBlockToAir(blockpos);
            }
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere()
    {
        BlockPos pos = new BlockPos(this);

        if (this.world.getBiome(pos) != ModBiomes.spectrite_dungeon)
            return true;

        int spawnChance = (pos.getY() + 8) >> 3;
        boolean shouldSpawn = spawnChance == 1 || (spawnChance == 2 && rand.nextBoolean()) || (spawnChance == 3 && rand.nextInt(3) == 0);
        return shouldSpawn && pos.down().getY() < 28;
    }
}
