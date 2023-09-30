package me.redth.mmcutils

import me.redth.mmcutils.config.ModConfig
import net.minecraft.block.BlockColored
import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import org.spongepowered.asm.mixin.injection.invoke.arg.Args

object Hooks {
    fun modifyArgs(args: Args, stateIn: IBlockState, blockPosIn: BlockPos) {
        if (!MMCUtils.inBridgingGame) return
        if (!ModConfig.heightLimitOverlay) return
        if (blockPosIn.y != 99) return
        if (stateIn.block !is BlockColored) return
        val meta = stateIn.getValue(BlockColored.COLOR).metadata
        if (meta != 14 && meta != 11) return
        val brightness = 1f - ModConfig.heightLimitDarkness / 10f
        for (i in 0..2) {
            args.set(i, args.get<Float>(i) * brightness)
        }
    }
}