package me.redth.mmcutils;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod(modid = "@ID@", name = "@NAME@", version = "@VER@")
public class MMCUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static final ImmutableList<String> ALL_PROXY = ImmutableList.of("AS Practice", "EU Practice", "NA Practice", "SA Practice");
    public static final ImmutableList<String> BRIDGING_GAMES = ImmutableList.of("Bed Fight", "Fireball Fight", "Bridges", "Battle Rush");
    public static boolean inMMC, inPractice, inPartyChat, inBridgingGame;

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        new Configuration();
    }

    @SubscribeEvent
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        if (!e.isLocal && mc.getCurrentServerData().serverIP.contains("minemen.club")) inMMC = true;
    }

    @SubscribeEvent
    public void onQuit(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        inMMC = inPractice = inPartyChat = inBridgingGame = false;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        if (!inMMC) return;

        String clean = e.message.getUnformattedText();
        if (!inPractice && "Minemen Club".equals(clean) && Configuration.autoQueue) {
            String[] split = mc.getCurrentServerData().serverIP.split(".minemen.club");
            String mmcProxy = split[0].length() == 2 ? split[0] : "na";
            mc.thePlayer.sendChatMessage("/joinqueue " + mmcProxy + "-practice");
            inPractice = true;
        }

        if (!inPartyChat && ALL_PROXY.contains(clean) && Configuration.autoPartyChat) {
            mc.thePlayer.sendChatMessage("/p chat");
            inPartyChat = true;
        }

        if (!inBridgingGame) {
            if (BRIDGING_GAMES.contains(clean))
                inBridgingGame = true;
        } else if (clean.startsWith("Match Results")) {
            inBridgingGame = false;
        }
    }

}
