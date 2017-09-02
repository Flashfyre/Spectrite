package com.samuel.spectrite.client.renderer.tileentity;

import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.items.ItemSpectriteSkull;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntitySpectriteWitherSkeletonSkullRenderer extends TileEntitySkullRenderer {

	private static final ResourceLocation[][] SPECTRITE_SKULL_ENTITY_TEXTURES;
	private final ModelSkeletonHead skeletonHead = new ModelSkeletonHead(0, 0, 64, 32);
    private final ModelSkeletonHead witherHead = new ModelSkeletonHead(0, 0, 64, 64);

	static {
        SPECTRITE_SKULL_ENTITY_TEXTURES = new ResourceLocation[ItemSpectriteSkull.SKULL_TYPES.length][];
	    for (int t = 0; t < SPECTRITE_SKULL_ENTITY_TEXTURES.length; t++) {
	        String type = ItemSpectriteSkull.SKULL_TYPES[t];
	        if (t == 1) {
	            type += "/normal";
            } else if (t == 2) {
                type = type.replace("wither_", "wither/");
            }
            SPECTRITE_SKULL_ENTITY_TEXTURES[t] = getSpectriteResourceLocations(type);
        }
    }

	@Override
	public void render(TileEntitySkull p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_)
    {
        EnumFacing enumfacing = EnumFacing.getFront(p_192841_1_.getBlockMetadata() & 7);
        float f = p_192841_1_.getAnimationProgress(p_192841_8_);
        this.renderSkull((float)p_192841_2_, (float)p_192841_4_, (float)p_192841_6_, enumfacing, p_192841_1_.getSkullRotation() * 360 / 16.0F, p_192841_1_.getSkullType(), p_192841_9_, f, p_192841_1_.getWorld());
    }

    public void renderSkull(float x, float y, float z, EnumFacing facing, float p_188190_5_, int skullType, int destroyStage, float animateTicks, World worldIn)
    {
        ModelBase modelbase = skullType == 0 ? this.skeletonHead : this.witherHead;

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0,1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            this.bindTexture(SPECTRITE_SKULL_ENTITY_TEXTURES[skullType][SpectriteHelper.getCurrentSpectriteFrame(worldIn)]);
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        if (facing == EnumFacing.UP)
        {
            GlStateManager.translate(x + 0.5F, y, z + 0.5F);
        }
        else
        {
            switch (facing)
            {
                case NORTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.74F);
                    break;
                case SOUTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.26F);
                    p_188190_5_ = 180.0F;
                    break;
                case WEST:
                    GlStateManager.translate(x + 0.74F, y + 0.25F, z + 0.5F);
                    p_188190_5_ = 270.0F;
                    break;
                case EAST:
                default:
                    GlStateManager.translate(x + 0.26F, y + 0.25F, z + 0.5F);
                    p_188190_5_ = 90.0F;
            }
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();

        modelbase.render(null, animateTicks, 0.0F, 0.0F, p_188190_5_, 0.0F, 0.0625F);
        GlStateManager.popMatrix();

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    private static ResourceLocation[] getSpectriteResourceLocations(String skullType) {
        ResourceLocation[] resourceLocations = new ResourceLocation[36];
        for (int s = 0; s < resourceLocations.length; s++) {
            resourceLocations[s] = new ResourceLocation(
                    String.format("spectrite:textures/entities/spectrite_%s/%d.png", skullType, s));
        }
        return resourceLocations;
    }
}
