package com.samuel.spectrite.items;

import java.util.Iterator;
import java.util.List;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModPotions;
import com.samuel.spectrite.potions.PotionEffectSpectrite;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemSpectriteArmor extends ItemArmor implements IPerfectSpectriteItem {

	public ItemSpectriteArmor(EntityEquipmentSlot equipmentSlotIn) {
		super(Spectrite.SPECTRITE, 0, equipmentSlotIn);
		
		this.addPropertyOverride(new ResourceLocation("time"), Spectrite.ItemPropertyGetterSpectrite);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		
		String displayName = super.getItemStackDisplayName(stack);
		displayName = getMultiColouredDisplayName(stack, displayName);
		return displayName;
	}
	
	@Override
	public void addInformation(ItemStack stack,
		World worldIn, List<String> list, ITooltipFlag adva) {
		int lineCount = 0;
		boolean isLastLine = false;
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = TextFormatting.RED + I18n
				.translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + (SpectriteHelper.isStackSpectriteEnhanced(stack) ? "_enhanced" : "")
				+ "." + SpectriteConfig.spectriteArmourBonusMode.ordinal()  + ".l" + ++lineCount))).endsWith("@");
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
			int armourCount = 0;
			int enhancedCount = 0;
			int spectriteResistanceLevel = -1;
			Iterator<ItemStack> armourIterator = player.getArmorInventoryList().iterator();
			while (armourIterator.hasNext()) {
				ItemStack stack = armourIterator.next();
				if (stack != null && stack.getItem() instanceof ItemSpectriteArmor) {
					float armourHealthIncrease = ((ItemSpectriteArmor) stack.getItem()).getHealthIncreaseValue(false);
					armourCount++;
					if (SpectriteHelper.isStackSpectriteEnhanced(stack)) {
						enhancedCount++;
						armourHealthIncrease *= 2;
					}
					healthIncrease += armourHealthIncrease;
				}
			}
			if (healthIncrease > 0f) {
				SpectriteConfig.EnumSpectriteArmourBonusMode spectriteArmourBonusMode = SpectriteConfig.spectriteArmourBonusMode;
				boolean allEnhanced = enhancedCount == 4;
				if (player.getMaxHealth() - 20f != healthIncrease) {
					player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20f + healthIncrease);
				}
				if (armourCount == 4 && player.getFoodStats().getFoodLevel() > 0) {
					if (spectriteArmourBonusMode.ordinal() > 2) {
						if (player.getActivePotionEffect(MobEffects.SPEED) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 220, SpectriteConfig.spectriteArmourBonusMode.ordinal() - (!allEnhanced ? 3 : 2), true, true));
						}
						if (player.getActivePotionEffect(MobEffects.STRENGTH) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 220, SpectriteConfig.spectriteArmourBonusMode.ordinal() - (!allEnhanced ? 3 : 2), true, true));
						}
						if (player.getActivePotionEffect(MobEffects.RESISTANCE) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 220, SpectriteConfig.spectriteArmourBonusMode.ordinal() - (!allEnhanced ? 3 : 2), true, true));
						}
						if (player.getActivePotionEffect(MobEffects.FIRE_RESISTANCE) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 220, SpectriteConfig.spectriteArmourBonusMode.ordinal() - (!allEnhanced ? 3 : 2), true, true));
						}
						spectriteResistanceLevel++;
					}
					if (allEnhanced) {
						spectriteResistanceLevel++;
					}
					if (player.getActivePotionEffect(MobEffects.REGENERATION) == null) {
						player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 220, Math.max(SpectriteConfig.spectriteArmourBonusMode.ordinal() - 1, 0), true, true));
					}
					if (spectriteResistanceLevel >= 0 && player.getActivePotionEffect(ModPotions.SPECTRITE_RESISTANCE) == null) {
						player.addPotionEffect(new PotionEffectSpectrite(ModPotions.SPECTRITE_RESISTANCE, 16, spectriteResistanceLevel, true, true));
					}
				}
			}
		}
    }
	
	public float getHealthIncreaseValue(boolean ignoreConfig) {
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
		
		if (!ignoreConfig) {
			healthIncreaseValue *= SpectriteConfig.spectriteArmourBonusMode.getHealthIncreaseMultiplier();
		}
		
		return healthIncreaseValue;
	}
}
