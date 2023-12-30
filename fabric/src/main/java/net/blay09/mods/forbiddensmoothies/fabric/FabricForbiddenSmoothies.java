package net.blay09.mods.forbiddensmoothies.fabric;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.fabricmc.api.ModInitializer;

public class FabricForbiddenSmoothies implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initialize(ForbiddenSmoothies.MOD_ID, ForbiddenSmoothies::initialize);
    }
}
