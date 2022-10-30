package me.redth.mmcutils;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod(modid = "mmcutils", name = "MMCUtils", version = "0.1", clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class MMCUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static final ImmutableList<String> ALL_PROXY = ImmutableList.of("AS Practice", "EU Practice", "NA Practice", "SA Practice");
    public static Config config;
    public static boolean inMMC, inPractice, inPartyChat, showConfig;

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new Config.Command());
        config = new Config();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) return;

        if (showConfig) {
            mc.displayGuiScreen(config.gui());
            showConfig = false;
        }
    }

    @SubscribeEvent
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        if (!e.isLocal && mc.getCurrentServerData().serverIP.contains("minemen.club")) inMMC = true;
    }

    @SubscribeEvent
    public void onQuit(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        inMMC = inPractice = inPartyChat = false;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        if (!inMMC) return;

        if (!inPractice && "Minemen Club".equals(e.message.getUnformattedText()) && Config.autoQueue) {
            String[] split = mc.getCurrentServerData().serverIP.split(".minemen.club");
            String mmcProxy = split[0].length() == 2 ? split[0] : "na";
            mc.thePlayer.sendChatMessage("/joinqueue " + mmcProxy + "-practice");
            inPractice = true;
        }

        if (!inPartyChat && ALL_PROXY.contains(e.message.getUnformattedText()) && Config.autoPartyChat) {
            mc.thePlayer.sendChatMessage("/p chat");
            inPartyChat = true;
        }
    }
}
