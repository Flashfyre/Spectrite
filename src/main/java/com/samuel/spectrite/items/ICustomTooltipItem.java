package com.samuel.spectrite.items;

import com.samuel.spectrite.etc.SpectriteHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public interface ICustomTooltipItem {

   default void addTooltipLines(ItemStack stack, List<String> list) {
       int lineCount = 0;
       boolean isLastLine = false;
       Item item = stack.getItem();
       String unlocalizedName = item.getUnlocalizedName();
       String curLine;
       while (!isLastLine) {
           isLastLine = (curLine = I18n
               .translateToLocal(("iteminfo." + unlocalizedName.substring(5) + (SpectriteHelper.isStackSpectriteEnhanced(stack) ? "_enhanced" : "")
               + ".l" + ++lineCount))).endsWith("@");
           list.add(!isLastLine ? curLine : curLine
                   .substring(0, curLine.length() - 1));
       }
   }
}
