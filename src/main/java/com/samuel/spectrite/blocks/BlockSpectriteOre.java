package com.samuel.spectrite.blocks;

import com.samuel.spectrite.etc.IMetaBlockName;
import com.samuel.spectrite.init.ModItems;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockSpectriteOre extends BlockOre implements IMetaBlockName {
	
	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 2);
	
	public BlockSpectriteOre() {
		super(Material.ROCK.getMaterialMapColor());
		this.setHarvestLevel("pickaxe", 3);
		this.setDefaultState(this.getBlockState().getBaseState());
	}
	
	@Override
	/**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ModItems.spectrite_gem;
    }

    @Override
    /**
     * Get the quantity dropped based on the given fortune level
     */
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(this.getBlockState().getValidStates().iterator().next(), random, fortune))
        {
            int i = random.nextInt(fortune + 2) - 1;

            if (i < 0)
            {
                i = 0;
            }

            return this.quantityDropped(random) * (i + 1);
        }
        else
        {
            return this.quantityDropped(random);
        }
    }
    
    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
            return MathHelper.getInt(rand, 5, 12);
        return 0;
    }
    
    @Override
    /**
     * Location sensitive version of getExplosionRestance
     *
     * @param world The current world
     * @param pos Block position in world
     * @param exploder The entity that caused the explosion, can be null
     * @param explosion The explosion
     * @return The amount of the explosion absorbed.
     */
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this) {
        	int type = state.getValue(TYPE);
        	if (type == 0)
        		return this.blockResistance;
        	else if (type == 1)
        		return Blocks.NETHERRACK.getExplosionResistance(world, pos, exploder, explosion);
        	else
        		return Blocks.END_STONE.getExplosionResistance(world, pos, exploder, explosion);
        }
        return getExplosionResistance(exploder);
    }
    
    @Override
    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
    	int type = state.getValue(TYPE);
        return type == 0 ? this.blockMapColor : type == 1 ?
        	Blocks.NETHERRACK.getMapColor(Blocks.NETHERRACK.getDefaultState(), worldIn, pos) :
        	Blocks.END_STONE.getMapColor(Blocks.END_STONE.getDefaultState(), worldIn, pos);
    }
    
    @Override
	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing;

		return this.getDefaultState().withProperty(TYPE, meta);
	}

	@Override
	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE);
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	/**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> list)
    {
    	Item item = Item.getItemFromBlock(this);
    	list.add(new ItemStack(item));
    	list.add(new ItemStack(item, 1, 1));
    	list.add(new ItemStack(item, 1, 2));
    }

	@Override
	public String getSpecialName(ItemStack stack) {
		return stack.getItemDamage() == 0 ? "surface" : stack.getItemDamage() == 1 ? "nether" : "end";
	}
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer.Builder(this).add(TYPE).build();
    }
}
