package net.blay09.mods.forbiddensmoothies.client.render;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothiesConfig;
import net.blay09.mods.forbiddensmoothies.block.CustomBlockStateProperties;
import net.blay09.mods.forbiddensmoothies.block.entity.PrinterBlockEntity;
import net.blay09.mods.forbiddensmoothies.client.ModModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

import java.util.Optional;

public class PrinterRenderer implements BlockEntityRenderer<PrinterBlockEntity> {

    private static final RandomSource random = RandomSource.create();

    private final TinyHumanModel tinyHumanModel;
    private final TinyHumanModel tinyHumanModelSlim;

    public PrinterRenderer(BlockEntityRendererProvider.Context context) {
        tinyHumanModel = new TinyHumanModel(context.bakeLayer(ModelLayers.PLAYER), false);
        tinyHumanModelSlim = new TinyHumanModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
    }

    @Override
    public void render(PrinterBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        final var level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        final var state = blockEntity.getBlockState();
        if (state.hasProperty(CustomBlockStateProperties.UGLY) && state.getValue(CustomBlockStateProperties.UGLY)) {
            return;
        }

        poseStack.pushPose();
        final var facing = state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ? state.getValue(BlockStateProperties.HORIZONTAL_FACING) : Direction.NORTH;
        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(Axis.YN.rotationDegrees(facing.toYRot() + 180f));
        poseStack.translate(-0.5f, 0f, -0.5f);

        final var dispatcher = Minecraft.getInstance().getBlockRenderer();
        poseStack.pushPose();
        poseStack.translate(-0.05f, 0.01f, 0.1f);
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

        final var animationTime = blockEntity.animate(partialTicks);
        final var scaledAnimationTime = animationTime * 2;

        final var itemStack = blockEntity.getCurrentResultItem();
        poseStack.pushPose();
        poseStack.translate(0.5f, 0f, 0.5f);
        final var itemScale = 0.3f;
        poseStack.translate(-0.08f, 0.43f, 0.116f);
        final var maxTime = ForbiddenSmoothiesConfig.getActive().printer.processingTicks + 20;
        poseStack.translate(0f, 0, -(animationTime % maxTime) / (float) maxTime * 0.003f);
        poseStack.mulPose(new Quaternionf(new AxisAngle4f((float) Math.toRadians(22.5f), 1f, 0f, 0f)));
        poseStack.scale(itemScale, itemScale, 0.1f);
        final var itemRenderer = Minecraft.getInstance().getItemRenderer();
        final var model = itemRenderer.getModel(itemStack, level, null, 0);
        RenderSystem.enableBlend();
        itemRenderer.render(itemStack, ItemDisplayContext.FIXED, false, poseStack, buffer, combinedLight, combinedOverlay, model);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(new Quaternionf(new AxisAngle4f((float) Math.toRadians(180), 1f, 0f, 0f)));
        poseStack.mulPose(new Quaternionf(new AxisAngle4f((float) Math.toRadians(100), 0f, 1f, 0f)));
        poseStack.translate(-0.2f, -0.95f, 0.18f);
        final var humanScale = 0.55f;
        poseStack.scale(humanScale, humanScale, humanScale);
        final GameProfile gameProfile = null; // TODO
        final var profileTexture = getPlayerSkin(gameProfile).orElse(null);
        final var playerModel = getPlayerModel(profileTexture);
        final var skinTexture = getPlayerSkinTexture(profileTexture);
        playerModel.animate(scaledAnimationTime);
        playerModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entitySolid(skinTexture)), combinedLight, combinedOverlay, 1f, 1f, 1f, 1f);
        poseStack.popPose();

        poseStack.pushPose();
        final var handItemScale = 0.3f;
        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.translate(0.025f, 0.4f, 0.06f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(45f));
        poseStack.mulPose(Axis.YP.rotation(0.1f));
        final var originX = 0.1f;
        final var originY = -0.1f;
        final var originZ = 0.2f;
        poseStack.mulPose(Axis.YN.rotationDegrees(10));
        poseStack.mulPose(Axis.XP.rotationDegrees(15));
        poseStack.translate(originX, originY, originZ);
        poseStack.mulPose(Axis.ZP.rotation((float) (-Math.sin(scaledAnimationTime / 20f) * 0.5f - Math.toRadians(30f))));
        poseStack.translate(-originX, -originY, -originZ);
        poseStack.scale(handItemScale, handItemScale, handItemScale);
        final var handItem = new ItemStack(Items.BRUSH);
        itemRenderer.renderStatic(handItem, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, level, 0);
        poseStack.popPose();

        poseStack.popPose();
    }

    private Optional<MinecraftProfileTexture> getPlayerSkin(@Nullable GameProfile gameProfile) {
        if (gameProfile != null) {
            final var skinInformation = Minecraft.getInstance().getSkinManager().getInsecureSkinInformation(gameProfile);
            return Optional.ofNullable(skinInformation.get(MinecraftProfileTexture.Type.SKIN));
        } else {
            return Optional.empty();
        }
    }

    private TinyHumanModel getPlayerModel(@Nullable MinecraftProfileTexture profileTexture) {
        if (profileTexture != null) {
            final var model = profileTexture.getMetadata("model");
            if ("slim".equals(model)) {
                return tinyHumanModelSlim;
            } else {
                return tinyHumanModel;
            }
        }

        return tinyHumanModelSlim;
    }

    private ResourceLocation getPlayerSkinTexture(@Nullable MinecraftProfileTexture profileTexture) {
        if (profileTexture != null) {
            return Minecraft.getInstance().getSkinManager().registerTexture(profileTexture, MinecraftProfileTexture.Type.SKIN);
        } else {
            return DefaultPlayerSkin.getDefaultSkin();
        }
    }
}
