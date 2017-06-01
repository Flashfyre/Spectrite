package com.samuel.spectritemod.client.gui;

import com.samuel.spectritemod.SpectriteMod;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GUIConfigSpectriteMod extends GuiConfig {
	public GUIConfigSpectriteMod(GuiScreen parent) {
		super(parent, new ConfigElement(SpectriteMod.Config.configuration
			.getCategory(Configuration.CATEGORY_GENERAL))
			.getChildElements(), SpectriteMod.MOD_ID, false,
			false, I18n.translateToLocal("config.title"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY,
		float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}