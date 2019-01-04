package com.samuel.spectrite.tileentity;

import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.blocks.BlockSpectriteChest;
import com.samuel.spectrite.containers.ContainerSpectriteChest;
import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.init.ModEnchantments;
import com.samuel.spectrite.inventory.InventoryLargeSpectriteChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class TileEntitySpectriteChest extends TileEntityChest {
	
	private int ticksSinceSync;
	private BlockChest.Type cachedChestType;
	private String customName;

	public TileEntitySpectriteChest() {
		this.cachedChestType = null;
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
		if (blockType == null)
			blockType = getBlockType();
		String name = blockType.getTranslationKey() + ".name";
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
	
	public BlockChest.Type getMineralChestType() {
		
		if (this.cachedChestType == null) {
			if (this.world == null
				|| !(this.getBlockType() instanceof BlockSpectriteChest)) {
				return BlockChest.Type.BASIC;
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

    @Override
	@SideOnly(Side.CLIENT)
	public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
		return new net.minecraft.util.math.AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 2, 2));
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

        if (!this.world.isRemote && (this.ticksSinceSync + i + j + k) % 200 == 0)
        {
        	if (this.numPlayersUsing != 0) {
	            this.numPlayersUsing = 0;
	
	            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(i - 5.0F, j - 5.0F, k - 5.0F, i + 1 + 5.0F, j + 1 + 5.0F, k + 1 + 5.0F)))
	            {
	                if (entityplayer.openContainer instanceof ContainerSpectriteChest)
	                {
	                    IInventory iinventory = ((ContainerSpectriteChest)entityplayer.openContainer).getLowerChestInventory();
	
	                    if (iinventory == this || iinventory instanceof InventoryLargeSpectriteChest &&
	                    	((InventoryLargeSpectriteChest)iinventory).isPartOfLargeChest(this))
	                    {
	                        ++this.numPlayersUsing;
	                    }
	                }
	            }
        	}
        	
        	if (trySpectriteEnhance()) {
        		((WorldServer) this.world).spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, EnumParticleTypes.FIREWORKS_SPARK.getShouldIgnoreRange(),
        				i + 0.5D, j + 1.5D, k + 0.5D, 7, this.world.rand.nextGaussian() * 0.125D, this.world.rand.nextGaussian() * 0.125D,
        				this.world.rand.nextGaussian() * 0.125D, 0.1D, new int[0]);
        		this.world.playSound((EntityPlayer)null, i + 0.5D, j + 0.5D, k + 0.5D, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS,
    				0.5F, (this.world.rand.nextFloat() * 0.4F + 1.0F));
        	}
        }

        this.prevLidAngle = this.lidAngle;

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

            this.world.playSound((EntityPlayer)null, d1, j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += 0.1F;
            }
            else
            {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            if (this.lidAngle < 0.5F && f2 >= 0.5F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
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

                this.world.playSound((EntityPlayer)null, d3, j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
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
		return new ContainerSpectriteChest(playerInventory,
			this, playerIn);
	}
	
	private Map<Integer, ItemStack> getPerfectSpectriteItemsAndSlots() {
		 Map<Integer, ItemStack> ret = new LinkedHashMap<Integer, ItemStack>();
		 Iterator<ItemStack> inventoryIterator = this.getItems().iterator();
		 
		 int i = 0;
		 
		 while (inventoryIterator.hasNext()) {
			 ItemStack s = inventoryIterator.next();
			 if (ModEnchantments.spectrite_enhance.canApplyAtSpectriteAnvil(s)) {
				 ret.put(i, s);
			 }
			 i++;
	 	}
		 
		 return ret;
	}
	
	public boolean trySpectriteEnhance() {
		double enchantRate = SpectriteConfig.blocks.spectriteChestEnchantRate;
		if (enchantRate > 0.0D && this.world.rand.nextInt((int) (100 / enchantRate)) == 0) {
			int index = this.world.rand.nextInt(this.getSizeInventory());
			Map<Integer, ItemStack> slotStacks = getPerfectSpectriteItemsAndSlots();
			
			if (slotStacks.containsKey(index)) {
				ItemStack enchantedStack = slotStacks.get(index);
				if (!SpectriteHelper.isStackSpectriteEnhanced(enchantedStack)) {
					enchantedStack.addEnchantment(ModEnchantments.spectrite_enhance, 1);
					this.setInventorySlotContents(index, enchantedStack);
					return true;
				}
			}
		}
		
		return false;
	}
}
