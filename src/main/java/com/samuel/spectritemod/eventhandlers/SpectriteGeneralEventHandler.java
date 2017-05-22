package com.samuel.spectritemod.eventhandlers;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.google.common.util.concurrent.ListenableFutureTask;
import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.capabilities.ISpectriteBossCapability;
import com.samuel.spectritemod.capabilities.SpectriteBossCapability;
import com.samuel.spectritemod.capabilities.SpectriteBossProvider;
import com.samuel.spectritemod.client.renderer.entity.layers.LayerSpectriteArmor;
import com.samuel.spectritemod.etc.SpectriteHelper;
import com.samuel.spectritemod.init.ModSounds;
import com.samuel.spectritemod.items.ItemSpectriteArmor;
import com.samuel.spectritemod.items.ItemSpectriteSword;
import com.samuel.spectritemod.items.ItemSpectriteSwordSpecial;
import com.samuel.spectritemod.packets.PacketSyncSpectriteBoss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.entity.player.PlayerEvent.StopTracking;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class SpectriteGeneralEventHandler {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onEntitySpawn(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityLiving) {
			EntityLivingBase entity = (EntityLivingBase) e.getEntity();
			if (entity instanceof EntityZombie || entity instanceof AbstractSkeleton) {
				if (!e.getWorld().isRemote && entity instanceof EntityLiving && SpectriteMod.Config.spectriteBossSpawnRate > 0d
						&& (int) entity.getUniqueID().getMostSignificantBits() % (100 / SpectriteMod.Config.spectriteBossSpawnRate) == 0) {
					final EntityLiving entityLiving = (EntityLiving) e.getEntity();
					final boolean hasPerfectSword = SpectriteMod.Config.spectriteBossPerfectSwordRate > 0d
						&& (entityLiving.getClass() == EntityWitherSkeleton.class
						|| (int) entity.getUniqueID().getLeastSignificantBits() % (100 / SpectriteMod.Config.spectriteBossPerfectSwordRate) == 0);
					entityLiving.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(SpectriteMod.ItemSpectriteHelmet));
	                entityLiving.setDropChance(EntityEquipmentSlot.HEAD, new Double(SpectriteMod.Config.spectriteBossArmourDropRate).floatValue() / 100f);
	                entityLiving.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(SpectriteMod.ItemSpectriteChestplate));
	                entityLiving.setDropChance(EntityEquipmentSlot.CHEST, new Double(SpectriteMod.Config.spectriteBossArmourDropRate).floatValue() / 100f);
	                entityLiving.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(SpectriteMod.ItemSpectriteLeggings));
	                entityLiving.setDropChance(EntityEquipmentSlot.LEGS, new Double(SpectriteMod.Config.spectriteBossArmourDropRate).floatValue() / 100f);
	                entityLiving.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(SpectriteMod.ItemSpectriteBoots));
	                entityLiving.setDropChance(EntityEquipmentSlot.FEET, new Double(SpectriteMod.Config.spectriteBossArmourDropRate).floatValue() / 100f);
	                entityLiving.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(!hasPerfectSword ? SpectriteMod.ItemSpectriteSword2
                		: SpectriteMod.ItemSpectriteSword2Special));
	                entityLiving.setDropChance(EntityEquipmentSlot.MAINHAND, new Double(SpectriteMod.Config.spectriteBossSwordDropRate).floatValue() / 100f);
	                entityLiving.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(SpectriteMod.ItemSpectriteOrb));
	                entityLiving.setDropChance(EntityEquipmentSlot.OFFHAND, new Double(SpectriteMod.Config.spectriteBossOrbDropRate).floatValue() / 100f);
	                entityLiving.getCapability(SpectriteBossProvider.sbc, null).setEnabled(true);
	                entityLiving.getCapability(SpectriteBossProvider.sbc, null).setPerfectSword(hasPerfectSword);
                	entityLiving.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100f);
                	entityLiving.setHealth(100f);
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> e) {
		if (e.getObject() instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase) e.getObject();
			if ((entity instanceof AbstractSkeleton || entity instanceof EntityZombie)) {
				if (!entity.hasCapability(SpectriteBossProvider.sbc, null)) {
					boolean isSpectriteBoss = !entity.getEntityWorld().isRemote && SpectriteMod.Config.spectriteBossSpawnRate > 0d
						&& (int) entity.getUniqueID().getMostSignificantBits() % (100 / SpectriteMod.Config.spectriteBossSpawnRate) == 0;
					boolean hasPerfectSword = !entity.getEntityWorld().isRemote && isSpectriteBoss && SpectriteMod.Config.spectriteBossPerfectSwordRate > 0
						&& (entity instanceof EntityWitherSkeleton || (int) entity.getUniqueID().getLeastSignificantBits() % (100 / SpectriteMod.Config.spectriteBossPerfectSwordRate) == 0);
					e.addCapability(SpectriteBossCapability.DefaultImpl.resLoc,
						new SpectriteBossProvider(new SpectriteBossCapability.DefaultImpl(entity, isSpectriteBoss, hasPerfectSword)));
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onSpectriteSwordAttack(LivingAttackEvent e) {
		Entity attacker = e.getSource().getEntity();
		if (!e.getSource().isMagicDamage() && attacker instanceof EntityLivingBase) {
			ItemStack heldItemStack = ((EntityLivingBase) attacker).getHeldItem(EnumHand.MAIN_HAND);
			Item heldItem = heldItemStack != null ? heldItemStack.getItem() : null;
			if (heldItem != null && heldItem instanceof ItemSpectriteSword) {
				World world = e.getEntityLiving().getEntityWorld();
				EntityLivingBase target = e.getEntityLiving();
				BlockPos pos = new BlockPos(e.getSource().getDamageLocation());
				if (!world.isRemote) {
					if (!(attacker instanceof EntityPlayer) || (((EntityPlayer) attacker).getCooldownTracker().getCooldown(heldItem, 0) == 0.0f && !attacker.isSneaking())) {
						WorldServer worldServer = (WorldServer) world;
						int power = ((heldItem instanceof ItemSpectriteSwordSpecial) ? 2 : 1) + (!((ItemSpectriteSword) heldItem).isLegendBlade() ? 0 : 2);
						
						List<Entity> surrounding = world.getEntitiesWithinAABBExcludingEntity(attacker,
							new AxisAlignedBB(pos.north(power).west(power).down(power),
							pos.south(power).east(power).up(power)));
					
						if (target.getMaxHealth() >= 200.0F) {
							target.attackEntityFrom(DamageSource.causeThornsDamage(attacker), 5 + (9 * power));
						}
						EnumParticleTypes particle = null;
						switch (power) {
							case 2:
								particle = EnumParticleTypes.EXPLOSION_NORMAL;
								
								world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.75F,
									1.0F + (world.rand.nextFloat()) * 0.4F);
							break;
							case 3:
								particle = EnumParticleTypes.EXPLOSION_LARGE;
								
								world.playSound(null, pos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F,
									1.0F + (world.rand.nextFloat()) * 0.4F);
							break;
							case 4:
								particle = EnumParticleTypes.EXPLOSION_HUGE;
								
								world.playSound(null, pos, ModSounds.explosion, SoundCategory.PLAYERS, 0.75F,
										0.75F + (world.rand.nextFloat()) * 0.4F);
								world.playSound(null, pos, ModSounds.fatality, SoundCategory.PLAYERS, 1.0F,
									1.0F);
								break;
							default:
								particle = EnumParticleTypes.FIREWORKS_SPARK;
								world.playSound(null, pos, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.PLAYERS, 1.0F,
										1.0F + (world.rand.nextFloat()) * 0.4F);
						}
						
						if (e.getEntityLiving() != null && particle != null) {
							worldServer.spawnParticle(particle,
								particle.getShouldIgnoreRange(), target.posX,
								target.getEntityBoundingBox().minY, target.posZ, 7,
								world.rand.nextGaussian(), world.rand.nextGaussian(),
								world.rand.nextGaussian(), 0.0D, new int[0]);
						}
						
						for (int s = 0; s < surrounding.size(); s++) {
							if (surrounding.get(s) instanceof EntityLivingBase &&
								(!((EntityLivingBase) surrounding.get(s)).isOnSameTeam(attacker) &&
								(surrounding.get(s).equals(target) || (attacker instanceof EntityPlayer && !(target instanceof EntityPlayer))))) {
								EntityLivingBase curEntity = ((EntityLivingBase) surrounding.get(s));
								double distance = curEntity.getDistanceToEntity(target);
								curEntity.addPotionEffect(new PotionEffect(!curEntity.isEntityUndead() ? MobEffects.INSTANT_DAMAGE :
									MobEffects.INSTANT_HEALTH, 5,
									(int) Math.floor(power - distance) - (attacker instanceof EntityPlayer ? 0 : 1)));
							}
						}
						
						if (attacker instanceof EntityPlayer && !((EntityPlayer) attacker).isCreative()) {
							((EntityPlayer) attacker).getCooldownTracker().setCooldown(heldItem, (int) Math.round(SpectriteMod.Config.spectriteToolCooldown * 20));
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onSpectriteBossUpdate(LivingUpdateEvent e) {
		EntityLivingBase entity = e.getEntityLiving();
		if (entity.hasCapability(SpectriteBossProvider.sbc, null)) {
			ISpectriteBossCapability sbc = entity.getCapability(SpectriteBossProvider.sbc, null);
			if (sbc.isEnabled()) {
				final int healRate;
				switch (entity.getEntityWorld().getDifficulty().ordinal()) {
					case 0:
					case 1:
						healRate = 20;
						break;
					case 2:
						healRate = 10;
						break;
					default:
						healRate = 5;
						break;
				}
				if (entity.ticksExisted % healRate == 0) {
					entity.heal(1f);
				}
				if (sbc.getBossInfo() != null) {
					float percent = entity.getHealth() / entity.getMaxHealth();
					sbc.getBossInfo().setPercent(percent);
					if (percent == 0f) {
						for (EntityPlayerMP p : ((BossInfoServer) sbc.getBossInfo()).getPlayers()) {
							((BossInfoServer) sbc.getBossInfo()).removePlayer(p);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onPlayerHurtSpectriteBoss(LivingHurtEvent e) {
		if (!e.getEntityLiving().getEntityWorld().isRemote && e.getSource().getDamageType().equals("player") && e.getEntityLiving().hasCapability(SpectriteBossProvider.sbc, null)) {
			ISpectriteBossCapability sbc = e.getEntityLiving().getCapability(SpectriteBossProvider.sbc, null);
			if (sbc.isEnabled()) {
				((BossInfoServer) sbc.getBossInfo()).addPlayer((EntityPlayerMP) e.getSource().getSourceOfDamage());
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onStartTrackingSpectriteBoss(StartTracking e) {
		Entity entity = e.getTarget();
		if (!entity.getEntityWorld().isRemote && entity instanceof EntityLivingBase && entity.hasCapability(SpectriteBossProvider.sbc, null)) {
			ISpectriteBossCapability sbc = entity.getCapability(SpectriteBossProvider.sbc, null);
			if (sbc.isEnabled()) {
                SpectriteMod.Network.sendTo(new PacketSyncSpectriteBoss(entity.getUniqueID(), true, sbc.isPerfectSword()), (EntityPlayerMP) e.getEntityPlayer());
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onStopTrackingSpectriteBoss(StopTracking e) {
		Entity entity = e.getTarget();
		if (!entity.getEntityWorld().isRemote && entity instanceof EntityLivingBase && entity.hasCapability(SpectriteBossProvider.sbc, null)) {
			ISpectriteBossCapability sbc = entity.getCapability(SpectriteBossProvider.sbc, null);
			if (sbc.isEnabled()) {
				((BossInfoServer) sbc.getBossInfo()).removePlayer((EntityPlayerMP) e.getEntityPlayer());
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onPlayerDeath(LivingDeathEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayerMP) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			World world = player.getEntityWorld();
			for (UUID uuid : SpectriteBossProvider.bossInfoMap.keySet()) {
				List<Entity> entityResults = world.getLoadedEntityList().stream().filter(m -> m.getUniqueID().equals(uuid)).collect(Collectors.toList());
				if (!entityResults.isEmpty()) {
					Entity entity = entityResults.get(0);
					if (entity.getCapability(SpectriteBossProvider.sbc, null).getBossInfo() != null) {
						((BossInfoServer) entity.getCapability(SpectriteBossProvider.sbc, null).getBossInfo()).removePlayer((EntityPlayerMP) player);
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onPlayerTravelToDimension(EntityTravelToDimensionEvent e) {
		if (e.getEntity() instanceof EntityPlayerMP) {
			EntityPlayer player = (EntityPlayer) e.getEntity();
			World world = player.getEntityWorld();
			for (UUID uuid : SpectriteBossProvider.bossInfoMap.keySet()) {
				List<Entity> entityResults = world.getLoadedEntityList().stream().filter(m -> m.getUniqueID().equals(uuid)).collect(Collectors.toList());
				if (!entityResults.isEmpty()) {
					Entity entity = entityResults.get(0);
					if (entity.getCapability(SpectriteBossProvider.sbc, null).getBossInfo() != null) {
						((BossInfoServer) entity.getCapability(SpectriteBossProvider.sbc, null).getBossInfo()).removePlayer((EntityPlayerMP) player);
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onAnvilRepair(AnvilRepairEvent e) {
		if (!e.getItemResult().isEmpty() && (e.getItemResult().getItem().equals(SpectriteMod.ItemSpectriteSword2) || e.getItemResult().getItem().equals(SpectriteMod.ItemSpectriteSword2Special))) {
			if (e.getItemResult().getTagCompound().hasKey("display")) {
				e.getItemResult().getTagCompound().removeTag("display");
			}
		}
	}
}
