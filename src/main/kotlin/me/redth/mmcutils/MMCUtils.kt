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
    private val ALL_PROXY = arrayOf("AS Practice", "EU Practice", "NA Practice", "SA Practice")
    private val BRIDGING_GAMES = arrayOf("Bed Fight", "Fireball Fight", "Bridges", "Battle Rush")
    private val mc = Minecraft.getMinecraft()

    var checkedScoreboard = false
    var inPractice = false
    var inPartyChat = false
    var inBridgingGame = false

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(this)
        ModConfig.initialize()
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
        val cleanText = e.message.unformattedText!!

        scheduleCheckScoreboard(cleanText)

        if (!inPractice) return

        tryPartyChat(cleanText)
        checkBridgingGame(cleanText)
        checkGameEnd(cleanText)
    }

    @SubscribeEvent
    fun preventTablist(e: RenderGameOverlayEvent.Pre) {
        if (!ModConfig.disablePlayerList) return
        if (e.type != RenderGameOverlayEvent.ElementType.PLAYER_LIST) return
        if (!inPractice) return
        e.isCanceled = true
    }

    private fun tryPartyChat(chat: String) {
        if (inPartyChat) return
        if (chat !in ALL_PROXY) return
        if (!ModConfig.autoPartyChat) return

        mc.thePlayer.sendChatMessage("/p chat")
        inPartyChat = true
    }

    private fun checkBridgingGame(chat: String) {
        if (inBridgingGame) return
        if (chat !in BRIDGING_GAMES) return

        inBridgingGame = true
    }

    private fun checkGameEnd(chat: String) {
        if (!chat.startsWith("Match Results")) return

        if (inBridgingGame) inBridgingGame = false

        if (ModConfig.autoGG) mc.thePlayer.sendChatMessage(ModConfig.autoGGText)
    }

    private fun scheduleCheckScoreboard(chat: String) {
        if (checkedScoreboard) return
        if (inPractice) return
        if (chat != "Minemen Club") return
        if (!ModConfig.autoQueue) return

        Timer().schedule(1000L) { checkScoreboard() }
    }

    private fun checkScoreboard() {
        checkedScoreboard = true
        val scoreboard = mc.theWorld.scoreboard ?: return
        val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return
        for (score in scoreboard.getSortedScores(objective)) {
            val team = scoreboard.getPlayersTeam(score.playerName) ?: continue
            val text = ChatColor.stripColorCodes(ScorePlayerTeam.formatPlayerName(team, score.playerName))?: continue
            val mmcProxy = text.split(".minemen.club").firstOrNull() ?: continue
            if (mmcProxy.length != 2) continue
            mc.thePlayer.sendChatMessage("/joinqueue $mmcProxy-practice")
            inPractice = true
            break
        }
    }
}