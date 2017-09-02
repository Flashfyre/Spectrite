package com.samuel.spectrite.items;

import com.google.common.collect.Multimap;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.entities.EntitySpectriteWitherSkull;
import com.samuel.spectrite.etc.SpectriteHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSpectriteWitherRod extends ItemSpectriteSimple implements IPerfectSpectriteItem, ICustomTooltipItem {

    private final boolean invulnerable;
    private final float attackDamage;
    private final Item.ToolMaterial material;

    public ItemSpectriteWitherRod(Item.ToolMaterial material, boolean invulnerable)
    {
        super();
        this.material = material;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.attackDamage = 1.0F + material.getDamageVsEntity();
        this.invulnerable = invulnerable;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {

        String displayName = super.getItemStackDisplayName(stack);
        displayName = this.getMultiColouredDisplayName(stack, displayName);
        return displayName;
    }

    @Override
    public void addTooltipLines(ItemStack stack, List<String> list) {
        int lineCount = 0;
        boolean isLastLine = false;
        double cooldown = SpectriteConfig.items.spectriteWitherRodCooldown;
        if (SpectriteHelper.isStackSpectriteEnhanced(stack)) {
            cooldown *= 0.5d;
        }
        String curLine;
        while (!isLastLine) {
            isLastLine = (curLine = I18n
                    .translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + ".l" +
                            ++lineCount))).endsWith("@");
            if (lineCount == 1) {
                curLine = curLine.replace("#", String.format("%.2f", cooldown));
            }
            list.add(!isLastLine ? curLine : curLine
                    .substring(0, curLine.length() - 1));
        }
    }

    /**
     * Called when the equipped item is right clicked.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.swingArm(handIn);
        if (!worldIn.isRemote) {
            if (playerIn.getCooldownTracker().getCooldown(this, 0f) == 0f) {
                launchWitherSkull(worldIn, playerIn, invulnerable || itemRand.nextInt(8) == 0);
                playerIn.getHeldItem(handIn).damageItem(1, playerIn);
                if (!playerIn.isCreative()) {
                    boolean isEnhanced = SpectriteHelper.isStackSpectriteEnhanced(playerIn.getHeldItem(handIn));
                    playerIn.getCooldownTracker().setCooldown(this, (int) Math.round(SpectriteConfig.items.spectriteWitherRodCooldown * (isEnhanced ? 10 : 20)));
                }
            }
        }

        return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    private void launchWitherSkull(World world, EntityLivingBase shooter, boolean invulnerable)
    {
        world.playEvent(null, 1024, new BlockPos(shooter), 0);
        double d0 = shooter.posX;
        double d1 = shooter.posY + shooter.getEyeHeight();
        double d2 = shooter.posZ;
        EntitySpectriteWitherSkull entitySpectriteWitherSkull = new EntitySpectriteWitherSkull(world, shooter, 0, 0, 0);
        entitySpectriteWitherSkull.setAim(shooter, shooter.rotationPitch, shooter.rotationYaw, 2);

        if (invulnerable)
        {
            entitySpectriteWitherSkull.setInvulnerable(true);
        }

        entitySpectriteWitherSkull.posY = d1;
        entitySpectriteWitherSkull.posX = d0;
        entitySpectriteWitherSkull.posZ = d2;
        entitySpectriteWitherSkull.rotationYaw = ((entitySpectriteWitherSkull.rotationYaw + 360) % 360) - 180;
        world.spawnEntity(entitySpectriteWitherSkull);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.damageItem(2, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if ((double)state.getBlockHardness(worldIn, pos) != 0.0D)
        {
            stack.damageItem(3, entityLiving);
        }

        return true;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    @Override
    public int getItemEnchantability()
    {
        return this.material.getEnchantability();
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        ItemStack mat = this.material.getRepairItemStack();
        if (!mat.isEmpty() && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }

    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.80000007153D, 0));
        }

        return multimap;
    }
}
