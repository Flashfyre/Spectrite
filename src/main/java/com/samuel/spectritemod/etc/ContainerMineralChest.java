package com.samuel.spectritemod.etc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;

public class ContainerMineralChest extends ContainerChest {

	public ContainerMineralChest(
		IInventory playerInventory,
		IInventory chestInventory, EntityPlayer player) {
		super(playerInventory, chestInventory, player);
	}
}
