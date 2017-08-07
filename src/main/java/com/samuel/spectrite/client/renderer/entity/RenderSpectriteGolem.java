package com.samuel.spectrite.client.renderer.entity;

import java.util.Map;

import com.google.common.collect.Maps;
import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.etc.SpectriteHelper;

import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectriteGolem extends RenderIronGolem {
	
	private static final Map<String, ResourceLocation> GOLEM_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();
	
	public RenderSpectriteGolem(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityIronGolem entity) {
		int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());
        String textureLoc = String.format("%s:textures/entities/spectrite_golem/%d.png", Spectrite.MOD_ID, curFrame);
		ResourceLocation resourceLocation = GOLEM_TEXTURE_RES_MAP.get(textureLoc);
		
        if (resourceLocation == null)
        {
            resourceLocation = new ResourceLocation(textureLoc);
            GOLEM_TEXTURE_RES_MAP.put(textureLoc, resourceLocation);
        }
		
		return resourceLocation;
	}
}
