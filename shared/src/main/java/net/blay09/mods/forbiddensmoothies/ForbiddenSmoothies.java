package net.blay09.mods.forbiddensmoothies;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.blay09.mods.forbiddensmoothies.crafting.ModRecipes;
import net.blay09.mods.forbiddensmoothies.menu.ModMenus;
import net.blay09.mods.forbiddensmoothies.item.ModItems;
import net.blay09.mods.forbiddensmoothies.network.ModNetworking;
import net.blay09.mods.forbiddensmoothies.block.entity.ModBlockEntities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForbiddenSmoothies {

    // TODO fancy in-world rendering for printer
    // TODO better textures for the models

    // TODO Slightly different blender UI?
    // TODO JEI/REI support

    public static final String MOD_ID = "forbiddensmoothies";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

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
