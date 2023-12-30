package net.blay09.mods.forbiddensmoothies.fabric.client;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.blay09.mods.forbiddensmoothies.client.ForbiddenSmoothiesClient;
import net.fabricmc.api.ClientModInitializer;

public class FabricForbiddenSmoothiesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BalmClient.initialize(ForbiddenSmoothies.MOD_ID, ForbiddenSmoothiesClient::initialize);
    }
}
