package com.samuel.spectrite.tileentity;

import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityFastUpdatingBeacon extends TileEntityBeacon {
	
	public TileEntityFastUpdatingBeacon() {
		super();
	}

	@Override
	public void update()
    {
        if (this.world.getTotalWorldTime() % 1L == 0L)
        {
            this.updateBeacon();
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
