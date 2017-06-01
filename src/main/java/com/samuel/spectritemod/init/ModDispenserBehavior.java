package com.samuel.spectritemod.init;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.entities.EntitySpectriteArrow;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ModDispenserBehavior {

	public static void initDispenserBehavior() {
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(
			SpectriteMod.ItemSpectriteArrow,
			new BehaviorProjectileDispense() {
				@Override
				protected IProjectile getProjectileEntity(World worldIn,
						IPosition position, ItemStack stack) {
					EntitySpectriteArrow entityarrow = new EntitySpectriteArrow(
						worldIn, position.getX(), position
						.getY(), position.getZ());
					return entityarrow;
				}
			});
	}
}
