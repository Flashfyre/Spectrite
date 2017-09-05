package com.samuel.spectrite.entities;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModBiomes;
import com.samuel.spectrite.items.ItemSpectriteBow;
import com.samuel.spectrite.packets.PacketSyncSpectriteBoss;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.lang.reflect.Field;

public abstract class AbstractSpectriteSkeleton extends AbstractSkeleton implements ISpectriteMob, ISpectriteBipedMob {

    protected boolean boss;
    protected boolean hasSpectriteResistance;
    protected int chanceMultiplier;
    protected final EntityAIAttackRangedBow aiArrowAttack = new EntityAIAttackRangedBow(this, 1.0D, 20, 15.0F) {

        private Field entity = null;

        @Override
        protected boolean isBowInMainhand()
        {
            if (entity == null) {
                entity = SpectriteHelper.findObfuscatedField(EntityAIAttackRangedBow.class, new String[] { "entity", "field_188499_a" });
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
    protected BossInfoServer bossInfo;

    public AbstractSpectriteSkeleton(World worldIn) {
        super(worldIn);

        setBoss(!this.world.isRemote && SpectriteConfig.mobs.spectriteMobBossSpawnRate > 0d
            && (int) getUniqueID().getMostSignificantBits() % (100 / SpectriteConfig.mobs.spectriteMobBossSpawnRate) == 0);

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
                Field aiAttackOnCollide = SpectriteHelper.findObfuscatedField(AbstractSkeleton.class, new String[] { "aiAttackOnCollide", "field_85038_e" });
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
    public boolean isBoss() {
        return boss;
    }

    @Override
    public void setBoss(boolean boss) {
        this.boss = boss;

        if (boss) {
            this.chanceMultiplier = 10;
            this.bossInfo = new BossInfoServer(new TextComponentString(SpectriteHelper.getMultiColouredString(this.getDisplayName().getUnformattedText(), true)),
                    BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);
        } else {
            this.chanceMultiplier = 1;
            this.bossInfo = null;
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

    @Override
    public boolean isNonBoss()
    {
        return !boss;
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!this.world.isRemote) {
            if (this.boss) {
                this.bossInfo.setName(new TextComponentString(SpectriteHelper.getMultiColouredString(this.getDisplayName().getUnformattedText(), true)));
                this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
            }
        }
    }

    /**
     * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
     * order to view its associated boss bar.
     */
    @Override
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);

        if (this.boss) {
            this.bossInfo.addPlayer(player);
            Spectrite.Network.sendTo(new PacketSyncSpectriteBoss(getUniqueID(), true), player);
        }
    }

    /**
     * Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for
     * more information on tracking.
     */
    @Override
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);

        if (this.boss) {
            this.bossInfo.removePlayer(player);
        }
    }

    @Override
    public void setCustomNameTag(String name)
    {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
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

        setBoss(!this.world.isRemote && SpectriteConfig.mobs.spectriteMobBossSpawnRate > 0d
                && (int) getUniqueID().getMostSignificantBits() % (100 / SpectriteConfig.mobs.spectriteMobBossSpawnRate) == 0);

        setHasSpectriteResistance(isArmorFullEnhanced());

        if (this.hasCustomName())
        {
            this.bossInfo.setName(this.getDisplayName());
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
        return shouldSpawn && pos.down().getY() < 28;
    }

    @Override
    public boolean isOnSameTeam(Entity entityIn) {
        return (entityIn instanceof ISpectriteMob && (entityIn.getClass() != EntitySpectriteGolem.class
            || !((EntitySpectriteGolem) entityIn).isPlayerCreated())) || super.isOnSameTeam(entityIn);
    }
}
