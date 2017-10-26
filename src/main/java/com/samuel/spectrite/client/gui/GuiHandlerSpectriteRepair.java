package com.samuel.spectrite.client.gui;

import com.samuel.spectrite.containers.ContainerSpectriteRepair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandlerSpectriteRepair implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID,  EntityPlayer player, World world, int x, int y,  int z) {
        if (ID == 0)
            return new ContainerSpectriteRepair(player.inventory, world, new BlockPos(x, y, z), player);

        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getClientGuiElement(int ID,  EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 0)
            return new GuiSpectriteRepair(player.inventory, world);

        return null;
    }
}
