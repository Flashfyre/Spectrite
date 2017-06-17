package com.samuel.spectritemod.eventhandlers;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.capabilities.ISpectriteBossCapability;
import com.samuel.spectritemod.capabilities.SpectriteBossCapability;
import com.samuel.spectritemod.capabilities.SpectriteBossProvider;
import com.samuel.spectritemod.entities.EntitySpectriteArrow;
import com.samuel.spectritemod.etc.SpectriteHelper;
import com.samuel.spectritemod.init.ModSounds;
import com.samuel.spectritemod.items.ItemSpectriteArrow;
import com.samuel.spectritemod.items.ItemSpectriteBow;
import com.samuel.spectritemod.items.ItemSpectriteOrb;
import com.samuel.spectritemod.items.ItemSpectriteShield;
import com.samuel.spectritemod.items.ItemSpectriteShieldSpecial;
import com.samuel.spectritemod.items.ItemSpectriteSword;
import com.samuel.spectritemod.items.ItemSpectriteSwordSpecial;
import com.samuel.spectritemod.packets.PacketSyncSpectriteBoss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.entity.player.PlayerEvent.StopTracking;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpectriteGeneralEventHandler {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onEntitySpawn(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityLiving) {
			EntityLivingBase entity = (EntityLivingBase) e.getEntity();
			if (entity instanceof EntityZombie || entity instanceof AbstractSkeleton) {
				if (!e.getWorld().isRemote && entity instanceof EntityLiving && SpectriteMod.Config.spectriteBossSpawnRate > 0d
						&& (int) entity.getUniqueID().getMostSignificantBits() % (100 / SpectriteMod.Config.spectriteBossSpawnRate) == 0) {
					final EntityLiving entityLiving = (EntityLiving) e.getEntity();
					final boolean hasPerfectWeapon = SpectriteMod.Config.spectriteBossPerfectWeaponRate > 0d
						&& (entityLiving.getClass() == EntityWitherSkeleton.class
						|| (int) entity.getUniqueID().getLeastSignificantBits() % (100 / SpectriteMod.Config.spectriteBossPerfectWeaponRate) == 0);
					entityLiving.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(SpectriteMod.ItemSpectriteHelmet));
	                entityLiving.setDropChance(EntityEquipmentSlot.HEAD, new Double(SpectriteMod.Config.spectriteBossArmourDropRate).floatValue() / 100f);
	                entityLiving.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(SpectriteMod.ItemSpectriteChestplate));
	                entityLiving.setDropChance(EntityEquipmentSlot.CHEST, new Double(SpectriteMod.Config.spectriteBossArmourDropRate).floatValue() / 100f);
	                entityLiving.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(SpectriteMod.ItemSpectriteLeggings));
	                entityLiving.setDropChance(EntityEquipmentSlot.LEGS, new Double(SpectriteMod.Config.spectriteBossArmourDropRate).floatValue() / 100f);
	                entityLiving.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(SpectriteMod.ItemSpectriteBoots));
	                entityLiving.setDropChance(EntityEquipmentSlot.FEET, new Double(SpectriteMod.Config.spectriteBossArmourDropRate).floatValue() / 100f);
	                if (entityLiving.getClass() == EntitySkeleton.class) {
	                	entityLiving.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(
                    		!hasPerfectWeapon ? SpectriteMod.ItemSpectriteBow : SpectriteMod.ItemSpectriteBowSpecial));
    	                entityLiving.setDropChance(EntityEquipmentSlot.MAINHAND, new Double(SpectriteMod.Config.spectriteBossBowDropRate).floatValue() / 100f);
	                	Field aiArrowAttack = SpectriteHelper.findObfuscatedField(AbstractSkeleton.class, new String[] { "aiArrowAttack", "field_85037_d" });
	                	try {
							entityLiving.tasks.removeTask((EntityAIAttackRangedBow) aiArrowAttack.get(entityLiving));
							EntityAIAttackRangedBow customAiArrowAttack = new EntityAIAttackRangedBow((EntitySkeleton) entityLiving, 1.0D, 20, 15.0F) {
								@Override
								protected boolean isBowInMainhand()
							    {
									Field entity = SpectriteHelper.findObfuscatedField(EntityAIAttackRangedBow.class, new String[] { "entity", "field_188499_a" });
									AbstractSkeleton entityInstance;
									try {
										entityInstance = (AbstractSkeleton) entity.get(this);
										return !entityInstance.getHeldItemMainhand().isEmpty() && entityInstance.getHeldItemMainhand().getItem() instanceof ItemBow;
									} catch (Exception e) {
										e.printStackTrace();
									}
									return false;
							    }
							};
							entityLiving.tasks.addTask(4, customAiArrowAttack);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
	                	entityLiving.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(SpectriteMod.ItemSpectriteArrow, e.getWorld().rand.nextInt(4) + 1));
	                	entityLiving.setDropChance(EntityEquipmentSlot.OFFHAND, new Double(SpectriteMod.Config.spectriteBossArrowDropRate).floatValue() / 100f);
	                } else {
	                	final boolean hasLegendBlade = SpectriteMod.Config.spectriteBossLegendSwordRate > 0d && 
                			(int) entity.getUniqueID().getMostSignificantBits() % (100 / SpectriteMod.Config.spectriteBossLegendSwordRate) == 0;
	                	if (!hasLegendBlade) {
	                		entityLiving.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(
	                			!hasPerfectWeapon ? SpectriteMod.ItemSpectriteSword : SpectriteMod.ItemSpectriteSwordSpecial));
	                	} else {
	                		entityLiving.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(
	                			!hasPerfectWeapon ? SpectriteMod.ItemSpectriteSword2 : SpectriteMod.ItemSpectriteSword2Special));
	                	}
    	                entityLiving.setDropChance(EntityEquipmentSlot.MAINHAND, new Double(SpectriteMod.Config.spectriteBossSwordDropRate).floatValue() / 100f);
	                	entityLiving.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(SpectriteMod.ItemSpectriteOrb));
	                	entityLiving.setDropChance(EntityEquipmentSlot.OFFHAND, new Double(SpectriteMod.Config.spectriteBossOrbDropRate).floatValue() / 100f);
	                }
	                entityLiving.getCapability(SpectriteBossProvider.sbc, null).setEnabled(true);
	                entityLiving.getCapability(SpectriteBossProvider.sbc, null).setPerfectWeapon(hasPerfectWeapon);
                	entityLiving.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100f);
                	entityLiving.setHealth(100f);
				}
			}
		} else if (e.getEntity() instanceof EntityTippedArrow) {
			EntityArrow arrow = (EntityArrow) e.getEntity();
			if (arrow.shootingEntity != null && arrow.shootingEntity instanceof EntityLivingBase) {
				EntityLivingBase shootingEntity = (EntityLivingBase) arrow.shootingEntity;
				ISpectriteBossCapability sbc = shootingEntity.getCapability(SpectriteBossProvider.sbc, null);
				if (sbc != null && sbc.isEnabled() && !((EntityLivingBase) shootingEntity).getHeldItemOffhand().isEmpty()
					&& ((EntityLivingBase) shootingEntity).getHeldItemOffhand().getItem().getClass() == ItemSpectriteArrow.class) {
					e.setCanceled(true);
					EntitySpectriteArrow spectriteArrow = (EntitySpectriteArrow) SpectriteMod.ItemSpectriteArrow.createArrow(e.getWorld(), shootingEntity.getHeldItemOffhand(), shootingEntity);
					float velocity = Math.min(e.getWorld().rand.nextFloat() + 0.25f, 1.0f);
					spectriteArrow.setAim(shootingEntity, shootingEntity.rotationPitch, shootingEntity.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);
					if (velocity == 1.0f) {
						spectriteArrow.setIsCritical(true);
					}
					e.getWorld().spawnEntity(spectriteArrow);
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
					boolean hasPerfectSword = !entity.getEntityWorld().isRemote && isSpectriteBoss && SpectriteMod.Config.spectriteBossPerfectWeaponRate > 0
						&& (entity instanceof EntityWitherSkeleton || (int) entity.getUniqueID().getLeastSignificantBits() % (100 / SpectriteMod.Config.spectriteBossPerfectWeaponRate) == 0);
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
			Item heldItem = !heldItemStack.isEmpty() ? heldItemStack.getItem() : null;
			if (heldItem instanceof ItemSpectriteSword) {
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
								particle = EnumParticleTypes.EXPLOSION_LARGE;
								
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
								particle = EnumParticleTypes.EXPLOSION_NORMAL;
								world.playSound(null, pos, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.PLAYERS, 1.0F,
										1.0F + (world.rand.nextFloat()) * 0.4F);
						}
						
						if (e.getEntityLiving() != null && particle != null) {
							worldServer.spawnParticle(particle,
								particle.getShouldIgnoreRange(), target.posX,
								target.getEntityBoundingBox().minY, target.posZ, power == 2 ? 1 : power == 4 ? 3 : 7,
								world.rand.nextGaussian(), world.rand.nextGaussian(),
								world.rand.nextGaussian(), 0.0D, new int[0]);
						}
						
						for (int s = 0; s < surrounding.size(); s++) {
							if (surrounding.get(s) instanceof EntityLivingBase &&
								(!((EntityLivingBase) surrounding.get(s)).isOnSameTeam(attacker))) {
								EntityLivingBase curEntity = ((EntityLivingBase) surrounding.get(s));
								double distance = curEntity.getDistanceToEntity(target);
								int relPower = (int) Math.ceil(power - distance);
								int damageLevel = relPower;
								if (!curEntity.getActiveItemStack().isEmpty() && curEntity.isActiveItemStackBlocking() && curEntity.getActiveItemStack().getItem() instanceof ItemShield) {
									damageLevel = Math.max(relPower - (curEntity.getActiveItemStack().getItem() instanceof ItemSpectriteShield ?
										curEntity.getActiveItemStack().getItem() instanceof ItemSpectriteShieldSpecial ? 2 : 1 : 0), 0);
									relPower = Math.max(relPower - (relPower - damageLevel), 0);
									int shieldDamage = 0;
									switch (damageLevel) {
										case 1:
											shieldDamage = 8;
											break;
										case 2:
											shieldDamage = 32;
											break;
										case 3:
											shieldDamage = 128;
											break;
										case 4:
											shieldDamage = 512;
											break;
									}
									if (shieldDamage > 0) {
										curEntity.getActiveItemStack().damageItem(shieldDamage, curEntity);
									}
								}
								curEntity.addPotionEffect(new PotionEffect(!curEntity.isEntityUndead() ? MobEffects.INSTANT_DAMAGE :
									MobEffects.INSTANT_HEALTH, 5,
									relPower - (curEntity instanceof EntityPlayer ? 1 : 0)));
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
				final int healingLevel = Math.max(entity.getEntityWorld().getDifficulty().ordinal() - (!entity.getHeldItemOffhand().isEmpty()
					&& entity.getHeldItemOffhand().getItem().getClass() == ItemSpectriteOrb.class ? 0 : 1), 0);
				switch (healingLevel) {
					case 0:
						healRate = 40;
						break;
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
	public void onEntityLivingHurt(LivingHurtEvent e) {
		if (!e.getEntityLiving().getEntityWorld().isRemote) {
			EntityLivingBase target = e.getEntityLiving();
			if (e.getSource().getDamageType().equals("player") && e.getEntityLiving().hasCapability(SpectriteBossProvider.sbc, null)) {
				ISpectriteBossCapability sbc = target.getCapability(SpectriteBossProvider.sbc, null);
				if (sbc.isEnabled()) {
					((BossInfoServer) sbc.getBossInfo()).addPlayer((EntityPlayerMP) e.getSource().getSourceOfDamage());
				}
			}
			if (target instanceof EntityPlayer && !target.getActiveItemStack().isEmpty() && target.getActiveItemStack().getItem() instanceof ItemSpectriteShield) {
				ItemStack activeItemStack = target.getActiveItemStack();
				if ((activeItemStack.getItem() instanceof ItemSpectriteShieldSpecial && e.getAmount() >= 8f) || e.getAmount() >= 5f) {
		            int i = 1 + (int) Math.floor(e.getAmount());
		            activeItemStack.damageItem(i, target);
	
		            if (activeItemStack.getCount() <= 0)
		            {
		                EnumHand enumhand = target.getActiveHand();
		                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem((EntityPlayer) target, activeItemStack, enumhand);
	
		                if (enumhand == EnumHand.MAIN_HAND)
		                {
		                    target.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
		                }
		                else
		                {
		                    target.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
		                }
	
		                target.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + target.getEntityWorld().rand.nextFloat() * 0.4F);
		            }
				}
	        }
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onStartTrackingSpectriteBoss(StartTracking e) {
		Entity entity = e.getTarget();
		if (!entity.getEntityWorld().isRemote && entity instanceof EntityLivingBase && entity.hasCapability(SpectriteBossProvider.sbc, null)) {
			ISpectriteBossCapability sbc = entity.getCapability(SpectriteBossProvider.sbc, null);
			if (sbc.isEnabled()) {
                SpectriteMod.Network.sendTo(new PacketSyncSpectriteBoss(entity.getUniqueID(), true, sbc.isPerfectWeapon()), (EntityPlayerMP) e.getEntityPlayer());
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
			List<Entity> entityResults = world.getEntities(EntityLivingBase.class, new Predicate<Entity>()
		    {
		        public boolean apply(@Nullable Entity p_apply_1_)
		        {
		            return p_apply_1_.hasCapability(SpectriteBossProvider.sbc, null) && p_apply_1_.getCapability(SpectriteBossProvider.sbc, null).isEnabled();
		        }
		    });
			for (Entity entity : entityResults) {
				((BossInfoServer) entity.getCapability(SpectriteBossProvider.sbc, null).getBossInfo()).removePlayer((EntityPlayerMP) player);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onPlayerTravelToDimension(EntityTravelToDimensionEvent e) {
		if (e.getEntity() instanceof EntityPlayerMP) {
			EntityPlayer player = (EntityPlayer) e.getEntity();
			World world = player.getEntityWorld();
			List<Entity> entityResults = world.getEntities(EntityLivingBase.class, new Predicate<Entity>()
		    {
		        public boolean apply(@Nullable Entity p_apply_1_)
		        {
		            return p_apply_1_.hasCapability(SpectriteBossProvider.sbc, null) && p_apply_1_.getCapability(SpectriteBossProvider.sbc, null).isEnabled();
		        }
		    });
			for (Entity entity : entityResults) {
				((BossInfoServer) entity.getCapability(SpectriteBossProvider.sbc, null).getBossInfo()).removePlayer((EntityPlayerMP) player);
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
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onFOVUpdateEvent(FOVUpdateEvent e) {
		if (e.getEntity() != null
			&& e.getEntity() instanceof EntityPlayer
			&& !e.getEntity().getHeldItemMainhand().isEmpty()
			&& e.getEntity().getHeldItemMainhand().getItem() instanceof ItemSpectriteBow) {
			EntityPlayer player = e.getEntity();
			int i = player.getItemInUseCount();
			float f1 = i / 20.0F;

			if (f1 > 1.0F) {
				f1 = 1.0F;
			} else {
				f1 *= f1;
			}

			e.setNewfov(e.getFov() * (1.0F - f1 * 0.15F));
		}
	}
}
