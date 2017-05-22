package com.samuel.spectritemod.proxy;

import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.blocks.BlockMineralChest;
import com.samuel.spectritemod.blocks.BlockMineralChest.Type;
import com.samuel.spectritemod.capabilities.ISpectriteBossCapability;
import com.samuel.spectritemod.capabilities.SpectriteBossCapability;
import com.samuel.spectritemod.eventhandlers.SpectriteGeneralEventHandler;
import com.samuel.spectritemod.blocks.BlockSpectrite;
import com.samuel.spectritemod.blocks.BlockSpectriteOre;
import com.samuel.spectritemod.init.ModBlocks;
import com.samuel.spectritemod.init.ModCrafting;
import com.samuel.spectritemod.init.ModItems;
import com.samuel.spectritemod.init.ModSounds;
import com.samuel.spectritemod.init.ModTileEntities;
import com.samuel.spectritemod.init.ModWorldGen;
import com.samuel.spectritemod.items.ItemDiamondRod;
import com.samuel.spectritemod.items.ItemSpectriteArmor;
import com.samuel.spectritemod.items.ItemSpectriteAxe;
import com.samuel.spectritemod.items.ItemSpectriteAxeSpecial;
import com.samuel.spectritemod.items.ItemSpectriteGem;
import com.samuel.spectritemod.items.ItemSpectriteOrb;
import com.samuel.spectritemod.items.ItemSpectritePickaxe;
import com.samuel.spectritemod.items.ItemSpectritePickaxeSpecial;
import com.samuel.spectritemod.items.ItemSpectriteRod;
import com.samuel.spectritemod.items.ItemSpectriteShovel;
import com.samuel.spectritemod.items.ItemSpectriteShovelSpecial;
import com.samuel.spectritemod.items.ItemSpectriteSword;
import com.samuel.spectritemod.items.ItemSpectriteSwordSpecial;
import com.samuel.spectritemod.tileentity.TileEntityMineralChest;
import com.samuel.spectritemod.world.WorldGenSpectrite;

public class CommonProxy {
	
	static
    {
        FluidRegistry.enableUniversalBucket();
    }

	public void preInit(FMLPreInitializationEvent e) {
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
	    
		(SpectriteMod.BlockIronChest = new BlockMineralChest(Type.IRON))
    		.setUnlocalizedName("iron_chest");
    	(SpectriteMod.BlockGoldChest = new BlockMineralChest(Type.GOLD))
    		.setUnlocalizedName("gold_chest");
    	(SpectriteMod.BlockDiamondChest = new BlockMineralChest(Type.DIAMOND))
    		.setUnlocalizedName("diamond_chest");
    	(SpectriteMod.BlockSpectriteChest = new BlockMineralChest(Type.SPECTRITE))
			.setUnlocalizedName("spectrite_chest");
    	(SpectriteMod.BlockTrappedIronChest = new BlockMineralChest(Type.IRON_TRAPPED))
        	.setUnlocalizedName("iron_chest_trapped");
        (SpectriteMod.BlockTrappedGoldChest = new BlockMineralChest(Type.GOLD_TRAPPED))
        	.setUnlocalizedName("gold_chest_trapped");
        (SpectriteMod.BlockTrappedDiamondChest = new BlockMineralChest(Type.DIAMOND_TRAPPED))
        	.setUnlocalizedName("diamond_chest_trapped");
        (SpectriteMod.BlockTrappedSpectriteChest = new BlockMineralChest(Type.SPECTRITE_TRAPPED))
    		.setUnlocalizedName("spectrite_chest_trapped");
		(SpectriteMod.BlockSpectriteOre = new BlockSpectriteOre())
			.setHardness(6.0F).setResistance(10.0F).setUnlocalizedName("spectrite_ore");
		(SpectriteMod.BlockSpectrite = new BlockSpectrite())
			.setHardness(10.0F).setResistance(15.0F).setUnlocalizedName("spectrite_block")
			.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		(SpectriteMod.ItemDiamondRod = new ItemDiamondRod())
			.setUnlocalizedName("diamond_rod")
			.setCreativeTab(CreativeTabs.MATERIALS);
		(SpectriteMod.ItemSpectriteRod = new ItemSpectriteRod())
			.setUnlocalizedName("spectrite_rod")
			.setCreativeTab(CreativeTabs.MATERIALS);
		(SpectriteMod.ItemSpectriteGem = new ItemSpectriteGem())
			.setUnlocalizedName("spectrite_gem")
			.setCreativeTab(CreativeTabs.MATERIALS);
		SpectriteMod.SPECTRITE_TOOL.setRepairItem(new ItemStack(SpectriteMod.ItemSpectriteGem));
		SpectriteMod.PERFECT_SPECTRITE_TOOL.setRepairItem(new ItemStack(SpectriteMod.ItemSpectriteGem));
		SpectriteMod.SPECTRITE_2_TOOL.setRepairItem(new ItemStack(SpectriteMod.ItemSpectriteGem));
		SpectriteMod.PERFECT_SPECTRITE_2_TOOL.setRepairItem(new ItemStack(SpectriteMod.ItemSpectriteGem));
		(SpectriteMod.ItemSpectriteOrb = new ItemSpectriteOrb())
			.setUnlocalizedName("spectrite_orb");
		(SpectriteMod.ItemSpectriteShovel = new ItemSpectriteShovel())
			.setUnlocalizedName("spectrite_shovel");
		(SpectriteMod.ItemSpectriteShovelSpecial = new ItemSpectriteShovelSpecial())
			.setUnlocalizedName("spectrite_shovel_special");
		(SpectriteMod.ItemSpectritePickaxe = new ItemSpectritePickaxe())
			.setUnlocalizedName("spectrite_pickaxe");
		(SpectriteMod.ItemSpectritePickaxeSpecial = new ItemSpectritePickaxeSpecial())
			.setUnlocalizedName("spectrite_pickaxe_special");
		(SpectriteMod.ItemSpectriteAxe = new ItemSpectriteAxe())
			.setUnlocalizedName("spectrite_axe");
		(SpectriteMod.ItemSpectriteAxeSpecial = new ItemSpectriteAxeSpecial())
			.setUnlocalizedName("spectrite_axe_special");
		(SpectriteMod.ItemSpectriteSword = new ItemSpectriteSword(false))
			.setUnlocalizedName("spectrite_sword");
		(SpectriteMod.ItemSpectriteSwordSpecial = new ItemSpectriteSwordSpecial(false))
			.setUnlocalizedName("spectrite_sword_special");
		(SpectriteMod.ItemSpectriteSword2 = new ItemSpectriteSword(true))
			.setUnlocalizedName("spectrite_sword_2");
		(SpectriteMod.ItemSpectriteSword2Special = new ItemSpectriteSwordSpecial(true))
			.setUnlocalizedName("spectrite_sword_2_special");
		(SpectriteMod.ItemSpectriteHelmet = new ItemSpectriteArmor(EntityEquipmentSlot.HEAD))
			.setUnlocalizedName("spectrite_helmet");
		(SpectriteMod.ItemSpectriteChestplate = new ItemSpectriteArmor(EntityEquipmentSlot.CHEST))
			.setUnlocalizedName("spectrite_chestplate");
		(SpectriteMod.ItemSpectriteLeggings = new ItemSpectriteArmor(EntityEquipmentSlot.LEGS))
			.setUnlocalizedName("spectrite_leggings");
		(SpectriteMod.ItemSpectriteBoots = new ItemSpectriteArmor(EntityEquipmentSlot.FEET))
			.setUnlocalizedName("spectrite_boots");
		SpectriteMod.TileEntityMineralChest = new TileEntityMineralChest();
		SpectriteMod.spectrite = new WorldGenSpectrite();
		
		CapabilityManager.INSTANCE.register(ISpectriteBossCapability.class,
				new SpectriteBossCapability.DefaultImpl.Storage(),
				new SpectriteBossCapability.DefaultImpl.Factory());
		
		ModSounds.initSounds();
		ModBlocks.createBlocks();
		ModItems.createItems();
		ModTileEntities.initTileEntities();
		ModWorldGen.initWorldGen();

		FMLCommonHandler.instance().bus().register(SpectriteMod.Instance);
		MinecraftForge.EVENT_BUS
			.register(new SpectriteGeneralEventHandler());
		MinecraftForge.EVENT_BUS.register(SpectriteMod.Instance);
	}

	public void init(FMLInitializationEvent e) {
		ModCrafting.initCrafting();
	}

	public void postInit(FMLPostInitializationEvent e) {

	}
}
