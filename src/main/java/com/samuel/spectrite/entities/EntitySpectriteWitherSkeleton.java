package com.samuel.spectrite.entities;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.init.ModEnchantments;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModLootTables;
import com.samuel.spectrite.items.IPerfectSpectriteItem;
import com.samuel.spectrite.items.ItemSpectriteArmor;
import com.samuel.spectrite.items.ItemSpectriteOrb;
import com.samuel.spectrite.packets.PacketSyncSpectriteBoss;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EntitySpectriteWitherSkeleton extends AbstractSpectriteSkeleton {

	public EntitySpectriteWitherSkeleton(World worldIn) {
		super(worldIn);

		if (SpectriteConfig.mobs.spectriteWitherSkeletonUseSkeletonHeight) {
			this.setSize(0.6F, 1.99F);
		} else {
			this.setSize(0.7F, 2.4F);
		}

		this.isImmuneToFire = true;
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		IEntityLivingData ientitylivingdata = super.onInitialSpawn(difficulty, livingdata);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		this.setCombatTask();
		return ientitylivingdata;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		Item spectriteWeapon;
		boolean hasPerfectWeapon = this.boss || SpectriteConfig.mobs.spectriteMobPerfectWeaponRate > 0d
			&& rand.nextInt((int) (100 / SpectriteConfig.mobs.spectriteMobPerfectWeaponRate)) < 1f;
		Map<Enchantment, Integer> enchantmentMap = new HashMap<Enchantment, Integer>();
		enchantmentMap.put(ModEnchantments.spectrite_enhance, 1);

		int exp = 20;

		boolean hasSword = this.boss || SpectriteConfig.mobs.spectriteWitherSkeletonBowRate == 0d ||
			rand.nextFloat() * 100f >= SpectriteConfig.mobs.spectriteWitherSkeletonBowRate;

		boolean hasLegendBlade = hasSword && hasPerfectWeapon && (this.boss || SpectriteConfig.mobs.spectriteMobLegendSwordRate > 0d
			&& rand.nextInt((int) (100f / SpectriteConfig.mobs.spectriteMobLegendSwordRate)) < 1f);

		if (!hasPerfectWeapon) {
			spectriteWeapon = hasSword ? ModItems.spectrite_sword : ModItems.spectrite_bow;
		} else {
			spectriteWeapon = !hasLegendBlade ? hasSword ? ModItems.spectrite_sword_special : ModItems.spectrite_bow_special : ModItems.spectrite_sword_2;
			exp += hasLegendBlade ? 12 : 5;
		}

		ItemStack weaponStack = new ItemStack(spectriteWeapon);

		if (hasPerfectWeapon && ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32 / chanceMultiplier) == 0)
				|| (this.world.getDifficulty() == EnumDifficulty.HARD && (this.boss || rand.nextInt(8) == 0)))) {
			EnchantmentHelper.setEnchantments(enchantmentMap, weaponStack);
			exp += hasLegendBlade ? 24 : 10;
		}

		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponStack);
		this.setDropChance(EntityEquipmentSlot.MAINHAND, new Double(hasSword ? SpectriteConfig.mobs.spectriteMobSwordDropRate : SpectriteConfig.mobs.spectriteMobBowDropRate).floatValue() / 100f);

		if (!hasSword) {
			this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(hasPerfectWeapon ? ModItems.spectrite_arrow_special : ModItems.spectrite_arrow, 1));
			this.setDropChance(EntityEquipmentSlot.OFFHAND, new Double(SpectriteConfig.mobs.spectriteMobArrowDropRate).floatValue() / 100f);
		}

		EntityEquipmentSlot[] orbEquipmentSlots = ItemSpectriteOrb.ORB_EQUIPMENT_SLOTS;
		int armourCount = 0;
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
			if (this.boss) {
				helmetStack.setTagCompound(new NBTTagCompound());
				helmetItem.updateItemStackNBT(helmetStack.getTagCompound());
				for (int o = 0; o < orbEquipmentSlots.length; o++) {
					if (helmetItem.armorType == orbEquipmentSlots[o] && ((this.world.getDifficulty() == EnumDifficulty.NORMAL
							&& rand.nextInt(3) == 0) || this.world.getDifficulty() == EnumDifficulty.HARD)) {
						helmetStack.getSubCompound("OrbEffects").setBoolean(new Integer(o).toString(), Boolean.TRUE);
					}
				}
			}
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, helmetStack);
			this.setDropChance(EntityEquipmentSlot.HEAD, new Double(SpectriteConfig.mobs.spectriteMobArmourDropRate).floatValue() / 100f);
			float healthIncrease = helmetItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
			maxHealth += healthIncrease;
			exp += ((int) healthIncrease >> 1);
			armourCount++;
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
			if (this.boss) {
				chestplateStack.setTagCompound(new NBTTagCompound());
				chestplateItem.updateItemStackNBT(chestplateStack.getTagCompound());
				for (int o = 0; o < orbEquipmentSlots.length; o++) {
					if (chestplateItem.armorType == orbEquipmentSlots[o] && ((this.world.getDifficulty() == EnumDifficulty.NORMAL
						&& rand.nextInt(3) == 0) || this.world.getDifficulty() == EnumDifficulty.HARD)) {
						chestplateStack.getSubCompound("OrbEffects").setBoolean(new Integer(o).toString(), Boolean.TRUE);
					}
				}
			}
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, chestplateStack);
			this.setDropChance(EntityEquipmentSlot.CHEST, new Double(SpectriteConfig.mobs.spectriteMobArmourDropRate).floatValue() / 100f);
			float healthIncrease = chestplateItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
			maxHealth += healthIncrease;
			exp += ((int) healthIncrease >> 1);
			armourCount++;
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
			if (this.boss) {
				leggingsStack.setTagCompound(new NBTTagCompound());
				leggingsItem.updateItemStackNBT(leggingsStack.getTagCompound());
				for (int o = 0; o < orbEquipmentSlots.length; o++) {
					if (leggingsItem.armorType == orbEquipmentSlots[o] && ((this.world.getDifficulty() == EnumDifficulty.NORMAL
							&& rand.nextInt(3) == 0) || this.world.getDifficulty() == EnumDifficulty.HARD)) {
						leggingsStack.getSubCompound("OrbEffects").setBoolean(new Integer(o).toString(), Boolean.TRUE);
					}
				}
			}
			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, leggingsStack);
			this.setDropChance(EntityEquipmentSlot.LEGS, new Double(SpectriteConfig.mobs.spectriteMobArmourDropRate).floatValue() / 100f);
			float healthIncrease = leggingsItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
			maxHealth += healthIncrease;
			exp += ((int) healthIncrease >> 1);
			armourCount++;
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
			if (this.boss) {
				bootsStack.setTagCompound(new NBTTagCompound());
				bootsItem.updateItemStackNBT(bootsStack.getTagCompound());
				for (int o = 0; o < orbEquipmentSlots.length; o++) {
					if (bootsItem.armorType == orbEquipmentSlots[o] && ((this.world.getDifficulty() == EnumDifficulty.NORMAL
							&& rand.nextInt(3) == 0) || this.world.getDifficulty() == EnumDifficulty.HARD)) {
						bootsStack.getSubCompound("OrbEffects").setBoolean(new Integer(o).toString(), Boolean.TRUE);
					}
				}
			}
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, bootsStack);
			this.setDropChance(EntityEquipmentSlot.FEET, new Double(SpectriteConfig.mobs.spectriteMobArmourDropRate).floatValue() / 100f);
			float healthIncrease = bootsItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
			maxHealth += healthIncrease;
			exp += ((int) healthIncrease >> 1);
			armourCount++;
		}

		if (armourCount == 4) {
			exp += 5;
			if (enhancedCount == 4) {
				exp += 10;
				setHasSpectriteResistance(true);
			}
		}

		if (this.boss) {
			exp *= 2.5F;
		}

		this.experienceValue = exp;
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
	public boolean isNonBoss() {
		return !boss;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (!this.world.isRemote) {
			if (this.boss) {
				this.bossInfo.setName(new TextComponentString(SpectriteHelper.getMultiColouredString(this.getDisplayName().getUnformattedText(), true)));
				this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
			}
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return SoundEvents.ENTITY_WITHER_SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WITHER_SKELETON_DEATH;
	}

	@Override
	protected SoundEvent getStepSound() {
		return SoundEvents.ENTITY_WITHER_SKELETON_STEP;
	}

	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return ModLootTables.spectrite_wither_skeleton;
	}

	/**
	 * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
	 * order to view its associated boss bar.
	 */
	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);

		if (this.boss) {
			this.bossInfo.addPlayer(player);
			Spectrite.Network.sendTo(new PacketSyncSpectriteBoss(getUniqueID(), true), player);
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);

		if (cause.getTrueSource() instanceof EntityCreeper) {
			EntityCreeper entitycreeper = (EntityCreeper) cause.getTrueSource();

			if (entitycreeper.getPowered() && entitycreeper.isAIEnabled()) {
				entitycreeper.incrementDroppedSkulls();
				this.entityDropItem(new ItemStack(ModItems.spectrite_wither_skeleton_skull), 0.0F);
			}
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (!super.attackEntityAsMob(entityIn)) {
			return false;
		} else {
			if (entityIn instanceof EntityLivingBase) {
				((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.WITHER, 200));
			}

			return true;
		}
	}

	@Override
	protected EntityArrow getArrow(float p_190726_1_) {
		EntityArrow ret;
		ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

		if (itemstack.getItem() == ModItems.spectrite_arrow) {
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
			EntitySpectriteArrow entityspectritearrow = new EntitySpectriteArrow(this.world, this,
				this.getHeldItemOffhand().getItem() == ModItems.spectrite_arrow_special, enhanced);
			entityspectritearrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);
			ret = entityspectritearrow;
		} else if (itemstack.getItem() == Items.SPECTRAL_ARROW) {
			EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(this.world, this);
			entityspectralarrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);
			ret = entityspectralarrow;
		} else {
			EntityArrow entityarrow = super.getArrow(p_190726_1_);

			if (itemstack.getItem() == Items.TIPPED_ARROW && entityarrow instanceof EntityTippedArrow) {
				((EntityTippedArrow) entityarrow).setPotionEffect(itemstack);
			}

			ret = entityarrow;
		}

		ret.setFire(100);

		return ret;
	}

    @Override
	public float getEyeHeight()
	{
		return SpectriteConfig.mobs.spectriteWitherSkeletonUseSkeletonHeight ? 1.74F : 2.1F;
	}
}
