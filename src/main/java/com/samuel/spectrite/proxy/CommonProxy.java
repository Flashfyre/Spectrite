package com.samuel.spectrite.proxy;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.capabilities.ISpectriteBossCapability;
import com.samuel.spectrite.capabilities.SpectriteBossCapability;
import com.samuel.spectrite.eventhandlers.SpectriteGeneralEventHandler;
import com.samuel.spectrite.init.ModBiomes;
import com.samuel.spectrite.init.ModBlocks;
import com.samuel.spectrite.init.ModCrafting;
import com.samuel.spectrite.init.ModDispenserBehavior;
import com.samuel.spectrite.init.ModEnchantments;
import com.samuel.spectrite.init.ModEntities;
import com.samuel.spectrite.init.ModItems;
import com.samuel.spectrite.init.ModLootTables;
import com.samuel.spectrite.init.ModPotions;
import com.samuel.spectrite.init.ModSounds;
import com.samuel.spectrite.init.ModTileEntities;
import com.samuel.spectrite.init.ModWorldGen;
import com.samuel.spectrite.update.UpdateNotifier;

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
		ModEntities.initEntities(Spectrite.Instance);
		
		Spectrite.ItemPropertyGetterSpectrite = new IItemPropertyGetter() {
			
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
		ModPotions.initPotionEffects();
		ModEnchantments.initEnchantments();
		ModBiomes.initBiomes();
		ModWorldGen.initWorldGen();

		FMLCommonHandler.instance().bus().register(Spectrite.Instance);
		MinecraftForge.EVENT_BUS.register(new ModBlocks());
		MinecraftForge.EVENT_BUS.register(new ModItems());
		MinecraftForge.EVENT_BUS.register(new ModEntities());
		MinecraftForge.EVENT_BUS.register(new ModBiomes());
		MinecraftForge.EVENT_BUS.register(new ModPotions());
		MinecraftForge.EVENT_BUS.register(new ModEnchantments());
		MinecraftForge.EVENT_BUS.register(new ModSounds());
		MinecraftForge.EVENT_BUS
			.register(new SpectriteGeneralEventHandler());
		MinecraftForge.EVENT_BUS
			.register(ModWorldGen.spectriteDungeon);
		MinecraftForge.EVENT_BUS
			.register(ModWorldGen.spectriteSkull);
		MinecraftForge.EVENT_BUS.register(Spectrite.Instance);
		MinecraftForge.EVENT_BUS.register(new UpdateNotifier());
	}

	public void init(FMLInitializationEvent e) {
		ModCrafting.initSmelting();
		ModCrafting.initBrewing();
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	public void spawnSpectriteSpellParticle(World world, double posX, double posY, double posZ, double r, double g, double b, int offsetLevel) { }
	
	public void spawnSpectriteSmokeNormalParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) { }

	public void spawnSpectriteSmokeLargeParticle(World world, double posX, double posY, double posZ, double xSpeed, double ySpeed, double zSpeed) { }
}
