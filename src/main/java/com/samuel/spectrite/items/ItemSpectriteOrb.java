package com.samuel.spectrite.items;

import java.util.List;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.etc.SpectriteHelper;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemSpectriteOrb extends Item implements IPerfectSpectriteItem {
	
	public ItemSpectriteOrb() {
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.addPropertyOverride(new ResourceLocation("time"), Spectrite.ItemPropertyGetterSpectrite);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		
		String displayName = SpectriteHelper.getMultiColouredString(super.getItemStackDisplayName(stack), false);
		
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
			curLine = curLine.replace("#", String.valueOf((lineCount == 1 || SpectriteHelper.isStackSpectriteEnhanced(stack)) ? 30 : 15));
			list.add(!isLastLine ? curLine : curLine
				.substring(0, curLine.length() - 1));
		}
		if (stack.isItemEnchanted()) {
			list.add("----------");
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ,
        	SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5F, (itemRand.nextFloat() * 0.4F + 1.0F));
        playerIn.getCooldownTracker().setCooldown(this, 600);
        
        playerIn.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, SpectriteHelper.isStackSpectriteEnhanced(
    		playerIn.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND)) ? 600 : 300,
    		!playerIn.isPotionActive(MobEffects.REGENERATION) ? 
    		0 : playerIn.getActivePotionEffect(MobEffects.REGENERATION).getAmplifier() + 1));

        return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
    }
}
