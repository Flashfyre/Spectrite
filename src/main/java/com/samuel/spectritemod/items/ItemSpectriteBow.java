package com.samuel.spectritemod.items;

import javax.annotation.Nullable;

import com.samuel.spectritemod.SpectriteMod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpectriteBow extends ItemBow {
	
	public ItemSpectriteBow() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), SpectriteMod.ItemPropertyGetterSpectrite);
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
			@Override
			@SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn == null ? 0.0F : (!(entityIn.getActiveItemStack().getItem() instanceof ItemSpectriteBow) ? 0.0F : (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F);
            }
        });
		this.setMaxDamage(this instanceof ItemSpectriteBowSpecial ? 888 : 600);
	}
}
