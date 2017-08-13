package com.samuel.spectrite.entities;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Nullable;

import com.samuel.spectrite.etc.SpectriteHelper;
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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntitySpectriteCreeper extends EntityCreeper implements ISpectriteMob {
	
	private final int explosionRadius = 4;
	private static Field timeSinceIgnitedField = null;
	private static Field fuseTimeField = null;

	public EntitySpectriteCreeper(World worldIn) {
		super(worldIn);
		if (timeSinceIgnitedField == null) {
    		timeSinceIgnitedField = SpectriteHelper.findObfuscatedField(EntityCreeper.class, new String[] { "timeSinceIgnited", "field_70833_d" });
    	}
    	if (fuseTimeField == null) {
    		fuseTimeField = SpectriteHelper.findObfuscatedField(EntityCreeper.class, new String[] { "fuseTime", "field_82225_f" });
    	}
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
	public void onLivingUpdate()
    {
        super.onLivingUpdate();
        
        if (!this.world.isRemote) {
	        if (this.getActivePotionEffect(ModPotions.SPECTRITE_RESISTANCE) == null) {
				this.addPotionEffect(new PotionEffect(ModPotions.SPECTRITE_RESISTANCE, 16, 0, true, true));
			}
        }
    }
	
	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
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
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius * f, flag);
            this.setDead();
            this.spawnLingeringCloud();
        }
    }
    
    private void doSpectriteDamage() {
    	int power = 4 + (this.getPowered() ? 1 : 0);
    	BlockPos pos = this.getPosition();
    	List<Entity> surrounding = world.getEntitiesWithinAABBExcludingEntity(this,
			new AxisAlignedBB(pos.north(power).west(power).down(power),
			this.getPosition().south(power).east(power).up(power)));
	
		EnumParticleTypes particle = EnumParticleTypes.EXPLOSION_HUGE;
		switch (power) {
			case 5:
				world.playSound(null, pos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F,
						0.75F + (world.rand.nextFloat()) * 0.4F);
				
				world.playSound(null, pos, ModSounds.fatality, SoundCategory.PLAYERS, 1.0F,
					1.0F);
				break;
			default:
				world.playSound(null, pos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F,
						0.75F + (world.rand.nextFloat()) * 0.4F);
				
				world.playSound(null, pos, ModSounds.fatality, SoundCategory.PLAYERS, 1.0F,
					1.0F);
		}
		
		((WorldServer) this.world).spawnParticle(particle,
			particle.getShouldIgnoreRange(), pos.getX(),
			pos.getY(), pos.getZ(), power == 4 ? 3 : 7,
			world.rand.nextGaussian() * (power - 3), world.rand.nextGaussian() * (power - 3),
			world.rand.nextGaussian() * (power - 3), 0.0D, new int[0]);
		
		for (int s = 0; s < surrounding.size(); s++) {
			if (surrounding.get(s) instanceof EntityLivingBase &&
				(!((EntityLivingBase) surrounding.get(s)).isOnSameTeam(this))) {
				EntityLivingBase curEntity = ((EntityLivingBase) surrounding.get(s));
				double distance = curEntity.getDistanceToEntity(this);
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
        EntitySpectriteAreaEffectCloud entityareaeffectcloud = new EntitySpectriteAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
        entityareaeffectcloud.setRadius((this.explosionRadius - 0.5f) + (this.getPowered() ? 1f : 0f));
        entityareaeffectcloud.setRadiusOnUse(-0.5F);
        entityareaeffectcloud.setWaitTime(10);
        entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
        entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());

        entityareaeffectcloud.setPotionType(this.getPowered() ? ModPotions.SPECTRITE_DAMAGE_IV : ModPotions.SPECTRITE_DAMAGE_V);
        entityareaeffectcloud.addEffect(new PotionEffect(ModPotions.SPECTRITE_DAMAGE, 25, this.getPowered() ? 4 : 3));
        
        this.world.spawnEntity(entityareaeffectcloud);
    }
}
