package net.blay09.mods.forbiddensmoothies.client;

import net.blay09.mods.balm.api.client.BalmClient;

public class ForbiddenSmoothiesClient {
    public static void initialize() {
        ModRenderers.initialize(BalmClient.getRenderers());
        ModScreens.initialize(BalmClient.getScreens());
        ModModels.initialize(BalmClient.getModels());
    }
}
