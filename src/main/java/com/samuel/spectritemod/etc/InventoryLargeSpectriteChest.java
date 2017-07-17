package com.samuel.spectritemod.etc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.world.ILockableContainer;

public class InventoryLargeSpectriteChest extends InventoryLargeChest {

	public InventoryLargeSpectriteChest(String nameIn,
		ILockableContainer upperChestIn,
		ILockableContainer lowerChestIn) {
		super(nameIn, upperChestIn, lowerChestIn);
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerSpectriteChest(playerInventory, this, playerIn);
    }
}