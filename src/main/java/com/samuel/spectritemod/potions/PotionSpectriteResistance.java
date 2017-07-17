package com.samuel.spectritemod.potions;

import java.util.Map;

import com.google.common.collect.Maps;
import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.etc.SpectriteHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionSpectriteResistance extends Potion {
	
	private static final Map<String, ResourceLocation> SPECTRITE_INVENTORY_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();

	public PotionSpectriteResistance() {
		super(false, SpectriteHelper.getCurrentSpectriteColour(true));
	}

	@Override
	public int getLiquidColor()
    {
        return SpectriteHelper.getCurrentSpectriteColour(true);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon() {
		int curFrame = SpectriteHelper.getCurrentSpectriteFrame(null);
        String textureLoc = String.format("%s:textures/gui/container/inventory/%d.png", SpectriteMod.MOD_ID, curFrame);
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
        return 0;
	}
}
