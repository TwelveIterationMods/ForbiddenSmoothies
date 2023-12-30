package net.blay09.mods.forbiddensmoothies.item;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItems {

    public static DeferredObject<CreativeModeTab> creativeModeTab;

    public static void initialize(BalmItems items) {
        creativeModeTab = items.registerCreativeModeTab(id(ForbiddenSmoothies.MOD_ID), () -> new ItemStack(ModBlocks.printer));
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(ForbiddenSmoothies.MOD_ID, name);
    }
}
