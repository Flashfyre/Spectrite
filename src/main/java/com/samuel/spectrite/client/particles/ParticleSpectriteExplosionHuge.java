package com.samuel.spectrite.client.particles;

import com.samuel.spectrite.Spectrite;
import net.minecraft.client.particle.ParticleExplosionHuge;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleSpectriteExplosionHuge extends ParticleExplosionHuge {

    private int timeSinceStart;
    /** the maximum time for the explosion */
    private final int maximumTime;
    private final double strength;

    public ParticleSpectriteExplosionHuge(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double strength) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0d, 0d, 0d);
        this.strength = strength;
        this.maximumTime = new Double(this.strength).intValue() << 1;
    }

    @Override
    public void onUpdate()
    {
        for (int i = 0; i < ((int) strength << 1) - 3; ++i)
        {
            double d0 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * strength;
            double d1 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * strength;
            double d2 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * strength;
            Spectrite.Proxy.spawnSpectriteExplosionParticle(this.world, true, d0, d1, d2, (double)((float)this.timeSinceStart / (float)this.maximumTime), 0.0D, 0.0D);
        }

        ++this.timeSinceStart;

        if (this.timeSinceStart == this.maximumTime)
        {
            this.setExpired();
        }
    }
}
