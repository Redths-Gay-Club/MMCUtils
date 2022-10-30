package me.redth.mmcutils.mixin;

import me.redth.mmcutils.Config;
import me.redth.mmcutils.MMCUtils;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {
    @Redirect(method = "renderQuadsSmooth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;putColorMultiplier(FFFI)V"))
    public void modifyArgs(WorldRenderer instance, float l, float i1, float j1, int k1, IBlockAccess worldIn, IBlockState stateIn, BlockPos blockPosIn) {
        if (MMCUtils.inMMC && Config.heightLimitOverlay && blockPosIn.getY() == 99 && stateIn.getBlock() instanceof BlockColored) {
            int meta = stateIn.getValue(BlockColored.COLOR).getMetadata();
            if (meta == 14 || meta == 11) {
                l *= Config.heightLimitBrightness;
                i1 *= Config.heightLimitBrightness;
                j1 *= Config.heightLimitBrightness;
            }
        }

        instance.putColorMultiplier(l, i1, j1, k1);
    }
}
