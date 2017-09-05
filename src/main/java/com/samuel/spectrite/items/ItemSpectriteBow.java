package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemSpectriteBow extends ItemBow implements ICustomTooltipItem {
	
	public ItemSpectriteBow() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), Spectrite.ItemPropertyGetterSpectrite);
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
			@Override
			@SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn == null ? 0.0F : (!(entityIn.getActiveItemStack().getItem() instanceof ItemSpectriteBow) ? 0.0F : (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F);
            }
        });
		this.setMaxDamage(this instanceof IPerfectSpectriteItem ? 888 : 600);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		
		String displayName = super.getItemStackDisplayName(stack);
		displayName = stack.getItem() instanceof IPerfectSpectriteItem ? ((IPerfectSpectriteItem) this).getMultiColouredDisplayName(stack, displayName)
			: (TextFormatting.LIGHT_PURPLE + displayName);
		return displayName;
	}
}
