package com.samuel.spectrite.client.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.IModGuiFactory;

public class GUIFactorySpectrite implements IModGuiFactory {
	
	private static String title;
	private Minecraft minecraft;
	
	public GUIFactorySpectrite() {
		GUIFactorySpectrite.title = I18n.translateToLocal("config.title");
	}

    @Override
    public boolean hasConfigGui()
    {
        return true;
    }

    @Override
    public void initialize(Minecraft minecraftInstance)
    {
        this.minecraft = minecraftInstance;
    }

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new GUIConfigSpectrite(parentScreen);
	}
	
	@Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }
}