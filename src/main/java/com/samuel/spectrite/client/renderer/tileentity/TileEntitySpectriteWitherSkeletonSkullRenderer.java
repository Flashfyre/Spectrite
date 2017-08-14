package com.samuel.spectrite.client.renderer.tileentity;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;
import com.samuel.spectrite.etc.SpectriteHelper;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntitySpectriteWitherSkeletonSkullRenderer extends TileEntitySkullRenderer {

	private static final ResourceLocation[] SPECTRITE_WITHER_SKELETON_TEXTURES = getSpectriteResourceLocations();
	private final ModelSkeletonHead skeletonHead = new ModelSkeletonHead(0, 0, 64, 32);
	
	@Override
	public void render(TileEntitySkull p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_)
    {
        EnumFacing enumfacing = EnumFacing.getFront(p_192841_1_.getBlockMetadata() & 7);
        float f = p_192841_1_.getAnimationProgress(p_192841_8_);
        this.renderSkull((float)p_192841_2_, (float)p_192841_4_, (float)p_192841_6_, enumfacing, p_192841_1_.getSkullRotation() * 360 / 16.0F, p_192841_1_.getSkullType(),
    		p_192841_1_.getPlayerProfile(), p_192841_9_, f, p_192841_1_.getWorld());
    }
	
    public void renderSkull(float x, float y, float z, EnumFacing facing, float p_188190_5_, int skullType, @Nullable GameProfile profile, int destroyStage, float animateTicks, World worldIn)
    {
        ModelBase modelbase = this.skeletonHead;

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            this.bindTexture(SPECTRITE_WITHER_SKELETON_TEXTURES[SpectriteHelper.getCurrentSpectriteFrame(worldIn)]);
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

        float f = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();

        if (skullType == 3)
        {
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        }

        modelbase.render((Entity)null, animateTicks, 0.0F, 0.0F, p_188190_5_, 0.0F, 0.0625F);
        GlStateManager.popMatrix();

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
    
    private static ResourceLocation[] getSpectriteResourceLocations() {
		ResourceLocation[] resourceLocations = new ResourceLocation[36];
		for (int s = 0; s < resourceLocations.length; s++) {
			resourceLocations[s] = new ResourceLocation(
				String.format("spectrite:textures/entities/spectrite_wither_skeleton/%d.png", s));
		}
		return resourceLocations;
	}
}
