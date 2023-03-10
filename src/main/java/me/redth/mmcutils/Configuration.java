package me.redth.mmcutils;


import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;

public class Configuration extends Config {

    @Switch(name = "Auto Practice", subcategory = "Joining", description = "Go straight into practice once joined.")
    public static boolean autoQueue = true;

    @Switch(name = "Auto Party Chat", subcategory = "Joining", description = "Enter party chat once joined practice.")
    public static boolean autoPartyChat = true;

    @Switch(name = "Height Overlay", subcategory = "Height Overlay", description = "Make wools and terracottas darker at height limit.")
    public static boolean heightLimitOverlay = true;

    @Slider(name = "Height Overlay Darkness", subcategory = "Height Overlay", min = 0, max = 10, step = 1, description = "Adjust the darkness of height limit overlay.")
    public static int heightLimitDarkness = 3;

    @HUD(name = "Height Limit Distance HUD", subcategory = "Shows how many blocks you are away from height limit.")
    public static HeightLimitHud hud = new HeightLimitHud();

    public Configuration() {
        super(new Mod("MMCUtils", ModType.UTIL_QOL), "mmcutils.json");
        initialize();
        addDependency("heightLimitDarkness", "heightLimitOverlay");
    }
}