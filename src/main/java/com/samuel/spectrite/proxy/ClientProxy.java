package com.samuel.spectrite.proxy;

import com.samuel.spectrite.client.eventhandlers.SpectriteClientEventHandler;
import com.samuel.spectrite.client.model.ModelMoltenSpectrite;
import com.samuel.spectrite.client.particles.ParticleSpectriteExplosionHuge;
import com.samuel.spectrite.client.particles.ParticleSpectriteExplosionLarge;
import com.samuel.spectrite.client.particles.ParticleSpectriteSpell;
import com.samuel.spectrite.client.renderer.*;
import com.samuel.spectrite.etc.SpectriteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleExplosion;
import net.minecraft.client.particle.ParticleSmokeLarge;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	private SpectriteClientEventHandler clientEventHandler = null;

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);

		this.clientEventHandler = new SpectriteClientEventHandler();
		ModelLoaderRegistry.registerLoader(ModelMoltenSpectrite.FluidLoader.INSTANCE);

		Field configsField = SpectriteHelper.findObfuscatedField(ConfigManager.class, "CONFIGS");
		try {
			Map<String, Configuration> configs = ((Map<String, Configuration>) configsField.get(null));
			Optional<String> key = configs.keySet().stream().filter(c -> c.contains("spectrite")).findFirst();
			if (key.isPresent()) {
				Configuration config = configs.get(key.get());
				config.getCategory("spectriteore").entrySet().forEach(p -> {
					p.getValue().setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
				});
			}
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

		MinecraftForge.EVENT_BUS
			.register(new BlockRenderRegister());
		MinecraftForge.EVENT_BUS
			.register(new ItemRenderRegister());
		MinecraftForge.EVENT_BUS
			.register(new EntityRenderRegister());
		MinecraftForge.EVENT_BUS
			.register(new TileEntityRenderRegister());
		MinecraftForge.EVENT_BUS
				.register(this.clientEventHandler);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);

		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this.clientEventHandler);
	}

	@Override
	public void spawnSpectriteSpellParticle(World world, double posX, double posY, double posZ, double r, double g, double b, float offsetLevel) {
		ParticleSpectriteSpell particle = new ParticleSpectriteSpell(world, posX, posY, posZ, r, g, b, offsetLevel);

		SpectriteParticleManager.INSTANCE.addParticle(particle);
	}

	@Override
	public void spawnSpectriteExplosionParticle(World world, boolean isLarge, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) {
		Particle particle = isLarge ? new ParticleSpectriteExplosionLarge(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, xSpeed, ySpeed, zSpeed)
			: new ParticleExplosion.Factory().createParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), world, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
		float offsetLevel = 18F * (new Double(posX + posY + posZ).floatValue() % 20F);
		float[] c = SpectriteHelper.getCurrentSpectriteRGBColour(offsetLevel);
		particle.setRBGColorF(c[0], c[1], c[2]);
		if (isLarge) {
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		} else {
			SpectriteParticleManager.INSTANCE.addParticle(particle);
		}
	}

	@Override
	public void spawnSpectriteExplosionHugeParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed, double strength) {
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleSpectriteExplosionHuge(world, posX, posY, posZ, strength));
	}

	@Override
	public void spawnSpectriteSmokeNormalParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) {
		ParticleSmokeNormal particle = (ParticleSmokeNormal) new ParticleSmokeNormal.Factory().createParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), world, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
		float[] c = SpectriteHelper.getCurrentSpectriteRGBColour(0);
		particle.setRBGColorF(c[0], c[1], c[2]);

		SpectriteParticleManager.INSTANCE.addParticle(particle);
	}
	
	@Override
	public void spawnSpectriteSmokeLargeParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) {
		ParticleSmokeLarge particle = (ParticleSmokeLarge) new ParticleSmokeLarge.Factory().createParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), world, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
		float[] c = SpectriteHelper.getCurrentSpectriteRGBColour(0);
		particle.setRBGColorF(c[0], c[1], c[2]);

		SpectriteParticleManager.INSTANCE.addParticle(particle);
	}
}
