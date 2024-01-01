package net.blay09.mods.forbiddensmoothies.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class TinyHumanModel extends PlayerModel<LivingEntity> {

    public TinyHumanModel(ModelPart modelPart, boolean slim) {
        super(modelPart, slim);
    }

    public void animate(float animationTime) {
        rightArm.xRot = 0.1f;
        leftLeg.xRot = -0.1f;
        leftArm.yRot = 0.1f;
        leftArm.yRot = (float) (-Math.sin(animationTime / 20f) * 0.5f);
        leftArm.zRot = (float) (-Math.sin(animationTime / 20f) - Math.toRadians(50f));
        leftArm.zRot = (float) (-Math.sin(0f / 20f) - Math.toRadians(50f));
        head.zRot =(float) (-Math.sin(animationTime / 40f)) * 0.1f;
        head.xRot =(float) (-Math.sin(animationTime / 60f)) * 0.05f;
        head.yRot =(float) (-Math.sin(animationTime / 30f)) * 0.05f;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float scale = 0.0625f;

        poseStack.pushPose();

        poseStack.scale(0.75F, 0.75F, 0.75F);
        poseStack.translate(0.0F, 16.0F * scale, 0.0F);
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0.0F, 24.0F * scale, 0.0F);
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        //this.hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

        poseStack.popPose();
    }

    public void renderItemInHand(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, Level level) {
        //final var itemRenderer = Minecraft.getInstance().getItemRenderer();
        //final var brushItem = new ItemStack(Items.BRUSH);
        //poseStack.pushPose();
        //rightArm.translateAndRotate(poseStack);
        //final var itemScale = 0.2f;
        //poseStack.translate(0.25f, 0.3f, 0f);
        //poseStack.scale(itemScale, itemScale, itemScale);
        //itemRenderer.renderStatic(brushItem, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, level, 0);
        //poseStack.popPose();

        final var itemRenderer = Minecraft.getInstance().getItemRenderer();
        final var itemStack = new ItemStack(Items.BRUSH);
        poseStack.pushPose();
        final var humanScale= 0.55f;
        //poseStack.scale(humanScale, humanScale, humanScale);
        translateToHand(HumanoidArm.LEFT, poseStack);
        //poseStack.mulPose(Axis.XP.rotationDegrees(-90f));
        poseStack.mulPose(Axis.YP.rotationDegrees(120f));
        poseStack.translate( -1f / 16f, 0.125f, -0.625f);
        final var itemScale = 1f;
        poseStack.scale(itemScale, itemScale, itemScale);
        //itemRenderer.renderStatic(itemStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, combinedLight, combinedOverlay, poseStack, buffer, level, 0);
        poseStack.popPose();
    }
}
