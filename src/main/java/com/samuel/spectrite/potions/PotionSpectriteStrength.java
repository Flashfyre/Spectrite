package com.samuel.spectrite.potions;

import com.google.common.collect.Maps;
import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.helpers.SpectriteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public class PotionSpectriteStrength extends Potion {

	private static final Map<String, ResourceLocation> SPECTRITE_INVENTORY_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();

	public PotionSpectriteStrength() {
		super(false, SpectriteHelper.getCurrentSpectriteColour(1));
	}

	@Override
	public int getLiquidColor()
    {
        return SpectriteHelper.getCurrentSpectriteColour(1);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon() {
		int curFrame = SpectriteHelper.getCurrentSpectriteFrame(null);
        String textureLoc = String.format("%s:textures/gui/container/inventory/%d.png", Spectrite.MOD_ID, curFrame);
		ResourceLocation resourceLocation = SPECTRITE_INVENTORY_TEXTURE_RES_MAP.get(textureLoc);
		
        if (resourceLocation == null)
        {
            resourceLocation = new ResourceLocation(textureLoc);
            SPECTRITE_INVENTORY_TEXTURE_RES_MAP.put(textureLoc, resourceLocation);
        }
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getStatusIconIndex()
	{
        return 1;
	}

	public int getGuiSortColor(PotionEffect potionEffect)
    {
        return 0;
    }
}
