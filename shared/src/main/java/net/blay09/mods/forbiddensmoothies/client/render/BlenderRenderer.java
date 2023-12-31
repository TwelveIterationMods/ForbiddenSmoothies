package net.blay09.mods.forbiddensmoothies.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.forbiddensmoothies.block.entity.BlenderBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

public class BlenderRenderer implements BlockEntityRenderer<BlenderBlockEntity> {

    public BlenderRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BlenderBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlayIn) {
        final var inputContainer = blockEntity.getInputContainer();
        poseStack.pushPose();
        final var itemScale = 0.2f;
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.scale(itemScale, itemScale, itemScale);
        final var animationTime = blockEntity.animate(partialTicks);
        final var offsetH = 0.5f;
        final var offsetV = 0.5f;
        final var speedH = 0.2f;
        final var speedV = 0.05f;
        final var angleSpeed = 0.25f;
        final var cosah = Math.cos(animationTime * speedH) * offsetH;
        final var sinah = Math.sin(animationTime * speedH) * offsetH;
        for (int i = 0; i < inputContainer.getContainerSize(); i++) {
            poseStack.pushPose();
            poseStack.translate(i * 0.2f - 0.5f, i * 0.2f - 0.5f, 0f);
            double sinav = Math.sin(i * animationTime * speedV) * offsetV;
            if (i % 2 == 0) {
                poseStack.translate(cosah, sinav, sinah);
            } else {
                poseStack.translate(sinah, sinav, cosah);
            }
            final var angle = (float) ((i + 1) * Math.PI * 2 / inputContainer.getContainerSize() + animationTime * angleSpeed);
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(angle, 0f, 1f, 0f)));

            final var itemStack = inputContainer.getItem(i);
            if (!itemStack.isEmpty()) {
                RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer, blockEntity.getLevel());
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }

}
