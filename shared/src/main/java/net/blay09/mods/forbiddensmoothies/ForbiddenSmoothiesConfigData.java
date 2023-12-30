package net.blay09.mods.forbiddensmoothies;

import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.config.Config;
import net.blay09.mods.balm.api.config.Synced;

@Config(ForbiddenSmoothies.MOD_ID)
public class ForbiddenSmoothiesConfigData implements BalmConfigData {

    public Printer printer = new Printer();
    public Blender blender = new Blender();

    public class Printer {
        @Synced
        @Comment("The maximum amount of energy the printer can store.")
        public int maxEnergy = 32000;

        @Synced
        @Comment("The amount of energy the printer consumes per tick.")
        public int energyPerTick = 20;

        @Synced
        @Comment("The amount of ticks it takes to process a recipe.")
        public int processingTicks = 40;
    }

    public class Blender {
    }
}
