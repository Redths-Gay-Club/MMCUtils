package me.redth.mmcutils;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class Config extends Vigilant {

    @Property(type = PropertyType.SWITCH, name = "Auto Queue", category = "General", description = "Automatically queues your current proxy's practice server when connected.")
    public static boolean autoQueue = true;

    @Property(type = PropertyType.SWITCH, name = "Auto Party Chat", category = "General", description = "Automatically enters party chat when connected.")
    public static boolean autoPartyChat = true;

    @Property(type = PropertyType.SWITCH, name = "Height Limit Overlay", category = "General", description = "Darkens wools and terracottas at height limit.")
    public static boolean heightLimitOverlay = true;

    @Property(type = PropertyType.PERCENT_SLIDER, name = "Height Limit Brightness", category = "General", description = "How bright the blocks will be.")
    public static float heightLimitBrightness = 0.5F;

    public Config() {
        super(new File("./config/mmcutils.toml"));
        initialize();
    }

    public static class Command extends CommandBase {

        @Override
        public String getCommandName() {
            return "mmcutils";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "/mmcutils";
        }

        @Override
        public void processCommand(ICommandSender sender, String[] args) {
            MMCUtils.showConfig = true;
        }

        @Override
        public List<String> getCommandAliases() {
            return Collections.emptyList();
        }

        @Override
        public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
            return Collections.emptyList();
        }

        @Override
        public boolean canCommandSenderUseCommand(ICommandSender sender) {
            return true;
        }
    }
}
