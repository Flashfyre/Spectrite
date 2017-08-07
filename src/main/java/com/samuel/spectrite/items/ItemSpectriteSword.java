package com.samuel.spectrite.items;

import java.util.List;

import com.google.common.collect.Multimap;
import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.etc.SpectriteHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpectriteSword extends ItemSword {
	
	private final Item.ToolMaterial material;
	
	public ItemSpectriteSword(ToolMaterial material) {
		super(material);
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.setMaxStackSize(1);
		this.addPropertyOverride(new ResourceLocation("time"), Spectrite.ItemPropertyGetterSpectrite);
		this.material = material;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String displayName = super.getItemStackDisplayName(stack);
		displayName = stack.getItem() instanceof IPerfectSpectriteItem ? ((IPerfectSpectriteItem) this).getMultiColouredDisplayName(stack, displayName)
			: (TextFormatting.LIGHT_PURPLE + displayName);
		return displayName;
	}
	
	@Override
	/**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase playerIn)
    {
        return true;
    }

    @Override
    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    public int getMetadata(int damage)
    {
        return damage;
    }
    
    @Override
    public void addInformation(ItemStack stack,
		World worldIn, List<String> list, ITooltipFlag adva) {
		int lineCount = 0;
		boolean isLastLine = false;
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = I18n
				.translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + (SpectriteHelper.isStackSpectriteEnhanced(stack) ? "_enhanced" : "") + ".l" +
				++lineCount))).endsWith("@");
			if (lineCount == 1) {
				curLine = curLine.replace("#", String.valueOf(SpectriteConfig.spectriteToolCooldown));
			}
			list.add(!isLastLine ? curLine : curLine
				.substring(0, curLine.length() - 1));
		}
		if (stack.isItemEnchanted()) {
			list.add("----------");
		}
	}
    
    @Override
    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    @Override
    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }
    
    /**
     * Called when the player Left Clicks (attacks) an entity.
     * Processed before damage is done, if return value is true further processing is canceled
     * and the entity is not attacked.
     *
     * @param stack The Item being used
     * @param player The player that is attacking
     * @param entity The entity being attacked
     * @return True to cancel the rest of the interaction.
     */
    @Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        return false;
    }
	
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
            	new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.8D, 0));
        }

        return multimap;
    }
}
