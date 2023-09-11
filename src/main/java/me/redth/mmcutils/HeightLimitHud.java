package me.redth.mmcutils;

import cc.polyfrost.oneconfig.hud.SingleTextHud;
import net.minecraft.client.Minecraft;

public class HeightLimitHud extends SingleTextHud {
    public HeightLimitHud() {
        super("Height Limit Distance", false);
    }

    @Override
    protected String getText(boolean example) {
        if (!MMCUtils.inBridgingGame) return "3";
        int distance = 100 - (int) Minecraft.getMinecraft().thePlayer.posY;
        return String.valueOf(distance);
    }

    @Override
    protected boolean shouldShow() {
        return super.shouldShow() && MMCUtils.inBridgingGame;
    }
}
