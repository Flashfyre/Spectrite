package com.samuel.spectritemod.proxy;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.capabilities.ISpectriteBossCapability;
import com.samuel.spectritemod.capabilities.SpectriteBossCapability;
import com.samuel.spectritemod.eventhandlers.SpectriteGeneralEventHandler;
import com.samuel.spectritemod.init.ModBiomes;
import com.samuel.spectritemod.init.ModBlocks;
import com.samuel.spectritemod.init.ModCrafting;
import com.samuel.spectritemod.init.ModDispenserBehavior;
import com.samuel.spectritemod.init.ModEntities;
import com.samuel.spectritemod.init.ModItems;
import com.samuel.spectritemod.init.ModLootTables;
import com.samuel.spectritemod.init.ModSounds;
import com.samuel.spectritemod.init.ModTileEntities;
import com.samuel.spectritemod.init.ModWorldGen;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy {
	
	static
    {
        FluidRegistry.enableUniversalBucket();
    }

	public void preInit(FMLPreInitializationEvent e) {
		ModEntities.initEntities(SpectriteMod.Instance);
		
		SpectriteMod.ItemPropertyGetterSpectrite = new IItemPropertyGetter() {
			
			public int curFrame = 0;
			
	        @Override
	        @SideOnly(Side.CLIENT)
	        public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
	            boolean flag = entityIn != null;
	            Entity entity = flag ? entityIn : stack.getItemFrame();
	            float value = 0.0F;
	            
	            if (worldIn == null && entity != null) {
	                worldIn = entity.world;
	            }

	            if (worldIn == null) {
	                return 0.0F;
	            } else {
	            	float time = MathHelper.ceil((((worldIn.getTotalWorldTime() >> 1) % 36)
	            		* 0.2777F) * 1000F) / 10000F;
	                curFrame = Math.round(time * 36);
	                return time;
	            }
	        }
	    };
		
		ModBlocks.createBlocks();
		ModItems.createItems();
		ModLootTables.registerLootTables();
		
		CapabilityManager.INSTANCE.register(ISpectriteBossCapability.class,
				new SpectriteBossCapability.DefaultImpl.Storage(),
				new SpectriteBossCapability.DefaultImpl.Factory());
		
		ModSounds.initSounds();
		ModTileEntities.initTileEntities();
		ModDispenserBehavior.initDispenserBehavior();
		ModBiomes.initBiomes();
		ModWorldGen.initWorldGen();

		FMLCommonHandler.instance().bus().register(SpectriteMod.Instance);
		MinecraftForge.EVENT_BUS.register(new ModBlocks());
		MinecraftForge.EVENT_BUS.register(new ModItems());
		MinecraftForge.EVENT_BUS.register(new ModSounds());
		MinecraftForge.EVENT_BUS
			.register(new SpectriteGeneralEventHandler());
		MinecraftForge.EVENT_BUS
			.register(ModWorldGen.spectriteDungeon);
		MinecraftForge.EVENT_BUS.register(SpectriteMod.Instance);
	}

	public void init(FMLInitializationEvent e) {
		ModCrafting.initCrafting();
	}

	public void postInit(FMLPostInitializationEvent e) {

	}
}
