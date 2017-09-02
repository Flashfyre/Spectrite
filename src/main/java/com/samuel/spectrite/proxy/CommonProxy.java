package com.samuel.spectrite.proxy;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.capabilities.ISpectriteBossCapability;
import com.samuel.spectrite.capabilities.SpectriteBossCapability;
import com.samuel.spectrite.client.particles.EnumSpectriteParticleTypes;
import com.samuel.spectrite.entities.EntitySpectriteWitherSkull;
import com.samuel.spectrite.etc.SpectriteExplosion;
import com.samuel.spectrite.eventhandlers.SpectriteGeneralEventHandler;
import com.samuel.spectrite.init.*;
import com.samuel.spectrite.packets.PacketSpectriteExplosion;
import com.samuel.spectrite.packets.PacketSpectriteParticles;
import com.samuel.spectrite.update.UpdateNotifier;
import jline.internal.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class CommonProxy {
	
	static
    {
        FluidRegistry.enableUniversalBucket();
    }

	public void preInit(FMLPreInitializationEvent e) {
		ModEntities.initEntities(Spectrite.Instance);
		
		Spectrite.ItemPropertyGetterSpectrite = new IItemPropertyGetter() {

	        @Override
	        @SideOnly(Side.CLIENT)
	        public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
	            boolean flag = entityIn != null;
	            Entity entity = flag ? entityIn : stack.getItemFrame();
	            
	            if (worldIn == null && entity != null) {
	                worldIn = entity.world;
	            }

	            if (worldIn == null) {
	                return 0.0F;
	            } else {
	            	float time = MathHelper.ceil((((worldIn.getTotalWorldTime() >> 1) % 36)
	            		* 0.2777F) * 1000F) / 10000F;
	                return time;
	            }
	        }
	    };
		ModBlocks.createBlocks();
		ModItems.createItems();
		ModLootTables.registerLootTables();
		
		CapabilityManager.INSTANCE.register(ISpectriteBossCapability.class,
				new SpectriteBossCapability.DefaultImpl.Storage(),
				new SpectriteBossCapability.DefaultImpl.Factory());
		
		ModSounds.initSounds();
		ModTileEntities.initTileEntities();
		ModDispenserBehavior.initDispenserBehavior();
		ModPotions.initPotionEffects();
		ModEnchantments.initEnchantments();
		ModDamageSources.initDamageSources();
		ModBiomes.initBiomes();
		ModWorldGen.initWorldGen();

		FMLCommonHandler.instance().bus().register(Spectrite.Instance);
		MinecraftForge.EVENT_BUS.register(new ModBlocks());
		MinecraftForge.EVENT_BUS.register(new ModItems());
		MinecraftForge.EVENT_BUS.register(new ModEntities());
		MinecraftForge.EVENT_BUS.register(new ModBiomes());
		MinecraftForge.EVENT_BUS.register(new ModPotions());
		MinecraftForge.EVENT_BUS.register(new ModEnchantments());
		MinecraftForge.EVENT_BUS.register(new ModSounds());
		MinecraftForge.EVENT_BUS
			.register(new SpectriteGeneralEventHandler());
		MinecraftForge.EVENT_BUS
			.register(ModWorldGen.spectriteDungeon);
		MinecraftForge.EVENT_BUS
			.register(ModWorldGen.spectriteSkull);
		MinecraftForge.EVENT_BUS.register(Spectrite.Instance);
		MinecraftForge.EVENT_BUS.register(new UpdateNotifier());
	}

	public void init(FMLInitializationEvent e) {
		ModCrafting.initSmelting();
		ModCrafting.initBrewing();
	}

	public void postInit(FMLPostInitializationEvent e) {
		ModItems.populateBowItems();
	}

	public void performDispersedSpectriteDamage(World world, int power, int explosionPower, Vec3d hitCoords, Entity source, @Nullable Entity indirectSource, Random rand) {
		if (!world.isRemote) {
			BlockPos hitPos = new BlockPos(hitCoords);
			List<Entity> surrounding = world.getEntitiesWithinAABBExcludingEntity(source,
					new AxisAlignedBB(hitPos.north(power).west(power).down(power),
							hitPos.south(power).east(power).up(power)));

			boolean arrow = source instanceof EntityArrow;

			if (indirectSource == null) {
				indirectSource = source;
			}

			if (explosionPower > -1) {
				boolean allowGriefing;
				if (source instanceof EntitySpectriteWitherSkull) {
					allowGriefing = indirectSource != null && indirectSource instanceof EntityPlayer ?
						SpectriteConfig.items.spectriteWitherRodGriefing : world.getGameRules().getBoolean("mobGriefing");
				} else {
					allowGriefing = world.getGameRules().getBoolean("mobGriefing");
				}
				Spectrite.Proxy.newSpectriteExplosion(world, source, source.posX, source.posY + source.height / 2.0F,
					source.posZ, new Integer(explosionPower).floatValue(), false, allowGriefing, true);
			}

			for (int s = 0; s < surrounding.size(); s++) {
				if (surrounding.get(s) instanceof EntityLivingBase &&
					(indirectSource == null || (!(surrounding.get(s)).isOnSameTeam(indirectSource))
					&& !surrounding.get(s).equals(indirectSource))) {
					EntityLivingBase curEntity = ((EntityLivingBase) surrounding.get(s));
					double distance = curEntity.getDistanceSq(hitPos);
					double health = distance >= 1 ? 1.0D - Math.sqrt(distance) / (power + 2) : 1.0D;
					if (health > 0.0D) {
						if (!arrow || SpectriteConfig.items.spectriteArrowDamageMode != SpectriteConfig.EnumSpectriteArrowDamageMode.EXPLOSION) {
							ModPotions.SPECTRITE_DAMAGE.affectEntity(source, indirectSource, curEntity, power, health);
						}
					}
				}
			}

			try {
				if (power == 1) {
					world.playSound(null, hitPos, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.PLAYERS, 1.0F + Math.max(explosionPower, 0F),
						1.0F + (rand.nextFloat()) * 0.4F);
				} else if (power == 2) {
					world.playSound(null, hitPos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.75F + Math.max(explosionPower, 0F),
						1.0F + (rand.nextFloat()) * 0.4F);
				} else if (power == 3) {
					world.playSound(null, hitPos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F + Math.max(explosionPower, 0F),
						1.0F + (rand.nextFloat()) * 0.4F);
				} else if (power == 4) {
					world.playSound(null, hitPos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F + Math.max(explosionPower, 0F),
						0.75F + (world.rand.nextFloat()) * 0.4F);
					world.playSound(null, hitPos, ModSounds.fatality, SoundCategory.PLAYERS, 1.0F + Math.max(explosionPower, 0F),
						1.0F);
				}

				if (explosionPower < 0) {
					if (power == 1) {
						Spectrite.Proxy.spawnSpectriteExplosionParticle(world, true, hitCoords.x, hitCoords.y, hitCoords.z, 0, 0, 0);
					} else {
						Spectrite.Proxy.spawnSpectriteExplosionHugeParticle(world,
							hitCoords.x, hitCoords.y, hitCoords.z, 0, 0, 0, power);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void spawnSpectriteSpellParticle(World world, double posX, double posY, double posZ, double r, double g, double b, float hueOffset) {
		spawnSpectriteParticle(world, EnumSpectriteParticleTypes.SPECTRITE_SPELL, posX, posY, posZ, r, g, b, (double) hueOffset);
	}

	public void spawnSpectriteExplosionParticle(World world, boolean isLarge, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) {
		spawnSpectriteParticle(world, isLarge ? EnumSpectriteParticleTypes.SPECTRITE_EXPLOSION_LARGE : EnumSpectriteParticleTypes.SPECTRITE_EXPLOSION_NORMAL,
			posX, posY, posZ, xSpeed, ySpeed, zSpeed, 0D);
	}

	public void spawnSpectriteExplosionHugeParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed, double strength) {
		spawnSpectriteParticle(world, EnumSpectriteParticleTypes.SPECTRITE_EXPLOSION_HUGE, posX, posY, posZ, xSpeed, ySpeed, zSpeed, strength);
	}

	public void spawnSpectriteSmokeNormalParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) {
		spawnSpectriteParticle(world, EnumSpectriteParticleTypes.SPECTRITE_SMOKE_NORMAL, posX, posY, posZ, xSpeed, ySpeed, zSpeed, 0D);
	}

	public void spawnSpectriteSmokeLargeParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) {
		spawnSpectriteParticle(world, EnumSpectriteParticleTypes.SPECTRITE_SMOKE_LARGE, posX, posY, posZ, xSpeed, ySpeed, zSpeed, 0D);
	}

	private void spawnSpectriteParticle(World world, EnumSpectriteParticleTypes particleTypes, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed, double hueOffset) {
		for (int i = 0; i < world.playerEntities.size(); ++i)
		{
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP)world.playerEntities.get(i);
			if (entityPlayerMP.getPosition().getDistance((int)posX, (int)posY, (int)posZ) <= 1024D) {
				PacketSpectriteParticles spectriteParticlesPacket = new PacketSpectriteParticles(particleTypes,
						(float)posX, (float)posY, (float)posZ, (float)xSpeed, (float)ySpeed, (float)zSpeed, (float)0, 1, hueOffset);
				Spectrite.Network.sendTo(spectriteParticlesPacket, entityPlayerMP);
			}
		}
	}

	public SpectriteExplosion newSpectriteExplosion(World world, @Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking, boolean overrideSound)
	{
		SpectriteExplosion explosion = new SpectriteExplosion(world, entityIn, x, y, z, strength, isFlaming, isSmoking, overrideSound);
		if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion))
			return explosion;
		explosion.doExplosionA();
		explosion.doExplosionB(true);

		if (!world.isRemote) {
			if (!isSmoking)
			{
				explosion.clearAffectedBlockPositions();
			}

			for (EntityPlayer entityPlayer : ((WorldServer) world).playerEntities)
			{
				if (entityPlayer.getDistanceSq(x, y, z) < 4096.0D)
				{
					Spectrite.Network.sendTo(new PacketSpectriteExplosion(x, y, z, strength, isSmoking, explosion.getAffectedBlockPositions(),
						explosion.getPlayerKnockbackMap().get(entityPlayer)), (EntityPlayerMP) entityPlayer);
				}
			}
		}

		return explosion;
	}
}
