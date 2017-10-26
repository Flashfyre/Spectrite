package com.samuel.spectrite.client.gui;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.containers.ContainerSpectriteRepair;
import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.items.ItemSpectriteArmor;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiSpectriteRepair extends GuiContainer implements IContainerListener {

    private static final ResourceLocation SPECTRITE_ANVIL_RESOURCE = new ResourceLocation(Spectrite.MOD_ID,"textures/gui/container/spectrite_anvil.png");
    private final ContainerSpectriteRepair anvil;
    private GuiTextField nameField;
    private final InventoryPlayer playerInventory;

    public GuiSpectriteRepair(InventoryPlayer inventoryIn, World worldIn) {
        super(new ContainerSpectriteRepair(inventoryIn, worldIn, Minecraft.getMinecraft().player));
        this.playerInventory = inventoryIn;
        this.anvil = (ContainerSpectriteRepair)this.inventorySlots;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui()
    {
        this.xSize = 176;
        super.initGui();
        this.xSize = 196;
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.nameField = new GuiTextField(0, this.fontRenderer, i + 62, j + 24, 103, 12);
        this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(35);
        this.inventorySlots.removeListener(this);
        this.inventorySlots.addListener(this);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.inventorySlots.removeListener(this);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.fontRenderer.drawString(I18n.format("container.repair"), 50, 6, 4210752);

        if (this.anvil.maximumCost > 0)
        {
            int i = 8453920;
            boolean flag = true;
            String s = I18n.format("container.repair.cost", this.anvil.maximumCost);

            if (this.anvil.maximumCost >= 40 && !this.mc.player.capabilities.isCreativeMode)
            {
                s = I18n.format("container.repair.expensive");
                i = 16736352;
            }
            else if (!this.anvil.getSlot(2).getHasStack())
            {
                flag = false;
            }
            else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player))
            {
                i = 16736352;
            }

            if (flag)
            {
                int j = -16777216 | (i & 16579836) >> 2 | i & -16777216;
                int k = this.xSize - 26 - this.fontRenderer.getStringWidth(s);

                if (this.fontRenderer.getUnicodeFlag())
                {
                    drawRect(k - 3, 65, this.xSize - 7, 77, -16777216);
                    drawRect(k - 2, 66, this.xSize - 8, 76, -12895429);
                }
                else
                {
                    this.fontRenderer.drawString(s, k, 68, j);
                    this.fontRenderer.drawString(s, k + 1, 67, j);
                    this.fontRenderer.drawString(s, k + 1, 68, j);
                }

                this.fontRenderer.drawString(s, k, 67, i);
            }
        }

        GlStateManager.enableLighting();
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (this.nameField.textboxKeyTyped(typedChar, keyCode))
        {
            this.renameItem();
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private void renameItem()
    {
        String s = this.nameField.getText();
        Slot slot = this.anvil.getSlot(0);

        if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName()))
        {
            s = "";
        }

        this.anvil.updateItemName(s);
        this.mc.player.connection.sendPacket(new CPacketCustomPayload("MC|ItemName", (new PacketBuffer(Unpooled.buffer())).writeString(s)));
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.drawTextBox();
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(SPECTRITE_ANVIL_RESOURCE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        int spectriteFrame = SpectriteHelper.getCurrentSpectriteFrame(Minecraft.getMinecraft().world);
        this.drawModalRectWithCustomSizedTexture(i, j, 0, 0, this.xSize, this.ySize, 256, 300);
        if (anvil.isOrbMode()) {
            for (int l = 0; l < 3; l++) {
                ItemStack inputStack = this.anvil.inventorySlots.get(l).getStack();
                Item inputItem = inputStack.isEmpty() ? null : inputStack.getItem();
                if (inputItem != null) {
                    for (int m = 0; m < 3; m++) {
                        if (m == 1 || inputItem == ModItems.spectrite_orb) {
                            int colSlotLimit = (m == 1 ? inputItem == ModItems.spectrite_orb ? 3 : ((ItemSpectriteArmor) inputItem).getNumOrbSlots() : 2);
                            for (int n = 0; n < colSlotLimit; n++) {
                                this.drawModalRectWithCustomSizedTexture(i + 8 + (18 * m) + 63 * l, j + 80 + (m == 1 && colSlotLimit != 2 ?
                                    colSlotLimit != 1 ? 0 : 18 : 9) + n * 18, 26, 46, 18, 18, 256, 300);
                            }
                        }
                    }
                }
            }
        } else {
            for (int o = 0; o < 3; o++) {
                this.drawModalRectWithCustomSizedTexture(i + 17, j + 83 + (o * 18), 17, 141, 162, 18, 256, 300);
            }
        }
        this.drawModalRectWithCustomSizedTexture(i + 59, j + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16, 256, 300);

        if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack())
        {
            this.drawModalRectWithCustomSizedTexture(i + 117, j + 45, this.xSize, 0, 28, 21, 256, 300);
        }

        int offsetX, offsetY;

        if (spectriteFrame < 10) {
            offsetX = 30;
            offsetY = spectriteFrame * 30;
        } else if (spectriteFrame < 19) {
            offsetX = 0;
            offsetY = 30 + (spectriteFrame - 10) * 30;
        } else if (spectriteFrame < 27) {
            offsetX = -30 - Math.floorDiv((spectriteFrame - 19), 4) * 30;
            offsetY = 180 + ((spectriteFrame - 19) % 4) * 30;
        } else {
            offsetX = -90 - Math.floorDiv((spectriteFrame - 27), 3) * 30;
            offsetY = 210 + ((spectriteFrame - 27) % 3) * 30;
        }

        this.drawModalRectWithCustomSizedTexture(i + 17, j + 7, this.xSize + offsetX, offsetY, 30, 30, 256, 300);
    }

    /**
     * update the crafting window inventory with the items in the list
     */
    @Override
    public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList)
    {
        this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot.
     */
    @Override
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
    {
        if (slotInd == 0)
        {
            this.nameField.setText(stack.isEmpty() ? "" : stack.getDisplayName());
            this.nameField.setEnabled(!stack.isEmpty());

            if (!stack.isEmpty())
            {
                this.renameItem();
            }
        }
    }

    /**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     */
    @Override
    public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue)
    {
    }

    @Override
    public void sendAllWindowProperties(Container containerIn, IInventory inventory)
    {
    }
}
