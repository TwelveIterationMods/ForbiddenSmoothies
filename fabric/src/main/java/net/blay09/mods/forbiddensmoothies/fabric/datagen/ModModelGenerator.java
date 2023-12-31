package net.blay09.mods.forbiddensmoothies.fabric.datagen;

import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;

public class ModModelGenerator extends FabricModelProvider {
    public ModModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.printer);
        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.blender);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
    }
}
