package me.redth.mmcutils;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Mod(modid = MMCUtils.MODID, name = MMCUtils.NAME, version = MMCUtils.VERSION)
public class MMCUtils {
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    public static final List<String> ALL_PROXY = ImmutableList.of("AS Practice", "EU Practice", "NA Practice", "SA Practice");
    public static final List<String> BRIDGING_GAMES = ImmutableList.of("Bed Fight", "Fireball Fight", "Bridges", "Battle Rush");

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean checkedScoreboard, inPractice, inPartyChat, inBridgingGame;

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        new Configuration();
    }

    @SubscribeEvent
    public void onQuit(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        checkedScoreboard = inPractice = inPartyChat = inBridgingGame = false;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        String clean = e.message.getUnformattedText();

        if (!checkedScoreboard && !inPractice && "Minemen Club".equals(clean) && Configuration.autoQueue) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    checkScoreboard();
                }
            }, 1000L);
        }

        if (!inPractice) return;

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

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre e) {
        if (e.type != RenderGameOverlayEvent.ElementType.PLAYER_LIST) return;
        if (!checkedScoreboard) return;
        if (!Configuration.disablePlayerList) return;
        e.setCanceled(true);
    }

    public static void checkScoreboard() {
        checkedScoreboard = true;
        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        if (scoreboard == null) return;
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) return;
        for (Score score : scoreboard.getSortedScores(objective)) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String text = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
            text = ChatColor.Companion.stripColorCodes(text);
            if (text == null) continue;
            String[] split = text.split(".minemen.club");
            if (split[0].length() != 2) continue;
            String mmcProxy = split[0];
            mc.thePlayer.sendChatMessage("/joinqueue " + mmcProxy + "-practice");
            inPractice = true;
            break;
        }
    }
}
