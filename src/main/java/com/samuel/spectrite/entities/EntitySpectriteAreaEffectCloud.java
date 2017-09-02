package com.samuel.spectrite.entities;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModPotions;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.PotionTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class EntitySpectriteAreaEffectCloud extends Entity
{
    private static final DataParameter<Float> RADIUS = EntityDataManager.<Float>createKey(EntitySpectriteAreaEffectCloud.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> IGNORE_RADIUS = EntityDataManager.<Boolean>createKey(EntitySpectriteAreaEffectCloud.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> HUE_OFFSET_LEVEL = EntityDataManager.<Integer>createKey(EntitySpectriteAreaEffectCloud.class, DataSerializers.VARINT);
    private PotionType potionType;
    private Potion potion;
    private final List<PotionEffect> effects;
    private final Map<Entity, Integer> reapplicationDelayMap;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private EntityLivingBase owner;
    private UUID ownerUniqueId;

    public EntitySpectriteAreaEffectCloud(World worldIn)
    {
        super(worldIn);
        this.potionType = PotionTypes.EMPTY;
        this.effects = Lists.<PotionEffect>newArrayList();
        this.reapplicationDelayMap = Maps.<Entity, Integer>newHashMap();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setRadius(3.0F);
    }

    public EntitySpectriteAreaEffectCloud(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    @Override
	protected void entityInit()
    {
        this.getDataManager().register(RADIUS, Float.valueOf(0.5F));
        this.getDataManager().register(IGNORE_RADIUS, Boolean.valueOf(false));
        this.getDataManager().register(HUE_OFFSET_LEVEL, Integer.valueOf(0));
    }

    public void setRadius(float radiusIn)
    {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.setSize(radiusIn * 2.0F, 0.5F);
        this.setPosition(d0, d1, d2);

        if (!this.world.isRemote)
        {
            this.getDataManager().set(RADIUS, Float.valueOf(radiusIn));
        }
    }

    public float getRadius()
    {
        return this.getDataManager().get(RADIUS).floatValue();
    }

    public void setPotionType(PotionType potionTypeIn)
    {
        this.potionType = potionTypeIn;
        this.potion = potionType.getEffects().get(0).getPotion();
        this.setHueOffsetLevel(ModPotions.SPECTRITE.equals(potionTypeIn) ? 0 : ModPotions.SPECTRITE_DAMAGE.equals(this.potion) ? 1 : 2);
    }

    public void addEffect(PotionEffect effect)
    {
        this.effects.add(effect);
    }

    /**
     * Sets if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    protected void setIgnoreRadius(boolean ignoreRadius)
    {
        this.getDataManager().set(IGNORE_RADIUS, Boolean.valueOf(ignoreRadius));
    }

    /**
     * Returns true if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    public boolean shouldIgnoreRadius()
    {
        return this.getDataManager().get(IGNORE_RADIUS).booleanValue();
    }

    public int getHueOffsetLevel()
    {
        return this.getDataManager().get(HUE_OFFSET_LEVEL).intValue();
    }


    public void setHueOffsetLevel(int hueOffsetLevel)
    {
        this.getDataManager().set(HUE_OFFSET_LEVEL, Integer.valueOf(hueOffsetLevel));
    }

    public int getDuration()
    {
        return this.duration;
    }

    
    public void setDuration(int durationIn)
    {
        this.duration = durationIn;
    }

    @Override
    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        boolean flag = this.shouldIgnoreRadius();
        float f = this.getRadius();

        if (this.world.isRemote) {
            float offsetLevel = this.getHueOffsetLevel() * 120f;
            float[] colour = SpectriteHelper.getCurrentSpectriteRGBColour(offsetLevel);
            double[] colourRGB = new double[]{colour[0] * 255d, colour[1] * 255d, colour[2] * 255d};
            if (flag) {
                if (this.rand.nextBoolean()) {
                    for (int i = 0; i < 2; ++i) {
                        float f1 = this.rand.nextFloat() * ((float) Math.PI * 2F);
                        float f2 = MathHelper.sqrt(this.rand.nextFloat()) * 0.2F;
                        float f3 = MathHelper.cos(f1) * f2;
                        float f4 = MathHelper.sin(f1) * f2;

                        Spectrite.Proxy.spawnSpectriteSpellParticle(this.world, this.posX + f3, this.posY, this.posZ + f4,
                                colourRGB[0], colourRGB[1], colourRGB[2], offsetLevel);
                    }
                }
            } else {
                float f5 = (float) Math.PI * f * f;

                for (int k1 = 0; k1 < f5; ++k1) {
                    float f6 = this.rand.nextFloat() * ((float) Math.PI * 2F);
                    float f7 = MathHelper.sqrt(this.rand.nextFloat()) * f;
                    float f8 = MathHelper.cos(f6) * f7;
                    float f9 = MathHelper.sin(f6) * f7;

                    Spectrite.Proxy.spawnSpectriteSpellParticle(this.world, this.posX + f8, this.posY, this.posZ + f9,
                            colourRGB[0], colourRGB[1], colourRGB[2], offsetLevel);
                }
            }
        } else {
            if (this.ticksExisted >= this.waitTime + this.duration)
            {
                this.setDead();
                return;
            }

            boolean flag1 = this.ticksExisted < this.waitTime;

            if (flag != flag1)
            {
                this.setIgnoreRadius(flag1);
            }

            if (flag1)
            {
                return;
            }

            if (this.radiusPerTick != 0.0F)
            {
                f += this.radiusPerTick;

                if (f < 0.5F)
                {
                    this.setDead();
                    return;
                }

                this.setRadius(f);
            }

            if (this.ticksExisted % 5 == 0)
            {
                Iterator<Entry<Entity, Integer>> iterator = this.reapplicationDelayMap.entrySet().iterator();

                while (iterator.hasNext())
                {
                    Entry<Entity, Integer> entry = iterator.next();

                    if (this.ticksExisted >= entry.getValue().intValue())
                    {
                        iterator.remove();
                    }
                }

                List<PotionEffect> potions = Lists.<PotionEffect>newArrayList();

                for (PotionEffect potioneffect1 : this.potionType.getEffects())
                {
                    potions.add(new PotionEffect(potioneffect1.getPotion(), potioneffect1.getDuration() / 4, potioneffect1.getAmplifier(), potioneffect1.getIsAmbient(), potioneffect1.doesShowParticles()));
                }

                potions.addAll(this.effects);

                if (potions.isEmpty())
                {
                    this.reapplicationDelayMap.clear();
                }
                else
                {
                    List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox());

                    if (!list.isEmpty())
                    {
                        for (EntityLivingBase entitylivingbase : list)
                        {
                            if (!this.reapplicationDelayMap.containsKey(entitylivingbase) && entitylivingbase.canBeHitWithPotion())
                            {
                                double d0 = entitylivingbase.posX - this.posX;
                                double d1 = entitylivingbase.posZ - this.posZ;
                                double d2 = d0 * d0 + d1 * d1;

                                if (d2 <= f * f)
                                {
                                    this.reapplicationDelayMap.put(entitylivingbase, Integer.valueOf(this.ticksExisted + this.reapplicationDelay));

                                    for (PotionEffect potioneffect : potions)
                                    {
                                        if (potioneffect.getPotion().isInstant())
                                        {
                                            if (potioneffect.getPotion() != ModPotions.SPECTRITE_DAMAGE || this.getOwner() == null || this.getOwner() != entitylivingbase) {
                                                potioneffect.getPotion().affectEntity(this, this.getOwner(), entitylivingbase, potioneffect.getAmplifier(), 0.5D);
                                            }
                                        }
                                        else
                                        {
                                            entitylivingbase.addPotionEffect(new PotionEffect(potioneffect));
                                        }
                                    }

                                    if (this.radiusOnUse != 0.0F)
                                    {
                                        f += this.radiusOnUse;

                                        if (f < 0.5F)
                                        {
                                            this.setDead();
                                            return;
                                        }

                                        this.setRadius(f);
                                    }

                                    if (this.durationOnUse != 0)
                                    {
                                        this.duration += this.durationOnUse;

                                        if (this.duration <= 0)
                                        {
                                            this.setDead();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setRadiusOnUse(float radiusOnUseIn)
    {
        this.radiusOnUse = radiusOnUseIn;
    }

    public void setRadiusPerTick(float radiusPerTickIn)
    {
        this.radiusPerTick = radiusPerTickIn;
    }

    public void setWaitTime(int waitTimeIn)
    {
        this.waitTime = waitTimeIn;
    }

    public void setOwner(@Nullable EntityLivingBase ownerIn)
    {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    @Nullable
    public EntityLivingBase getOwner()
    {
        if (this.owner == null && this.ownerUniqueId != null && this.world instanceof WorldServer)
        {
            Entity entity = ((WorldServer)this.world).getEntityFromUuid(this.ownerUniqueId);

            if (entity instanceof EntityLivingBase)
            {
                this.owner = (EntityLivingBase)entity;
            }
        }

        return this.owner;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.ticksExisted = compound.getInteger("Age");
        this.duration = compound.getInteger("Duration");
        this.waitTime = compound.getInteger("WaitTime");
        this.reapplicationDelay = compound.getInteger("ReapplicationDelay");
        this.durationOnUse = compound.getInteger("DurationOnUse");
        this.setHueOffsetLevel(compound.getInteger("HueOffsetLevel"));
        this.radiusOnUse = compound.getFloat("RadiusOnUse");
        this.radiusPerTick = compound.getFloat("RadiusPerTick");
        this.setRadius(compound.getFloat("Radius"));
        this.ownerUniqueId = compound.getUniqueId("OwnerUUID");

        if (compound.hasKey("Potion", 8))
        {
            this.setPotionType(PotionUtils.getPotionTypeFromNBT(compound));
        }

        if (compound.hasKey("Effects", 9))
        {
            NBTTagList nbttaglist = compound.getTagList("Effects", 10);
            this.effects.clear();

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttaglist.getCompoundTagAt(i));

                if (potioneffect != null)
                {
                    this.addEffect(potioneffect);
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("Age", this.ticksExisted);
        compound.setInteger("Duration", this.duration);
        compound.setInteger("WaitTime", this.waitTime);
        compound.setInteger("ReapplicationDelay", this.reapplicationDelay);
        compound.setInteger("DurationOnUse", this.durationOnUse);
        compound.setInteger("HueOffsetLevel", this.getHueOffsetLevel());
        compound.setFloat("RadiusOnUse", this.radiusOnUse);
        compound.setFloat("RadiusPerTick", this.radiusPerTick);
        compound.setFloat("Radius", this.getRadius());

        if (this.ownerUniqueId != null)
        {
            compound.setUniqueId("OwnerUUID", this.ownerUniqueId);
        }

        if (this.potionType != PotionTypes.EMPTY && this.potionType != null)
        {
            compound.setString("Potion", PotionType.REGISTRY.getNameForObject(this.potionType).toString());
        }

        if (!this.effects.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (PotionEffect potioneffect : this.effects)
            {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            compound.setTag("Effects", nbttaglist);
        }
    }

    @Override
	public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (RADIUS.equals(key))
        {
            this.setRadius(this.getRadius());
        }

        super.notifyDataManagerChange(key);
    }

    @Override
	public EnumPushReaction getPushReaction()
    {
        return EnumPushReaction.IGNORE;
    }
}