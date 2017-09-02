package com.samuel.spectrite.eventhandlers;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.blocks.BlockSpectrite;
import com.samuel.spectrite.capabilities.ISpectriteBossCapability;
import com.samuel.spectrite.capabilities.SpectriteBossCapability;
import com.samuel.spectrite.capabilities.SpectriteBossProvider;
import com.samuel.spectrite.entities.*;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.init.ModBlocks;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModPotions;
import com.samuel.spectrite.init.ModWorldGen;
import com.samuel.spectrite.items.*;
import com.samuel.spectrite.packets.PacketSyncSpectriteBoss;
import com.samuel.spectrite.packets.PacketSyncSpectriteDungeonSpawnPos;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTippedArrow;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.entity.player.PlayerEvent.StopTracking;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

public class SpectriteGeneralEventHandler {
	
	private static boolean skipAttackPlayer = false;

	private static Field healthField = null;
	private static Field immuneToFireField = null;

	private static Field potionField_tippedArrow = null;
	private static Field potionField_areaEffectCloud = null;
	private static Method getArrowStackMethod = null;
	
	private static Field primaryEffectField = null;
    private static Field secondaryEffectField = null;
    private static Field paymentField = null;
    private static Field customNameField = null;
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onEntitySpawn(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityLiving) {
			EntityLivingBase entity = (EntityLivingBase) e.getEntity();
			if (entity instanceof AbstractSkeleton && !(entity instanceof ISpectriteBipedMob)) {
				if (!e.getWorld().isRemote && entity instanceof EntityLiving && (SpectriteConfig.mobs.spectriteMobSpawnRate > 0d
						&& (int) entity.getUniqueID().getLeastSignificantBits() % (100 / SpectriteConfig.mobs.spectriteMobSpawnRate) == 0)) {
					Class entityClass = entity.getClass();
					EntityLiving spectriteEntity = null;

					if (entityClass == EntitySkeleton.class) {
						spectriteEntity = new EntitySpectriteSkeleton(entity.getEntityWorld());
					} else if (entityClass == EntityWitherSkeleton.class) {
						spectriteEntity = new EntitySpectriteWitherSkeleton(entity.getEntityWorld());
					} else if (entityClass == EntityCreeper.class) {
						spectriteEntity = new EntitySpectriteCreeper(entity.getEntityWorld());
					} else if (entityClass == EntityBlaze.class) {
						spectriteEntity = new EntitySpectriteBlaze(entity.getEntityWorld());
					}

					if (spectriteEntity != null) {
						e.setCanceled(true);
						spectriteEntity.setLocationAndAngles(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ, entity.rotationYaw, entity.rotationPitch);
						e.getWorld().spawnEntity(spectriteEntity);
						spectriteEntity.onInitialSpawn(e.getWorld().getDifficultyForLocation(entity.getPosition()), null);
					}
				}
			}
		} else if (e.getEntity() instanceof EntityItem) {
			EntityItem entityItem = (EntityItem) e.getEntity();

			if (!entityItem.getItem().isEmpty() && entityItem.getItem().getItem() instanceof IPerfectSpectriteItem) {
				if (healthField == null) {
					healthField = SpectriteHelper.findObfuscatedField(EntityItem.class, "health", "field_70291_e");
				}
				if (immuneToFireField == null) {
					immuneToFireField = SpectriteHelper.findObfuscatedField(Entity.class, "isImmuneToFire", "field_70178_ae");
				}
				try {
					healthField.set(entityItem, 5000);
					immuneToFireField.set(entityItem, true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getEntity() instanceof EntityTippedArrow) {
			EntityTippedArrow arrow = (EntityTippedArrow) e.getEntity();
			if (potionField_tippedArrow == null) {
				potionField_tippedArrow = SpectriteHelper.findObfuscatedField(EntityTippedArrow.class, "potion", "field_184560_g");
			}
			if (getArrowStackMethod == null) {
				getArrowStackMethod = SpectriteHelper.findObfuscatedMethod(EntityTippedArrow.class, "getArrowStack", "func_184550_j");
			}
			PotionType potionType;
			try {
				potionType = (PotionType) potionField_tippedArrow.get(arrow);
				Potion potion;
				if (!potionType.getEffects().isEmpty() && ((potion = potionType.getEffects().get(0).getPotion()).equals(ModPotions.SPECTRITE) 
					|| potion.equals(ModPotions.SPECTRITE_DAMAGE) || potion.equals(ModPotions.SPECTRITE_RESISTANCE))) {
					e.setCanceled(true);
					Object arrowStackObj = getArrowStackMethod.invoke(arrow);
					if (arrowStackObj != null) {
						ItemTippedArrow arrowItem = (ItemTippedArrow) (((ItemStack) arrowStackObj).getItem());
						ItemStack newStack = new ItemStack(arrowItem);
						EntitySpectriteTippedArrow newArrow = new EntitySpectriteTippedArrow((EntityTippedArrow) arrowItem.createArrow(e.getWorld(),
							newStack, arrow.shootingEntity != null ? (EntityLivingBase) arrow.shootingEntity : null), potionType);
						e.getWorld().spawnEntity(newArrow);
						if (arrow.shootingEntity != null && arrow.shootingEntity instanceof EntityLivingBase) {
							SpectriteHelper.damageBow((EntityLivingBase) arrow.shootingEntity, potionType);
						}
					}
				} else if (arrow.shootingEntity != null && arrow.shootingEntity instanceof EntityLivingBase) {
					EntityLivingBase shootingEntity = (EntityLivingBase) arrow.shootingEntity;
					if (!shootingEntity.getHeldItemOffhand().isEmpty()
						&& shootingEntity.getHeldItemOffhand().getItem().getClass() == ItemSpectriteArrow.class) {
						e.setCanceled(true);
						EntitySpectriteArrow spectriteArrow = (EntitySpectriteArrow) ModItems.spectrite_arrow.createArrow(e.getWorld(), shootingEntity.getHeldItemOffhand(), shootingEntity);
						float velocity = Math.min(e.getWorld().rand.nextFloat() + 0.25f, 1.0f);
						spectriteArrow.setAim(shootingEntity, shootingEntity.rotationPitch, shootingEntity.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);
						if (velocity == 1.0f) {
							spectriteArrow.setIsCritical(true);
						}
						e.getWorld().spawnEntity(spectriteArrow);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (!e.getWorld().isRemote && e.getEntity() instanceof EntityPlayer) {
			if (e.getWorld().getWorldType() != WorldType.FLAT && e.getWorld().getActualHeight() >= 30 && SpectriteConfig.spectriteDungeon.generateSpectriteDungeon) {
				Spectrite.Network.sendTo(new PacketSyncSpectriteDungeonSpawnPos(ModWorldGen.spectriteDungeon.getSpawnPos()), (EntityPlayerMP) e.getEntity());
			}
		} else if (!e.getWorld().isRemote && e.getEntity() instanceof EntityAreaEffectCloud) {
			EntityAreaEffectCloud entity = (EntityAreaEffectCloud) e.getEntity();
			if (potionField_areaEffectCloud == null) {
				potionField_areaEffectCloud = SpectriteHelper.findObfuscatedField(EntityAreaEffectCloud.class, new String[] { "potion", "field_184502_e" });
			}
			PotionType potionType;
			try {
				potionType = (PotionType) potionField_areaEffectCloud.get(entity);
				if (!potionType.getEffects().isEmpty()) {
					Potion potion = potionType.getEffects().get(0).getPotion();
					if (potion.equals(ModPotions.SPECTRITE) || potion.equals(ModPotions.SPECTRITE_DAMAGE) || potion.equals(ModPotions.SPECTRITE_RESISTANCE)) {
						e.setCanceled(true);
						EntitySpectriteAreaEffectCloud newEntity = new EntitySpectriteAreaEffectCloud(e.getWorld(), entity.posX, entity.posY, entity.posZ);
						
						newEntity.setOwner(entity.getOwner());
				        newEntity.setRadius(3.0F);
				        newEntity.setRadiusOnUse(-0.5F);
				        newEntity.setWaitTime(10);
				        newEntity.setRadiusPerTick(-newEntity.getRadius() / newEntity.getDuration());
				        newEntity.setPotionType(potionType);
			            newEntity.addEffect(potionType.getEffects().get(0));
			            
			            e.getWorld().spawnEntity(newEntity);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> e) {
		if (e.getObject() instanceof ISpectriteBipedMob) {
			EntityLivingBase entity = (EntityLivingBase) e.getObject();
			if (!entity.hasCapability(SpectriteBossProvider.sbc, null)) {
				e.addCapability(SpectriteBossCapability.DefaultImpl.resLoc,
					new SpectriteBossProvider(new SpectriteBossCapability.DefaultImpl(entity, ((ISpectriteBipedMob) entity).isBoss())));
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled=true)
	public void onLivingAttack(LivingAttackEvent e) {
		Entity attacker = e.getSource().getTrueSource();
		EntityLivingBase target = e.getEntityLiving();
		if (!target.world.isRemote) {
			if (target instanceof EntityPlayer && e.getAmount() > 0.0F) {
				skipAttackPlayer = !skipAttackPlayer;
				if (skipAttackPlayer) {
					return;
				}
			}
			if (!e.getSource().isMagicDamage() && (attacker instanceof EntityLivingBase ||
				(attacker instanceof EntityArrow && ((EntityArrow) attacker).shootingEntity != null))) {
				ItemStack heldItemStack = ((EntityLivingBase) (attacker instanceof EntityArrow ? ((EntityArrow) attacker).shootingEntity : attacker)).getHeldItem(EnumHand.MAIN_HAND);
				Item heldItem = !heldItemStack.isEmpty() ? heldItemStack.getItem() : null;
				if (attacker instanceof EntitySpectriteGolem || heldItem instanceof ItemSpectriteSword) {
					World world = e.getEntityLiving().getEntityWorld();
					if (!world.isRemote) {
						if (!(attacker instanceof EntityPlayer) || (((EntityPlayer) attacker).getCooldownTracker().getCooldown(heldItem, 0) == 0.0f && !attacker.isSneaking())) {
							int power;
							if (!(attacker instanceof EntitySpectriteGolem)) {
								boolean enhanced = SpectriteHelper.isStackSpectriteEnhanced(heldItemStack);
								power = ((heldItem instanceof ItemSpectriteSwordSpecial) ? 2 : 1)
										+ ((!(heldItem instanceof ItemSpectriteLegendBlade) ? 0 : 1) + (enhanced ? 1 : 0));
							} else {
								power = 3;
							}

							Spectrite.Proxy.performDispersedSpectriteDamage(world, power, -1, new Vec3d(target.posX, target.getEntityBoundingBox().minY + target.height / 2, target.posZ), attacker, null, world.rand);
							
							if (attacker instanceof EntityPlayer && !((EntityPlayer) attacker).isCreative()) {
								((EntityPlayer) attacker).getCooldownTracker().setCooldown(heldItem, (int) Math.round(SpectriteConfig.items.spectriteToolCooldown * 20));
							}
						}
					}
				} else if (target instanceof EntityPlayer && !target.getActiveItemStack().isEmpty() && target.isActiveItemStackBlocking()
					&& SpectriteHelper.canBlockDamageSource((EntityPlayer) target, e.getSource()) && target.getActiveItemStack().getItem() instanceof ItemSpectriteShield) {
					SpectriteHelper.damageShield((EntityPlayer) target, e.getAmount());
					e.setCanceled(true);
					if (!e.getSource().isProjectile() && attacker instanceof EntityLivingBase) {
						((EntityLivingBase) attacker).knockBack(target, 0.5F, target.posX - attacker.posX, target.posZ - attacker.posZ);
						target.world.setEntityState(target, (byte) 29);
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onLivingHurt(LivingHurtEvent e) {
		if (e.getSource().isExplosion()) {
			if (e.getEntityLiving() instanceof ISpectriteMob) {
				e.setCanceled(true);
			} else if (e.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null) {
				ItemStack helmetStack = e.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD);
				if (helmetStack.getItem() instanceof ItemSpectriteSkull) {
					int skullType = ((ItemSpectriteSkull) helmetStack.getItem()).getSkullType();
					if (skullType > 0) {
						float damage = e.getAmount() - (skullType * 10);
						if (damage > 0f) {
							e.setAmount(damage);
						} else {
							e.setCanceled(true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onBlockPlaced(PlaceEvent e) {
		Block block = e.getPlacedBlock().getBlock();
		if (block == Blocks.PUMPKIN) {
			BlockPattern golemPattern = BlockSpectrite.getGolemPattern();
			BlockPattern.PatternHelper patternHelper = golemPattern.match(e.getWorld(), e.getPos());
	
	        if (patternHelper != null)
	        {
	        	World world = e.getWorld();
	        	
	            for (int j = 0; j < golemPattern.getPalmLength(); ++j)
	            {
	                for (int k = 0; k < golemPattern.getThumbLength(); ++k)
	                {
	                    world.setBlockState(patternHelper.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
	                }
	            }
	
	            BlockPos blockpos = patternHelper.translateOffset(1, 2, 0).getPos();
	            EntitySpectriteGolem entitySpectriteGolem = new EntitySpectriteGolem(world, true);
	            entitySpectriteGolem.setLocationAndAngles(blockpos.getX() + 0.5D, blockpos.getY() + 0.05D, blockpos.getZ() + 0.5D, 0.0F, 0.0F);
	            world.spawnEntity(entitySpectriteGolem);
	
	            for (EntityPlayerMP player : world.getEntitiesWithinAABB(EntityPlayerMP.class, entitySpectriteGolem.getEntityBoundingBox().grow(5.0D)))
	            {
	                CriteriaTriggers.SUMMONED_ENTITY.trigger(player, entitySpectriteGolem);
	            }
	
	            for (int j1 = 0; j1 < 120; ++j1)
	            {
	                Spectrite.Proxy.spawnSpectriteSpellParticle(world, blockpos.getX() + world.rand.nextDouble(), blockpos.getY() + world.rand.nextDouble() * 3.9D,
	            		blockpos.getZ() + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D, 0);
	            }
	
	            for (int k1 = 0; k1 < golemPattern.getPalmLength(); ++k1)
	            {
	                for (int l1 = 0; l1 < golemPattern.getThumbLength(); ++l1)
	                {
	                    BlockWorldState blockworldstate1 = patternHelper.translateOffset(k1, l1, 0);
	                    world.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR, false);
	                }
	            }
	        }
		} else if (block == ModBlocks.spectrite_glass) {
			World world = e.getWorld();
			BlockPos pos = e.getPos();
			
			while (pos.getY() > 0) {
				pos = pos.down();
				IBlockState state = world.getBlockState(pos);
				if (state.getBlock() == Blocks.BEACON) {
					TileEntity te = world.getTileEntity(pos);
					if (te.getClass() == TileEntityBeacon.class) {
						if (primaryEffectField == null) {
							primaryEffectField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class,  "primaryEffect", "field_146013_m");
							secondaryEffectField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class, "secondaryEffect", "field_146010_n");
							paymentField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class, "payment", "field_146011_o");
							customNameField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class, "customName", "field_146008_p");
						}
						try {
							Potion primaryEffect = (Potion) primaryEffectField.get(te), secondaryEffect = (Potion) secondaryEffectField.get(te);
							ItemStack payment = (ItemStack) paymentField.get(te);
							String customName = (String) customNameField.get(te);
							world.setBlockState(pos, ModBlocks.fast_updating_beacon.getDefaultState());
							te = world.getTileEntity(pos);
							primaryEffectField.set(te, primaryEffect);
							secondaryEffectField.set(te, secondaryEffect);
							paymentField.set(te, payment);
							customNameField.set(te, customName);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						break;
					}
				}
			}
		} else if (block == Blocks.BEACON) {
			World world = e.getWorld();
			BlockPos pos = e.getPos();
			
			while (pos.getY() < 256) {
				pos = pos.up();
				IBlockState state = world.getBlockState(pos);
				if (state.getBlock() == ModBlocks.spectrite_glass) {
					TileEntity te = world.getTileEntity(e.getPos());
					if (te.getClass() == TileEntityBeacon.class) {
						if (primaryEffectField == null) {
							primaryEffectField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class,  "primaryEffect", "field_146013_m");
							secondaryEffectField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class, "secondaryEffect", "field_146010_n");
							paymentField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class, "payment", "field_146011_o");
							customNameField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class, "customName", "field_146008_p");
						}
						try {
							Potion primaryEffect = (Potion) primaryEffectField.get(te), secondaryEffect = (Potion) secondaryEffectField.get(te);
							ItemStack payment = (ItemStack) paymentField.get(te);
							String customName = (String) customNameField.get(te);
							world.setBlockState(e.getPos(), ModBlocks.fast_updating_beacon.getDefaultState());
							te = world.getTileEntity(e.getPos());
							primaryEffectField.set(te, primaryEffect);
							secondaryEffectField.set(te, secondaryEffect);
							paymentField.set(te, payment);
							customNameField.set(te, customName);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						break;
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onBlockBreak(BreakEvent e) {
		Block block = e.getState().getBlock();
		if (block == ModBlocks.spectrite_glass) {
			World world = e.getWorld();
			BlockPos beaconPos = e.getPos();
			boolean hasBeacon = false;
			
			while (beaconPos.getY() > 0) {
				beaconPos = beaconPos.down();
				IBlockState state = world.getBlockState(beaconPos);
				if (state.getBlock() == ModBlocks.fast_updating_beacon) {
					hasBeacon = true;
					break;
				} else if (state.getBlock() == ModBlocks.spectrite_glass) {
					break;
				}
			}
			if (hasBeacon) {
				BlockPos pos = e.getPos();
				while (pos.getY() < 256) {
					pos = pos.up();
					IBlockState state = world.getBlockState(pos);
					if (state.getBlock() == ModBlocks.spectrite_glass) {
						break;
					}
				}
				if (pos.getY() == 256) {
					TileEntity te = world.getTileEntity(beaconPos);
					if (primaryEffectField == null) {
						primaryEffectField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class,  "primaryEffect", "field_146013_m");
						secondaryEffectField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class, "secondaryEffect", "field_146010_n");
						paymentField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class, "payment", "field_146011_o");
						customNameField = SpectriteHelper.findObfuscatedField(TileEntityBeacon.class, "customName", "field_146008_p");
					}
					try {
						Potion primaryEffect = (Potion) primaryEffectField.get(te), secondaryEffect = (Potion) secondaryEffectField.get(te);
						ItemStack payment = (ItemStack) paymentField.get(te);
						String customName = (String) customNameField.get(te);
						world.setBlockState(beaconPos, Blocks.BEACON.getDefaultState());
						te = world.getTileEntity(beaconPos);
						primaryEffectField.set(te, primaryEffect);
						secondaryEffectField.set(te, secondaryEffect);
						paymentField.set(te, payment);
						customNameField.set(te, customName);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onSpectriteEntityUpdate(LivingUpdateEvent e) {
		EntityLivingBase entity = e.getEntityLiving();
		if (entity instanceof ISpectriteMob) {
			if (entity instanceof ISpectriteBipedMob && ((ISpectriteBipedMob) entity).isBoss()) {
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
			}
			if (entity.getActivePotionEffect(ModPotions.SPECTRITE_RESISTANCE) == null) {
				entity.addPotionEffect(new PotionEffect(ModPotions.SPECTRITE_RESISTANCE, 16, entity instanceof ISpectriteBipedMob ?
					((ISpectriteBipedMob) entity).isHasSpectriteResistance() ? 1 : 0 : 0, true, true));
			}
		} else if (entity instanceof EntityPlayer) {
			ItemStack orbStack = null;
			boolean hasHealingOrb = (!entity.getHeldItemMainhand().isEmpty() && (orbStack = entity.getHeldItemMainhand()).getItem() == ModItems.spectrite_orb)
				|| (!entity.getHeldItemOffhand().isEmpty() && (orbStack = entity.getHeldItemOffhand()).getItem() == ModItems.spectrite_orb);
			int regenLevel = -1;
			int armourCount = 0;
			PotionEffect activePotionEffect = entity.getActivePotionEffect(MobEffects.REGENERATION);
			Iterator<ItemStack> armourIterator = entity.getArmorInventoryList().iterator();
			while (armourIterator.hasNext()) {
				ItemStack stack = armourIterator.next();
				if (stack != null && stack.getItem() instanceof ItemSpectriteArmor) {
					armourCount++;
				}
			}
			if (armourCount == 4) {
				regenLevel = (armourCount == 4 ? Math.max(SpectriteConfig.items.spectriteArmourBonusMode.ordinal() - 1, 0) : 0);
			}
			if (hasHealingOrb && (((EntityPlayer) entity).getCooldownTracker().getCooldown(orbStack.getItem(), 0) == 0.0f)) {
				regenLevel += SpectriteHelper.isStackSpectriteEnhanced(orbStack) ? 2 : 1;
			}
			if (regenLevel >= 0 && (activePotionEffect == null || activePotionEffect.getAmplifier() < regenLevel)) {
				entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 220, regenLevel, true, true));
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onStartTrackingSpectriteBoss(StartTracking e) {
		Entity entity = e.getTarget();
		if (!entity.getEntityWorld().isRemote && entity instanceof ISpectriteBipedMob) {
			ISpectriteBossCapability sbc = entity.getCapability(SpectriteBossProvider.sbc, null);
			if (sbc.isEnabled()) {
                Spectrite.Network.sendTo(new PacketSyncSpectriteBoss(entity.getUniqueID(), true), (EntityPlayerMP) e.getEntityPlayer());
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onStopTrackingSpectriteBoss(StopTracking e) {
		Entity entity = e.getTarget();
		if (!entity.getEntityWorld().isRemote && entity instanceof ISpectriteBipedMob) {
			ISpectriteBossCapability sbc = entity.getCapability(SpectriteBossProvider.sbc, null);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onAnvilRepair(AnvilRepairEvent e) {
		if (!e.getItemResult().isEmpty() && (e.getItemResult().getItem() instanceof IPerfectSpectriteItem)) {
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
