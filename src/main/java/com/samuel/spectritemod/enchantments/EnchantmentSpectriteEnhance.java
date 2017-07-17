package com.samuel.spectritemod.enchantments;

import com.samuel.spectritemod.etc.SpectriteHelper;
import com.samuel.spectritemod.items.IPerfectSpectriteItem;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentSpectriteEnhance extends Enchantment {
	
	private final EntityEquipmentSlot[] applicableEquipmentTypes;

	public EnchantmentSpectriteEnhance(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		this.setName("spectrite_enhance");
		
		this.applicableEquipmentTypes = slots;
	}
	
	@Override
	public boolean canApply(ItemStack stack)
    {
        return !stack.isEmpty() && stack.getItem() instanceof IPerfectSpectriteItem && !SpectriteHelper.isStackSpectriteEnhanced(stack) && canApplyAtEnchantingTable(stack);
    }
	
	@Override
	public String getName()
    {
        return "enchantment." + this.name;
    }
	
	@Override
	public String getTranslatedName(int level)
	{
		return SpectriteHelper.getMultiColouredString(super.getTranslatedName(level), false);
	}
}
