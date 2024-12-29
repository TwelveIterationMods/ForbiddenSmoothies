package net.blay09.mods.forbiddensmoothies.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.forbiddensmoothies.ForbiddenSmoothies;
import net.blay09.mods.forbiddensmoothies.block.ModBlocks;
import net.blay09.mods.forbiddensmoothies.crafting.BlenderRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class JeiBlenderRecipeCategory implements IRecipeCategory<BlenderRecipe> {

    public static final RecipeType<BlenderRecipe> TYPE = RecipeType.create(ForbiddenSmoothies.MOD_ID, "blender", BlenderRecipe.class);

    private static final ResourceLocation TEXTURE = new ResourceLocation(ForbiddenSmoothies.MOD_ID, "textures/gui/jei_printer.png");

    private final IDrawable icon;
    private final IDrawableStatic background;

    public JeiBlenderRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.blender));
        background = guiHelper.createDrawable(TEXTURE, 0, 0, 128, 36);
    }

    @Override
    public RecipeType<BlenderRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.forbiddensmoothies.blender");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BlenderRecipe recipe, IFocusGroup focuses) {
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            int xi = i % 4;
            int yi = i / 4;
            final var ingredient = recipe.getIngredients().get(i);
            builder.addSlot(RecipeIngredientRole.INPUT, 1 + xi * 18, 1 + yi * 18)
                    .addIngredients(ingredient);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 111, 10)
                .addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem(RegistryAccess.EMPTY));
    }
}
