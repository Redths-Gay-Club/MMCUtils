package me.redth.mmcutils;

import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.hud.TextHud;
import net.minecraft.client.Minecraft;

import java.util.List;

public class HeightLimitHud extends TextHud {
    @Text(name = "Format", description = "How the text should be displayed.")
    public String format = "Height Limit Distance: %s";

    public HeightLimitHud() {
        super(false);
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (MMCUtils.inBridgingGame) {
            int dist = 100 - (int) Minecraft.getMinecraft().thePlayer.posY;
            lines.add(format.replace("%s", String.valueOf(dist)));
        } else {
            lines.add(format.replace("%s", "3"));
        }
    }

    @Override
    protected boolean shouldShow() {
        return super.shouldShow() && MMCUtils.inBridgingGame;
    }
}
