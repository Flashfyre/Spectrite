package com.samuel.spectrite.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySkull;

public class TileEntitySpectriteSkull extends TileEntitySkull {

    private int skullType;
    private int damage;

	@Override
    public int getSkullType()
    {
        return this.skullType;
    }

    @Override
    public void setType(int type)
    {
        this.skullType = type;
    }

    public int getDamage() { return this.damage; }

    public void setDamage(int damage) { this.damage = damage; }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("SkullType", (byte)(this.skullType & 255));
        compound.setInteger("Damage", this.damage);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.skullType = compound.getByte("SkullType");
        this.damage = compound.getInteger("Damage");
    }
}