package me.redth.mmcutils.core

import cc.polyfrost.oneconfig.utils.dsl.mc
import me.redth.mmcutils.config.ModConfig
import net.minecraft.block.BlockColored
import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import org.spongepowered.asm.mixin.injection.invoke.arg.Args

object Core {
    private val ALL_PROXY = listOf("AS Practice", "EU Practice", "NA Practice", "SA Practice")
    private val BRIDGING_GAMES = listOf("Bed Fight", "Fireball Fight", "Bridges", "Battle Rush")
    private var checkedFirstMessage = false
    private var checkedScoreboard = false
    private var inPractice = false
    private var inPartyChat = false
    var inBridgingGame = false

    fun modifyArgs(args: Args, stateIn: IBlockState, blockPosIn: BlockPos) {
        if (!inBridgingGame) return
        if (!ModConfig.heightLimitOverlay) return
        if (blockPosIn.y != 99) return
        if (stateIn.block !is BlockColored) return
        val meta = stateIn.getValue(BlockColored.COLOR).metadata
        if (meta != 14 && meta != 11) return
        val brightness = 1f - ModConfig.heightLimitDarkness / 10f
        for (i in 0..2) {
            args.set(i, args.get<Float>(i) * brightness)
        }
    }

    @SubscribeEvent
    fun onQuit(e: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        checkedFirstMessage = false
        inBridgingGame = false
        inPartyChat = false
        inPractice = false
        checkedScoreboard = false
    }

    @SubscribeEvent
    fun onChat(e: ClientChatReceivedEvent) {
        val cleanText = e.message.unformattedText.trim()
        if (cleanText.isEmpty()) return

        if (!checkedFirstMessage) {
            checkedFirstMessage = true
            inPractice = cleanText in ALL_PROXY
            return
        }

        if (!inPractice) return

        tryPartyChat()
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

    private fun tryPartyChat() {
        if (!ModConfig.autoPartyChat) return
        if (inPartyChat) return

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
}