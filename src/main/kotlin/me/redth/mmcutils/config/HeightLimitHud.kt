package me.redth.mmcutils.config

import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.utils.dsl.mc
import me.redth.mmcutils.core.Core

class HeightLimitHud : SingleTextHud("Height Limit Distance", false) {
    override fun getText(example: Boolean) = if (Core.inBridgingGame) {
        val distance = 100 - mc.thePlayer.posY.toInt()
        distance.toString()
    } else "3"

    override fun shouldShow() = super.shouldShow() && Core.inBridgingGame
}