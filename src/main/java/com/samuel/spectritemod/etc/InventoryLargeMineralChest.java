package com.samuel.spectritemod.etc;

import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.world.ILockableContainer;

public class InventoryLargeMineralChest extends
	InventoryLargeChest {

	public InventoryLargeMineralChest(String nameIn,
		ILockableContainer upperChestIn,
		ILockableContainer lowerChestIn) {
		super(nameIn, upperChestIn, lowerChestIn);
	}
}