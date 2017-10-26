package com.samuel.spectrite.init;

import com.samuel.spectrite.damagesources.DamageSourceSpectriteIndirectPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;

public class ModDamageSources {

    public static DamageSource SPECTRITE_DAMAGE;
    public static DamageSource MOLTEN_SPECTRITE = new DamageSource("molten_spectrite") {
        @Override
        /**
         * Gets the death message that is displayed when the player dies
         */
        public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
        {
            EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
            String s = "death.attack." + this.damageType;
            String s1 = s + ".player";
            return entitylivingbase != null && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName()}): new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName()});
        }
    };

    public static void initDamageSources() {
        SPECTRITE_DAMAGE = new DamageSource("spectrite").setMagicDamage();
        SPECTRITE_DAMAGE = new DamageSource("spectrite").setMagicDamage();
    }

    public static DamageSource causeIndirectSpectriteDamage(Entity source, @Nullable Entity indirectEntityIn)
    {
        return (new EntityDamageSourceIndirect("indirectSpectrite", source, indirectEntityIn)).setMagicDamage();
    }

    public static DamageSource causeIndirectPlayerSpectriteDamage(EntityPlayer source)
    {
        return (new DamageSourceSpectriteIndirectPlayer(source));
    }
}
