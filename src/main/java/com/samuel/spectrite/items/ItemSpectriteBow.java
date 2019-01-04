package com.samuel.spectrite.items;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.helpers.SpectriteHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSpectriteBow extends ItemBow implements ISpectriteCustomTooltipItem {
	
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
	@SideOnly(Side.CLIENT)
	public void addTooltipLines(ItemStack stack, List<String> list) {
		int lineCount = 0;
		boolean isLastLine = false;
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = I18n
					.translateToLocal(("iteminfo." + getTranslationKey().substring(5) + (SpectriteHelper.isStackSpectriteEnhanced(stack) ? "_enhanced" : "") + ".l" +
							++lineCount))).endsWith("@");
			list.add(!isLastLine ? curLine : curLine
					.substring(0, curLine.length() - 1));
		}
		list.set(0, getMultiColouredDisplayName(stack, stack.getDisplayName()));
	}
}
