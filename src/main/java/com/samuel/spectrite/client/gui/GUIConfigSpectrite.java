package com.samuel.spectrite.client.gui;

import com.samuel.spectrite.Spectrite;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GUIConfigSpectrite extends GuiConfig {
	public GUIConfigSpectrite(GuiScreen parent) {
		super(parent, new ConfigElement(Spectrite.Config.configuration
			.getCategory(Configuration.CATEGORY_GENERAL))
			.getChildElements(), Spectrite.MOD_ID, false,
			false, I18n.translateToLocal("config.title"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY,
		float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}