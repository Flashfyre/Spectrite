package com.samuel.spectrite.helpers;

import com.samuel.spectrite.init.ModEnchantments;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModPotions;
import com.samuel.spectrite.items.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpectriteHelper {
	
	public static final TextFormatting[] textColours = new TextFormatting[] { TextFormatting.RED, TextFormatting.GOLD, TextFormatting.YELLOW, TextFormatting.GREEN,
		TextFormatting.BLUE, TextFormatting.DARK_PURPLE, TextFormatting.LIGHT_PURPLE };
	private static final MapColor[] mapColours = new MapColor[] { MapColor.RED, MapColor.ORANGE_STAINED_HARDENED_CLAY,
		MapColor.YELLOW, MapColor.LIME, MapColor.BLUE, MapColor.PURPLE, MapColor.PINK };
	private static final HashMap<Integer, Integer[]> ORB_TEXT_COLOUR_INDEXES_CACHE = new HashMap<>();

	public static int getCurrentSpectriteFrame(World worldIn) {
		if (worldIn == null) {
            return Math.round((System.currentTimeMillis() >> 6) % 36);
		} else {
        	float time = MathHelper.ceil((((worldIn.getTotalWorldTime() >> 1) % 36)
        		* 0.2777F) * 1000F) / 10000F;
            return Math.round(time * 36);
        }
	}

	public static float[] getCurrentSpectriteRGBColour(float offsetLevel) {
		int hueFrame = Math.round((System.currentTimeMillis() >> 5) % 180);
		if (offsetLevel >= 0f) {
			hueFrame = (hueFrame + ((int) (offsetLevel))) % 180;
		}
		float r = hueFrame >= 120 && hueFrame < 150 ? (1f / 30) * (hueFrame - 120) : hueFrame < 30 || hueFrame >= 150 ? 1f : hueFrame < 60 ? (1f / 30) * (30 - (hueFrame - 30)) : 0f,
			g = hueFrame < 30 ? (1f / 30) * hueFrame : hueFrame < 90 ? 1f : hueFrame < 120 ? (1f / 30) * (30 - (hueFrame - 90)) : 0f,
			b = hueFrame >= 60 && hueFrame < 90 ? (1f / 30) * (hueFrame - 60) : hueFrame >= 90 && hueFrame < 150 ? 1f : hueFrame >= 150 ? (1f / 30) * (30 - (hueFrame - 150)) : 0f;

		if (offsetLevel < 0f) {
			r = 1.0f - r;
			g = 1.0f - g;
			b = 1.0f - b;
		}
				
		return new float[] { r, g, b };
	}
	
	public static int getCurrentSpectriteColour(int offsetLevel) {
		int hueFrame = Math.round((System.currentTimeMillis() >> 5) % 180);
		int r = MathHelper.floor(hueFrame >= 120 && hueFrame < 150 ? (255f / 30) * (hueFrame - 120) : hueFrame < 30 || hueFrame >= 150 ? 255f : hueFrame < 60 ? (255f / 30) * (30 - (hueFrame - 30)) : 0f),
			g = MathHelper.floor(hueFrame < 30 ? (255f / 30) * hueFrame : hueFrame < 90 ? 255f : hueFrame < 120 ? (255f / 30) * (30 - (hueFrame - 90)) : 0f),
			b = MathHelper.floor(hueFrame >= 60 && hueFrame < 90 ? (255f / 30) * (hueFrame - 60) : hueFrame >= 90 && hueFrame < 150 ? 255f : hueFrame >= 150 ? (255f / 30) * (30 - (hueFrame - 150)) : 0f);
		
		int tempR = r;
			
		switch (offsetLevel) {
			case 1:
				r = g;
				g = b;
				b = tempR;
				break;
			case 2:
				int tempG = g;
				r = b;
				g  = tempR;
				b = tempG;
				break;
			default:
				break;
		}
		
		return (r << 16) + (g << 8) + b + (255 >> 24);
	}
	
	public static String getMultiColouredString(String text, boolean rotateTextColour) {
		StringBuilder formattedText = new StringBuilder();
		final int textColourCount = textColours.length;
		final int colourIndex = rotateTextColour ? Math.round((System.currentTimeMillis() >> 7) % 7) : 0;
		for (int c = 0; c < text.length(); c++) {
			formattedText.append(textColours[(c + colourIndex) % textColourCount]).append(String.valueOf(text.charAt(c)));
		}
		
		return formattedText.toString();
	}

	public static String getMultiColouredString(String text, int orbDamage) {
		if (orbDamage < 127) {
			StringBuilder formattedText = new StringBuilder();
			if (orbDamage > 0) {
				final Integer[] textColourIndexes;
				if (ORB_TEXT_COLOUR_INDEXES_CACHE.containsKey(orbDamage)) {
					textColourIndexes = ORB_TEXT_COLOUR_INDEXES_CACHE.get(orbDamage);
				} else {
					List<Integer> textColourIndexesList = new ArrayList<>();
					int[] orbColours = ItemSpectriteOrb.ORB_COLOURS;
					for (int c = 0; c < orbColours.length; c++) {
						if ((orbDamage & orbColours[c]) == orbColours[c]) {
							textColourIndexesList.add(c);
						}
					}
					textColourIndexes = textColourIndexesList.toArray(new Integer[0]);
					ORB_TEXT_COLOUR_INDEXES_CACHE.put(orbDamage, textColourIndexes);
				}
				final int textColourCount = textColourIndexes.length;
				for (int c = 0; c < text.length(); c++) {
					formattedText.append(textColours[textColourIndexes[c % textColourCount]]).append(String.valueOf(text.charAt(c)));
				}
			} else {
				formattedText.append(text);
			}
			return formattedText.toString();
		} else {
			return getMultiColouredString(text, true);
		}
	}

	public static MapColor getSpectriteMapColour(World worldIn, BlockPos pos) {
		int posOffset = pos.getX() + pos.getY() + pos.getZ();

		return mapColours[(posOffset + (int) (worldIn.getWorldTime() >> 2)) % 7];
	}
	
	public static boolean isStackSpectriteEnhanced(ItemStack stack) {
		boolean ret = false;
		if (!stack.isEmpty() && stack.getItem() instanceof IPerfectSpectriteItem) {
			NBTTagList enchantmentTags = stack.getEnchantmentTagList();
			for (int ec = 0; ec < enchantmentTags.tagCount(); ec++) {
				if (enchantmentTags.getCompoundTagAt(ec).getShort("id") == Enchantment.getEnchantmentID(ModEnchantments.spectrite_enhance)) {
					ret = true;
					break;
				}
			}
		}
		
		return ret;
	}
	
	public static int getPlayerReceivedSpectriteDamageDecreaseForDifficulty(EnumDifficulty difficulty) {
		return 3 - difficulty.ordinal();
	}

	public static int getSpectriteDamageAmplifierAfterStrength(int amplifier, @Nullable Entity attacker) {
		PotionEffect strengthEffect = attacker != null && attacker instanceof EntityLivingBase ?
				((EntityLivingBase) attacker).getActivePotionEffect(ModPotions.SPECTRITE_STRENGTH) : null;
		int strengthLevel = (strengthEffect == null ? -1 : strengthEffect.getAmplifier());
		return amplifier + (strengthLevel + 1);
	}

	public static int getSpectriteDamageAmplifierAfterResistance(int amplifier, EntityLivingBase target) {
		PotionEffect resistanceEffect = target.getActivePotionEffect(ModPotions.SPECTRITE_RESISTANCE);
		int resistanceLevel = (resistanceEffect == null ? -1 : resistanceEffect.getAmplifier());
		return amplifier - (resistanceLevel + 1);
	}

	public static boolean canBlockDamageSource(EntityPlayer player, DamageSource damageSourceIn) {
		if (!damageSourceIn.isUnblockable() && player.isActiveItemStackBlocking()) {
			Vec3d vec3d = damageSourceIn.getDamageLocation();

			if (vec3d != null) {
				Vec3d vec3d1 = player.getLook(1.0F);
				Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(player.posX, player.posY, player.posZ)).normalize();
				vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

				if (vec3d2.dotProduct(vec3d1) < 0.0D) {
					return true;
				}
			}
		}

		return false;
	}

	public static void damageBow(EntityLivingBase entity, ItemStack arrowStack, PotionType potionType) {
		ItemStack bowStack = entity.getActiveItemStack();
		if (!bowStack.isEmpty() && bowStack.getItem() instanceof ItemBow) {
			ItemBow bowItem = (ItemBow) bowStack.getItem();
			int bowDamage = -1;

			if (potionType != null && !potionType.getEffects().isEmpty()) {
				for (PotionEffect pe : potionType.getEffects()) {
					Potion potion = pe.getPotion();
					if (potion == ModPotions.SPECTRITE_DAMAGE || potion == ModPotions.SPECTRITE_STRENGTH
						|| potion == ModPotions.SPECTRITE_RESISTANCE || potion == ModPotions.SPECTRITE) {
						bowDamage += 5 << (pe.getAmplifier() + 1);
					}
				}
			} else {
				bowDamage = arrowStack.getItem() == ModItems.spectrite_arrow_special ? 100 : 33;
			}

			if (bowItem instanceof ItemSpectriteBow) {
				if (!(bowItem instanceof ItemSpectriteBowSpecial)) {
					bowDamage *= 0.1;
				} else {
					bowDamage *= 0.033;
				}
			}

			bowDamage = new Float(bowDamage).intValue();

			entity.getActiveItemStack().damageItem(bowDamage, entity);
		}
	}
	
	public static void damageShield(EntityPlayer player, float damage) {
		ItemStack activeItemStack = player.getActiveItemStack();
		if (damage >= 3f) {
			int i = 1 + MathHelper.floor(damage);
			activeItemStack.damageItem(i, player);
	
			if (player.getActiveItemStack().isEmpty()) {
				EnumHand enumhand = player.getActiveHand();
				net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, activeItemStack, enumhand);
	
				if (enumhand == EnumHand.MAIN_HAND) {
					player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
				} else {
					player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
				}
	
				player.resetActiveHand();
				player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + player.world.rand.nextFloat() * 0.4F);
			}
		}
	}
	
	public static int getSpectriteShieldTier(ItemStack shieldStack) {
		Item item = shieldStack.getItem();
		return item instanceof ItemSpectriteShieldSpecial ? SpectriteHelper.isStackSpectriteEnhanced(shieldStack) ? 3 : 2 : item instanceof ItemSpectriteShield ? 1 : 0;
	}
	
	public static void populateRegisteredObjectsList(Map<String, IForgeRegistryEntry> registeredObjects,
		IForgeRegistryEntry ... objects) {
		for (IForgeRegistryEntry o : objects) {
			registeredObjects.put(o.getRegistryName().getResourcePath(), o);
		}
	}
}