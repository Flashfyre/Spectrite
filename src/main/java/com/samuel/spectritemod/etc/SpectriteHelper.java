package com.samuel.spectritemod.etc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.samuel.spectritemod.init.ModEnchantments;
import com.samuel.spectritemod.init.ModPotions;
import com.samuel.spectritemod.items.IPerfectSpectriteItem;
import com.samuel.spectritemod.items.ItemSpectriteShield;
import com.samuel.spectritemod.items.ItemSpectriteShieldSpecial;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpectriteHelper {
	
	private static final TextFormatting[] textColours = new TextFormatting[] { TextFormatting.RED, TextFormatting.GOLD, TextFormatting.YELLOW, TextFormatting.GREEN,
			TextFormatting.BLUE, TextFormatting.AQUA, TextFormatting.LIGHT_PURPLE };
	
	public static Field findObfuscatedField(Class<?> clazz,
		String... names) {
		return ReflectionHelper.findField(clazz,
			ObfuscationReflectionHelper.remapFieldNames(
				clazz.getName(), names));
	}
	
	public static <E> Method findObfuscatedMethod(Class<? super E> clazz,
		String name, String obfuscatedName, Class<?>... methodTypes) {
		return ReflectionHelper.findMethod(clazz, name, obfuscatedName, methodTypes);
	}
	
	public static int getCurrentSpectriteFrame(World worldIn) {
		if (worldIn == null) {
            return Math.round((System.currentTimeMillis() >> 6) % 36);
		} else {
        	float time = MathHelper.ceil((((worldIn.getTotalWorldTime() >> 1) % 36)
        		* 0.2777F) * 1000F) / 10000F;
            return Math.round(time * 36);
        }
	}
	
	public static float[] getCurrentSpectriteRGBColour(boolean inverted) {
		int hueFrame = Math.round((System.currentTimeMillis() >> 5) % 180);
		float r = hueFrame >= 120 && hueFrame < 150 ? (1f / 30) * (hueFrame - 120) : hueFrame < 30 || hueFrame >= 150 ? 1f : hueFrame < 60 ? (1f / 30) * (30 - (hueFrame - 30)) : 0f,
			g = hueFrame < 30 ? (1f / 30) * hueFrame : hueFrame < 90 ? 1f : hueFrame < 120 ? (1f / 30) * (30 - (hueFrame - 90)) : 0f,
			b = hueFrame >= 60 && hueFrame < 90 ? (1f / 30) * (hueFrame - 60) : hueFrame >= 90 && hueFrame < 150 ? 1f : hueFrame >= 150 ? (1f / 30) * (30 - (hueFrame - 150)) : 0f;
			
		if (inverted) {
			r = 255f - r;
			g = 255f - g;
			b = 255f - b;
		}
				
		return new float[] { r, g, b };
	}
	
	public static int getCurrentSpectriteColour(boolean inverted) {
		int hueFrame = Math.round((System.currentTimeMillis() >> 5) % 180);
		int r = MathHelper.floor(hueFrame >= 120 && hueFrame < 150 ? (255f / 30) * (hueFrame - 120) : hueFrame < 30 || hueFrame >= 150 ? 255f : hueFrame < 60 ? (255f / 30) * (30 - (hueFrame - 30)) : 0f),
			g = MathHelper.floor(hueFrame < 30 ? (255f / 30) * hueFrame : hueFrame < 90 ? 255f : hueFrame < 120 ? (255f / 30) * (30 - (hueFrame - 90)) : 0f),
			b = MathHelper.floor(hueFrame >= 60 && hueFrame < 90 ? (255f / 30) * (hueFrame - 60) : hueFrame >= 90 && hueFrame < 150 ? 255f : hueFrame >= 150 ? (255f / 30) * (30 - (hueFrame - 150)) : 0f);
			
		if (inverted) {
			r = 255 - r;
			g = 255 - g;
			b = 255 - b;
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
	
	public static int getSpectriteDamageAmplifierAfterResistance(int amplifier, EntityLivingBase entity) {
		PotionEffect resistanceEffect = entity.getActivePotionEffect(ModPotions.SPECTRITE_RESISTANCE);
		int resistanceLevel = (resistanceEffect == null ? -1 : resistanceEffect.getAmplifier());
		return amplifier - (resistanceLevel + 1);
	}
	
	public static boolean canBlockDamageSource(EntityPlayer player, DamageSource damageSourceIn) {
		if (!damageSourceIn.isUnblockable() && player.isActiveItemStackBlocking()) {
			Vec3d vec3d = damageSourceIn.getDamageLocation();

			if (vec3d != null) {
				Vec3d vec3d1 = player.getLook(1.0F);
				Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(player.posX, player.posY, player.posZ)).normalize();
				vec3d2 = new Vec3d(vec3d2.xCoord, 0.0D, vec3d2.zCoord);

				if (vec3d2.dotProduct(vec3d1) < 0.0D) {
					return true;
				}
			}
		}

		return false;
	}
	
	public static void damageShield(EntityPlayer player, float damage) {
		ItemStack activeItemStack = player.getActiveItemStack();
		if (damage >= 3f) {
			int i = 1 + MathHelper.floor(damage);
			player.getActiveItemStack().damageItem(i, player);
	
			if (player.getActiveItemStack().isEmpty()) {
				EnumHand enumhand = player.getActiveHand();
				net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, player.getActiveItemStack(), enumhand);
	
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
	
	@SideOnly(Side.CLIENT)
    public static void addPotionTooltip(ItemStack itemIn, List<String> lores, float durationFactor)
    {
        List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemIn);
        List<Tuple<String, AttributeModifier>> list1 = Lists.<Tuple<String, AttributeModifier>>newArrayList();

        if (list.isEmpty())
        {
            String s = I18n.translateToLocal("effect.none").trim();
            lores.add(TextFormatting.GRAY + s);
        }
        else
        {
            for (PotionEffect potioneffect : list)
            {
                String s1 = I18n.translateToLocal(potioneffect.getEffectName()).trim();
                Potion potion = potioneffect.getPotion();
                Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();

                if (!map.isEmpty())
                {
                    for (Entry<IAttribute, AttributeModifier> entry : map.entrySet())
                    {
                        AttributeModifier attributemodifier = entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        list1.add(new Tuple(entry.getKey().getName(), attributemodifier1));
                    }
                }

                if (potioneffect.getAmplifier() > 0)
                {
                    s1 = s1 + " " + I18n.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
                }

                if (potioneffect.getDuration() > 20)
                {
                    s1 = s1 + " (" + Potion.getPotionDurationString(potioneffect, durationFactor) + ")";
                }

                if (potion.isBadEffect())
                {
                    lores.add(TextFormatting.RED + s1);
                }
                else
                {
                    lores.add(TextFormatting.BLUE + s1);
                }
            }
        }

        if (!list1.isEmpty())
        {
            lores.add("");
            lores.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("potion.whenDrank"));

            for (Tuple<String, AttributeModifier> tuple : list1)
            {
                AttributeModifier attributemodifier2 = tuple.getSecond();
                double d0 = attributemodifier2.getAmount();
                double d1;

                if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2)
                {
                    d1 = attributemodifier2.getAmount();
                }
                else
                {
                    d1 = attributemodifier2.getAmount() * 100.0D;
                }

                if (d0 > 0.0D)
                {
                    lores.add(TextFormatting.BLUE + I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + tuple.getFirst())));
                }
                else if (d0 < 0.0D)
                {
                    d1 = d1 * -1.0D;
                    lores.add(TextFormatting.RED + I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + tuple.getFirst())));
                }
            }
        }
    }
}