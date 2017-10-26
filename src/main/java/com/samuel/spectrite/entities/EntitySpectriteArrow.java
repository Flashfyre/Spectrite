package com.samuel.spectrite.entities;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySpectriteArrow extends EntityArrow {

	private static final DataParameter<Boolean> PERFECT_ARROW = EntityDataManager.<Boolean>createKey(EntityArrow.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> FORCE_CRTICIAL = EntityDataManager.<Boolean>createKey(EntityArrow.class, DataSerializers.BOOLEAN);

	public EntitySpectriteArrow(World worldIn, EntityLivingBase shooter, boolean perfectArrow, boolean forceCritical) {
		super(worldIn, shooter);

		this.setPerfectArrow(perfectArrow);
		this.setForceCrticial(forceCritical);
	}
	
	public EntitySpectriteArrow(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }
	
	public EntitySpectriteArrow(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(PERFECT_ARROW, Boolean.FALSE);
		this.dataManager.register(FORCE_CRTICIAL, Boolean.FALSE);
	}
	
	@Override
	protected void onHit(RayTraceResult raytraceResultIn)
    {
		int power = (this.isForceCritical() || this.getIsCritical() ? 2 : 1) + (this.isPerfectArrow() ? 1 : 0);
		Spectrite.Proxy.performDispersedSpectriteDamage(this.world, power,
			SpectriteConfig.items.spectriteArrowDamageMode != SpectriteConfig.EnumSpectriteArrowDamageMode.SPECTRITE_DAMAGE ?
			power : -1, raytraceResultIn.hitVec, this,  this.shootingEntity, this.rand);
		
		super.onHit(raytraceResultIn);
		
		if (!world.isRemote) {
			this.setDead();
		}
    }

	@Override
	protected ItemStack getArrowStack()
    {
        return new ItemStack(this.isPerfectArrow() ? ModItems.spectrite_arrow_special : ModItems.spectrite_arrow);
    }

	public boolean isPerfectArrow() { return ((Boolean)this.dataManager.get(PERFECT_ARROW).booleanValue()); }

	public void setPerfectArrow(boolean perfectArrow) {
		this.dataManager.set(PERFECT_ARROW, Boolean.valueOf(perfectArrow));
	}

	public boolean isForceCritical() { return ((Boolean)this.dataManager.get(FORCE_CRTICIAL).booleanValue()); }

	public void setForceCrticial(boolean forceCritical) {
		this.dataManager.set(FORCE_CRTICIAL, Boolean.valueOf(forceCritical));
	}
}
