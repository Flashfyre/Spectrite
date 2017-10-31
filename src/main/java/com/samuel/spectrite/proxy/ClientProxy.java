package com.samuel.spectrite.proxy;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.client.eventhandlers.SpectriteClientEventHandler;
import com.samuel.spectrite.client.model.ModelMoltenSpectrite;
import com.samuel.spectrite.client.particles.ParticleSpectriteExplosionHuge;
import com.samuel.spectrite.client.particles.ParticleSpectriteExplosionLarge;
import com.samuel.spectrite.client.particles.ParticleSpectriteSpell;
import com.samuel.spectrite.client.renderer.*;
import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.items.ItemSpectriteOrb;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	private SpectriteClientEventHandler clientEventHandler = null;

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);

		this.clientEventHandler = new SpectriteClientEventHandler();
		ModelLoaderRegistry.registerLoader(ModelMoltenSpectrite.FluidLoader.INSTANCE);

		Field configsField = ReflectionHelper.findField(ConfigManager.class, "CONFIGS");
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

		ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
		itemColors.registerItemColorHandler(new IItemColor()
		{
			Int2ObjectMap<Integer[]> colourCache = new Int2ObjectOpenHashMap<>();
			Int2ObjectMap<Integer[]> coloursListCache = new Int2ObjectOpenHashMap<>();

			@Override
			@SideOnly(Side.CLIENT)
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				int ret = 16777215;
				if (tintIndex < 17) {
					int spectriteFrame = SpectriteHelper.getCurrentSpectriteFrame(null);
					int itemDamage = stack.getItemDamage();
					if (itemDamage > 0) {
						if (colourCache.containsKey(itemDamage) && colourCache.get(itemDamage)[tintIndex * 36 + spectriteFrame] != null) {
							ret = colourCache.get(itemDamage)[tintIndex * 36 + spectriteFrame];
						} else {
							Integer[] colours;
							if (coloursListCache.containsKey(itemDamage)) {
								colours = coloursListCache.get(itemDamage);
							} else {
								List<Integer> coloursList = new ArrayList<>();
								final int[] orbTypes = ItemSpectriteOrb.ORB_COLOURS;
								final int[][] orbTypeRGB = ItemSpectriteOrb.ORB_COLOUR_RGB;
								for (int c = 0; c < orbTypes.length; c++) {
									if ((itemDamage & orbTypes[c]) > 0) {
										for (int rgb = 0; rgb < 3; rgb++) {
											coloursList.add(orbTypeRGB[c][rgb]);
										}
									}
								}
								colours = coloursList.toArray(new Integer[0]);
								coloursListCache.put(itemDamage, colours);
							}
							if (colours.length == 3) {
								ret = new Color(colours[0], colours[1], colours[2]).getRGB();
							} else {
								float partSize = 51f / colours.length - 1;
								float progress = ((tintIndex / partSize) + ((colours.length / 108f) * spectriteFrame)) % (colours.length / 3);
								int startIndex = (int) Math.floor(progress);
								progress -= startIndex;
								int[] rgb = new int[3];
								for (int c = 0; c < 3; c++) {
									rgb[c] = Math.round((colours[startIndex * 3 + c] * (1f - progress)) + (colours[(startIndex < (colours.length / 3) - 1 ? startIndex + 1 : 0) * 3 + c] * progress));
								}
								ret = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
							}
							if (colourCache.containsKey(itemDamage)) {
								colourCache.get(itemDamage)[tintIndex * 36 + spectriteFrame] = ret;
							} else {
								Integer[] colourCacheEntry = new Integer[612];
								colourCacheEntry[tintIndex * 36 + spectriteFrame] = ret;
								colourCache.put(itemDamage, colourCacheEntry);
							}
						}
					}
				}
				return ret;
			}
		}, ModItems.spectrite_orb);

		Map<ResourceLocation, Integer> spectriteEntityIndexes = new HashMap<>();

		EntityList.ENTITY_EGGS.entrySet().stream().filter(ee -> ee.getKey() != null
			&& Spectrite.MOD_ID.equals(ee.getKey().getResourceDomain())).map(ee -> ee.getKey()).forEach(ee -> {
			spectriteEntityIndexes.put(ee, spectriteEntityIndexes.size());
		});

		itemColors.registerItemColorHandler(new IItemColor()
		{
			Map<Integer, int[][]> colourCache = new HashMap<>();

			@Override
			@SideOnly(Side.CLIENT)
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				int ret = -1;
				ResourceLocation entityRL = ItemMonsterPlacer.getNamedIdFrom(stack);
				if (entityRL != null) {
					if (Spectrite.MOD_ID.equals(entityRL.getResourceDomain())) {
						Integer entityCacheIndex = spectriteEntityIndexes.get(entityRL);
						int spectriteFrame = SpectriteHelper.getCurrentSpectriteFrame(null);
						int[][] entityColourCache;
						if (!colourCache.containsKey(entityCacheIndex)) {
							colourCache.put(entityCacheIndex, new int[34][36]);
						}
						entityColourCache = colourCache.get(entityCacheIndex);
						if (entityColourCache[tintIndex][spectriteFrame] != 0) {
							ret = entityColourCache[tintIndex][spectriteFrame];
						} else {
							int[][] colours = ItemSpectriteOrb.ORB_COLOUR_RGB;

							int tintIndex2 = tintIndex % 17;

							float partSize = 17f / (colours.length - 1);
							float progress = ((tintIndex2 / partSize) + ((colours.length / 36f) * spectriteFrame)
									+ (tintIndex > 16 ? 34 : 0) + (36 * (entityCacheIndex / (float) spectriteEntityIndexes.size()))) % colours.length;
							int startIndex = (int) Math.floor(progress);
							progress -= startIndex;
							int[] rgb = new int[3];
							for (int c = 0; c < 3; c++) {
								rgb[c] = Math.round((colours[startIndex][c] * (1f - progress)) + (colours[startIndex < colours.length - 1 ? startIndex + 1 : 0][c] * progress));
							}
							ret = new Color(rgb[0], rgb[1], rgb[2]).getRGB();

							entityColourCache[tintIndex][spectriteFrame] = ret;
						}
					} else {
						EntityList.EntityEggInfo entityEggInfo = EntityList.ENTITY_EGGS.get(entityRL);

						if (entityEggInfo == null) {
							ret = -1;
						} else {
							ret = tintIndex == 0 ? entityEggInfo.primaryColor : entityEggInfo.secondaryColor;
						}
					}
				}

				return ret;
			}
		}, Items.SPAWN_EGG);
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
		float offsetLevel = 18F * (new Double(Math.abs(posX + posZ) + posY).floatValue() % 20F);
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

	@Override
	public void spawnSpectritePortalParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) {
		ParticlePortal particle = (ParticlePortal) new ParticlePortal.Factory().createParticle(EnumParticleTypes.PORTAL.getParticleID(), world, posX, posY, posZ, xSpeed, ySpeed, zSpeed);
		float[] c = SpectriteHelper.getCurrentSpectriteRGBColour(0);
		particle.setRBGColorF(c[0], c[1], c[2]);

		SpectriteParticleManager.INSTANCE.addParticle(particle);
	}
}
