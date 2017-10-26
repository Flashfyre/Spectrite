package com.samuel.spectrite.potions;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.helpers.SpectriteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PotionSpectriteOrb extends Potion {

    private static ResourceLocation SPECTRITE_INVENTORY_TEXTURE = new ResourceLocation(Spectrite.MOD_ID, "textures/gui/container/inventory/0.png");

    protected PotionSpectriteOrb(int color) {
        super(false, color);
    }

    @Override
    public int getLiquidColor()
    {
        return SpectriteHelper.getCurrentSpectriteColour(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(SPECTRITE_INVENTORY_TEXTURE);
        return true;
    }
}
