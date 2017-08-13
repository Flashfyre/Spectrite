package com.samuel.spectrite.entities;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModEnchantments;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModLootTables;
import com.samuel.spectrite.init.ModPotions;
import com.samuel.spectrite.init.ModWorldGen;
import com.samuel.spectrite.items.ItemSpectriteArmor;
import com.samuel.spectrite.packets.PacketSyncSpectriteBoss;
import com.samuel.spectrite.potions.PotionEffectSpectrite;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntitySpectriteWitherSkeleton extends EntityWitherSkeleton implements ISpectriteMob, ISpectriteBipedMob {

	protected boolean boss;
	protected boolean hasSpectriteResistance;
	protected int chanceMultiplier;
	private BossInfoServer bossInfo;
	
	public EntitySpectriteWitherSkeleton(World worldIn) {
		super(worldIn);
		
		setBoss(!this.world.isRemote && SpectriteConfig.spectriteMobBossSpawnRate > 0d
			&& (int) getUniqueID().getMostSignificantBits() % (100 / SpectriteConfig.spectriteMobBossSpawnRate) == 0);
		
		setHasSpectriteResistance(isArmorFullEnhanced());
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
		Item spectriteSword;
		boolean hasPerfectWeapon = this.boss || SpectriteConfig.spectriteMobPerfectWeaponRate > 0d
			&& rand.nextInt((int) (100 / SpectriteConfig.spectriteMobPerfectWeaponRate)) < chanceMultiplier;
		Map<Enchantment, Integer> enchantmentMap = new HashMap<Enchantment, Integer>();
		enchantmentMap.put(ModEnchantments.spectrite_enhance, 1);
		
		if (!hasPerfectWeapon) {
			spectriteSword = ModItems.spectrite_sword;
		} else {
			boolean hasLegendBlade =  this.boss || SpectriteConfig.spectriteMobLegendSwordRate > 0d
					&& rand.nextInt((int) (100 / SpectriteConfig.spectriteMobLegendSwordRate)) < chanceMultiplier;
			spectriteSword = !hasLegendBlade ? ModItems.spectrite_sword_special : ModItems.spectrite_sword_2;
		}
		
		ItemStack swordStack = new ItemStack(spectriteSword);
		
		if ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32 / chanceMultiplier) == 0) 
			|| (this.world.getDifficulty() == EnumDifficulty.HARD && (this.boss || rand.nextInt(8) == 0))) {
			EnchantmentHelper.setEnchantments(enchantmentMap, swordStack);
		}
		
	    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, swordStack);
		this.setDropChance(EntityEquipmentSlot.OFFHAND, new Double(SpectriteConfig.spectriteMobSwordDropRate).floatValue() / (100f / chanceMultiplier));
		
		if (this.boss) {
			this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(ModItems.spectrite_orb));
			this.setDropChance(EntityEquipmentSlot.OFFHAND, new Double(SpectriteConfig.spectriteMobOrbDropRate).floatValue() / (100f / chanceMultiplier));
		}
		
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
	
	@Override
	@Nullable
    protected ResourceLocation getLootTable()
    {
		return ModLootTables.spectrite_skeleton;
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
        
        ItemStack helmetStack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		ItemStack chestplateStack = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		ItemStack leggingsStack = this.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		ItemStack bootsStack = this.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        
		setHasSpectriteResistance(isArmorFullEnhanced());
    }
	
	@Override
	/**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
		BlockPos pos = new BlockPos(this);
		
		if (SpectriteConfig.generateSpectriteSkull && ModWorldGen.spectriteSkull.isPosInSkullBounds(pos, this.world.provider.getDimension()))
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
