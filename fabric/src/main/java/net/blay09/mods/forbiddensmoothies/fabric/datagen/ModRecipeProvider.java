package net.blay09.mods.forbiddensmoothies.fabric.datagen;

import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.blay09.mods.forbiddensmoothies.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.printer)
                .pattern("ICI")
                .pattern("GLG")
                .pattern("IRI")
                .define('C', Items.COPPER_INGOT)
                .define('G', Items.GLASS_PANE)
                .define('I', Items.IRON_INGOT)
                .define('L', Items.LIGHTNING_ROD)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_redstone", has(Items.REDSTONE))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.blender)
                .pattern("III")
                .pattern("GFG")
                .pattern("III")
                .define('G', Items.GLASS_PANE)
                .define('I', Items.IRON_INGOT)
                .define('F', Items.FLINT)
                .unlockedBy("has_flint", has(Items.FLINT))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.uglySteelPlating)
                .pattern(" I ")
                .pattern("IPI")
                .pattern(" I ")
                .define('P', Items.POTATO)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_potato", has(Items.POTATO))
                .save(exporter);
    }
}
