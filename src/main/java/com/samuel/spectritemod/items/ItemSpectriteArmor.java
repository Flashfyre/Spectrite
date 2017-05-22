package com.samuel.spectritemod.items;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.SpectriteModConfig;
import com.samuel.spectritemod.etc.SpectriteHelper;

public class ItemSpectriteArmor extends ItemArmor {

	public ItemSpectriteArmor(EntityEquipmentSlot equipmentSlotIn) {
		super(SpectriteMod.SPECTRITE, 0, equipmentSlotIn);
		
		this.addPropertyOverride(new ResourceLocation("time"), SpectriteMod.ItemPropertyGetterSpectrite);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		
		String displayName = super.getItemStackDisplayName(stack);
		displayName = TextFormatting.LIGHT_PURPLE + displayName;
		return displayName;
	}
	
	@Override
	public void addInformation(ItemStack stack,
		EntityPlayer player, List list, boolean Adva) {
		int lineCount = 0;
		boolean isLastLine = false;
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = TextFormatting.RED + I18n
				.translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + "." + SpectriteMod.Config.spectriteArmourBonusMode.ordinal() + ".l" +
				++lineCount))).endsWith("@");
			list.add(!isLastLine ? curLine : curLine
				.substring(0, curLine.length() - 1));
		}
		if (stack.isItemEnchanted()) {
			list.add("----------");
		}
	}
	
	@Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote) {
			float healthIncrease = 0f;
			Iterator<ItemStack> armourIterator = player.getArmorInventoryList().iterator();
			while (armourIterator.hasNext()) {
				ItemStack stack = armourIterator.next();
				if (stack != null && stack.getItem() instanceof ItemSpectriteArmor)
					healthIncrease += ((ItemSpectriteArmor) stack.getItem()).getHealthIncreaseValue();
			}
			if (healthIncrease > 0f) {
				SpectriteModConfig.EnumSpectriteArmourBonusMode spectriteArmourBonusMode = SpectriteMod.Config.spectriteArmourBonusMode;
				if (player.getMaxHealth() - 20f != healthIncrease) {
					player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20f + healthIncrease);
				}
				if (healthIncrease == 20f * spectriteArmourBonusMode.getHealthIncreaseMultiplier()) {
					if (player.getActivePotionEffect(MobEffects.REGENERATION) == null) {
						player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 220, Math.max(SpectriteMod.Config.spectriteArmourBonusMode.ordinal() - 1, 0), true, true));
					}
					if (spectriteArmourBonusMode.ordinal() > 2) {
						if (player.getActivePotionEffect(MobEffects.SPEED) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 220, SpectriteMod.Config.spectriteArmourBonusMode.ordinal() - 3, true, true));
						}
						if (player.getActivePotionEffect(MobEffects.STRENGTH) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 220, SpectriteMod.Config.spectriteArmourBonusMode.ordinal() - 3, true, true));
						}
						if (player.getActivePotionEffect(MobEffects.RESISTANCE) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 220, SpectriteMod.Config.spectriteArmourBonusMode.ordinal() - 3, true, true));
						}
						if (player.getActivePotionEffect(MobEffects.FIRE_RESISTANCE) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 220, SpectriteMod.Config.spectriteArmourBonusMode.ordinal() - 3, true, true));
						}
					}
				}
			}
		}
    }
	
	public float getHealthIncreaseValue() {
		float healthIncreaseValue = 0f;
		
		if (this.armorType == EntityEquipmentSlot.HEAD) {
			healthIncreaseValue = 4f;
		} else if (this.armorType == EntityEquipmentSlot.CHEST) {
			healthIncreaseValue = 8f;
		} else if (this.armorType == EntityEquipmentSlot.LEGS) {
			healthIncreaseValue = 6f;
		} else {
			healthIncreaseValue = 2f;
		}
		
		return healthIncreaseValue * SpectriteMod.Config.spectriteArmourBonusMode.getHealthIncreaseMultiplier();
	}
}
