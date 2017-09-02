package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
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

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class ItemSpectriteArmor extends ItemArmor implements IPerfectSpectriteItem, ICustomTooltipItem {

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
	public void addTooltipLines(ItemStack stack, List<String> list) {
		int lineCount = 0;
		boolean isLastLine = false;
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = TextFormatting.RED + I18n
				.translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + (SpectriteHelper.isStackSpectriteEnhanced(stack) ? "_enhanced" : "")
				+ "." + SpectriteConfig.items.spectriteArmourBonusMode.ordinal()  + ".l" + ++lineCount))).endsWith("@");
			list.add(!isLastLine ? curLine : curLine
				.substring(0, curLine.length() - 1));
		}
	}

	@Override
	@Nullable
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		String domain = Spectrite.MOD_ID;
		int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());

		return String.format("%s:textures/models/armor/spectrite_layer_%d/%d.png", domain, slot == EntityEquipmentSlot.LEGS ? 2 : 1, curFrame);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return this.onEntitySpectriteItemUpdate(entityItem);
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
				SpectriteConfig.EnumSpectriteArmourBonusMode spectriteArmourBonusMode = SpectriteConfig.items.spectriteArmourBonusMode;
				boolean allEnhanced = enhancedCount == 4;
				if (player.getMaxHealth() - 20f != healthIncrease) {
					player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20f + healthIncrease);
				}
				if (armourCount == 4 && player.getFoodStats().getFoodLevel() > 0) {
					if (spectriteArmourBonusMode.ordinal() > 2) {
						if (player.getActivePotionEffect(MobEffects.SPEED) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 220, SpectriteConfig.items.spectriteArmourBonusMode.ordinal() - (!allEnhanced ? 3 : 2), true, true));
						}
						if (player.getActivePotionEffect(MobEffects.STRENGTH) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 220, SpectriteConfig.items.spectriteArmourBonusMode.ordinal() - (!allEnhanced ? 3 : 2), true, true));
						}
						if (player.getActivePotionEffect(MobEffects.RESISTANCE) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 220, SpectriteConfig.items.spectriteArmourBonusMode.ordinal() - (!allEnhanced ? 3 : 2), true, true));
						}
						if (player.getActivePotionEffect(MobEffects.FIRE_RESISTANCE) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 220, SpectriteConfig.items.spectriteArmourBonusMode.ordinal() - (!allEnhanced ? 3 : 2), true, true));
						}
						spectriteResistanceLevel++;
					}
					if (allEnhanced) {
						spectriteResistanceLevel++;
					}
					if ((!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == ModItems.spectrite_orb)
						|| (!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() == ModItems.spectrite_orb)) {
						if (player.getActivePotionEffect(MobEffects.REGENERATION) == null) {
							player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 220, Math.max(SpectriteConfig.items.spectriteArmourBonusMode.ordinal() - 1, 0), true, true));
						}
					}
					if (spectriteResistanceLevel >= 0 && player.getActivePotionEffect(ModPotions.SPECTRITE_RESISTANCE) == null) {
						player.addPotionEffect(new PotionEffect(ModPotions.SPECTRITE_RESISTANCE, 16, spectriteResistanceLevel, true, true));
					}
				}
			}
		}
    }
	
	public float getHealthIncreaseValue(boolean ignoreConfig) {
		float healthIncreaseValue;
		
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
			healthIncreaseValue *= SpectriteConfig.items.spectriteArmourBonusMode.getHealthIncreaseMultiplier();
		}
		
		return healthIncreaseValue;
	}
}
