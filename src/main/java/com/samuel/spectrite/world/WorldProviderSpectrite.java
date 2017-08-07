package com.samuel.spectrite.world;

import javax.annotation.Nullable;

import com.samuel.spectrite.client.renderer.sky.SpectriteSkyRenderer;
import com.samuel.spectrite.init.ModBiomes;
import com.samuel.spectrite.init.ModWorldGen;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderSpectrite extends WorldProvider {
	
	private static final SpectriteSkyRenderer skyRenderer = new SpectriteSkyRenderer();

	
	/**
     * creates a new world chunk manager for WorldProvider
     */
	@Override
    public void init()
    {
        this.biomeProvider = new BiomeProviderSingle(ModBiomes.spectrite_dungeon);
        NBTTagCompound nbttagcompound = this.world.getWorldInfo().getDimensionData(this.world.provider.getDimension());
    }

    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return new ChunkGeneratorSpectrite(this.world, this.world.getWorldInfo().isMapFeaturesEnabled(), this.world.getSeed(), this.getSpawnCoordinate());
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks)
    {
        return 0.0F;
    }

    /**
     * Returns array with sunrise/sunset colors
     */
    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
    {
        return null;
    }

    /**
     * Return Vec3D with biome specific fog color
     */
    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
    {
        int i = 10518688;
        float f = MathHelper.cos(p_76562_1_ * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int hueFrame = ((int) getWorldTime()) % 720;
		float r = hueFrame >= 480 && hueFrame < 600 ? (1f / 120) * (hueFrame - 480) : hueFrame < 120 || hueFrame >= 600 ? 1f : hueFrame < 240 ? (1f / 120) * (120 - (hueFrame - 120)) : 0f,
			g = hueFrame < 120 ? (1f / 120) * hueFrame : hueFrame < 360 ? 1f : hueFrame < 480 ? (1f / 120) * (120 - (hueFrame - 360)) : 0f,
			b = hueFrame >= 240 && hueFrame < 360 ? (1f / 120) * (hueFrame - 240) : hueFrame >= 360 && hueFrame < 600 ? 1f : hueFrame >= 600 ? (1f / 120) * (120 - (hueFrame - 600)) : 0f;
        r = r * (f * 0.0F + 0.15F);
        g = g * (f * 0.0F + 0.15F);
        b = b * (f * 0.0F + 0.15F);
        return new Vec3d(r, g, b);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isSkyColored()
    {
        return false;
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    @Override
    public boolean canRespawnHere()
    {
        return false;
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    @Override
    public boolean isSurfaceWorld()
    {
        return false;
    }

    /**
     * the y level at which clouds are rendered.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public float getCloudHeight()
    {
        return 8.0F;
    }

    @Override
    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    public boolean canCoordinateBeSpawn(int x, int z)
    {
        return this.world.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
    }

    @Override
    public BlockPos getSpawnCoordinate()
    {
        return new BlockPos(100, 50, 0);
    }

    @Override
    public int getAverageGroundLevel()
    {
        return 50;
    }

    @Override
    /**
     * Returns true if the given X,Z coordinate should show environmental fog.
     */
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z)
    {
        return false;
    }

    @Override
    public DimensionType getDimensionType()
    {
        return ModWorldGen.SPECTRITE;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public net.minecraftforge.client.IRenderHandler getSkyRenderer()
    {
        return WorldProviderSpectrite.skyRenderer;
    }
}
