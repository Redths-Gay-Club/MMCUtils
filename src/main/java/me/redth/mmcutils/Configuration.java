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

    @Switch(name = "Disable Player List", subcategory = "Other", description = "Prevent accidentally tapping tab and lag your pc.")
    public static boolean disablePlayerList = true;

    @Switch(name = "Height Overlay", category = "Height Overlay", description = "Make wools and terracottas darker at height limit.")
    public static boolean heightLimitOverlay = true;

    @Slider(name = "Height Overlay Darkness", category = "Height Overlay", min = 0, max = 10, step = 1, description = "Adjust the darkness of height limit overlay.")
    public static int heightLimitDarkness = 3;

    @HUD(name = "Height Limit Distance HUD", category = "Height Limit Distance HUD")
    public static HeightLimitHud hud = new HeightLimitHud();

    public Configuration() {
        super(new Mod(MMCUtils.NAME, ModType.UTIL_QOL), MMCUtils.MODID + ".json");
        initialize();
        addDependency("heightLimitDarkness", "heightLimitOverlay");
    }
}