package net.blay09.mods.forbiddensmoothies.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.blay09.mods.forbiddensmoothies.crafting.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEIAddon implements IModPlugin {

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        final var recipeManager = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(JeiPrinterRecipeCategory.TYPE, recipeManager.getAllRecipesFor(ModRecipes.printerRecipeType));
        registration.addRecipes(JeiBlenderRecipeCategory.TYPE, recipeManager.getAllRecipesFor(ModRecipes.blenderRecipeType));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.blender), JeiBlenderRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.printer), JeiPrinterRecipeCategory.TYPE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JeiBlenderRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JeiPrinterRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ForbiddenSmoothies.MOD_ID, "jei");
    }
}
