package com.samuel.spectrite.entities;

import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.init.ModEnchantments;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModLootTables;
import com.samuel.spectrite.init.ModSounds;
import com.samuel.spectrite.items.IPerfectSpectriteItem;
import com.samuel.spectrite.items.ItemSpectriteArmor;
import com.samuel.spectrite.items.ItemSpectriteBow;
import com.samuel.spectrite.items.ItemSpectriteOrb;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntitySpectriteSkeleton extends AbstractSpectriteSkeleton {

	public EntitySpectriteSkeleton(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
		Item spectriteWeapon;
		boolean hasPerfectWeapon = this.boss || SpectriteConfig.mobs.spectriteMobPerfectWeaponRate > 0d
			&& rand.nextInt((int) (100 / SpectriteConfig.mobs.spectriteMobPerfectWeaponRate)) < 1f;
		Map<Enchantment, Integer> enchantmentMap = new HashMap<Enchantment, Integer>();
		enchantmentMap.put(ModEnchantments.spectrite_enhance, 1);

		int exp = 15;

		boolean hasBow = this.boss || SpectriteConfig.mobs.spectriteSkeletonSwordRate == 0d ||
			rand.nextFloat() * 100f >= SpectriteConfig.mobs.spectriteSkeletonSwordRate;

		spectriteWeapon = !hasPerfectWeapon ? hasBow ? ModItems.spectrite_bow : ModItems.spectrite_sword
			: hasBow ? ModItems.spectrite_bow_special : ModItems.spectrite_sword_special;

		if (hasPerfectWeapon) {
			exp += 5;
		}

		ItemStack weaponStack = new ItemStack(spectriteWeapon);

		if (hasPerfectWeapon && ((this.world.getDifficulty() == EnumDifficulty.NORMAL && rand.nextInt(32 / chanceMultiplier) == 0)
			|| (this.world.getDifficulty() == EnumDifficulty.HARD && (this.boss || rand.nextInt(8) == 0)))) {
			EnchantmentHelper.setEnchantments(enchantmentMap, weaponStack);
			exp += 10;
		}

	    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weaponStack);
	    this.setDropChance(EntityEquipmentSlot.MAINHAND, new Double(hasBow ? SpectriteConfig.mobs.spectriteMobBowDropRate : SpectriteConfig.mobs.spectriteMobSwordDropRate).floatValue() / 100f);

	    if (hasBow) {
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
			float healthIncrease =  chestplateItem.getHealthIncreaseValue(true) * (enhanced ? 2f : 1f);
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
	protected SoundEvent getAmbientSound()
	{
		return ModSounds.spectrite_skeleton_ambient;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_184601_1_)
	{
		return ModSounds.spectrite_skeleton_hurt;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return ModSounds.spectrite_skeleton_death;
	}

	@Override
	protected SoundEvent getStepSound()
	{
		return ModSounds.spectrite_skeleton_step;
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
            	Field aiAttackOnCollide = ReflectionHelper.findField(AbstractSkeleton.class, new String[] { "aiAttackOnCollide", "field_85038_e" });
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
            EntitySpectriteArrow entityspectritearrow = new EntitySpectriteArrow(this.world, this,
				this.getHeldItemOffhand().getItem() == ModItems.spectrite_arrow_special, enhanced);
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
	@Nullable
    protected ResourceLocation getLootTable()
    {
        return ModLootTables.spectrite_skeleton;
    }
}