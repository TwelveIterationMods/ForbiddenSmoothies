package net.blay09.mods.forbiddensmoothies.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class BlenderRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final ItemStack resultItem;
    private final NonNullList<Ingredient> ingredients;
    private final int weight;

    public BlenderRecipe(ResourceLocation id, ItemStack resultItem, NonNullList<Ingredient> ingredients, int weight) {
        this.id = id;
        this.resultItem = resultItem;
        this.ingredients = ingredients;
        this.weight = weight;
    }

    @Override
    public boolean matches(Container container, Level level) {
        final var stackedContents = new StackedContents();
        int foundInputs = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (!itemStack.isEmpty()) {
                foundInputs++;
                stackedContents.accountStack(itemStack, 1);
            }
        }
        return foundInputs == ingredients.size() && stackedContents.canCraft(this, null);
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.resultItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return resultItem;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.printerRecipeSerializer;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.printerRecipeType;
    }


    static class Serializer implements RecipeSerializer<BlenderRecipe> {

        @Override
        public BlenderRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            final var ingredients = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for printer recipe");
            } else if (ingredients.size() > 4) {
                throw new JsonParseException("Too many ingredients for printer recipe");
            } else {
                final var resultItem = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
                final var weight = GsonHelper.getAsInt(jsonObject, "weight", 1);
                return new BlenderRecipe(id, resultItem, ingredients, weight);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> ingredients = NonNullList.create();

            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i), false);
                if (!ingredient.isEmpty()) {
                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        }

        @Override
        public BlenderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            final var resultItem = buf.readItem();
            final var ingredientCount = buf.readVarInt();
            final NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(ingredientCount);
            for (int i = 0; i < ingredientCount; i++) {
                ingredients.add(Ingredient.fromNetwork(buf));
            }
            final var weight = buf.readVarInt();
            return new BlenderRecipe(id, resultItem, ingredients, weight);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BlenderRecipe recipe) {
            buf.writeItem(recipe.resultItem);
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buf);
            }
            buf.writeVarInt(recipe.weight);
        }
    }
}
