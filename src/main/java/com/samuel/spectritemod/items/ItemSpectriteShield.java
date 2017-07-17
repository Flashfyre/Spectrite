package com.samuel.spectritemod.items;

import java.util.List;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.etc.SpectriteHelper;
import com.samuel.spectritemod.init.ModItems;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemSpectriteShield extends ItemShield {
	
	public ItemSpectriteShield() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), SpectriteMod.ItemPropertyGetterSpectrite);
		this.setMaxDamage(this instanceof ItemSpectriteShieldSpecial ? 777 : 512);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		
		String displayName = super.getItemStackDisplayName(stack);
		if (displayName.equals(I18n.translateToLocal("item.shield.name"))) {
			displayName = I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
		}
		displayName = stack.getItem() instanceof IPerfectSpectriteItem ? ((IPerfectSpectriteItem) this).getMultiColouredDisplayName(stack, displayName)
			: (TextFormatting.LIGHT_PURPLE + displayName);
		
		return displayName;
	}
	
	@Override
	public void addInformation(ItemStack stack,
		World worldIn, List<String> list, ITooltipFlag adva) {
		int lineCount = 0;
		boolean isLastLine = false;
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = I18n
				.translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + (SpectriteHelper.isStackSpectriteEnhanced(stack) ? "_enhanced" : "")
				+ ".l" + ++lineCount))).endsWith("@");
			list.add(!isLastLine ? curLine : curLine
				.substring(0, curLine.length() - 1));
		}
		if (stack.isItemEnchanted()) {
			list.add("----------");
		}
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == ModItems.spectrite_gem;
    }
}
