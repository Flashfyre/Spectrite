package com.samuel.spectrite.items;

import com.samuel.spectrite.creative.CreativeTabSpectrite;
import net.minecraftforge.fluids.UniversalBucket;

public class ItemMoltenSpectriteBucket extends UniversalBucket {
	
    public ItemMoltenSpectriteBucket()
    {
    	super();
        setMaxStackSize(1);
        setCreativeTab(CreativeTabSpectrite.INSTANCE);
    }
}
