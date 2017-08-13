package com.samuel.spectrite.tileentity;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBeacon;

public class TileEntityFastUpdatingBeacon extends TileEntityBeacon {
	
	private static final Set<Potion> VALID_EFFECTS = Sets.<Potion>newHashSet();
    /** A list of beam segments for this beacon */
    private final List<TileEntityBeacon.BeamSegment> beamSegments = Lists.<TileEntityBeacon.BeamSegment>newArrayList();
    private boolean isComplete;
    /** Level of this beacon's pyramid. */
    private int levels = -1;
    /** Primary potion effect given by this beacon. */
    @Nullable
    private Potion primaryEffect;
    /** Secondary potion effect given by this beacon. */
    @Nullable
    private Potion secondaryEffect;
    /** Item given to this beacon as payment. */
    private ItemStack payment = ItemStack.EMPTY;
    private String customName;
	
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
}
