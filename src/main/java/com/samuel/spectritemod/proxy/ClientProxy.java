package com.samuel.spectritemod.proxy;

import com.samuel.spectritemod.SpectriteModConfig;
import com.samuel.spectritemod.client.eventhandlers.SpectriteClientEventHandler;
import com.samuel.spectritemod.client.model.ModelMoltenSpectrite;
import com.samuel.spectritemod.client.particles.ParticleSpectriteSpell;
import com.samuel.spectritemod.client.renderer.BlockRenderRegister;
import com.samuel.spectritemod.client.renderer.EntityRenderRegister;
import com.samuel.spectritemod.client.renderer.ItemRenderRegister;
import com.samuel.spectritemod.client.renderer.TileEntityRenderRegister;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		
		ModelLoaderRegistry.registerLoader(ModelMoltenSpectrite.FluidLoader.INSTANCE);
		
		SpectriteModConfig.propSpectriteCountSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMinSizeSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMaxSizeSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMinYSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMaxYSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteCountNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMinSizeNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMaxSizeNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMinYNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMaxYNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteCountEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMinSizeEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMaxSizeEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMinYEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteModConfig.propSpectriteMaxYEnd.setConfigEntryClass(NumberSliderEntry.class);
		
		MinecraftForge.EVENT_BUS
			.register(new BlockRenderRegister());
		MinecraftForge.EVENT_BUS
			.register(new ItemRenderRegister());
		MinecraftForge.EVENT_BUS
			.register(new EntityRenderRegister());
		MinecraftForge.EVENT_BUS
			.register(new TileEntityRenderRegister());
		MinecraftForge.EVENT_BUS
			.register(new SpectriteClientEventHandler());
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}
	
	@Override
	public void spawnSpectriteSpellParticle(World world, double posX, double posY, double posZ, double r, double g, double b, boolean invertColour) {
		ParticleSpectriteSpell particle = new ParticleSpectriteSpell(world, posX, posY, posZ, r, g, b, invertColour);
        particle.onUpdate();

        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}
}
