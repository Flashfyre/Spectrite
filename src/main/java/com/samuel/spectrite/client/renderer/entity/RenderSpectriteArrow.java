package com.samuel.spectrite.client.renderer.entity;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.entities.EntitySpectriteArrow;
import com.samuel.spectrite.helpers.SpectriteHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectriteArrow<T extends EntitySpectriteArrow> extends RenderArrow<T> {

	private static final Int2ObjectMap<ResourceLocation> DIAMOND_SPECTRITE_ARROW_TEXTURE_RES_MAP = new Int2ObjectOpenHashMap<>();
	private static final Int2ObjectMap<ResourceLocation> SPECTRITE_ARROW_TEXTURE_RES_MAP = new Int2ObjectOpenHashMap<>();

	public RenderSpectriteArrow(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());
		ResourceLocation resourceLocation;
		if (entity.isPerfectArrow()) {
			if (SPECTRITE_ARROW_TEXTURE_RES_MAP.containsKey(curFrame)) {
				resourceLocation = SPECTRITE_ARROW_TEXTURE_RES_MAP.get(curFrame);;
			} else {
				resourceLocation = new ResourceLocation(String.format("%s:textures/entities/spectrite_arrow_special/%d.png", Spectrite.MOD_ID, curFrame));
				SPECTRITE_ARROW_TEXTURE_RES_MAP.put(curFrame, resourceLocation);
			}
		} else {
			if (DIAMOND_SPECTRITE_ARROW_TEXTURE_RES_MAP.containsKey(curFrame)) {
				resourceLocation = DIAMOND_SPECTRITE_ARROW_TEXTURE_RES_MAP.get(curFrame);;
			} else {
				resourceLocation = new ResourceLocation(String.format("%s:textures/entities/spectrite_arrow/%d.png", Spectrite.MOD_ID, curFrame));
				DIAMOND_SPECTRITE_ARROW_TEXTURE_RES_MAP.put(curFrame, resourceLocation);
			}
		}
		
		return resourceLocation;
	}
}