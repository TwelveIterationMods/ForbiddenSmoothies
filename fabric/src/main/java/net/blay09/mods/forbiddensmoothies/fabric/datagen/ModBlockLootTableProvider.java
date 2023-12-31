package net.blay09.mods.forbiddensmoothies.fabric.datagen;

import net.blay09.mods.forbiddensmoothies.block.CustomBlockStateProperties;
import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.blay09.mods.forbiddensmoothies.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        add(ModBlocks.printer, createSingleItemTable(ModBlocks.printer)
                .withPool(new LootPool.Builder().add(LootItem.lootTableItem(ModItems.uglySteelPlating))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.printer)
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CustomBlockStateProperties.UGLY, true)))));
        add(ModBlocks.blender, createSingleItemTable(ModBlocks.blender)
                .withPool(new LootPool.Builder().add(LootItem.lootTableItem(ModItems.uglySteelPlating))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.blender)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CustomBlockStateProperties.UGLY, true)))));
    }
}
