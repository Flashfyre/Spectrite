package com.samuel.spectrite.entities;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.init.ModBiomes;
import com.samuel.spectrite.init.ModLootTables;
import com.samuel.spectrite.init.ModPotions;
import com.samuel.spectrite.init.ModSounds;
import com.samuel.spectrite.items.ItemSpectriteShield;
import com.samuel.spectrite.items.ItemSpectriteShieldSpecial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemShield;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

public class EntitySpectriteCreeper extends EntityCreeper implements ISpectriteMob {
	
	private final int explosionRadius = 4;
	private static Field timeSinceIgnitedField = null;
	private static Field fuseTimeField = null;

	public EntitySpectriteCreeper(World worldIn) {
		super(worldIn);
		if (timeSinceIgnitedField == null) {
    		timeSinceIgnitedField = ObfuscationReflectionHelper.findField(EntityCreeper.class, "field_70833_d");
    	}
    	if (fuseTimeField == null) {
    		fuseTimeField = ObfuscationReflectionHelper.findField(EntityCreeper.class, "field_82225_f");
    	}
    	this.experienceValue = 25;
	}
	
	/**
     * Called to update the entity's position/logic.
     */
	@Override
    public void onUpdate()
    {
        if (this.isEntityAlive())
        {
        	int i = this.getCreeperState();
        	
            int timeSinceIgnited = 0;
            int fuseTime = 0;
            
            try {
            	timeSinceIgnited = ((int) timeSinceIgnitedField.get(this)) + i;
            	fuseTime = fuseTimeField.getInt(this);
            } catch (Exception e) {
            	e.printStackTrace();
            }

            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }

            if (timeSinceIgnited >= fuseTime)
            {
                try {
					timeSinceIgnitedField.set(this, fuseTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
                this.explode();
            }
        }

        super.onUpdate();
    }
	
	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
    }

    @Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_)
	{
		return ModSounds.spectrite_creeper_hurt;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return ModSounds.spectrite_creeper_death;
	}
	
	@Override
	@Nullable
    protected ResourceLocation getLootTable()
    {
		return ModLootTables.spectrite_creeper;
    }

	/**
     * Creates an explosion as determined by this creeper's power and explosion radius.
     */
    private void explode()
    {
        if (!this.world.isRemote)
        {
            boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
            float f = this.getPowered() ? 2.0F : 1.0F;
            this.doSpectriteDamage();
            this.dead = true;
			Spectrite.Proxy.newSpectriteExplosion(this.world,this, this.posX, this.posY, this.posZ,
				this.explosionRadius * f, false, flag, false);
            this.setDead();
            this.spawnLingeringCloud();
        }
    }
    
    private void doSpectriteDamage() {
    	int power = 3 + (this.getPowered() ? 1 : 0);
    	BlockPos pos = this.getPosition();
    	List<Entity> surrounding = world.getEntitiesWithinAABBExcludingEntity(this,
			new AxisAlignedBB(pos.north(power).west(power).down(power),
			this.getPosition().south(power).east(power).up(power)));

		switch (power) {
			case 4:
				world.playSound(null, pos, ModSounds.explosion, SoundCategory.PLAYERS, 4.75F,
					0.75F + (world.rand.nextFloat()) * 0.4F);
				
				world.playSound(null, pos, ModSounds.fatality, SoundCategory.PLAYERS, 5.0F,
					0.75F);
				break;
			default:
				world.playSound(null, pos, ModSounds.explosion, SoundCategory.PLAYERS, 3.75F,
					0.75F + (world.rand.nextFloat()) * 0.4F);
				
				world.playSound(null, pos, ModSounds.fatality, SoundCategory.PLAYERS, 4.0F,
					1.0F);
		}
		
		for (int s = 0; s < surrounding.size(); s++) {
			if (surrounding.get(s) instanceof EntityLivingBase &&
				(!(surrounding.get(s)).isOnSameTeam(this))) {
				EntityLivingBase curEntity = ((EntityLivingBase) surrounding.get(s));
				double distance = curEntity.getDistance(this);
				int relPower = (int) Math.ceil(power - distance);
				int damageLevel = relPower;
				if (!curEntity.getActiveItemStack().isEmpty() && curEntity.isActiveItemStackBlocking() && curEntity.getActiveItemStack().getItem() instanceof ItemShield) {
					damageLevel = Math.max(relPower - (curEntity.getActiveItemStack().getItem() instanceof ItemSpectriteShield ?
						curEntity.getActiveItemStack().getItem() instanceof ItemSpectriteShieldSpecial ? 2 : 1 : 0), 0);
					relPower = Math.max(relPower - (relPower - (damageLevel - 1)), 0);
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
						case 4:
							shieldDamage = 512;
							break;
						case 5:
							shieldDamage = 2048;
							break;
					}
					if (shieldDamage > 0) {
						curEntity.getActiveItemStack().damageItem(shieldDamage, curEntity);
					}
				}
				if (relPower >= 0) {
					curEntity.addPotionEffect(new PotionEffect(ModPotions.SPECTRITE_DAMAGE, 5, relPower));
				}
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
	
	@Override
	public boolean isOnSameTeam(Entity entityIn) {
		return (entityIn instanceof ISpectriteMob && (entityIn.getClass() != EntitySpectriteGolem.class
			|| !((EntitySpectriteGolem) entityIn).isPlayerCreated())) || super.isOnSameTeam(entityIn);
	}

    private void spawnLingeringCloud()
    {
        EntitySpectriteAreaEffectCloud entitySpectriteAreaEffectCloud = new EntitySpectriteAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
        entitySpectriteAreaEffectCloud.setRadius((this.explosionRadius - 0.5f) + (this.getPowered() ? 1f : 0f));
        entitySpectriteAreaEffectCloud.setRadiusOnUse(-0.5F);
        entitySpectriteAreaEffectCloud.setWaitTime(10);
        entitySpectriteAreaEffectCloud.setDuration(entitySpectriteAreaEffectCloud.getDuration() / 2);
        entitySpectriteAreaEffectCloud.setRadiusPerTick(-entitySpectriteAreaEffectCloud.getRadius() / entitySpectriteAreaEffectCloud.getDuration());

        entitySpectriteAreaEffectCloud.setPotionType(this.getPowered() ? ModPotions.SPECTRITE_DAMAGE_IV : ModPotions.SPECTRITE_DAMAGE_V);
        entitySpectriteAreaEffectCloud.addEffect(new PotionEffect(ModPotions.SPECTRITE_DAMAGE, 25, this.getPowered() ? 4 : 3));
        
        this.world.spawnEntity(entitySpectriteAreaEffectCloud);
    }
}
