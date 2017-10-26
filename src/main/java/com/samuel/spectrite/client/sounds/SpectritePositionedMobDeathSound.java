package com.samuel.spectrite.client.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpectritePositionedMobDeathSound extends SpectritePositionedSound {

    private int spectriteFrame;

    public SpectritePositionedMobDeathSound(PositionedSound sound) {
        super(sound);
        this.spectriteFrame = 9 + Minecraft.getMinecraft().world.rand.nextInt(10);
    }

    @Override
    public void update() {
        if (this.spectriteFrame != 0) {
            if (this.playTime % 2 == 0) {
                this.spectriteFrame--;
            }
            this.pitch = 1 + ((this.spectriteFrame < 18 ? (this.spectriteFrame - 9F) : (9F - (this.spectriteFrame - 18))) * 0.065F);
        }
       this.playTime++;
    }
}