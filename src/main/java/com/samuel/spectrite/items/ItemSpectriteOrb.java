package com.samuel.spectrite.items;

import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModPotions;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ItemSpectriteOrb extends Item implements IPerfectSpectriteItem, ISpectriteCustomTooltipItem {

	public static final int RED = 1, ORANGE = 2, YELLOW = 4, GREEN = 8, BLUE = 16, INDIGO = 32, VIOLET = 64;
	public static final int[] ORB_COLOURS = new int[] { RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET };
	public static final int[][] ORB_COLOUR_RGB = new int[][]{ new int[] { 255, 0, 0 }, new int[] { 255, 128, 0 },
		new int[] { 255, 255, 0 }, new int[] { 0, 255, 0 }, new int[] { 0, 0, 255 }, new int[] { 128, 0, 255 }, new int[] { 188, 0, 255 } };
	public static final EntityEquipmentSlot[] ORB_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {
		EntityEquipmentSlot.CHEST, EntityEquipmentSlot.FEET, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.LEGS,
		EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD };
	public final Potion[] ORB_POTIONS;
	
	public ItemSpectriteOrb() {
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);

		ORB_POTIONS = new Potion[] {ModPotions.BERSERK, ModPotions.LIGHTWEIGHT, ModPotions.PROSPERITY, ModPotions.AGILITY,
			ModPotions.AMPHIBIOUS, ModPotions.ENDURANCE, ModPotions.RESILIENCE };
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String displayName = net.minecraft.util.text.translation.I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack)
			+ (stack.getItemDamage() == 127 ? "_perfect" : "") + ".name");
		return displayName;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addTooltipLines(ItemStack stack, List<String> list) {
		int damage = stack.getItemDamage();
		if (damage > 0) {
			double cooldown = SpectriteConfig.items.spectriteOrbCooldown * (SpectriteHelper.isStackSpectriteEnhanced(stack) ? 0.5D : 1D);
			double duration = SpectriteConfig.items.spectriteOrbDuration;
			String name = getUnlocalizedName().substring(5);
			String level1 = I18n.format("iteminfo." + name + ".effect.level.1");
			String level2 = I18n.format("iteminfo." + name + ".effect.level.2");
			List<Integer> colours = new ArrayList<>();
			List<String> lines = new ArrayList<>();
			boolean[] armourSlotCompatibility = new boolean[4];
			lines.add(I18n.format("iteminfo." + name + ".l1"));
			for (int c = 0; c < ORB_COLOURS.length; c++) {
				if ((damage & ORB_COLOURS[c]) > 0) {
					armourSlotCompatibility[ORB_EQUIPMENT_SLOTS[c].ordinal() - 2] = true;
					colours.add(c);
					lines.add(SpectriteHelper.textColours[c] + " " + I18n.format(ModItems.spectrite_orb.ORB_POTIONS[c].getName()) + level1);
				}
			}
			lines.add(I18n.format("iteminfo." + name + ".l2", cooldown));
			for (int c = 0; c < colours.size(); c++) {
				lines.add(SpectriteHelper.textColours[colours.get(c)] + " " + I18n.format(ModItems.spectrite_orb.ORB_POTIONS[colours.get(c)].getName())
						+ level2 + I18n.format("iteminfo." + name + ".effect.duration", duration));
			}
			for (int s = armourSlotCompatibility.length - 1; s >= 0; s--) {
				if (armourSlotCompatibility[s]) {
					EntityEquipmentSlot compatibleArmourSlot = EntityEquipmentSlot.values()[s + 2];
					String compatibleArmourName;
					if (compatibleArmourSlot == EntityEquipmentSlot.HEAD) {
						compatibleArmourName = ModItems.spectrite_helmet.getUnlocalizedName();
					} else if (compatibleArmourSlot == EntityEquipmentSlot.CHEST) {
						compatibleArmourName = ModItems.spectrite_chestplate.getUnlocalizedName();
					} else if (compatibleArmourSlot == EntityEquipmentSlot.LEGS) {
						compatibleArmourName = ModItems.spectrite_leggings.getUnlocalizedName();
					} else {
						compatibleArmourName = ModItems.spectrite_boots.getUnlocalizedName();
					}
					compatibleArmourName = SpectriteHelper.getMultiColouredString(I18n.format(compatibleArmourName + ".name"), false);
					lines.add(I18n.format("iteminfo." + name + ".l3", compatibleArmourName));
				}
			}
			lines.add(I18n.format("iteminfo." + name + ".l4"));
			for (String l : lines) {
				list.add(l);
			}
			list.set(0, SpectriteHelper.getMultiColouredString(stack.getDisplayName(), stack.getItemDamage()));
		}
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return this.onEntitySpectriteItemUpdate(entityItem);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
			SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5F, (itemRand.nextFloat() * 0.4F + 1.0F));

		if (playerIn.getHeldItem(hand).getItemDamage() > 0) {
			playerIn.getCooldownTracker().setCooldown(this, (int) SpectriteConfig.items.spectriteOrbCooldown * 20);

			List<Potion> potionsList = getPotions(playerIn.getHeldItem(hand));

			if (potionsList.size() == 7) {
				potionsList.add(ModPotions.SPECTRITE_STRENGTH);
			}

			for (Potion p : potionsList) {
				playerIn.removePotionEffect(p);
				playerIn.addPotionEffect(new PotionEffect(p, (int) SpectriteConfig.items.spectriteOrbDuration * 20, 1));
			}
		} else {
			playerIn.getHeldItemMainhand().setItemDamage(ItemSpectriteOrb.ORB_COLOURS[worldIn.rand.nextInt(7)]);
		}

		return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
	}

	public List<Potion> getPotions(ItemStack stack) {
		final List<Potion> potions = new ArrayList<>();
		final int itemDamage = stack.getItemDamage();

		for (int c = 0; c < ORB_COLOURS.length; c++) {
			if ((itemDamage & ORB_COLOURS[c]) == ORB_COLOURS[c]) {
				potions.add(ORB_POTIONS[c]);
			}
		}

		return potions;
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (this.isInCreativeTab(tab))
		{
			int total = 0;
			for (int t = 0; t < ORB_COLOURS.length; t++) {
				items.add(new ItemStack(this, 1, ORB_COLOURS[t]));
				total |= ORB_COLOURS[t];
			}
			items.add(new ItemStack(this, 1, total));
		}
	}
}
