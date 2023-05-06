package net.yupno.culinarycultists.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.yupno.culinarycultists.CulinaryCultists;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SacrificialAltarRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final int requiredFood;

    private List<ItemStack> itemsToRemove = new ArrayList<>();


    public SacrificialAltarRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, int requiredFood) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.requiredFood = requiredFood;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if(simpleContainer.isEmpty())
            return false;
        itemsToRemove = new ArrayList<>();

        // Converts simpleContainer into an itemStack list
        List<ItemStack> simpleContainer_ = new ArrayList<>();
        for (int i = 0; i < simpleContainer.getContainerSize(); i++) {
            simpleContainer_.add(simpleContainer.getItem(i));
        }

        if(requiredFood > 0){
            int food = 0;

            // Checks through all FoodItems on top of the block and adds their food values up
            for (ItemStack itemStack : simpleContainer_) {
                if(itemStack.getItem().isEdible()){
                    for (int i = 0; i < itemStack.getCount(); i++) {
                        food += itemStack.getFoodProperties(null).getNutrition();
                        itemsToRemove.add(itemStack);

                        if(food >= requiredFood)
                            break;
                    }
                }
                if(food >= requiredFood)
                    break;
            }
            if(food < requiredFood)
                return false;
        }

        // Removes any duplicates from the SimpleContainer
        for (int i = 0; i < simpleContainer_.size() - 1; i++) {
            for (int j = i + 1; j < simpleContainer_.size(); j++) {
                if (simpleContainer_.get(i).getItem().equals(simpleContainer_.get(j).getItem())) {
                    simpleContainer_.remove(j);
                    j--;
                }
            }
        }

        int match = 0;
        // Checks every item in recipeItems against every item in noDuplicates
        for (int i = 0; i < recipeItems.size(); i++) {
            for (int j = 0; j < simpleContainer_.size(); j++) {
                if(recipeItems.get(i).test(simpleContainer_.get(j))){
                    match++;
                    itemsToRemove.add(simpleContainer_.get(j));
                }
            }
        }

        // When "match" is equal to recipeItems.size() that means that all recipeItems are present on top of the block
        if(match == recipeItems.size())
            return true;
        else
            return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    public List<ItemStack> getItemsToRemove(){
        return itemsToRemove;
    }

    public NonNullList<Ingredient> getRecipeItems(){
        return recipeItems;
    }

    public int getRequiredFood(){
        return requiredFood;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<SacrificialAltarRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "sacrificial_altar";
    }

    public static class Serializer implements RecipeSerializer<SacrificialAltarRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(CulinaryCultists.MOD_ID, "sacrificial_altar");

        @Override
        public SacrificialAltarRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            int requiredFood = GsonHelper.getAsInt(pSerializedRecipe, "requiredFood");

            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new SacrificialAltarRecipe(pRecipeId, output, inputs, requiredFood);
        }

        @Override
        public @Nullable SacrificialAltarRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            int requiredFood = buf.readInt();
            return new SacrificialAltarRecipe(id, output, inputs, requiredFood);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SacrificialAltarRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }

            buf.writeItemStack(recipe.getResultItem(null), false);
            buf.writeInt(recipe.getRequiredFood());
        }
    }
}
