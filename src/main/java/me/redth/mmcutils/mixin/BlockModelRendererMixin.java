package me.redth.mmcutils.mixin;

import me.redth.mmcutils.core.Core;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.optifine.render.RenderEnv;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(BlockModelRenderer.class)
public abstract class BlockModelRendererMixin {
    @Dynamic("optifine")
    @ModifyArgs(
        remap = false,
        method = "renderQuadsSmooth",
        at = @At(
            value = "INVOKE",
            remap = true,
            target = "Lnet/minecraft/client/renderer/WorldRenderer;putColorMultiplier(FFFI)V"
        )
    )
    private void mmcUtils$modifyShadow(Args args, IBlockAccess worldIn, IBlockState stateIn, BlockPos blockPosIn, WorldRenderer instance, List<BakedQuad> list, RenderEnv env) {
        Core.INSTANCE.modifyArgs(args, stateIn, blockPosIn);
    }
}
