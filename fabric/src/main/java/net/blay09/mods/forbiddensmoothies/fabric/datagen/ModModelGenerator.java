package net.blay09.mods.forbiddensmoothies.fabric.datagen;

import net.blay09.mods.forbiddensmoothies.block.CustomBlockStateProperties;
import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.blay09.mods.forbiddensmoothies.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import static net.minecraft.data.models.BlockModelGenerators.createHorizontalFacingDispatch;

public class ModModelGenerator extends FabricModelProvider {
    public ModModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        createNonTemplateUglifyableHorizontalBlock(blockStateModelGenerator, ModBlocks.printer);
        createNonTemplateUglifyableHorizontalBlock(blockStateModelGenerator, ModBlocks.blender);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.uglySteelPlating, ModelTemplates.FLAT_ITEM);
    }

    public final void createNonTemplateUglifyableHorizontalBlock(BlockModelGenerators blockStateModelGenerator, Block block) {
        final var modelLocation = ModelLocationUtils.getModelLocation(block);
        final var uglyModelLocation = ModelLocationUtils.getModelLocation(block, "_ugly");
        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block,
                Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(block))).with(createHorizontalFacingDispatch())
                .with(PropertyDispatch.property(CustomBlockStateProperties.UGLY)
                        .select(false, Variant.variant().with(VariantProperties.MODEL, modelLocation))
                        .select(true, Variant.variant().with(VariantProperties.MODEL, uglyModelLocation))));
    }
}
