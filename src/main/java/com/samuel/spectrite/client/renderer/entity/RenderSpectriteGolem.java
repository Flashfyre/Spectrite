package com.samuel.spectrite.client.renderer.entity;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.helpers.SpectriteHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectriteGolem extends RenderIronGolem {
	
	private static final Int2ObjectMap<ResourceLocation> GOLEM_TEXTURE_RES_MAP = new Int2ObjectOpenHashMap<>();
	
	public RenderSpectriteGolem(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityIronGolem entity) {
		int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());

		ResourceLocation resourceLocation;
		if (GOLEM_TEXTURE_RES_MAP.containsKey(curFrame)) {
			resourceLocation = GOLEM_TEXTURE_RES_MAP.get(curFrame);
		} else {
			resourceLocation = new ResourceLocation(String.format("%s:textures/entities/spectrite_golem/%d.png", Spectrite.MOD_ID, curFrame));
			GOLEM_TEXTURE_RES_MAP.put(curFrame, resourceLocation);
		}

		return resourceLocation;
	}
}
