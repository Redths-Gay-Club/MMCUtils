package me.redth.mmcutils;


import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;

public class Configuration extends Config {

    @Switch(name = "Auto Practice", description = "Go straight into practice once joined.")
    public static boolean autoQueue = true;

    @Switch(name = "Auto Party Chat", description = "Enter party chat once joined practice.")
    public static boolean autoPartyChat = true;

    @Switch(name = "Height Overlay", description = "Make wools and terracottas darker at height limit.")
    public static boolean heightLimitOverlay = true;

    @Slider(name = "Height Overlay Darkness", min = 0, max = 1000, step = 100, description = "Adjust the darkness of height limit overlay.")
    public static int heightLimitDarkness = 300;

    public Configuration() {
        super(new Mod("MMCUtils", ModType.UTIL_QOL), "mmcutils.json");
        initialize();
        addDependency("heightLimitDarkness", "heightLimitOverlay");
    }
}