package com.samuel.spectrite.enchantments;

import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.items.IPerfectSpectriteItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentSpectriteEnhance extends Enchantment {

	public EnchantmentSpectriteEnhance(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		this.setName("spectrite_enhance");
	}
	
	@Override
	public boolean canApply(ItemStack stack)
    {
        return false;
    }

	public boolean canApplyAtSpectriteAnvil(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() instanceof IPerfectSpectriteItem
			&& !SpectriteHelper.isStackSpectriteEnhanced(stack) && canApplyAtEnchantingTable(stack)
			&& stack.getItem() != ModItems.spectrite_orb;
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
