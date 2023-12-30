package net.blay09.mods.forbiddensmoothies.crafting;

import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {
    public static final String PRINTER_RECIPE_GROUP = "printer";
    public static final String BLENDER_RECIPE_GROUP = "blender";
    public static final ResourceLocation PRINTER_RECIPE_TYPE = new ResourceLocation(ForbiddenSmoothies.MOD_ID, PRINTER_RECIPE_GROUP);
    public static final ResourceLocation BLENDER_RECIPE_TYPE = new ResourceLocation(ForbiddenSmoothies.MOD_ID, BLENDER_RECIPE_GROUP);

    public static RecipeType<PrinterRecipe> printerRecipeType;
    public static RecipeSerializer<PrinterRecipe> printerRecipeSerializer;

    public static RecipeType<BlenderRecipe> blenderRecipeType;
    public static RecipeSerializer<BlenderRecipe> blenderRecipeSerializer;

    public static void initialize(BalmRecipes registry) {
        registry.registerRecipeType(() -> printerRecipeType = new RecipeType<>() {
                    @Override
                    public String toString() {
                        return PRINTER_RECIPE_GROUP;
                    }
                },
                () -> printerRecipeSerializer = new PrinterRecipe.Serializer(), PRINTER_RECIPE_TYPE);

        registry.registerRecipeType(() -> blenderRecipeType = new RecipeType<>() {
                    @Override
                    public String toString() {
                        return BLENDER_RECIPE_GROUP;
                    }
                },
                () -> blenderRecipeSerializer = new BlenderRecipe.Serializer(), BLENDER_RECIPE_TYPE);
    }
}
