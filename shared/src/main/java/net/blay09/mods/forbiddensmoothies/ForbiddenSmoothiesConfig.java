package net.blay09.mods.forbiddensmoothies;

import net.blay09.mods.balm.api.Balm;

public class ForbiddenSmoothiesConfig {
    public static ForbiddenSmoothiesConfigData getActive() {
        return Balm.getConfig().getActive(ForbiddenSmoothiesConfigData.class);
    }

    public static void initialize() {
        Balm.getConfig().registerConfig(ForbiddenSmoothiesConfigData.class, null);
    }
}
