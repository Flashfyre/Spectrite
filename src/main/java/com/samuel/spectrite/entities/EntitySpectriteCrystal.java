package com.samuel.spectrite.entities;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.init.ModBlocks;
import com.samuel.spectrite.init.ModSounds;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySpectriteCrystal extends EntityEnderCrystal {

    public EntitySpectriteCrystal(World world) {
        super(world);
    }

    public EntitySpectriteCrystal(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.innerRotation;

        if (!this.world.isRemote)
        {
            BlockPos blockpos = new BlockPos(this);

            if (this.world.getBlockState(blockpos).getBlock() != ModBlocks.spectrite_fire)
            {
                this.world.setBlockState(blockpos, ModBlocks.spectrite_fire.getDefaultState());
            }
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if (!this.isDead && !this.world.isRemote)
            {
                this.setDead();

                if (!this.world.isRemote)
                {
                    BlockPos hitPos = new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
                    Spectrite.Proxy.newSpectriteExplosion(this.world,this, this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, 6F, true, this.world.getGameRules().getBoolean("mobGriefing"), true);
                    this.world.playSound(null, hitPos, ModSounds.explosion, SoundCategory.BLOCKS, 1.0f, 0.5F + (world.rand.nextFloat()) * 0.4F);
                    world.playSound(null, hitPos, ModSounds.fatality, SoundCategory.BLOCKS, 1.0F, 0.75F);
                }
            }

            return true;
        }
    }

}
