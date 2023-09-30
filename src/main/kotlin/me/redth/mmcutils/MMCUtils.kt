package me.redth.mmcutils

import cc.polyfrost.oneconfig.libs.universal.ChatColor
import me.redth.mmcutils.config.ModConfig
import net.minecraft.client.Minecraft
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent
import java.util.Timer
import kotlin.concurrent.schedule

@Mod(modid = MMCUtils.MODID, name = MMCUtils.NAME, version = MMCUtils.VERSION, modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter")
object MMCUtils {
    const val MODID = "@ID@"
    const val NAME = "@NAME@"
    const val VERSION = "@VER@"
    val ALL_PROXY = arrayOf("AS Practice", "EU Practice", "NA Practice", "SA Practice")
    val BRIDGING_GAMES = arrayOf("Bed Fight", "Fireball Fight", "Bridges", "Battle Rush")
    private val mc = Minecraft.getMinecraft()

    var checkedScoreboard = false
    var inPractice = false
    var inPartyChat = false
    var inBridgingGame = false

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(this)
        ModConfig
    }

    @SubscribeEvent
    fun onQuit(e: ClientDisconnectionFromServerEvent) {
        inBridgingGame = false
        inPartyChat = false
        inPractice = false
        checkedScoreboard = false
    }

    @SubscribeEvent
    fun onChat(e: ClientChatReceivedEvent) {
        val clean = e.message.unformattedText
        if (!checkedScoreboard && !inPractice && "Minemen Club" == clean && ModConfig.autoQueue) {
            Timer().schedule(1000L) { checkScoreboard() }
        }
        if (!inPractice) return
        if (!inPartyChat && ALL_PROXY.contains(clean) && ModConfig.autoPartyChat) {
            mc.thePlayer.sendChatMessage("/p chat")
            inPartyChat = true
        }
        if (!inBridgingGame) {
            if (BRIDGING_GAMES.contains(clean)) inBridgingGame = true
        }
        if (clean.startsWith("Match Results")) {
            if (inBridgingGame) inBridgingGame = false
            if (ModConfig.autoGG) mc.thePlayer.sendChatMessage(ModConfig.autoGGText)
        }
    }

    @SubscribeEvent
    fun onRenderGameOverlay(e: RenderGameOverlayEvent.Pre) {
        if (e.type != RenderGameOverlayEvent.ElementType.PLAYER_LIST) return
        if (!checkedScoreboard) return
        if (!ModConfig.disablePlayerList) return
        e.isCanceled = true
    }

    fun checkScoreboard() {
        checkedScoreboard = true
        val scoreboard = mc.theWorld.scoreboard ?: return
        val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return
        for (score in scoreboard.getSortedScores(objective)) {
            val team = scoreboard.getPlayersTeam(score.playerName)
            var text = ScorePlayerTeam.formatPlayerName(team, score.playerName)
            text = ChatColor.stripColorCodes(text) ?: continue
            val split = text.split(".minemen.club")
            if (split[0].length != 2) continue
            val mmcProxy = split[0]
            mc.thePlayer.sendChatMessage("/joinqueue $mmcProxy-practice")
            inPractice = true
            break
        }
    }
}