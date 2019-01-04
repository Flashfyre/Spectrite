package com.samuel.spectrite.blocks;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.containers.ContainerSpectriteRepair;
import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.init.ModBlocks;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSpectriteAnvil extends BlockAnvil {

    public static final PropertyBool ODD = PropertyBool.create("odd");

    public BlockSpectriteAnvil() {
        super();
        this.setSoundType(SoundType.ANVIL);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return SpectriteHelper.getSpectriteMapColour((World) worldIn, pos);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(ODD, (pos.getX() + pos.getY() + pos.getZ()) % 2 != 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING, DAMAGE, ODD});
    }

    /**
     * Called when the block is right clicked by a player.
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            playerIn.openGui(Spectrite.Instance,0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    public static class SpectriteAnvil extends Anvil
    {
        private final World world;
        private final BlockPos position;

        public SpectriteAnvil(World worldIn, BlockPos pos)
        {
            super(worldIn, pos);
            this.world = worldIn;
            this.position = pos;
        }

        /**
         * Get the name of this object. For players this returns their username
         */
        @Override
        public String getName()
        {
            return "spectrite_anvil";
        }

        /**
         * Get the formatted ChatComponent that will be used for the sender's username in chat
         */
        @Override
        public ITextComponent getDisplayName()
        {
            return new TextComponentTranslation(ModBlocks.spectrite_anvil.getTranslationKey() + ".name", new Object[0]);
        }

        @Override
        public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
        {
            return new ContainerSpectriteRepair(playerInventory, this.world, this.position, playerIn);
        }
    }
}
