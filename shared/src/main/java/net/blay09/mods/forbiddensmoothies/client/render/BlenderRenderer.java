package net.blay09.mods.forbiddensmoothies.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.forbiddensmoothies.block.entity.BlenderBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BlenderRenderer implements BlockEntityRenderer<BlenderBlockEntity> {

    public BlenderRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BlenderBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
    }

}
