package net.blay09.mods.forbiddensmoothies.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.forbiddensmoothies.block.CustomBlockStateProperties;
import net.blay09.mods.forbiddensmoothies.block.entity.PrinterBlockEntity;
import net.blay09.mods.forbiddensmoothies.client.ModModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

public class PrinterRenderer implements BlockEntityRenderer<PrinterBlockEntity> {

    private static final RandomSource random = RandomSource.create();

    public PrinterRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PrinterBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlayIn) {
        final var level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        final var state = blockEntity.getBlockState();
        if (state.hasProperty(CustomBlockStateProperties.UGLY) && state.getValue(CustomBlockStateProperties.UGLY)) {
            return;
        }

        final var dispatcher = Minecraft.getInstance().getBlockRenderer();
        poseStack.pushPose();
        poseStack.translate(-0.05f, 0f, 0.1f);
        dispatcher.getModelRenderer()
                .tesselateBlock(level,
                        ModModels.printerEasel.get(),
                        blockEntity.getBlockState(),
                        blockEntity.getBlockPos(),
                        poseStack,
                        buffer.getBuffer(RenderType.solid()),
                        false,
                        random,
                        0,
                        0);
        poseStack.popPose();

        final var itemStack = blockEntity.getCurrentResultItem();
        poseStack.pushPose();
        poseStack.translate(0.5f, 0f, 0.5f);
        final var itemScale = 0.3f;
        poseStack.translate(-0.08f, 0.425f, 0.115f);
        poseStack.mulPose(new Quaternionf(new AxisAngle4f((float) Math.toRadians(22.5f), 1f, 0f, 0f)));
        poseStack.scale(itemScale, itemScale, 0.1f);
        final var itemRenderer = Minecraft.getInstance().getItemRenderer();
        final var model = itemRenderer.getModel(itemStack, level, null, combinedLight);
        RenderSystem.enableBlend();
        itemRenderer.render(itemStack, ItemDisplayContext.FIXED, false, poseStack, buffer, LightTexture.FULL_BLOCK, combinedOverlayIn, model);
        poseStack.popPose();
    }

}
