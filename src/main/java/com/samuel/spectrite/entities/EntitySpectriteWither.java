package com.samuel.spectrite.entities;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModPotions;
import com.samuel.spectrite.init.ModSounds;
import com.samuel.spectrite.items.ItemSpectriteSkull;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntitySpectriteWither extends EntityMob implements IRangedAttackMob, ISpectriteMob {

    private static final DataParameter<Integer> FIRST_HEAD_TARGET = EntityDataManager.<Integer>createKey(EntitySpectriteWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SECOND_HEAD_TARGET = EntityDataManager.<Integer>createKey(EntitySpectriteWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> THIRD_HEAD_TARGET = EntityDataManager.<Integer>createKey(EntitySpectriteWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer>[] HEAD_TARGETS = new DataParameter[] {FIRST_HEAD_TARGET, SECOND_HEAD_TARGET, THIRD_HEAD_TARGET};
    private static final DataParameter<Integer> INVULNERABILITY_TIME = EntityDataManager.<Integer>createKey(EntitySpectriteWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SKULL_COUNT = EntityDataManager.<Integer>createKey(EntitySpectriteWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> RADIAL_SKULL_TICKS = EntityDataManager.<Integer>createKey(EntitySpectriteWither.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> RADIAL_SPURTS = EntityDataManager.<Boolean>createKey(EntitySpectriteWither.class, DataSerializers.BOOLEAN);
    private final float[] xRotationHeads = new float[2];
    private final float[] yRotationHeads = new float[2];
    private final float[] xRotOHeads = new float[2];
    private final float[] yRotOHeads = new float[2];
    private final int[] nextHeadUpdate = new int[2];
    private final int[] idleHeadUpdates = new int[2];
    /** Time before the Wither tries to break blocks */
    private int blockBreakCounter;
    private float radialStartYaw = -999;
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
    /** Selector used to determine the entities a wither boss should attack. */
    private static final Predicate<Entity> NOT_SPECTRITE = p_apply_1_ -> {
        final boolean ret;
        if (!(p_apply_1_ instanceof EntityPlayer)) {
            ret = p_apply_1_ instanceof EntityLivingBase && !(p_apply_1_ instanceof ISpectriteMob) && ((EntityLivingBase)p_apply_1_).attackable();
        } else {
            ItemStack helmetStack = ((EntityPlayer) p_apply_1_).getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            ret = helmetStack.isEmpty() || helmetStack.getItem() instanceof ItemSpectriteSkull;
        }
        return ret;
    };

    public EntitySpectriteWither(World worldIn)
    {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(0.9F, 3.5F);
        this.isImmuneToFire = true;
        ((PathNavigateGround)this.getNavigator()).setCanSwim(true);
        this.experienceValue = 350;
    }

    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntitySpectriteWither.AIDoNothing());
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 30.0F));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, NOT_SPECTRITE));
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(FIRST_HEAD_TARGET, Integer.valueOf(0));
        this.dataManager.register(SECOND_HEAD_TARGET, Integer.valueOf(0));
        this.dataManager.register(THIRD_HEAD_TARGET, Integer.valueOf(0));
        this.dataManager.register(INVULNERABILITY_TIME, Integer.valueOf(0));
        this.dataManager.register(SKULL_COUNT, Integer.valueOf(0));
        this.dataManager.register(RADIAL_SKULL_TICKS, Integer.valueOf(-1));
        this.dataManager.register(RADIAL_SPURTS, Boolean.FALSE);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Invul", this.getInvulTime());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setInvulTime(compound.getInteger("Invul"));

        if (this.hasCustomName())
        {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    /**
     * Sets the custom name tag for this entity
     */
    @Override
    public void setCustomNameTag(String name)
    {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_WITHER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_WITHER_DEATH;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate()
    {
        this.motionY *= 0.6000000238418579D;

        if (!this.world.isRemote && this.getWatchedTargetId(0) > 0)
        {
            Entity entity = this.world.getEntityByID(this.getWatchedTargetId(0));

            if (entity != null)
            {
                if (this.posY < entity.posY || !this.isArmored() && this.posY < entity.posY + 5.0D)
                {
                    if (this.motionY < 0.0D)
                    {
                        this.motionY = 0.0D;
                    }

                    this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
                }

                double d0 = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1;

                if (d3 > 9.0D)
                {
                    double d5 = (double)MathHelper.sqrt(d3);
                    this.motionX += (d0 / d5 * 0.5D - this.motionX) * 0.6000000238418579D;
                    this.motionZ += (d1 / d5 * 0.5D - this.motionZ) * 0.6000000238418579D;
                }
            }
        }

        if (this.getRadialSkullTicks() == -1 && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D)
        {
            this.rotationYaw = (float)MathHelper.atan2(this.motionZ, this.motionX) * (180F / (float)Math.PI) - 90.0F;
        }

        super.onLivingUpdate();

        if (this.getRadialSkullTicks() == -1) {
            for (int i = 0; i < 2; ++i) {
                this.yRotOHeads[i] = this.yRotationHeads[i];
                this.xRotOHeads[i] = this.xRotationHeads[i];
            }

            for (int j = 0; j < 2; ++j) {
                int k = this.getWatchedTargetId(j + 1);
                Entity entity1 = null;

                if (k > 0) {
                    entity1 = this.world.getEntityByID(k);
                }

                if (entity1 != null) {
                    double d11 = this.getHeadX(j + 1);
                    double d12 = this.getHeadY(j + 1);
                    double d13 = this.getHeadZ(j + 1);
                    double d6 = entity1.posX - d11;
                    double d7 = entity1.posY + (double) entity1.getEyeHeight() - d12;
                    double d8 = entity1.posZ - d13;
                    double d9 = (double) MathHelper.sqrt(d6 * d6 + d8 * d8);
                    float f = (float) (MathHelper.atan2(d8, d6) * (180D / Math.PI)) - 90.0F;
                    float f1 = (float) (-(MathHelper.atan2(d7, d9) * (180D / Math.PI)));
                    this.xRotationHeads[j] = this.rotlerp(this.xRotationHeads[j], f1, 40.0F);
                    this.yRotationHeads[j] = this.rotlerp(this.yRotationHeads[j], f, 10.0F);
                } else {
                    this.yRotationHeads[j] = this.rotlerp(this.yRotationHeads[j], this.renderYawOffset, 10.0F);
                }
            }
        }

        boolean flag = this.isArmored();

        if (!world.isRemote) {
            for (int l = 0; l < 3; ++l) {
                double d10 = this.getHeadX(l);
                double d2 = this.getHeadY(l);
                double d4 = this.getHeadZ(l);
                Spectrite.Proxy.spawnSpectriteSmokeNormalParticle(this.world, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);

                if (flag && this.world.rand.nextInt(4) == 0) {
                    Spectrite.Proxy.spawnSpectriteSpellParticle(this.world, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D, 0);
                }
            }

            if (this.getInvulTime() > 0) {
                for (int i1 = 0; i1 < 3; ++i1) {
                    Spectrite.Proxy.spawnSpectriteSpellParticle(this.world, this.posX + this.rand.nextGaussian(), this.posY + (double) (this.rand.nextFloat() * 3.3F), this.posZ + this.rand.nextGaussian(), 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, 0);
                }
            }
        }
    }

    @Override
    protected void updateAITasks()
    {
        int radialSkullTicks = this.getRadialSkullTicks();
        boolean radial = radialSkullTicks > -1;
        if (this.getInvulTime() > 0 && !radial)
        {
            int j1 = this.getInvulTime() - 1;

            if (j1 <= 0)
            {
                Spectrite.Proxy.newSpectriteExplosion(this.world,this, this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, 10.0F, true, this.world.getGameRules().getBoolean("mobGriefing"), false);
                this.world.playSound(null, new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), ModSounds.explosion, SoundCategory.HOSTILE, 1.0f, 1.0f);
                this.world.playBroadcastSound(1023, new BlockPos(this), 0);
            } else if (j1 == 30) {
                this.world.playSound(null, new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), ModSounds.preexplosion, SoundCategory.HOSTILE, 1.0f, 1.0f);
            }

            this.setInvulTime(j1);

            if (this.ticksExisted % 10 == 0)
            {
                this.heal(25.0F);
            }
        }
        else
        {
            super.updateAITasks();

            final int difficulty = this.world.getDifficulty().ordinal();
            int skullCount = this.getSkullCount();

            for (int i = 1; i < 3; ++i) {
                boolean needsUpdate = this.ticksExisted >= this.nextHeadUpdate[i - 1];
                if (needsUpdate) {
                    this.nextHeadUpdate[i - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);

                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) {
                        int k3 = this.idleHeadUpdates[i - 1];
                        int j3 = i - 1;

                        this.idleHeadUpdates[j3] = this.idleHeadUpdates[i - 1] + 1;

                        if (i == 2 && k3 > 15) {
                            this.setSkullCount(++skullCount);
                            if (skullCount >= 10) {
                                radial = startRadialAttack();
                            }
                        }

                        if (!radial && k3 > 15) {
                            double d0 = MathHelper.nextDouble(this.rand, this.posX - 10.0D, this.posX + 10.0D);
                            double d1 = MathHelper.nextDouble(this.rand, this.posY - 5.0D, this.posY + 5.0D);
                            double d2 = MathHelper.nextDouble(this.rand, this.posZ - 10.0D, this.posZ + 10.0D);
                            this.launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
                            this.idleHeadUpdates[i - 1] = 0;
                        }
                    }
                }

                int k1 = this.getWatchedTargetId(i);

                if (needsUpdate && i == 2 && !radial && k1 > 0) {
                    this.setSkullCount(++skullCount);
                    if (skullCount >= 10) {
                        radial = startRadialAttack();
                    }
                }

                if (i == 2 && radial && radialSkullTicks > 20 && ((!this.isRadialSpurts() && radialSkullTicks % 4 == 0)
                        || (this.isRadialSpurts() && (radialSkullTicks + 4) % 24 < 16))) {
                    this.launchRadialWitherSkulls(getInvulTime() > 0);
                    if (needsUpdate) {
                        this.nextHeadUpdate[i - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                        this.idleHeadUpdates[i - 1] = 0;
                    }
                } else if (needsUpdate) {
                    if (k1 > 0) {
                        Entity entity = this.world.getEntityByID(k1);

                        if (entity != null && entity.isEntityAlive() && this.getDistanceSqToEntity(entity) <= 900.0D && this.canEntityBeSeen(entity)) {
                            if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.disableDamage) {
                                this.updateWatchedTargetId(i, 0);
                            } else {
                                this.launchWitherSkullToEntity(i + 1, (EntityLivingBase) entity);
                                this.nextHeadUpdate[i - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                                this.idleHeadUpdates[i - 1] = 0;
                            }
                        } else {
                            this.updateWatchedTargetId(i, 0);
                        }
                    } else {
                        List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(20.0D, 8.0D, 20.0D), Predicates.and(NOT_SPECTRITE, EntitySelectors.NOT_SPECTATING));

                        for (int j2 = 0; j2 < 10 && !list.isEmpty(); ++j2) {
                            EntityLivingBase entitylivingbase = list.get(this.rand.nextInt(list.size()));

                            if (entitylivingbase != this && entitylivingbase.isEntityAlive() && this.canEntityBeSeen(entitylivingbase)) {
                                if (entitylivingbase instanceof EntityPlayer) {
                                    if (!((EntityPlayer) entitylivingbase).capabilities.disableDamage) {
                                        this.updateWatchedTargetId(i, entitylivingbase.getEntityId());
                                    }
                                } else {
                                    this.updateWatchedTargetId(i, entitylivingbase.getEntityId());
                                }

                                break;
                            }

                            list.remove(entitylivingbase);
                        }
                    }
                }
            }

            if (radial) {
                if (radialSkullTicks < 84) {
                    this.setRadialSkullTicks(++radialSkullTicks);
                    if (getInvulTime() > 0) {
                        setInvulTime(getInvulTime() - 1);
                    }
                } else {
                    this.setRadialSkullTicks(-1);
                    this.setRadialSpurts(false);
                    this.radialStartYaw = -999;
                }
            }

            if (skullCount >= 10) {
                setSkullCount(0);
            }

            if (this.getAttackTarget() != null)
            {
                this.updateWatchedTargetId(0, this.getAttackTarget().getEntityId());
            }
            else
            {
                this.updateWatchedTargetId(0, 0);
            }

            if (this.blockBreakCounter > 0)
            {
                --this.blockBreakCounter;

                if (this.blockBreakCounter == 0 && this.world.getGameRules().getBoolean("mobGriefing"))
                {
                    int i1 = MathHelper.floor(this.posY);
                    int l1 = MathHelper.floor(this.posX);
                    int i2 = MathHelper.floor(this.posZ);
                    boolean flag = false;

                    for (int k2 = -1; k2 <= 1; ++k2)
                    {
                        for (int l2 = -1; l2 <= 1; ++l2)
                        {
                            for (int j = 0; j <= 3; ++j)
                            {
                                int i3 = l1 + k2;
                                int k = i1 + j;
                                int l = i2 + l2;
                                BlockPos blockpos = new BlockPos(i3, k, l);
                                IBlockState iblockstate = this.world.getBlockState(blockpos);
                                Block block = iblockstate.getBlock();

                                if (!block.isAir(iblockstate, this.world, blockpos) && (block.canEntityDestroy(iblockstate, world, blockpos, this)
                                    && canDestroyBlock(iblockstate.getBlock()) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, iblockstate)))
                                {
                                    flag = this.world.destroyBlock(blockpos, true) || flag;
                                }
                            }
                        }
                    }

                    if (flag)
                    {
                        this.world.playEvent(null, 1022, new BlockPos(this), 0);
                    }
                }
            }

            final int healRate;

            switch (difficulty) {
                case 0:
                case 1:
                    healRate = 20;
                    break;
                case 2:
                    healRate = 15;
                    break;
                default:
                    healRate = 10;
                    break;
            }

            if (this.ticksExisted % healRate == 0)
            {
                this.heal(1.0F);
            }

            if (radial) {
                if (radialStartYaw == -999) {
                    radialStartYaw = ((this.rotationYaw + 180) % 360) - 180;
                }
                if (radialSkullTicks >= 10) {
                    this.motionY = 0.25f;
                }
                if (radialSkullTicks < 20 || (this.isRadialSpurts() && (this.getRadialSkullTicks() + 4) % 24 >= 16)) {
                    this.renderYawOffset = this.rotationYaw = radialStartYaw;
                } else if (this.isRadialSpurts()) {
                    this.renderYawOffset = this.rotationYaw = ((radialStartYaw + (22.5F * ((radialSkullTicks - 4) % 16))) % 360) - 180f;
                } else {
                    this.renderYawOffset = this.rotationYaw = ((radialStartYaw + (11.25f * ((radialSkullTicks + 12) % 32))) % 360) - 180f;
                }
            }

            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }

        this.bossInfo.setName(new TextComponentString(SpectriteHelper.getMultiColouredString(this.getDisplayName().getUnformattedText(), true)));
    }

    private boolean startRadialAttack() {
        boolean ret = this.world.getDifficulty() == EnumDifficulty.HARD || (this.world.getDifficulty() == EnumDifficulty.NORMAL && this.getHealth() <= this.getMaxHealth() * 0.75f);
        if (ret) {
            if (this.getHealth() <= this.getMaxHealth() * 0.25f) {
                if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                    this.setInvulTime(84);
                }
            } else if (this.getHealth() <= this.getMaxHealth() * 0.5f && this.world.getDifficulty() == EnumDifficulty.HARD) {
                this.setRadialSpurts(true);
            }
            this.setRadialSkullTicks(this.getRadialSkullTicks() + 1);
            radialStartYaw = ((this.rotationYaw + 180) % 360) - 180;
            this.setAttackTarget(null);
            this.world.playSound(null, new BlockPos(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ), ModSounds.charge, SoundCategory.HOSTILE, 1.0f, 1.0f);
        }
        return ret;
    }

    public static boolean canDestroyBlock(Block blockIn)
    {
        return blockIn != Blocks.BEDROCK && blockIn != Blocks.END_PORTAL && blockIn != Blocks.END_PORTAL_FRAME && blockIn != Blocks.COMMAND_BLOCK && blockIn != Blocks.REPEATING_COMMAND_BLOCK && blockIn != Blocks.CHAIN_COMMAND_BLOCK && blockIn != Blocks.BARRIER && blockIn != Blocks.STRUCTURE_BLOCK && blockIn != Blocks.STRUCTURE_VOID && blockIn != Blocks.PISTON_EXTENSION && blockIn != Blocks.END_GATEWAY;
    }

    /**
     * Initializes this Wither's explosion sequence and makes it invulnerable. Called immediately after spawning.
     */
    public void ignite()
    {
        this.setInvulTime(220);
        this.setHealth(this.getMaxHealth() / 3.0F);
    }

    /**
     * Sets the Entity inside a web block.
     */
    @Override
    public void setInWeb()
    {
    }

    /**
     * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
     * order to view its associated boss bar.
     */
    @Override
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    /**
     * Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for
     * more information on tracking.
     */
    @Override
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    private double getHeadX(int p_82214_1_)
    {
        if (p_82214_1_ <= 0)
        {
            return this.posX;
        }
        else
        {
            float f = (this.renderYawOffset + (float)(180 * (p_82214_1_ - 1))) * 0.017453292F;
            float f1 = MathHelper.cos(f);
            return this.posX + (double)f1 * 1.3D;
        }
    }

    private double getHeadY(int p_82208_1_)
    {
        return p_82208_1_ <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
    }

    private double getHeadZ(int p_82213_1_)
    {
        if (p_82213_1_ <= 0)
        {
            return this.posZ;
        }
        else
        {
            float f = (this.renderYawOffset + (float)(180 * (p_82213_1_ - 1))) * 0.017453292F;
            float f1 = MathHelper.sin(f);
            return this.posZ + (double)f1 * 1.3D;
        }
    }

    private float rotlerp(float p_82204_1_, float p_82204_2_, float p_82204_3_)
    {
        float f = MathHelper.wrapDegrees(p_82204_2_ - p_82204_1_);

        if (f > p_82204_3_)
        {
            f = p_82204_3_;
        }

        if (f < -p_82204_3_)
        {
            f = -p_82204_3_;
        }

        return p_82204_1_ + f;
    }

    private void launchWitherSkullToEntity(int p_82216_1_, EntityLivingBase p_82216_2_)
    {
        this.launchWitherSkullToCoords(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + (double)p_82216_2_.getEyeHeight() * 0.5D, p_82216_2_.posZ, p_82216_1_ == 0 && this.rand.nextFloat() < 0.001F);
    }

    /**
     * Launches a Wither skull toward (par2, par4, par6)
     */
    private void launchWitherSkullToCoords(int p_82209_1_, double x, double y, double z, boolean invulnerable)
    {
        this.world.playEvent(null, 1024, new BlockPos(this), 0);
        double d0 = this.getHeadX(p_82209_1_);
        double d1 = this.getHeadY(p_82209_1_);
        double d2 = this.getHeadZ(p_82209_1_);
        double d3 = x - d0;
        double d4 = y - d1;
        double d5 = z - d2;
        EntitySpectriteWitherSkull entitySpectriteWitherSkull = new EntitySpectriteWitherSkull(this.world, this, d3, d4, d5);

        if (invulnerable)
        {
            entitySpectriteWitherSkull.setInvulnerable(true);
        }

        entitySpectriteWitherSkull.posY = d1;
        entitySpectriteWitherSkull.posX = d0;
        entitySpectriteWitherSkull.posZ = d2;
        this.world.spawnEntity(entitySpectriteWitherSkull);
    }

    private void launchRadialWitherSkulls(boolean invulnerable)
    {
        this.world.playEvent(null, 1024, new BlockPos(this), 0);
        double d0 = this.posX;
        double d1 = this.posY + this.getEyeHeight() * 0.50d;
        double d2 = this.posZ;

        boolean hasSpaceBelow = true;

        for (int i = 1; i <= 3; i++) {
            BlockPos pos = this.getPosition().down(i);
            hasSpaceBelow = hasSpaceBelow && this.world.getBlockState(pos).getBlock().isPassable(this.world, pos);
            if (!hasSpaceBelow) {
                break;
            }
        }

        float yaw;

        if (this.isRadialSpurts()) {
            yaw = (radialStartYaw + ((22.5f * ((this.getRadialSkullTicks() - 4) % 16)) % 360)) - 180;
        } else {
            yaw = (radialStartYaw + ((22.5f * (((this.getRadialSkullTicks() - 4) >> 2) % 16)) % 360)) - 180;
        }

        EntitySpectriteWitherSkull entitySpectriteWitherSkull = new EntitySpectriteWitherSkull(this.world, this, 0, 0, 0);
        entitySpectriteWitherSkull.setAim(this, hasSpaceBelow ? 45f : 90f, yaw, 1);

        if (invulnerable)
        {
            entitySpectriteWitherSkull.setInvulnerable(true);
        }

        entitySpectriteWitherSkull.posY = d1;
        entitySpectriteWitherSkull.posX = d0;
        entitySpectriteWitherSkull.posZ = d2;
        this.world.spawnEntity(entitySpectriteWitherSkull);
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        this.launchWitherSkullToEntity(0, target);
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else if (source != DamageSource.DROWN && !(source.getTrueSource() instanceof EntitySpectriteWither))
        {
            if (this.getInvulTime() > 0 && source != DamageSource.OUT_OF_WORLD)
            {
                return false;
            }
            else
            {
                if (this.isArmored())
                {
                    Entity entity = source.getImmediateSource();

                    if (entity instanceof EntityArrow)
                    {
                        return false;
                    }
                }

                Entity entity1 = source.getTrueSource();

                if (entity1 != null && !(entity1 instanceof EntityPlayer) && entity1 instanceof EntityLivingBase && ((EntityLivingBase)entity1).getCreatureAttribute() == this.getCreatureAttribute())
                {
                    return false;
                }
                else
                {
                    if (this.blockBreakCounter <= 0)
                    {
                        this.blockBreakCounter = 20;
                    }

                    for (int i = 0; i < this.idleHeadUpdates.length; ++i)
                    {
                        this.idleHeadUpdates[i] += 3;
                    }

                    boolean success = super.attackEntityFrom(source, amount) && !source.isExplosion();

                    if (success && amount > 20f && this.getRadialSkullTicks() > -1) {
                        this.setRadialSkullTicks(-1);
                        this.setRadialSpurts(false);
                        this.radialStartYaw = -999;
                    }

                    return success;
                }
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Drop 0-2 items of this living's type
     */
    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        Item[] drops = new Item[] { ModItems.spectrite_star, ModItems.spectrite_wither_skull, ModItems.spectrite_wither_torso, ModItems.spectrite_wither_tail };
        for (Item d : drops) {
            EntityItem entityitem = this.dropItem(d, 1);

            if (entityitem != null) {
                entityitem.setNoDespawn();
            }
        }
    }

    /**
     * Makes the entity despawn if requirements are reached
     */
    @Override
    protected void despawnEntity()
    {
        this.idleTime = 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return 15728880;
    }

    @Override
    public void fall(float distance, float damageMultiplier)
    {
    }

    /**
     * adds a PotionEffect to the entity
     */
    @Override
    public void addPotionEffect(PotionEffect potioneffectIn)
    {
        if (potioneffectIn.getPotion() == ModPotions.SPECTRITE_RESISTANCE || potioneffectIn.getPotion() == ModPotions.SPECTRITE_DAMAGE) {
            super.addPotionEffect(potioneffectIn);
        }
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(750.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6000000238418579D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
    }

    @Override
    public boolean isOnSameTeam(Entity entityIn) {
        return (entityIn instanceof ISpectriteMob && (entityIn.getClass() != EntitySpectriteGolem.class
                || !((EntitySpectriteGolem) entityIn).isPlayerCreated())) || super.isOnSameTeam(entityIn);
    }

    @SideOnly(Side.CLIENT)
    public float getHeadYRotation(int p_82207_1_)
    {
        return this.yRotationHeads[p_82207_1_];
    }

    @SideOnly(Side.CLIENT)
    public float getHeadXRotation(int p_82210_1_)
    {
        return this.xRotationHeads[p_82210_1_];
    }

    public int getInvulTime()
    {
        return ((Integer)this.dataManager.get(INVULNERABILITY_TIME)).intValue();
    }

    public void setInvulTime(int time)
    {
        this.dataManager.set(INVULNERABILITY_TIME, Integer.valueOf(time));
    }

    public int getSkullCount()
    {
        return ((Integer)this.dataManager.get(SKULL_COUNT)).intValue();
    }

    public void setSkullCount(int skullCount)
    {
        this.dataManager.set(SKULL_COUNT, Integer.valueOf(skullCount));
    }

    public int getRadialSkullTicks()
    {
        return ((Integer)this.dataManager.get(RADIAL_SKULL_TICKS)).intValue();
    }

    public void setRadialSkullTicks(int radialSkullTicks)
    {
        this.dataManager.set(RADIAL_SKULL_TICKS, Integer.valueOf(radialSkullTicks));
    }

    public boolean isRadialSpurts() { return ((Boolean)this.dataManager.get(RADIAL_SPURTS).booleanValue()); }

    public void setRadialSpurts(boolean radialSpurts) {
        this.dataManager.set(RADIAL_SPURTS, Boolean.valueOf(radialSpurts));
    }

    /**
     * Returns the target entity ID if present, or -1 if not @param par1 The target offset, should be from 0-2
     */
    public int getWatchedTargetId(int head)
    {
        return ((Integer)this.dataManager.get(HEAD_TARGETS[head])).intValue();
    }

    /**
     * Updates the target entity ID
     */
    public void updateWatchedTargetId(int targetOffset, int newId)
    {
        this.dataManager.set(HEAD_TARGETS[targetOffset], Integer.valueOf(newId));
    }

    /**
     * Returns whether the wither is armored with its boss armor or not by checking whether its health is below half of
     * its maximum.
     */
    public boolean isArmored()
    {
        return this.getHealth() <= this.getMaxHealth() / 2.0F;
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn)
    {
        return false;
    }

    /**
     * Returns false if this Entity is a boss, true otherwise.
     */
    public boolean isNonBoss()
    {
        return false;
    }

    @Override
    public void setSwingingArms(boolean swingingArms)
    {
    }

    class AIDoNothing extends EntityAIBase
    {
        public AIDoNothing()
        {
            this.setMutexBits(7);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute()
        {
            return EntitySpectriteWither.this.getInvulTime() > 0;
        }
    }

}
