package com.samuel.spectrite.proxy;

import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.client.eventhandlers.SpectriteClientEventHandler;
import com.samuel.spectrite.client.model.ModelMoltenSpectrite;
import com.samuel.spectrite.client.particles.ParticleSpectriteSpell;
import com.samuel.spectrite.client.renderer.BlockRenderRegister;
import com.samuel.spectrite.client.renderer.EntityRenderRegister;
import com.samuel.spectrite.client.renderer.ItemRenderRegister;
import com.samuel.spectrite.client.renderer.TileEntityRenderRegister;
import com.samuel.spectrite.etc.SpectriteHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleSmokeLarge;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.util.EnumParticleTypes;
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
	public void spawnSpectriteSpellParticle(World world, double posX, double posY, double posZ, double r, double g, double b, int offsetLevel) {
		ParticleSpectriteSpell particle = new ParticleSpectriteSpell(world, posX, posY, posZ, r, g, b, offsetLevel);

        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}
	
	@Override
	public void spawnSpectriteSmokeNormalParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) {
		ParticleSmokeNormal particle = (ParticleSmokeNormal) new ParticleSmokeLarge.Factory().createParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), world, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
		float[] c = SpectriteHelper.getCurrentSpectriteRGBColour(0);
		particle.setRBGColorF(c[0], c[1], c[2]);

        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}
	
	@Override
	public void spawnSpectriteSmokeLargeParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) {
		ParticleSmokeLarge particle = (ParticleSmokeLarge) new ParticleSmokeLarge.Factory().createParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), world, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
		float[] c = SpectriteHelper.getCurrentSpectriteRGBColour(0);
		particle.setRBGColorF(c[0], c[1], c[2]);

        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}
}
