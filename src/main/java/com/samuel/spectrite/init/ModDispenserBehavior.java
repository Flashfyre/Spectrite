package com.samuel.spectrite.init;

import com.samuel.spectrite.entities.EntitySpectriteArrow;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ModDispenserBehavior {

	public static void initDispenserBehavior() {
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(
			ModItems.spectrite_arrow,
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
