package net.blay09.mods.forbiddensmoothies.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.forbiddensmoothies.block.entity.PrinterBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class PrinterRenderer implements BlockEntityRenderer<PrinterBlockEntity> {

    public PrinterRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PrinterBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
    }

}
