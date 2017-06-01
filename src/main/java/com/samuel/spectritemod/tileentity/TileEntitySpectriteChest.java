package com.samuel.spectritemod.tileentity;

import com.samuel.spectritemod.blocks.BlockSpectriteChest;
import com.samuel.spectritemod.blocks.BlockSpectriteChest.Type;
import com.samuel.spectritemod.etc.ContainerMineralChest;
import com.samuel.spectritemod.etc.InventoryLargeMineralChest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntitySpectriteChest extends TileEntityChest
	implements ITickable, IInventory {
	
	private int ticksSinceSync;
	private Type cachedChestType;
	private String customName;

	public TileEntitySpectriteChest() {
		this.cachedChestType = null;
	}

	public TileEntitySpectriteChest(Type chestType) {
		this.cachedChestType = chestType;
	}

	@Override
	/**
	 * Gets the name of this command sender (usually username, but possibly "Rcon")
	 */
	public String getName() {
		return getDisplayName().getUnformattedText();
	}

	@Override
	/**
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName() {
		return this.customName != null
			&& this.customName.length() > 0;
	}

	@Override
	public void setCustomName(String name) {
		super.setCustomName(name);
		this.customName = name;
	}

	@Override
	public ITextComponent getDisplayName() {
		final int chestType = cachedChestType != null ? cachedChestType.ordinal() +
			(cachedChestType.ordinal() == 0 ? 0 : -1) : 0;
		if (blockType == null)
			blockType = getBlockType();
		String name = blockType.getUnlocalizedName() + ".name";
		if (((BlockSpectriteChest) blockType).isDoubleChest(world, pos)) {
			name = name.substring(0, 5) + "large_" + name.substring(5);
		}
		return new TextComponentTranslation(name);
	}
	
	@Override
	public BlockChest.Type getChestType() {
		if (this.cachedChestType == null) {
			if (this.world == null
				|| !(this.getBlockType() instanceof BlockSpectriteChest)) {
				return BlockChest.Type.BASIC;
			}

			this.cachedChestType = ((BlockSpectriteChest) this
				.getBlockType()).chestType;
		}

		return this.cachedChestType.ordinal() == 0 ? BlockChest.Type.BASIC :
			BlockChest.Type.TRAP;
	}
	
	public Type getMineralChestType() {
		
		if (this.cachedChestType == null) {
			if (this.world == null
				|| !(this.getBlockType() instanceof BlockSpectriteChest)) {
				return Type.NORMAL;
			}

			this.cachedChestType = ((BlockSpectriteChest) this
				.getBlockType()).chestType;
		}
		
		return this.cachedChestType;
	}

	@Override
    protected TileEntityChest getAdjacentChest(EnumFacing side)
    {
        BlockPos blockpos = this.pos.offset(side);

        if (this.isChestAt(blockpos))
        {
            TileEntity tileentity = this.world.getTileEntity(blockpos);

            if (tileentity instanceof TileEntitySpectriteChest)
            {
                TileEntitySpectriteChest tileentitychest = (TileEntitySpectriteChest)tileentity;
                tileentitychest.func_174910_a(this, side.getOpposite());
                return tileentitychest;
            }
        }

        return null;
    }
    
    private boolean isChestAt(BlockPos posIn) {
		if (this.world == null) {
			return false;
		} else {
			Block block = this.world
				.getBlockState(posIn).getBlock();
			return block instanceof BlockSpectriteChest
				&& ((BlockSpectriteChest) block).chestType == this
					.getMineralChestType();
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void func_174910_a(
		TileEntitySpectriteChest chestTe, EnumFacing side) {
		if (chestTe.isInvalid()) {
			this.adjacentChestChecked = false;
		} else if (this.adjacentChestChecked) {
			switch (side) {
			case NORTH:

				if (this.adjacentChestZNeg != chestTe) {
					this.adjacentChestChecked = false;
				}

				break;
			case SOUTH:

				if (this.adjacentChestZPos != chestTe) {
					this.adjacentChestChecked = false;
				}

				break;
			case EAST:

				if (this.adjacentChestXPos != chestTe) {
					this.adjacentChestChecked = false;
				}

				break;
			case WEST:

				if (this.adjacentChestXNeg != chestTe) {
					this.adjacentChestChecked = false;
				}
			}
		}
	}

	@Override
	/**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        this.checkForAdjacentChests();
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;

        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            float f = 5.0F;

            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class,
            	new AxisAlignedBB(i - f, j - f, k - f,
            	i + 1 + f, j + 1 + f, k + 1 + f)))
            {
                if (entityplayer.openContainer instanceof ContainerMineralChest)
                {
                    IInventory iinventory = ((ContainerMineralChest)entityplayer.openContainer).getLowerChestInventory();

                    if (iinventory == this || iinventory instanceof InventoryLargeMineralChest &&
                    	((InventoryLargeMineralChest)iinventory).isPartOfLargeChest(this))
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        float f1 = 0.1F;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
        {
            double d1 = i + 0.5D;
            double d2 = k + 0.5D;

            if (this.adjacentChestZPos != null)
            {
                d2 += 0.5D;
            }

            if (this.adjacentChestXPos != null)
            {
                d1 += 0.5D;
            }

            this.world.playSound((EntityPlayer)null, d1, j + 0.5D, d2,
            	SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += f1;
            }
            else
            {
                this.lidAngle -= f1;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float f3 = 0.5F;

            if (this.lidAngle < f3 && f2 >= f3 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
            {
                double d3 = i + 0.5D;
                double d0 = k + 0.5D;

                if (this.adjacentChestZPos != null)
                {
                    d0 += 0.5D;
                }

                if (this.adjacentChestXPos != null)
                {
                    d3 += 0.5D;
                }

                this.world.playSound((EntityPlayer)null, d3, j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE,
                	SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }
	
	@Override
	/**
     * Gets the block type at the location of this entity (client-only).
     */
    public Block getBlockType()
    {
        if (this.blockType == null && this.world != null)
        {
            this.blockType = this.world.getBlockState(this.pos).getBlock();
            this.cachedChestType = getMineralChestType();
        }

        return this.blockType;
    }

	@Override
	/**
	 * invalidates a tile entity
	 */
	public void invalidate() {
		super.invalidate();
		this.updateContainingBlockInfo();
		this.checkForAdjacentChests();
	}

	@Override
	public String getGuiID() {
		return "minecraft:chest";
	}

	@Override
	public Container createContainer(
		InventoryPlayer playerInventory,
		EntityPlayer playerIn) {
		return new ContainerMineralChest(playerInventory,
			this, playerIn);
	}
}
