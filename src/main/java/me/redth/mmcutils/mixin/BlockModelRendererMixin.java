package me.redth.mmcutils.mixin;

import me.redth.mmcutils.Configuration;
import me.redth.mmcutils.MMCUtils;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.optifine.render.RenderEnv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {
    @ModifyArgs(method = "renderQuadsSmooth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;putColorMultiplier(FFFI)V"))
    public void modifyArgs(Args args, IBlockAccess worldIn, IBlockState stateIn, BlockPos blockPosIn, WorldRenderer instance, List<BakedQuad> list, RenderEnv env) {
        if (MMCUtils.inMMC && Configuration.heightLimitOverlay && blockPosIn.getY() == 99 && stateIn.getBlock() instanceof BlockColored) {
            int meta = stateIn.getValue(BlockColored.COLOR).getMetadata();
            if (meta == 14 || meta == 11) {
                float f = 1F - Configuration.heightLimitDarkness / 1000F;
                for (int i = 0; i < 3; i++) {
                    args.set(i, (float) args.get(i) * f);
                }
            }
        }
    }

}
