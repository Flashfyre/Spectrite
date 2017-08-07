package com.samuel.spectrite.etc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;

public class ContainerSpectriteChest extends ContainerChest {

	public ContainerSpectriteChest(
		IInventory playerInventory,
		IInventory chestInventory, EntityPlayer player) {
		super(playerInventory, chestInventory, player);
	}	
}
