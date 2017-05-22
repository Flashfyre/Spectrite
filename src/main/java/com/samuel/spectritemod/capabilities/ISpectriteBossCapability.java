package com.samuel.spectritemod.capabilities;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo;

public interface ISpectriteBossCapability {
	
	public boolean isEnabled();
	
	public void setEnabled(boolean enabled);
	
	public boolean isPerfectSword();
	
	public void setPerfectSword(boolean hasPerfectSword);
	
	public BossInfo getBossInfo();
}
