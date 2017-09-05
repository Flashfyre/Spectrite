package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.entities.EntitySpectriteArrow;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemSpectriteArrow extends ItemArrow implements ICustomTooltipItem {
	
	public ItemSpectriteArrow() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), Spectrite.ItemPropertyGetterSpectrite);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String displayName = super.getItemStackDisplayName(stack);
		return TextFormatting.LIGHT_PURPLE  + displayName;
	}
	
	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
		boolean enhanced = false;
		if (!shooter.getActiveItemStack().isEmpty() && shooter.getActiveItemStack().getItem() instanceof ItemBow) {
			if (shooter instanceof EntityPlayer && !((EntityPlayer) shooter).capabilities.isCreativeMode) {
				for (ItemBow b : ModItems.bowItems) {
					((EntityPlayer) shooter).getCooldownTracker().setCooldown(b, (int) Math.round(SpectriteConfig.items.spectriteToolCooldown * 20));
				}
			}

			SpectriteHelper.damageBow(shooter, null);
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
