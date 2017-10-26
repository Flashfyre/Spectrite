package com.samuel.spectrite.client.sounds;

import com.samuel.spectrite.helpers.SpectriteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpectritePositionedSound implements ITickableSound {

    private final PositionedSound sound;
    protected float pitch;
    protected int playTime = 0;

    public SpectritePositionedSound(PositionedSound sound) {
        this.sound = sound;
    }

    @Override
    public void update() {
        int spectriteFrame = SpectriteHelper.getCurrentSpectriteFrame(Minecraft.getMinecraft().world);
        this.pitch = 1 + ((spectriteFrame < 18 ? (spectriteFrame - 9F) : (9F - (spectriteFrame - 18))) * 0.065F);
        this.playTime++;
    }

    public ResourceLocation getSoundLocation()
    {
        return this.sound.getSoundLocation();
    }

    public SoundEventAccessor createAccessor(SoundHandler handler)
    {
        return this.sound.createAccessor(handler);
    }

    public Sound getSound()
    {
        return this.sound.getSound();
    }

    public SoundCategory getCategory()
    {
        return this.sound.getCategory();
    }

    public boolean canRepeat()
    {
        return this.sound.canRepeat();
    }

    public int getRepeatDelay()
    {
        return this.sound.getRepeatDelay();
    }

    public float getVolume()
    {
        return this.sound.getVolume();
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public float getXPosF()
    {
        return this.sound.getXPosF();
    }

    public float getYPosF()
    {
        return this.sound.getYPosF();
    }

    public float getZPosF()
    {
        return this.sound.getZPosF();
    }

    public ISound.AttenuationType getAttenuationType()
    {
        return this.sound.getAttenuationType();
    }

    @Override
    public boolean isDonePlaying() {
        return !this.canRepeat() && playTime >= 330 || this.canRepeat() && playTime >= 660;
    }
}
