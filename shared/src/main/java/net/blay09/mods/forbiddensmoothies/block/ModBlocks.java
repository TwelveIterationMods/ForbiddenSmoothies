package net.blay09.mods.forbiddensmoothies.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class ModBlocks {

    public static Block printer;
    public static Block blender;

    public static void initialize(BalmBlocks blocks) {
        blocks.register(() -> printer = new PrinterBlock(), () -> itemBlock(printer), id("printer"));
        blocks.register(() -> blender = new BlenderBlock(), () -> itemBlock(blender), id("blender"));
    }

    private static BlockItem itemBlock(Block block) {
        return new BlockItem(block, Balm.getItems().itemProperties());
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(ForbiddenSmoothies.MOD_ID, name);
    }

}
