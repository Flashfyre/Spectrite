package com.samuel.spectrite.entities;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModEnchantments;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModPotions;
import com.samuel.spectrite.items.IPerfectSpectriteItem;
import com.samuel.spectrite.items.ItemSpectriteArmor;
import com.samuel.spectrite.items.ItemSpectriteBow;
import com.samuel.spectrite.packets.PacketSyncSpectriteBoss;
import com.samuel.spectrite.potions.PotionEffectSpectrite;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntitySpectriteSkeleton extends EntitySkeleton implements ISpectriteMob, ISpectriteBipedMob {
	
	protected boolean boss;
	protected boolean hasSpectriteResistance;
	protected int chanceMultiplier;
	protected final EntityAIAttackRangedBow aiArrowAttack = new EntityAIAttackRangedBow(this, 1.0D, 20, 15.0F) {
		@Override
		protected boolean isBowInMainhand()
	    {
			Field entity = SpectriteHelper.findObfuscatedField(EntityAIAttackRangedBow.class, new String[] { "entity", "field_188499_a" });
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
	private BossInfoServer bossInfo;
	
	public EntitySpectriteSkeleton(World worldIn) {
		super(worldIn);
		
		setBoss(!this.world.isRemote && SpectriteConfig.spectriteMobBossSpawnRate > 0d
			&& (int) getUniqueID().getMostSignificantBits() % (100 / SpectriteConfig.spectriteMobBossSpawnRate) == 0);
		
		setHasSpectriteResistance(isArmorFullEnhanced());
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
		Item spectriteBow;
		boolean hasPerfectWeapon = this.boss || SpectriteConfig.spectriteMobPerfectWeaponRate > 0d
			&& rand.nextInt((int) (100 / SpectriteConfig.spectriteMobPerfectWeaponRate)) < chanceMultiplier;
		Map<Enchantment, Integer> enchantmentMap = new HashMap<Enchantment, Integer>();
		enchantmentMap.put(ModEnchantments.spectrite_enhance, 1);
		
		spectriteBow = !hasPerfectWeapon ? ModItems.spectrite_bow : ModItems.spectrite_bow_special;
		
		ItemStack bowStack = new ItemStack(spectriteBow);
		
		if ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32 / chanceMultiplier) == 0) 
			|| (this.world.getDifficulty() == EnumDifficulty.HARD && (this.boss || rand.nextInt(8) == 0))) {
			EnchantmentHelper.setEnchantments(enchantmentMap, bowStack);
		}
		
	    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, bowStack);
	    this.setDropChance(EntityEquipmentSlot.MAINHAND, new Double(SpectriteConfig.spectriteMobBowDropRate).floatValue() / (100f / chanceMultiplier));
		
		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.spectrite_arrow, rand.nextInt(4) + 1));
		this.setDropChance(EntityEquipmentSlot.OFFHAND, new Double(SpectriteConfig.spectriteMobArrowDropRate).floatValue() / (100f / chanceMultiplier));
		
		int enhancedCount = 0;
		double maxHealth = 50.0D;
		
		if (this.boss || rand.nextInt(4) == 0) {
			ItemSpectriteArmor helmetItem = ModItems.spectrite_helmet;
			ItemStack helmetStack = new ItemStack(helmetItem);
			boolean enhanced = false;
			if ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32 / chanceMultiplier) == 0) 
				|| (this.world.getDifficulty() == EnumDifficulty.HARD && (this.boss || rand.nextInt(8) == 0))) {
				EnchantmentHelper.setEnchantments(enchantmentMap, helmetStack);
				enhanced = true;
				enhancedCount++;
			}
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, helmetStack);
			this.setDropChance(EntityEquipmentSlot.HEAD, new Double(SpectriteConfig.spectriteMobArmourDropRate).floatValue() / (100f / chanceMultiplier));
			maxHealth += helmetItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
		}
		if (this.boss || rand.nextInt(4) == 0) {
			ItemSpectriteArmor chestplateItem = ModItems.spectrite_chestplate;
			ItemStack chestplateStack = new ItemStack(chestplateItem);
			boolean enhanced = false;
			if ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32 / chanceMultiplier) == 0) 
				|| (this.world.getDifficulty() == EnumDifficulty.HARD && (this.boss || rand.nextInt(8) == 0))) {
				EnchantmentHelper.setEnchantments(enchantmentMap, chestplateStack);
				enhanced = true;
				enhancedCount++;
			}
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, chestplateStack);
			this.setDropChance(EntityEquipmentSlot.CHEST, new Double(SpectriteConfig.spectriteMobArmourDropRate).floatValue() / (100f / chanceMultiplier));
			maxHealth += chestplateItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
		}
		if (this.boss || rand.nextInt(4) == 0) {
			ItemSpectriteArmor leggingsItem = ModItems.spectrite_leggings;
			ItemStack leggingsStack = new ItemStack(leggingsItem);
			boolean enhanced = false;
			if ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32 / chanceMultiplier) == 0) 
				|| (this.world.getDifficulty() == EnumDifficulty.HARD && (this.boss || rand.nextInt(8) == 0))) {
				EnchantmentHelper.setEnchantments(enchantmentMap, leggingsStack);
				enhanced = true;
				enhancedCount++;
			}
			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, leggingsStack);
			this.setDropChance(EntityEquipmentSlot.LEGS, new Double(SpectriteConfig.spectriteMobArmourDropRate).floatValue() / (100f / chanceMultiplier));
			maxHealth += leggingsItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
		}
		if (this.boss || rand.nextInt(4) == 0) {
			ItemSpectriteArmor bootsItem = ModItems.spectrite_boots;
			ItemStack bootsStack = new ItemStack(bootsItem);
			boolean enhanced = false;
			if ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32 / chanceMultiplier) == 0) 
				|| (this.world.getDifficulty() == EnumDifficulty.HARD && (this.boss || rand.nextInt(8) == 0))) {
				EnchantmentHelper.setEnchantments(enchantmentMap, bootsStack);
				enhanced = true;
				enhancedCount++;
			}
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, bootsStack);
			this.setDropChance(EntityEquipmentSlot.FEET, new Double(SpectriteConfig.spectriteMobArmourDropRate).floatValue() / (100f / chanceMultiplier));
			maxHealth += bootsItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
		}
		
		if (enhancedCount == 4) {
			setHasSpectriteResistance(true);
		}
		
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
		this.setHealth(new Double(maxHealth).floatValue());
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
	protected EntityArrow getArrow(float p_190726_1_)
    {
        ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

        if (itemstack.getItem() == ModItems.spectrite_arrow)
        {
        	boolean enhanced = false;
			if (this.getActiveItemStack().getItem() instanceof IPerfectSpectriteItem) {
				ItemStack bowItemStack = this.getActiveItemStack();
				NBTTagList enchantmentTags = bowItemStack.getEnchantmentTagList();
				for (int ec = 0; ec < enchantmentTags.tagCount(); ec++) {
					if (enchantmentTags.get(ec).getId() == Enchantment.getEnchantmentID(ModEnchantments.spectrite_enhance)) {
						enhanced = true;
						break;
					}
				}
			}
            EntitySpectriteArrow entityspectritearrow = new EntitySpectriteArrow(this.world, this, enhanced);
            entityspectritearrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);
            return entityspectritearrow;
        }
        else if (itemstack.getItem() == Items.SPECTRAL_ARROW)
        {
            EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(this.world, this);
            entityspectralarrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);
            return entityspectralarrow;
        }
        else
        {
            EntityArrow entityarrow = super.getArrow(p_190726_1_);

            if (itemstack.getItem() == Items.TIPPED_ARROW && entityarrow instanceof EntityTippedArrow)
            {
                ((EntityTippedArrow)entityarrow).setPotionEffect(itemstack);
            }

            return entityarrow;
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
			
			if (this.getActivePotionEffect(ModPotions.SPECTRITE_RESISTANCE) == null) {
				this.addPotionEffect(new PotionEffectSpectrite(ModPotions.SPECTRITE_RESISTANCE, 16, this.hasSpectriteResistance ? 1 : 0, true, true));
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

        if (this.hasCustomName())
        {
            this.bossInfo.setName(this.getDisplayName());
        }
        
        setBoss(!this.world.isRemote && SpectriteConfig.spectriteMobBossSpawnRate > 0d
			&& (int) getUniqueID().getMostSignificantBits() % (100 / SpectriteConfig.spectriteMobBossSpawnRate) == 0);
        
        setHasSpectriteResistance(isArmorFullEnhanced());
    }
	
	@Override
	/**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
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
}