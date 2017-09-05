package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.etc.SpectriteHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.List;

public class ItemSpectriteOrb extends Item implements IPerfectSpectriteItem, ICustomTooltipItem {
	
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
	public void addTooltipLines(ItemStack stack, List<String> list) {
		int lineCount = 0;
		boolean isLastLine = false;
		double cooldown = SpectriteConfig.items.spectriteOrbCooldown * (SpectriteHelper.isStackSpectriteEnhanced(stack) ? 0.5D : 1D);
		double duration = SpectriteConfig.items.spectriteOrbDuration;
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = I18n
				.translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + ".l" + ++lineCount))).endsWith("@");
			if (lineCount == 3) {
				curLine = curLine.replace("#", String.format("%.2f", cooldown));
			}
			if (lineCount == 4) {
				curLine = curLine.replace("#", String.format("%.2f", duration));
			}
			list.add(!isLastLine ? curLine : curLine
					.substring(0, curLine.length() - 1));
		}
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return this.onEntitySpectriteItemUpdate(entityItem);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		boolean isEnhanced = SpectriteHelper.isStackSpectriteEnhanced(playerIn.getHeldItem(hand));
		worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
				SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5F, (itemRand.nextFloat() * 0.4F + 1.0F));
		playerIn.getCooldownTracker().setCooldown(this, (int) SpectriteConfig.items.spectriteOrbCooldown * (isEnhanced ? 10 : 20));

		playerIn.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, (int) SpectriteConfig.items.spectriteOrbDuration * 20,
			(!playerIn.isPotionActive(MobEffects.REGENERATION) ?
			0 : playerIn.getActivePotionEffect(MobEffects.REGENERATION).getAmplifier() + 1)));

		return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
	}
}
