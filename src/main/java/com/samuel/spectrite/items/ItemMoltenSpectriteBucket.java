package com.samuel.spectrite.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.UniversalBucket;

public class ItemMoltenSpectriteBucket extends UniversalBucket {
	
    public ItemMoltenSpectriteBucket()
    {
    	super();
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.MISC);
    }
}
