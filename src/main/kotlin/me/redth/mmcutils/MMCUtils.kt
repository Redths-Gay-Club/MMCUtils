package me.redth.mmcutils

import me.redth.mmcutils.core.Core
import me.redth.mmcutils.config.ModConfig
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = MMCUtils.MODID, name = MMCUtils.NAME, version = MMCUtils.VERSION, modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter")
object MMCUtils {
    const val MODID = "@ID@"
    const val NAME = "@NAME@"
    const val VERSION = "@VER@"

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(Core)
        ModConfig
    }

}