package com.samuel.spectrite.etc;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidMoltenSpectrite extends Fluid {
	
	protected final ResourceLocation stillOdd;
    protected final ResourceLocation flowingOdd;

	public FluidMoltenSpectrite(String fluidName, ResourceLocation still, ResourceLocation flowing, ResourceLocation stillOdd, ResourceLocation flowingOdd) {
		super(fluidName, still, flowing);
		this.stillOdd = stillOdd;
		this.flowingOdd = flowingOdd;
	}

	public ResourceLocation getStillOdd()
    {
        return stillOdd;
    }

    public ResourceLocation getFlowingOdd()
    {
        return flowingOdd;
    }
}
