package me.redth.mmcutils.config

import cc.polyfrost.oneconfig.hud.SingleTextHud
import me.redth.mmcutils.MMCUtils
import net.minecraft.client.Minecraft

class HeightLimitHud : SingleTextHud("Height Limit Distance", false) {
    override fun getText(example: Boolean) = if (MMCUtils.inBridgingGame) {
        val distance = 100 - Minecraft.getMinecraft().thePlayer.posY.toInt()
        distance.toString()
    } else "3"

    override fun shouldShow() = super.shouldShow() && MMCUtils.inBridgingGame
}