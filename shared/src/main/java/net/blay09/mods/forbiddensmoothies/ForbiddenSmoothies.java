package net.blay09.mods.forbiddensmoothies;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.blay09.mods.forbiddensmoothies.crafting.ModRecipes;
import net.blay09.mods.forbiddensmoothies.menu.ModMenus;
import net.blay09.mods.forbiddensmoothies.item.ModItems;
import net.blay09.mods.forbiddensmoothies.network.ModNetworking;
import net.blay09.mods.forbiddensmoothies.block.entity.ModBlockEntities;

public class ForbiddenSmoothies {

    public static final String MOD_ID = "forbiddensmoothies";

    public static void initialize() {
        ForbiddenSmoothiesConfig.initialize();
        ModNetworking.initialize(Balm.getNetworking());
        ModBlocks.initialize(Balm.getBlocks());
        ModBlockEntities.initialize(Balm.getBlockEntities());
        ModItems.initialize(Balm.getItems());
        ModMenus.initialize(Balm.getMenus());
        ModRecipes.initialize(Balm.getRecipes());
    }

}
