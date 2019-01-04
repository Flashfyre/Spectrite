package com.samuel.spectrite.entities;

import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.items.ItemSpectriteArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public interface ISpectriteBipedMob {

	public default boolean isHasSpectriteStrength() {
		return false;
	}

	public default boolean isHasSpectriteResistance() {
		return false;
	}
	
	public void setHasSpectriteResistance(boolean hasSpectriteResistance);
	
	public default boolean isArmorFullEnhanced() {
		ItemStack helmetStack = ((EntityLivingBase) this).getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		ItemStack chestplateStack = ((EntityLivingBase) this).getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		ItemStack leggingsStack = ((EntityLivingBase) this).getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		ItemStack bootsStack = ((EntityLivingBase) this).getItemStackFromSlot(EntityEquipmentSlot.FEET);
		
		boolean armorFullEnhanced = (helmetStack.getItem() instanceof ItemSpectriteArmor && chestplateStack.getItem() instanceof ItemSpectriteArmor
			&& leggingsStack.getItem() instanceof ItemSpectriteArmor && bootsStack.getItem() instanceof ItemSpectriteArmor)
			&& SpectriteHelper.isStackSpectriteEnhanced(helmetStack) && SpectriteHelper.isStackSpectriteEnhanced(chestplateStack)
			&& SpectriteHelper.isStackSpectriteEnhanced(leggingsStack) && SpectriteHelper.isStackSpectriteEnhanced(bootsStack);
		
		return armorFullEnhanced;
	}
}
