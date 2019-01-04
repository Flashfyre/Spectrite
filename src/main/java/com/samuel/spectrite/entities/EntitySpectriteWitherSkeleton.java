package com.samuel.spectrite.entities;

import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.init.ModEnchantments;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModLootTables;
import com.samuel.spectrite.items.IPerfectSpectriteItem;
import com.samuel.spectrite.items.ItemSpectriteArmor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

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
		boolean hasPerfectWeapon = (equipmentSet == null && SpectriteConfig.mobs.spectriteMobPerfectWeaponRate > 0d
			&& rand.nextInt((int) (100 / SpectriteConfig.mobs.spectriteMobPerfectWeaponRate)) < 1f)
			|| (equipmentSet != null && (equipmentSet & EQUIPMENT_SPECIAL_WEAPON) > 0);
		Map<Enchantment, Integer> enchantmentMap = new HashMap<Enchantment, Integer>();
		enchantmentMap.put(ModEnchantments.spectrite_enhance, 1);

		int exp = 20;

		boolean hasSword = (equipmentSet == null && SpectriteConfig.mobs.spectriteWitherSkeletonBowRate == 0d
			|| rand.nextFloat() * 100f >= SpectriteConfig.mobs.spectriteWitherSkeletonBowRate)
			|| (equipmentSet != null && (equipmentSet & EQUIPMENT_BOW) == 0);

		boolean hasLegendBlade = hasSword && hasPerfectWeapon && (SpectriteConfig.mobs.spectriteMobLegendSwordRate > 0d
			&& rand.nextInt((int) (100f / SpectriteConfig.mobs.spectriteMobLegendSwordRate)) < 1f);

		if (!hasPerfectWeapon) {
			spectriteWeapon = hasSword ? ModItems.spectrite_sword : ModItems.spectrite_bow;
		} else {
			spectriteWeapon = !hasLegendBlade ? hasSword ? ModItems.spectrite_sword_special : ModItems.spectrite_bow_special : ModItems.spectrite_sword_2;
			exp += hasLegendBlade ? 12 : 5;
		}

		ItemStack weaponStack = new ItemStack(spectriteWeapon);

		if (hasPerfectWeapon && ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32) == 0)
				|| (this.world.getDifficulty() == EnumDifficulty.HARD && (rand.nextInt(8) == 0)))) {
			EnchantmentHelper.setEnchantments(enchantmentMap, weaponStack);
			exp += hasLegendBlade ? 24 : 10;
		}

		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponStack);
		this.setDropChance(EntityEquipmentSlot.MAINHAND, new Double(hasSword ? SpectriteConfig.mobs.spectriteMobSwordDropRate : SpectriteConfig.mobs.spectriteMobBowDropRate).floatValue() / 100f);

		if (!hasSword) {
			this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(hasPerfectWeapon ? ModItems.spectrite_arrow_special : ModItems.spectrite_arrow, 1));
			this.setDropChance(EntityEquipmentSlot.OFFHAND, new Double(SpectriteConfig.mobs.spectriteMobArrowDropRate).floatValue() / 100f);
		}

		int armourCount = 0;
		int enhancedCount = 0;
		double maxHealth = 50.0D;

		if ((equipmentSet == null && rand.nextInt(4) == 0) || (equipmentSet != null && (equipmentSet & EQUIPMENT_HELMET) > 0)) {
			ItemSpectriteArmor helmetItem = ModItems.spectrite_helmet;
			ItemStack helmetStack = new ItemStack(helmetItem);
			boolean enhanced = false;
			if ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32) == 0)
					|| (this.world.getDifficulty() == EnumDifficulty.HARD && (rand.nextInt(8) == 0))) {
				EnchantmentHelper.setEnchantments(enchantmentMap, helmetStack);
				enhanced = true;
				enhancedCount++;
			}
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, helmetStack);
			this.setDropChance(EntityEquipmentSlot.HEAD, new Double(SpectriteConfig.mobs.spectriteMobArmourDropRate).floatValue() / 100f);
			float healthIncrease = helmetItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
			maxHealth += healthIncrease;
			exp += ((int) healthIncrease >> 1);
			armourCount++;
		}
		if ((equipmentSet == null && rand.nextInt(4) == 0) || (equipmentSet != null && (equipmentSet & EQUIPMENT_CHESTPLATE) > 0)) {
			ItemSpectriteArmor chestplateItem = ModItems.spectrite_chestplate;
			ItemStack chestplateStack = new ItemStack(chestplateItem);
			boolean enhanced = false;
			if ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32) == 0)
					|| (this.world.getDifficulty() == EnumDifficulty.HARD && (rand.nextInt(8) == 0))) {
				EnchantmentHelper.setEnchantments(enchantmentMap, chestplateStack);
				enhanced = true;
				enhancedCount++;
			}
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, chestplateStack);
			this.setDropChance(EntityEquipmentSlot.CHEST, new Double(SpectriteConfig.mobs.spectriteMobArmourDropRate).floatValue() / 100f);
			float healthIncrease = chestplateItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
			maxHealth += healthIncrease;
			exp += ((int) healthIncrease >> 1);
			armourCount++;
		}
		if ((equipmentSet == null && rand.nextInt(4) == 0) || (equipmentSet != null && (equipmentSet & EQUIPMENT_LEGGINGS) > 0)) {
			ItemSpectriteArmor leggingsItem = ModItems.spectrite_leggings;
			ItemStack leggingsStack = new ItemStack(leggingsItem);
			boolean enhanced = false;
			if ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32) == 0)
					|| (this.world.getDifficulty() == EnumDifficulty.HARD && (rand.nextInt(8) == 0))) {
				EnchantmentHelper.setEnchantments(enchantmentMap, leggingsStack);
				enhanced = true;
				enhancedCount++;
			}
			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, leggingsStack);
			this.setDropChance(EntityEquipmentSlot.LEGS, new Double(SpectriteConfig.mobs.spectriteMobArmourDropRate).floatValue() / 100f);
			float healthIncrease = leggingsItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
			maxHealth += healthIncrease;
			exp += ((int) healthIncrease >> 1);
			armourCount++;
		}
		if ((equipmentSet == null && rand.nextInt(4) == 0) || (equipmentSet != null && (equipmentSet & EQUIPMENT_BOOTS) > 0)) {
			ItemSpectriteArmor bootsItem = ModItems.spectrite_boots;
			ItemStack bootsStack = new ItemStack(bootsItem);
			boolean enhanced = false;
			if ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32) == 0)
					|| (this.world.getDifficulty() == EnumDifficulty.HARD && (rand.nextInt(8) == 0))) {
				EnchantmentHelper.setEnchantments(enchantmentMap, bootsStack);
				enhanced = true;
				enhancedCount++;
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

		this.experienceValue = exp;
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
		this.setHealth(new Double(maxHealth).floatValue());
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

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);

		if (cause.getTrueSource() instanceof EntityCreeper) {
			EntityCreeper entitycreeper = (EntityCreeper) cause.getTrueSource();

			if (entitycreeper.getPowered() && entitycreeper.ableToCauseSkullDrop()) {
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
				NBTTagList enchantmentTags = itemstack.getEnchantmentTagList();
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
