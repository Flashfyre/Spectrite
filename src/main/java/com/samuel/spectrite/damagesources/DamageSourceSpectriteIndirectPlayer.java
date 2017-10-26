package com.samuel.spectrite.damagesources;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

/**
 *  This damage source exists to to fool Vanilla logic into not treating it as a player source but still allow
 *  Spectrite logic to retrieve the player entity
 */
public class DamageSourceSpectriteIndirectPlayer extends DamageSource {

    private EntityPlayer player;

    public DamageSourceSpectriteIndirectPlayer(EntityPlayer player) {
        super("spectrite");
        this.player = player;
        this.setMagicDamage();
    }

    @Override
    /**
     * Gets the death message that is displayed when the player dies
     */
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
        EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity() != null ? entityLivingBaseIn.getAttackingEntity() : this.player;
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";
        return entitylivingbase != null && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(),
            entitylivingbase.getDisplayName()}) : new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName()});
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }
}
