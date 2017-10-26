package com.samuel.spectrite.containers;

import com.samuel.spectrite.blocks.BlockSpectriteAnvil;
import com.samuel.spectrite.enchantments.EnchantmentSpectriteEnhance;
import com.samuel.spectrite.init.ModBlocks;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.inventory.InventorySpectriteOrbEffects;
import com.samuel.spectrite.inventory.SlotSpectriteOrbEffect;
import com.samuel.spectrite.items.ItemSpectriteArmor;
import com.samuel.spectrite.items.ItemSpectriteOrb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class ContainerSpectriteRepair extends ContainerRepair {

    private static Field outputSlot;
    private static Field inputSlots;
    private static Field repairedItemName;

    /** The player that has this container open. */
    private final EntityPlayer player;
    private final BlockPos selfPosition;
    private final InventorySpectriteOrbEffects input1OrbSlots;
    private final InventorySpectriteOrbEffects input2OrbSlots;
    private final InventorySpectriteOrbEffects outputOrbSlots;

    private boolean orbMode = false;

    @SideOnly(Side.CLIENT)
    public ContainerSpectriteRepair(InventoryPlayer playerInventory, World worldIn, EntityPlayer player) {
        this(playerInventory, worldIn, BlockPos.ORIGIN, player);
    }

    public ContainerSpectriteRepair(InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, EntityPlayer player)
    {
        super(playerInventory, worldIn, blockPosIn, player);
        if (this.outputSlot == null) {
            this.outputSlot = ReflectionHelper.findField(ContainerRepair.class, "outputSlot", "field_82852_f");
            this.inputSlots = ReflectionHelper.findField(ContainerRepair.class, "inputSlots", "field_82853_g");
            this.repairedItemName = ReflectionHelper.findField(ContainerRepair.class, "repairedItemName", "field_82857_m");
        }
        IInventory outputSlot = null;
        try {
            outputSlot = (IInventory) this.outputSlot.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.player = player;
        this.selfPosition = blockPosIn;
        this.input1OrbSlots = new InventorySpectriteOrbEffects("Input1OrbEffects", true);
        this.input2OrbSlots = new InventorySpectriteOrbEffects("Input2OrbEffects", true);
        this.outputOrbSlots = new InventorySpectriteOrbEffects("OutputOrbEffects", true);
        this.inventorySlots.get(0).xPos -= 10;
        this.inventorySlots.get(1).xPos += 4;
        this.inventorySlots.remove(2);
        Slot newOutputSlot = new Slot(outputSlot, 2, 143, 47)
        {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }
            /**
             * Return whether this slot's stack can be taken from this slot.
             */
            @Override
            public boolean canTakeStack(EntityPlayer playerIn)
            {
                return (playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= ContainerSpectriteRepair.this.maximumCost) && ContainerSpectriteRepair.this.maximumCost > 0 && this.getHasStack();
            }
            @Override
            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
            {
                if (!thePlayer.capabilities.isCreativeMode)
                {
                    thePlayer.addExperienceLevel(-ContainerSpectriteRepair.this.maximumCost);
                }

                IInventory inputSlots = null;
                try {
                    inputSlots = (IInventory) ContainerSpectriteRepair.inputSlots.get(ContainerSpectriteRepair.this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                ItemStack stack1 = inputSlots.getStackInSlot(0);
                ItemStack stack2 = inputSlots.getStackInSlot(1);
                Item stack1Item = inputSlots.getStackInSlot(0).getItem();
                int[] orbColours = ItemSpectriteOrb.ORB_COLOURS;
                float breakChance = stack2.getItem() == ModItems.spectrite_orb
                    ? net.minecraftforge.common.ForgeHooks.onAnvilRepair(thePlayer, stack, inputSlots.getStackInSlot(0), inputSlots.getStackInSlot(1)) : 0F;
                int input1Damage = 0;
                int outputDamage = 0;

                if (ContainerSpectriteRepair.this.isOrbMode()) {
                    if (stack1Item == ModItems.spectrite_orb) {
                        input1Damage = inputSlots.getStackInSlot(0).getItemDamage();
                        outputDamage = stack.getItemDamage();
                    } else {
                        NBTTagCompound input1TagCompound = stack1.getSubCompound("OrbEffects");
                        NBTTagCompound outputTagCompound = stack.getSubCompound("OrbEffects");
                        for (int c = 0; c < orbColours.length; c++) {
                            if (ItemSpectriteOrb.ORB_EQUIPMENT_SLOTS[c] == ((ItemSpectriteArmor) stack1Item).armorType) {
                                if (outputTagCompound.getBoolean(new Integer(c).toString())) {
                                    if (input1TagCompound.getBoolean(new Integer(c).toString())) {
                                        input1Damage ^= orbColours[c];
                                    }
                                    outputDamage ^= orbColours[c];
                                }
                            }
                        }
                    }
                }

                inputSlots.setInventorySlotContents(0, ItemStack.EMPTY);

                if (ContainerSpectriteRepair.this.materialCost > 0)
                {
                    if (!stack2.isEmpty() && stack2.getCount() > ContainerSpectriteRepair.this.materialCost)
                    {
                        stack2.shrink(ContainerSpectriteRepair.this.materialCost);
                        inputSlots.setInventorySlotContents(1, stack2);
                    }
                    else
                    {
                        inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
                    }
                }
                else
                {
                    boolean clearInput2 = stack2.getItem() != ModItems.spectrite_orb;
                    if (!clearInput2) {
                        int input2Damage = stack2.getItemDamage();
                        for (int c : orbColours) {
                            if ((outputDamage & c) > 0) {
                                if ((input1Damage & c) == 0) {
                                    input2Damage ^= c;
                                }
                            }
                        }
                        clearInput2 = input2Damage == 0;
                        if (!clearInput2) {
                            stack2.setItemDamage(input2Damage);
                            inputSlots.setInventorySlotContents(1, stack2);
                        }
                    }
                    if (clearInput2) {
                        inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
                    }
                }

                ContainerSpectriteRepair.this.maximumCost = 0;
                IBlockState iblockstate = worldIn.getBlockState(blockPosIn);

                if (!thePlayer.capabilities.isCreativeMode && !worldIn.isRemote && iblockstate.getBlock() == ModBlocks.spectrite_anvil && thePlayer.getRNG().nextFloat() < breakChance)
                {
                    int l = ((Integer)iblockstate.getValue(BlockSpectriteAnvil.DAMAGE)).intValue();
                    ++l;

                    if (l > 2)
                    {
                        worldIn.setBlockToAir(blockPosIn);
                        worldIn.playEvent(1029, blockPosIn, 0);
                    }
                    else
                    {
                        worldIn.setBlockState(blockPosIn, iblockstate.withProperty(BlockSpectriteAnvil.DAMAGE, Integer.valueOf(l)), 2);
                        worldIn.playEvent(1030, blockPosIn, 0);
                    }
                }
                else if (!worldIn.isRemote)
                {
                    worldIn.playEvent(1030, blockPosIn, 0);
                }

                try {
                    ContainerSpectriteRepair.inputSlots.set(ContainerSpectriteRepair.this, inputSlots);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                return stack;
            }
        };
        newOutputSlot.slotNumber = 2;
        this.inventorySlots.add(2, newOutputSlot);
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
    @Override
    public void updateRepairOutput()
    {
        IInventory outputSlot = null;
        IInventory inputSlots = null;
        String repairedItemName = null;
        try {
            outputSlot = (IInventory) this.outputSlot.get(this);
            inputSlots = (IInventory) this.inputSlots.get(this);
            repairedItemName = (String) this.repairedItemName.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ItemStack itemstack = inputSlots.getStackInSlot(0);
        this.maximumCost = 1;
        int i = 0;
        int j = 0;
        int k = 0;

        if (itemstack.isEmpty())
        {
            outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
            this.maximumCost = 0;
            if (orbMode) {
                this.switchMode();
            }
        }
        else
        {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = inputSlots.getStackInSlot(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
            j = j + itemstack.getRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getRepairCost());
            this.materialCost = 0;
            boolean flag = false;

            if ((itemstack1.getItem() == ModItems.spectrite_orb || itemstack1.getItem() instanceof ItemSpectriteArmor)
                && !itemstack2.isEmpty() && itemstack2.getItem() == ModItems.spectrite_orb) {
                if (!orbMode) {
                    this.switchMode();
                } else {
                    this.updateOrbSlots();
                }
            } else if (orbMode) {
                this.switchMode();
            }

            if (!itemstack2.isEmpty())
            {
                if (!net.minecraftforge.common.ForgeHooks.onAnvilChange(this, itemstack, itemstack2, outputSlot, repairedItemName, j)) return;
                flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.getEnchantments(itemstack2).hasNoTags();

                if (this.isOrbMode()) {
                    Item stack1Item = itemstack.getItem();
                    boolean orbFusion = stack1Item == ModItems.spectrite_orb;
                    int[] orbColours = ItemSpectriteOrb.ORB_COLOURS;
                    EntityEquipmentSlot[] orbEquipmentSlots = ItemSpectriteOrb.ORB_EQUIPMENT_SLOTS;
                    int stack2Damage = itemstack2.getItemDamage();
                    int orbsPresent = 0;
                    int orbsAdded = 0;
                    int stack1Damage = itemstack1.getItemDamage();
                    int outputStackDamage = stack1Damage;
                    boolean input2Cost = false;
                    NBTTagCompound tagCompound = orbFusion ? null : itemstack1.getSubCompound("OrbEffects");
                    for (int a = 0; a < 3; a++) {
                        if (a == 2) {
                            for (int c = 0; c < orbColours.length; c++) {
                                int cIndex = orbColours[c];
                                if (orbFusion || (orbEquipmentSlots[c] == ((ItemSpectriteArmor) stack1Item).armorType)) {
                                    boolean hasOrb = false;
                                    if (((orbFusion || !input2Cost) && ((input2Cost ? stack1Damage : stack2Damage) & cIndex) > 0)
                                        || (!orbFusion && input2Cost && tagCompound.getBoolean(new Integer(c).toString()))) {
                                        outputStackDamage |= cIndex;
                                        orbsAdded++;
                                        hasOrb = true;
                                    }
                                    if (((orbFusion || input2Cost) && ((input2Cost ? stack2Damage : stack1Damage) & cIndex) > 0)
                                        || (!orbFusion && !input2Cost && tagCompound.getBoolean(new Integer(c).toString()))) {
                                        orbsPresent++;
                                        if (hasOrb) {
                                            orbsAdded--;
                                        } else {
                                            outputStackDamage |= cIndex;
                                        }
                                    }
                                }
                            }
                            for (int o = orbsPresent; o < orbsPresent + orbsAdded; o++) {
                                j += Math.pow(2, o + (orbFusion ? 0 : 4));
                            }
                        } else {
                            int damage = a == 0 ? stack1Damage : stack2Damage;
                            for (int c = 0; c < orbColours.length; c++) {
                                int cIndex = orbColours[c];
                                if (orbFusion || orbEquipmentSlots[c] == ((ItemSpectriteArmor) stack1Item).armorType) {
                                    if ((damage & cIndex) > 0) {
                                        if (a == 0) {
                                            orbsPresent++;
                                        } else {
                                            orbsAdded++;
                                        }
                                    }
                                }
                            }
                            if (a == 1) {
                                if (orbsAdded > orbsPresent && orbFusion) {
                                    input2Cost = true;
                                } else if (!orbFusion && tagCompound == null) {
                                    if (itemstack1.getTagCompound() == null) {
                                        itemstack.setTagCompound(new NBTTagCompound());
                                        itemstack1.setTagCompound(new NBTTagCompound());
                                    }
                                    (itemstack.getItem()).updateItemStackNBT(itemstack.getTagCompound());
                                    (itemstack1.getItem()).updateItemStackNBT(itemstack1.getTagCompound());
                                    tagCompound = itemstack1.getSubCompound("OrbEffects");
                                }
                                orbsPresent = 0;
                                orbsAdded = 0;
                            }
                        }
                    }
                    if (orbsAdded > 0 && (!orbFusion || orbsPresent > 0) && j > 0) {
                        if (orbFusion) {
                            itemstack1.setItemDamage(outputStackDamage);
                        } else {
                            for (int c = 0; c < orbColours.length; c++) {
                                int cIndex = orbColours[c];
                                if (ItemSpectriteOrb.ORB_EQUIPMENT_SLOTS[c] == ((ItemSpectriteArmor) stack1Item).armorType
                                    && (cIndex & outputStackDamage) > 0) {
                                    tagCompound.setBoolean(new Integer(c).toString(), true);
                                }
                            }
                            itemstack1.getTagCompound().setTag("OrbEffects", tagCompound);
                        }
                    } else {
                        itemstack1 = ItemStack.EMPTY;
                    }
                    outputSlot.setInventorySlotContents(0, itemstack1);
                }
                else if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2))
                {
                    flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.getEnchantments(itemstack2).hasNoTags();
                    int l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);

                    if (l2 <= 0)
                    {
                        outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                        this.maximumCost = 0;
                        return;
                    }

                    int i3;

                    for (i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3)
                    {
                        int j3 = itemstack1.getItemDamage() - l2;
                        itemstack1.setItemDamage(j3);
                        ++i;
                        l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
                    }

                    this.materialCost = i3;
                }
                else
                {
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable()))
                    {
                        outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                        this.maximumCost = 0;
                        return;
                    }

                    if (itemstack1.isItemStackDamageable() && !flag)
                    {
                        int l = itemstack.getMaxDamage() - itemstack.getItemDamage();
                        int i1 = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
                        int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                        int k1 = l + j1;
                        int l1 = itemstack1.getMaxDamage() - k1;

                        if (l1 < 0)
                        {
                            l1 = 0;
                        }

                        if (l1 < itemstack1.getMetadata())
                        {
                            itemstack1.setItemDamage(l1);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
                    boolean flag2 = false;
                    boolean flag3 = false;

                    for (Enchantment enchantment1 : map1.keySet())
                    {
                        if (enchantment1 != null)
                        {
                            int i2 = map.containsKey(enchantment1) ? ((Integer)map.get(enchantment1)).intValue() : 0;
                            int j2 = ((Integer)map1.get(enchantment1)).intValue();
                            j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                            boolean flag1 = enchantment1 instanceof EnchantmentSpectriteEnhance ? ((EnchantmentSpectriteEnhance) enchantment1).canApplyAtSpectriteAnvil(itemstack)
                                : enchantment1.canApply(itemstack);

                            if (this.player.capabilities.isCreativeMode || itemstack.getItem() == Items.ENCHANTED_BOOK)
                            {
                                flag1 = true;
                            }

                            for (Enchantment enchantment : map.keySet())
                            {
                                if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment))
                                {
                                    flag1 = false;
                                    ++i;
                                }
                            }

                            if (!flag1)
                            {
                                flag3 = true;
                            }
                            else
                            {
                                flag2 = true;

                                if (j2 > enchantment1.getMaxLevel())
                                {
                                    j2 = enchantment1.getMaxLevel();
                                }

                                map.put(enchantment1, Integer.valueOf(j2));
                                int k3 = 0;

                                switch (enchantment1.getRarity()) {
                                    case COMMON:
                                        k3 = 1;
                                        break;
                                    case UNCOMMON:
                                        k3 = 2;
                                        break;
                                    case RARE:
                                        k3 = 4;
                                        break;
                                    case VERY_RARE:
                                        k3 = 8;
                                }

                                if (flag)
                                {
                                    k3 = Math.max(1, k3 / 2);
                                }

                                i += k3 * j2;

                                if (itemstack.getCount() > 1)
                                {
                                    i = 40;
                                }
                            }
                        }
                    }

                    if (flag3 && !flag2)
                    {
                        outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                        this.maximumCost = 0;
                        return;
                    }
                }
            } else if (this.isOrbMode()) {
                outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                this.maximumCost = 0;
            }

            if (StringUtils.isBlank(repairedItemName))
            {
                if (itemstack.hasDisplayName())
                {
                    k = 1;
                    i += k;
                    itemstack1.clearCustomName();
                }
            }
            else if (!repairedItemName.equals(itemstack.getDisplayName()))
            {
                k = 1;
                i += k;
                itemstack1.setStackDisplayName(repairedItemName);
            }

            this.maximumCost = j + i;

            if (!this.isOrbMode()) {
                if (flag && !itemstack1.getItem().isBookEnchantable(itemstack1, itemstack2))
                    itemstack1 = ItemStack.EMPTY;

                if (i <= 0) {
                    itemstack1 = ItemStack.EMPTY;
                }

                if (k == i && k > 0 && this.maximumCost >= 40) {
                    this.maximumCost = 39;
                }

                if (this.maximumCost >= 40 && !this.player.capabilities.isCreativeMode) {
                    itemstack1 = ItemStack.EMPTY;
                }

                if (!itemstack1.isEmpty()) {
                    int k2 = itemstack1.getRepairCost();

                    if (!itemstack2.isEmpty() && k2 < itemstack2.getRepairCost()) {
                        k2 = itemstack2.getRepairCost();
                    }

                    if (k != i || k == 0) {
                        k2 = k2 * 2 + 1;
                    }

                    itemstack1.setRepairCost(k2);
                    EnchantmentHelper.setEnchantments(map, itemstack1);
                }

                outputSlot.setInventorySlotContents(0, itemstack1);
            }
            this.detectAndSendChanges();
        }

        try {
            this.outputSlot.set(this, outputSlot);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        if (player.getEntityWorld().getBlockState(this.selfPosition).getBlock() != ModBlocks.spectrite_anvil)
        {
            return false;
        }
        else
        {
            return playerIn.getDistanceSq((double)this.selfPosition.getX() + 0.5D, (double)this.selfPosition.getY() + 0.5D, (double)this.selfPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to check if
     * the slot can be added to a list of Slots to split the held ItemStack across.
     */
    @Override
    public boolean canDragIntoSlot(Slot slotIn)
    {
        return slotIn.getClass() != SlotSpectriteOrbEffect.class && super.canDragIntoSlot(slotIn);
    }

    private void switchMode() {
        if (this.orbMode) {
            this.inventorySlots.removeIf(slot -> slot.inventory instanceof InventorySpectriteOrbEffects);
            this.inventorySlots.forEach(slot -> { if (slot.inventory instanceof InventoryPlayer && slot.slotNumber < slot.inventory.getSizeInventory() - 11) { slot.yPos -= 8192; } });
        } else {
            this.inventorySlots.forEach(slot -> { if (slot.inventory instanceof InventoryPlayer && slot.slotNumber < slot.inventory.getSizeInventory() - 11) { slot.yPos += 8192; } });
            updateOrbSlots();
        }

        this.orbMode = !this.orbMode;
    }

    private void updateOrbSlots() {
        IInventory inputSlots = null;
        IInventory outputSlot = null;
        try {
            inputSlots = (IInventory) ContainerSpectriteRepair.inputSlots.get(this);
            outputSlot = (IInventory) ContainerSpectriteRepair.outputSlot.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ItemStack input1Stack = inputSlots.getStackInSlot(0);
        ItemStack input2Stack = inputSlots.getStackInSlot(1);
        ItemStack outputStack = outputSlot.getStackInSlot(0);
        int slotLimit = 0;
        Item input1Item = input1Stack.getItem();
        if (input1Item == ModItems.spectrite_orb) {
            slotLimit = 7;
        } else if (input1Item instanceof ItemSpectriteArmor) {
            slotLimit = ((ItemSpectriteArmor) input1Item).getNumOrbSlots();
        }

        final int[] slotOrder = new int[] { 0, 1, 5, 6, 2, 4, 3 };

        this.inventorySlots.removeIf(slot -> slot.inventory instanceof InventorySpectriteOrbEffects);

        for (int i = 1; i <= 7; ++i) {
            int row = i % 3;
            int col = (int) Math.floor(i / 3);
            int yOffset = -9 + 9 * col;

            if (!input2Stack.isEmpty() && input2Stack.getItem() == ModItems.spectrite_orb) {
                this.addSlotToContainer(new SlotSpectriteOrbEffect(input2OrbSlots, slotOrder[i - 1], 62 + 18 * col, 81 + row * 18 + yOffset));
            }
            if ((slotLimit == 7 && i <= slotLimit) || (slotLimit < 7 && i > 2 && i <= slotLimit + 2)) {
                int slotIndex;
                if (slotLimit < 7) {
                    slotIndex = i - 3;
                    yOffset += (3 - slotLimit) * 9;
                } else {
                    slotIndex = slotOrder[i - 1];
                }
                this.addSlotToContainer(new SlotSpectriteOrbEffect(input1OrbSlots, slotIndex, -1 + 18 * col, 81 + row * 18 + yOffset));
                if (!outputStack.isEmpty()) {
                    this.addSlotToContainer(new SlotSpectriteOrbEffect(outputOrbSlots, slotLimit == 7 ? slotOrder[i - 1] : i - 3, 125 + 18 * col, 81 + row * 18 + yOffset));
                }
            }
        }

        input1OrbSlots.setOrbStack(input1Stack);
        input2OrbSlots.setOrbStack(input2Stack);
        outputOrbSlots.setOrbStack(outputStack);
    }

    public boolean isOrbMode() {
        return this.orbMode;
    }
}
