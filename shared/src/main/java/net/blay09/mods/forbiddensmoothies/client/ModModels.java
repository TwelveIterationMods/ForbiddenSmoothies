package net.blay09.mods.forbiddensmoothies.client;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public class ModModels {
    public static DeferredObject<BakedModel> blenderBlade;

    public static void initialize(BalmModels models) {
        blenderBlade = models.loadModel(id("block/blender_blade"));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(ForbiddenSmoothies.MOD_ID, path);
    }

}
