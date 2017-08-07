package com.samuel.spectrite.proxy;

import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.client.eventhandlers.SpectriteClientEventHandler;
import com.samuel.spectrite.client.model.ModelMoltenSpectrite;
import com.samuel.spectrite.client.particles.ParticleSpectriteSpell;
import com.samuel.spectrite.client.renderer.BlockRenderRegister;
import com.samuel.spectrite.client.renderer.EntityRenderRegister;
import com.samuel.spectrite.client.renderer.ItemRenderRegister;
import com.samuel.spectrite.client.renderer.TileEntityRenderRegister;

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
		
		SpectriteConfig.propSpectriteCountSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMinSizeSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMaxSizeSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMinYSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMaxYSurface.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteCountNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMinSizeNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMaxSizeNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMinYNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMaxYNether.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteCountEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMinSizeEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMaxSizeEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMinYEnd.setConfigEntryClass(NumberSliderEntry.class);
		SpectriteConfig.propSpectriteMaxYEnd.setConfigEntryClass(NumberSliderEntry.class);
		
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
