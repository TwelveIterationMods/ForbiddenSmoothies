package net.blay09.mods.forbiddensmoothies.block.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static DeferredObject<BlockEntityType<PrinterBlockEntity>> printer;
    public static DeferredObject<BlockEntityType<BlenderBlockEntity>> blender;

    public static void initialize(BalmBlockEntities blockEntities) {
        printer = blockEntities.registerBlockEntity(id("printer"),
                PrinterBlockEntity::new,
                () -> new Block[]{
                        ModBlocks.printer
                });

        blender = blockEntities.registerBlockEntity(id("blender"),
                BlenderBlockEntity::new,
                () -> new Block[]{
                        ModBlocks.blender
                });
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(ForbiddenSmoothies.MOD_ID, name);
    }

}
