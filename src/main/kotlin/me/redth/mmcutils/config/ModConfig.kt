package me.redth.mmcutils.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import me.redth.mmcutils.MMCUtils

object ModConfig : Config(Mod(MMCUtils.NAME, ModType.UTIL_QOL), "${MMCUtils.MODID}.json") {
    @Switch(name = "Auto Practice", subcategory = "Joining", description = "Go straight into practice once joined.")
    var autoQueue = true

    @Switch(name = "Auto Party Chat", subcategory = "Joining", description = "Enter party chat once joined practice.")
    var autoPartyChat = true

    @Switch(name = "Auto GG", subcategory = "Other", description = "Send specified text once a game ended.")
    var autoGG = false

    @Text(name = "Auto GG Text", subcategory = "Other", description = "Specify what text to send.")
    var autoGGText = "gg"

    @Switch(name = "Disable Player List", subcategory = "Other", description = "Prevent accidentally tapping tab and lag your pc.")
    var disablePlayerList = true

    @Switch(name = "Height Overlay", category = "Height Overlay", subcategory = "Height Overlay", description = "Make wools and terracottas darker at height limit.")
    var heightLimitOverlay = true

    @Slider(name = "Height Overlay Darkness", category = "Height Overlay", subcategory = "Height Overlay", description = "Adjust the darkness of height limit overlay.", min = 0F, max = 10F, step = 1)
    var heightLimitDarkness = 3

    @HUD(name = "Height Limit Distance HUD", category = "Height Limit Distance HUD")
    var hud = HeightLimitHud()

    init {
        initialize()
        addDependency("heightLimitDarkness", "heightLimitOverlay")
    }
}