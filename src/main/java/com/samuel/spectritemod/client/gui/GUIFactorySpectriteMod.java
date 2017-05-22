package com.samuel.spectritemod.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.samuel.spectritemod.SpectriteMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionGuiHandler;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.ModContainer;

public class GUIFactorySpectriteMod implements IModGuiFactory {
	
	private static String title;
	private Minecraft minecraft;
	
	public GUIFactorySpectriteMod() {
		this.title = I18n.translateToLocal("config.title");
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
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return GUIConfigSpectriteMod.class;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
        List<IConfigElement> configElements = new ConfigElement(SpectriteMod.Config.configuration.getCategory(SpectriteMod.Config.configuration.CATEGORY_GENERAL)).getChildElements();
        
		return new GuiConfig(parentScreen, configElements, SpectriteMod.MOD_ID, false, false, title);
	}
	
	@Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
    {
        return null;
    }
}