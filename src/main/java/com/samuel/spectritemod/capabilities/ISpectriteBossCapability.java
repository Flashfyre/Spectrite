package com.samuel.spectritemod.capabilities;

import net.minecraft.world.BossInfo;

public interface ISpectriteBossCapability {
	
	public boolean isEnabled();
	
	public void setEnabled(boolean enabled);
	
	public boolean isPerfectWeapon();
	
	public void setPerfectWeapon(boolean perfectWeapon);
	
	public BossInfo getBossInfo();
}
