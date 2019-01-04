package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.helpers.SpectriteHelper;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemSpectriteArmor extends ItemArmor implements IPerfectSpectriteItem, ISpectriteCustomTooltipItem {

	private final int orbSlots;

	public ItemSpectriteArmor(EntityEquipmentSlot equipmentSlotIn) {
		super(Spectrite.SPECTRITE, 0, equipmentSlotIn);
		
		this.addPropertyOverride(new ResourceLocation("time"), Spectrite.ItemPropertyGetterSpectrite);

		this.orbSlots = equipmentSlotIn == EntityEquipmentSlot.HEAD ? 3 : equipmentSlotIn == EntityEquipmentSlot.CHEST ? 2 : 1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addTooltipLines(ItemStack stack, List<String> list) {
		int lineCount = 0;
		int potionStartIndex = list.size() - (SpectriteConfig.items.spectriteArmourBonusMode.ordinal() == 0 ? 1 : 0);
		boolean isLastLine = false;
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = TextFormatting.RED + I18n
				.translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + (SpectriteHelper.isStackSpectriteEnhanced(stack) ? "_enhanced" : "")
				+ "." + SpectriteConfig.items.spectriteArmourBonusMode.ordinal()  + ".l" + ++lineCount))).endsWith("@");
			list.add(!isLastLine ? curLine : curLine
				.substring(0, curLine.length() - 1));
		}
		if (stack.hasTagCompound()) {
			NBTTagCompound orbEffectsCompound = stack.getSubCompound("OrbEffects");
			if (orbEffectsCompound != null) {
				int potionEffectsCount = 0;
				for (String o : orbEffectsCompound.getKeySet()) {
					if (orbEffectsCompound.getBoolean(o)) {
						int c = Integer.parseInt(o);
						list.add(potionStartIndex + ++potionEffectsCount, SpectriteHelper.TEXT_COLORS[c] + " " + I18n.translateToLocal(ModItems.spectrite_orb.ORB_POTIONS[c].getName())
								+ I18n.translateToLocal("iteminfo." + ModItems.spectrite_orb.getUnlocalizedName().substring(5) + ".effect.level.1"));
					}
				}
			}
		}
		list.set(0, getMultiColouredDisplayName(stack, stack.getDisplayName()));
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
	{
		return enchantment.type.canEnchantItem(stack.getItem());
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

	/**
	 * Called when an ItemStack with NBT data is read to potentially that ItemStack's NBT data
	 */
	@Override
	public boolean updateItemStackNBT(NBTTagCompound nbt)
	{
		if (!nbt.hasKey("OrbEffects")) {
			NBTTagCompound orbEffectsCompound = new NBTTagCompound();
			for (int c = 0; c < ItemSpectriteOrb.ORB_COLOURS.length; c++) {
				if (ItemSpectriteOrb.ORB_EQUIPMENT_SLOTS[c] == this.armorType) {
					orbEffectsCompound.setBoolean(new Integer(c).toString(), false);
				}
			}
			nbt.setTag("OrbEffects", orbEffectsCompound);
		}

		return true;
	}

	public int getNumOrbSlots() {
		return this.orbSlots;
	}

	public List<Potion> getPotions(ItemStack stack) {
		final List<Potion> potions = new ArrayList<>();

		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		this.updateItemStackNBT(stack.getTagCompound());
		NBTTagCompound tagCompound = stack.getSubCompound("OrbEffects");

		int[] orbColours = ItemSpectriteOrb.ORB_COLOURS;
		for (int c = 0; c < orbColours.length; c++) {
			String key = new Integer(c).toString();
			if (tagCompound.hasKey(key) && tagCompound.getBoolean(key)) {
				potions.add(ModItems.spectrite_orb.ORB_POTIONS[c]);
			}
		}

		return potions;
	}
	
	@Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote) {
			float healthIncrease = 0f;
			int armourCount = 0;
			int enhancedCount = 0;
			int spectriteResistanceLevel = 0;
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
					if (player.getActivePotionEffect(ModPotions.SPECTRITE_RESISTANCE) == null) {
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
