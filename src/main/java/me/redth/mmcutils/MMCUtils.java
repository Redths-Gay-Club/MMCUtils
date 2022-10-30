package me.redth.mmcutils;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod(modid = "mmcutils", name = "MMCUtils", version = "1.0", clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class MMCUtils {
    public static final ImmutableList<String> ALL_PROXY = ImmutableList.of("AS Practice", "EU Practice", "NA Practice", "SA Practice");
    public static final Minecraft MC = Minecraft.getMinecraft();
    public static boolean onMMC;
    public static boolean inPartyChat = false;

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        if (!e.isLocal && MC.getCurrentServerData().serverIP.contains("minemen.club")) onMMC = true;
    }

    @SubscribeEvent
    public void onQuit(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        onMMC = false;
        inPartyChat = false;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        if (!onMMC || inPartyChat) return;

        String s = e.message.getUnformattedText();

        if (ALL_PROXY.contains(s)) {
            MC.thePlayer.sendChatMessage("/p chat");
            inPartyChat = true;
        } else if ("Minemen Club".equals(s)) {
            String[] split = MC.getCurrentServerData().serverIP.split(".minemen.club");
            String mmcProxy = split[0].length() == 2 ? split[0] : "na";
            MC.thePlayer.sendChatMessage("/joinqueue " + mmcProxy + "-practice");
        }

    }
}
