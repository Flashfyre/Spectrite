package com.samuel.spectrite.init;

import com.samuel.spectrite.blocks.BlockSpectriteSkull;
import com.samuel.spectrite.entities.EntitySpectriteArrow;
import com.samuel.spectrite.items.ItemSpectriteSkull;
import com.samuel.spectrite.tileentity.TileEntitySpectriteSkull;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSkull;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModDispenserBehavior {

	public static void initDispenserBehavior() {
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(
			ModItems.spectrite_arrow,
			new BehaviorProjectileDispense() {
				@Override
				protected IProjectile getProjectileEntity(World worldIn,
						IPosition position, ItemStack stack) {
					EntitySpectriteArrow entityArrow = new EntitySpectriteArrow(
						worldIn, position.getX(), position
						.getY(), position.getZ());
					return entityArrow;
				}
			});
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(
			ModItems.spectrite_wither_skeleton_skull,
			new Bootstrap.BehaviorDispenseOptional()
			{
				@Override
				/**
				 * Dispense the specified stack, play the dispense sound and spawn particles.
				 */
				protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
				{
					World world = source.getWorld();
					EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
					BlockPos blockpos = source.getBlockPos().offset(enumfacing);
					BlockSkull blockSpectriteWitherSkeletonSkull = ModBlocks.spectrite_skull;
					this.successful = true;

					if (world.isAirBlock(blockpos) && blockSpectriteWitherSkeletonSkull.canDispenserPlace(world, blockpos, stack))
					{
						if (!world.isRemote)
						{
							world.setBlockState(blockpos, blockSpectriteWitherSkeletonSkull.getDefaultState()
								.withProperty(BlockSpectriteSkull.TYPE, ((ItemSpectriteSkull) stack.getItem()).getSkullType()).withProperty(BlockSpectriteSkull.FACING, EnumFacing.UP), 3);
							TileEntity tileentity = world.getTileEntity(blockpos);

							if (tileentity instanceof TileEntitySpectriteSkull)
							{
								((TileEntitySkull)tileentity).setSkullRotation(enumfacing.getOpposite().getHorizontalIndex() * 4);
								ModBlocks.spectrite_skull.checkWitherSpawn(world, blockpos, (TileEntitySkull) tileentity);
							}

							stack.shrink(1);
						}
					}
					else if (ItemArmor.dispenseArmor(source, stack).isEmpty())
					{
						this.successful = false;
					}

					return stack;
				}
			});
		}
}
