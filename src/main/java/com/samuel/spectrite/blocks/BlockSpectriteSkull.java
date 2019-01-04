package com.samuel.spectrite.blocks;

import com.google.common.base.Predicate;
import com.samuel.spectrite.entities.EntitySpectriteWither;
import com.samuel.spectrite.helpers.SpectriteHelper;
import com.samuel.spectrite.init.ModBlocks;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.items.ItemSpectriteSkull;
import com.samuel.spectrite.tileentity.TileEntitySpectriteSkull;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockSpectriteSkull extends BlockSkull {

    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, ItemSpectriteSkull.SKULL_TYPES.length - 1);

    private BlockPattern spectriteWitherBasePattern;
    private BlockPattern spectriteWitherPattern;
    private static final Predicate<BlockWorldState> IS_SPECTRITE_WITHER_SKELETON = new Predicate<BlockWorldState>()
    {
        public boolean apply(@Nullable BlockWorldState p_apply_1_)
        {
            return p_apply_1_.getBlockState() != null && p_apply_1_.getBlockState().getBlock() == ModBlocks.spectrite_skull
            && p_apply_1_.getTileEntity() instanceof TileEntitySpectriteSkull && ((TileEntitySpectriteSkull)p_apply_1_.getTileEntity()).getSkullType() == 0;
        }
    };


    public BlockSpectriteSkull() {
        super();
        this.setSoundType(SoundType.METAL);
    }

    @Override
    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySpectriteSkull();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        int type = 0;
        int damage = 0;
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntitySpectriteSkull)
        {
            type = ((TileEntitySpectriteSkull)tileentity).getSkullType();
            damage = ((TileEntitySpectriteSkull)tileentity).getDamage();
        }

        return new ItemStack(type == 0 ? ModItems.spectrite_wither_skeleton_skull : type == 1 ? ModItems.spectrite_wither_skull : ModItems.spectrite_wither_invulnerable_skull, 1, damage);
    }

    @Override
    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
    public String getLocalizedName()
    {
        return I18n.translateToLocal(this.getTranslationKey() + ".name");
    }

    @Override
    public java.util.List<ItemStack> getDrops(IBlockAccess worldIn, BlockPos pos,
        IBlockState state, int fortune)
    {
        List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        if (!state.getValue(NODROP).booleanValue())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntitySpectriteSkull) {
                int skullType = ((TileEntitySpectriteSkull) tileentity).getSkullType();
                ItemStack itemstack = new ItemStack(skullType == 0 ? ModItems.spectrite_wither_skeleton_skull : skullType == 1 ? ModItems.spectrite_wither_skull : ModItems.spectrite_wither_invulnerable_skull,
                    1, ((TileEntitySpectriteSkull) tileentity).getDamage());

                ret.add(itemstack);
            }
        }
        return ret;
    }

    @Override
    public void checkWitherSpawn(World worldIn, BlockPos pos, TileEntitySkull te)
    {
        if (te.getSkullType() == 0 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote && te instanceof TileEntitySpectriteSkull)
        {
            BlockPattern blockpattern = this.getWitherPattern();
            BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(worldIn, pos);

            if (blockpattern$patternhelper != null) {
                for (int i = 0; i < 3; ++i) {
                    BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, 0, 0);
                    worldIn.setBlockState(blockworldstate.getPos(), blockworldstate.getBlockState().withProperty(NODROP, Boolean.valueOf(true)), 2);
                }

                for (int j = 0; j < blockpattern.getPalmLength(); ++j) {
                    for (int k = 0; k < blockpattern.getThumbLength(); ++k) {
                        BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(j, k, 0);
                        worldIn.setBlockState(blockworldstate1.getPos(), Blocks.AIR.getDefaultState(), 2);
                    }
                }

                BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 0, 0).getPos();
                EntitySpectriteWither entitySpectriteWither = new EntitySpectriteWither(worldIn);
                BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
                entitySpectriteWither.setLocationAndAngles((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.55D, (double) blockpos1.getZ() + 0.5D, blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F);
                entitySpectriteWither.renderYawOffset = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F;
                entitySpectriteWither.ignite();

                for (EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entitySpectriteWither.getEntityBoundingBox().grow(50.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitySpectriteWither);
                }

                worldIn.spawnEntity(entitySpectriteWither);

                for (int l = 0; l < 120; ++l) {
                    worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, (double) blockpos.getX() + worldIn.rand.nextDouble(), (double) (blockpos.getY() - 2) + worldIn.rand.nextDouble() * 3.9D, (double) blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                }

                for (int i1 = 0; i1 < blockpattern.getPalmLength(); ++i1) {
                    for (int j1 = 0; j1 < blockpattern.getThumbLength(); ++j1) {
                        BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(i1, j1, 0);
                        worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.AIR, false);
                    }
                }
            }
        }
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return SpectriteHelper.getSpectriteMapColour((World) worldIn, pos);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        int type = 0;
        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null && te instanceof TileEntitySpectriteSkull) {
            type = ((TileEntitySpectriteSkull)te).getSkullType();
        }
        return state.withProperty(TYPE, type);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer.Builder(this).add(TYPE).add(FACING).add(NODROP).build();
    }

    @Override
    protected BlockPattern getWitherBasePattern()
    {
        if (this.spectriteWitherBasePattern == null)
        {
            this.spectriteWitherBasePattern = FactoryBlockPattern.start().aisle("   ", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(ModBlocks.spectrite_sand)))
                .where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.spectriteWitherBasePattern;
    }

    @Override
    protected BlockPattern getWitherPattern()
    {
        if (this.spectriteWitherPattern == null)
        {
            this.spectriteWitherPattern = FactoryBlockPattern.start().aisle("^^^", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(ModBlocks.spectrite_sand)))
                .where('^', IS_SPECTRITE_WITHER_SKELETON).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.spectriteWitherPattern;
    }
}
