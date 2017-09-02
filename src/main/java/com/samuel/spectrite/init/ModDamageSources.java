package com.samuel.spectrite.init;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

import javax.annotation.Nullable;

public class ModDamageSources {

    public static DamageSource SPECTRITE_DAMAGE;

    public static void initDamageSources() {
        SPECTRITE_DAMAGE = new DamageSource("spectrite").setMagicDamage();
    }

    public static DamageSource causeIndirectSpectriteDamage(Entity source, @Nullable Entity indirectEntityIn)
    {
        return (new EntityDamageSourceIndirect("indirectSpectrite", source, indirectEntityIn)).setMagicDamage();
    }
}
