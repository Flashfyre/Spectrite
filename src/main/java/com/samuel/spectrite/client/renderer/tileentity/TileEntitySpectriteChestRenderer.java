package com.samuel.spectrite.client.renderer.tileentity;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.blocks.BlockSpectriteChest;
import com.samuel.spectrite.etc.SpectriteHelper;
import com.samuel.spectrite.tileentity.TileEntitySpectriteChest;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntitySpectriteChestRenderer extends TileEntitySpecialRenderer {
	private static final ResourceLocation[] textureSpectrite =
		getSpectriteResourceLocations("");
	private static final ResourceLocation[] textureDoubleSpectrite =
		getSpectriteResourceLocations("_double");
	private static final ResourceLocation[] textureTrappedSpectrite =
		getSpectriteResourceLocations("_trapped");
	private static final ResourceLocation[] textureDoubleTrappedSpectrite =
		getSpectriteResourceLocations("_trapped_double");
	private ModelChest simpleChest = new ModelChest();
	private ModelLargeChest largeChest = new ModelLargeChest();

	public TileEntitySpectriteChestRenderer() {
	}

	public void render(TileEntitySpectriteChest tile,
		double x, double y_, double z, float partialTick,
		int breakStage) {
		int var10;

		GlStateManager.enableDepth();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		if (!tile.hasWorld()) {
			var10 = 0;
		} else {
			Block var11 = tile.getBlockType();
			var10 = tile.getBlockMetadata();

			if (var11 instanceof BlockSpectriteChest
				&& var10 == 0) {
				((BlockSpectriteChest) var11)
					.checkForSurroundingChests(tile
						.getWorld(), tile.getPos(), tile
						.getWorld().getBlockState(
							tile.getPos()));
				var10 = tile.getBlockMetadata();
			}

			tile.checkForAdjacentChests();
		}

		if (tile.adjacentChestZNeg == null
			&& tile.adjacentChestXNeg == null) {
			final int chestType = tile.getMineralChestType().ordinal();
			ModelChest var15;

			if (tile.adjacentChestXPos == null
				&& tile.adjacentChestZPos == null) {
				var15 = this.simpleChest;

				if (breakStage >= 0) {
					this.bindTexture(DESTROY_STAGES[breakStage]);
					GlStateManager.matrixMode(5890);
					GlStateManager.pushMatrix();
					GlStateManager.scale(4.0F, 4.0F, 1.0F);
					GlStateManager.translate(0.0625F,
						0.0625F, 0.0625F);
					GlStateManager.matrixMode(5888);
				} else {
					ResourceLocation texture = null;
					switch (chestType) {
						case 0:
							textureSpectrite[SpectriteHelper.getCurrentSpectriteFrame(tile.getWorld())] = new ResourceLocation(
									String.format("spectrite:textures/tileentities/chest_spectrite%s/%d.png", "", SpectriteHelper.getCurrentSpectriteFrame(tile.getWorld())));
							texture = textureSpectrite[SpectriteHelper.getCurrentSpectriteFrame(tile.getWorld())];
							break;
						case 1:
							texture = textureTrappedSpectrite[SpectriteHelper.getCurrentSpectriteFrame(tile.getWorld())];
						default:
					}
					this.bindTexture(texture);
				}
			} else {
				var15 = this.largeChest;
				ResourceLocation textureDouble = null;
				switch (chestType) {
    				case 0:
    					textureDouble = textureDoubleSpectrite[SpectriteHelper.getCurrentSpectriteFrame(tile.getWorld())];
    					break;
    				case 1:
    					textureDouble = textureDoubleTrappedSpectrite[SpectriteHelper.getCurrentSpectriteFrame(tile.getWorld())];
    					break;
    				default:
    			}

				if (breakStage >= 0) {
					this.bindTexture(DESTROY_STAGES[breakStage]);
					GlStateManager.matrixMode(5890);
					GlStateManager.pushMatrix();
					GlStateManager.scale(8.0F, 4.0F, 1.0F);
					GlStateManager.translate(0.0625F,
						0.0625F, 0.0625F);
					GlStateManager.matrixMode(5888);
				} else {
					this.bindTexture(textureDouble);
				}
			}

			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();

			if (breakStage < 0) {
				GlStateManager
					.color(1.0F, 1.0F, 1.0F, 1.0F);
			}

			GlStateManager.translate((float) x,
				(float) y_ + 1.0F, (float) z + 1.0F);
			GlStateManager.scale(1.0F, -1.0F, -1.0F);
			GlStateManager.translate(0.5F, 0.5F, 0.5F);
			short var12 = 0;

			if (var10 == 2) {
				var12 = 180;
			}

			if (var10 == 3) {
				var12 = 0;
			}

			if (var10 == 4) {
				var12 = 90;
			}

			if (var10 == 5) {
				var12 = -90;
			}

			if (var10 == 2
				&& tile.adjacentChestXPos != null) {
				GlStateManager.translate(1.0F, 0.0F, 0.0F);
			}

			if (var10 == 5
				&& tile.adjacentChestZPos != null) {
				GlStateManager.translate(0.0F, 0.0F, -1.0F);
			}

			GlStateManager.rotate(var12, 0.0F,
				1.0F, 0.0F);
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);
			float var13 = tile.prevLidAngle
				+ (tile.lidAngle - tile.prevLidAngle)
				* partialTick;
			float var14;

			if (tile.adjacentChestZNeg != null) {
				var14 = tile.adjacentChestZNeg.prevLidAngle
					+ (tile.adjacentChestZNeg.lidAngle - tile.adjacentChestZNeg.prevLidAngle)
					* partialTick;

				if (var14 > var13) {
					var13 = var14;
				}
			}

			if (tile.adjacentChestXNeg != null) {
				var14 = tile.adjacentChestXNeg.prevLidAngle
					+ (tile.adjacentChestXNeg.lidAngle - tile.adjacentChestXNeg.prevLidAngle)
					* partialTick;

				if (var14 > var13) {
					var13 = var14;
				}
			}

			var13 = 1.0F - var13;
			var13 = 1.0F - var13 * var13 * var13;
			var15.chestLid.rotateAngleX = -(var13
				* (float) Math.PI / 2.0F);
			var15.renderAll();
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			if (breakStage >= 0) {
				GlStateManager.matrixMode(5890);
				GlStateManager.popMatrix();
				GlStateManager.matrixMode(5888);
			}
		}
	}

	@Override
	public void render(TileEntity tileentity,
		double x, double y, double z, float partialTick, int breakStage, float partial) {
		this.render((TileEntitySpectriteChest) tileentity, x,
			y, z, partialTick, breakStage);
	}
	
	private static ResourceLocation[] getSpectriteResourceLocations(String suffix) {
		ResourceLocation[] resourceLocations = new ResourceLocation[36];
		for (int s = 0; s < resourceLocations.length; s++) {
			resourceLocations[s] = new ResourceLocation(Spectrite.MOD_ID,
				String.format("textures/tileentities/chest_spectrite%s/%d.png", suffix, s));
		}
		return resourceLocations;
	}
}
