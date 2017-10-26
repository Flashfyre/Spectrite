package com.samuel.spectrite.update;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.SpectriteConfig;
import com.samuel.spectrite.helpers.SpectriteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UpdateNotifier {

    public static final String DOWNLOAD_URL = "https://minecraft.curseforge.com/projects/spectrite-mod/files";
    public static boolean success;
    private static boolean notified = false;
    public static String updateVersion = "";
    public static boolean newVersion = false;
    private static int ticksElapsed = 0;
    private UpdateCheckThread updateCheckThread = null;

    public UpdateNotifier() {
        if (SpectriteConfig.checkForUpdates) {
            updateCheckThread = new UpdateCheckThread();
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    @SideOnly(Side.CLIENT)
    public void onTick(TickEvent.ClientTickEvent event) {
        if(SpectriteConfig.checkForUpdates && !notified && (UpdateNotifier.newVersion || !UpdateNotifier.success) && Minecraft.getMinecraft().player != null) {
            ticksElapsed++;
            if(ticksElapsed >= 700){
                EntityPlayer player = Minecraft.getMinecraft().player;
                String modName = SpectriteHelper.getMultiColouredString(I18n.translateToLocal("spectrite.update.modname"), false);
                if (!UpdateNotifier.success) {
                    player.sendMessage(ITextComponent.Serializer.jsonToComponent(String.format(I18n.translateToLocal("spectrite.update.failed"), modName)));
                } else {
                    player.sendMessage(ITextComponent.Serializer.jsonToComponent(String.format(I18n.translateToLocal("spectrite.update.update").replace("{MCVERSION}", Spectrite.MC_VERSION), modName)));
                    player.sendMessage(ITextComponent.Serializer.jsonToComponent(String.format(I18n.translateToLocal("spectrite.update.versions"), Spectrite.VERSION, updateVersion)));
                    player.sendMessage(ITextComponent.Serializer.jsonToComponent(String.format(I18n.translateToLocal("spectrite.update.download"), DOWNLOAD_URL)));
                }
                notified = true;
                ticksElapsed = 0;
            }
        }
    }
}