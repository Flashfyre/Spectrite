package com.samuel.spectrite.client.particles;

import com.samuel.spectrite.etc.SpectriteHelper;

import net.minecraft.client.particle.ParticleSpell;
import net.minecraft.world.World;

public class ParticleSpectriteSpell extends ParticleSpell {
	
	boolean invertColour;

	public ParticleSpectriteSpell(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double r, double g, double b, boolean invertColour) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, r, g, b);
		this.invertColour = invertColour;
	}

	@Override
	public void onUpdate()
    {
		float[] c = SpectriteHelper.getCurrentSpectriteRGBColour(invertColour);
		this.setRBGColorF(c[0], c[1], c[2]);
		
		super.onUpdate();
    }
}
