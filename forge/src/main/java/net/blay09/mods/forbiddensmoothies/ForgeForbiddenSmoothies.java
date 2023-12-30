package net.blay09.mods.forbiddensmoothies;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.forbiddensmoothies.client.ForbiddenSmoothiesClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(ForbiddenSmoothies.MOD_ID)
public class ForgeForbiddenSmoothies {

    public ForgeForbiddenSmoothies() {
        Balm.initialize(ForbiddenSmoothies.MOD_ID, ForbiddenSmoothies::initialize);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> BalmClient.initialize(ForbiddenSmoothies.MOD_ID, ForbiddenSmoothiesClient::initialize));
    }
}
