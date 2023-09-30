package me.redth.mmcutils.config

import cc.polyfrost.oneconfig.hud.SingleTextHud
import me.redth.mmcutils.MMCUtils
import net.minecraft.client.Minecraft

class HeightLimitHud : SingleTextHud("Height Limit Distance", false) {
    override fun getText(example: Boolean): String {
        if (!MMCUtils.inBridgingGame) return "3"
        val distance = 100 - Minecraft.getMinecraft().thePlayer.posY.toInt()
        return distance.toString()
    }

    override fun shouldShow(): Boolean {
        return super.shouldShow() && MMCUtils.inBridgingGame
    }
}