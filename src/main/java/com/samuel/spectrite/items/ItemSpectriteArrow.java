package com.samuel.spectrite.items;

import java.util.List;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.entities.EntitySpectriteArrow;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModItems;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemSpectriteArrow extends ItemArrow {
	
	public ItemSpectriteArrow() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), Spectrite.ItemPropertyGetterSpectrite);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		
		String displayName = super.getItemStackDisplayName(stack);
		displayName = SpectriteHelper.getMultiColouredString(displayName, SpectriteHelper.isStackSpectriteEnhanced(stack));
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
				.translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + ".l" +
				++lineCount))).endsWith("@");
			list.add(!isLastLine ? curLine : curLine
				.substring(0, curLine.length() - 1));
		}
	}
	
	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
		boolean enhanced = false;
		if (!shooter.getActiveItemStack().isEmpty() && shooter.getActiveItemStack().getItem() instanceof ItemBow) {
			ItemStack bowItemStack = shooter.getActiveItemStack();
			ItemBow bowItem = (ItemBow) bowItemStack.getItem();
			enhanced = SpectriteHelper.isStackSpectriteEnhanced(bowItemStack);
			int bowDamage = 2;
			
			if (bowItem instanceof ItemSpectriteBow) {
				if (!(bowItem instanceof ItemSpectriteBowSpecial)) {
					bowDamage = 9;
				}
			} else {
				bowDamage = 99;
			}
			
			if (shooter instanceof EntityPlayer && !((EntityPlayer) shooter).capabilities.isCreativeMode) {
				((EntityPlayer) shooter).getCooldownTracker().setCooldown(Items.BOW, (int) Math.round(SpectriteConfig.spectriteToolCooldown * 20));
				((EntityPlayer) shooter).getCooldownTracker().setCooldown(ModItems.spectrite_bow, (int) Math.round(SpectriteConfig.spectriteToolCooldown * 20));
				((EntityPlayer) shooter).getCooldownTracker().setCooldown(ModItems.spectrite_bow_special, (int) Math.round(SpectriteConfig.spectriteToolCooldown * 20));
			}
			shooter.getActiveItemStack().damageItem(bowDamage, shooter);
		}
        return new EntitySpectriteArrow(worldIn, shooter, enhanced);
    }
	
	@Override
	public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player)
    {
        int enchant = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY, bow);
        return enchant <= 0 ? false : this.getClass() == ItemSpectriteArrow.class;
    }
}
