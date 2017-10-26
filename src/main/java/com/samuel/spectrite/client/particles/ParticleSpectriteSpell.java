package com.samuel.spectrite.client.particles;

import com.samuel.spectrite.helpers.SpectriteHelper;
import net.minecraft.client.particle.ParticleSpell;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleSpectriteSpell extends ParticleSpell {
	
	private float offsetLevel;

	public ParticleSpectriteSpell(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double r, double g, double b, float offsetLevel) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, r, g, b);
		this.offsetLevel = offsetLevel;
		
		this.onUpdate();
	}

	@Override
	public void onUpdate()
    {
		float[] c = SpectriteHelper.getCurrentSpectriteRGBColour(this.offsetLevel);
		this.setRBGColorF(c[0], c[1], c[2]);
		
		super.onUpdate();
    }
}
