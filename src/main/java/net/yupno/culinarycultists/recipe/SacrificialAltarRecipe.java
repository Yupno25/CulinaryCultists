package net.yupno.culinarycultists.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.yupno.culinarycultists.CulinaryCultists;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

        // Converts simpleContainer into an ItemStack List
        List<ItemStack> simpleContainerDuplicates = new ArrayList<>();
        for (int i = 0; i < simpleContainer.getContainerSize(); i++) {
            simpleContainerDuplicates.add(simpleContainer.getItem(i));
        }

        // Removes any duplicates from the SimpleContainer
        List<ItemStack> simpleContainerNoDuplicates = simpleContainerDuplicates;
        for (int i = 0; i < simpleContainerNoDuplicates.size() - 1; i++) {
            for (int j = i + 1; j < simpleContainerNoDuplicates.size(); j++) {
                if (simpleContainerNoDuplicates.get(i).getItem().equals(simpleContainerNoDuplicates.get(j).getItem())) {
                    simpleContainerNoDuplicates.remove(j);
                    j--;
                }
            }
        }

        ItemStack[][] recipe_ = new ItemStack[recipeItems.size()][];
        List<ItemStack> recipe = new ArrayList<>();
        for (int i = 0; i < recipeItems.size(); i++) {
            recipe_[i] = recipeItems.get(i).getItems();
            recipe.add(recipe_[i][0]);
        }

        int match = 0;
        // Check if all the items from the recipe are present within the simpleContainer
        // This should include count
        // If there are any food items they shouldn't be included any longer to avoid using them in the food Check
        for (int i = 0; i < recipeItems.size(); i++) {
            for (int j = 0; j < simpleContainerNoDuplicates.size(); j++) {
                if (recipeItems.get(i).test(simpleContainerNoDuplicates.get(j))){
                    for (int k = 0; k < recipe.get(i).getCount(); k++) {
                        if(simpleContainerNoDuplicates.get(j).getCount() > 0){
                            itemsToRemove.add(simpleContainerNoDuplicates.get(j));
                            match++;

                        }else {
                            boolean secondItem = false;
                            for (int l = 0; l < simpleContainerDuplicates.size(); l++) {
                                if(secondItem && simpleContainerDuplicates.get(l).getItem().equals(simpleContainerNoDuplicates.get(j))){
                                    simpleContainerNoDuplicates.remove(j);
                                    simpleContainerNoDuplicates.add(simpleContainerDuplicates.get(l));
                                }

                                if(simpleContainerDuplicates.get(l).getItem().equals(simpleContainerNoDuplicates.get(j))){
                                    secondItem = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
        // Converts the recipeItems into an ItemStack List
        ItemStack[][] recipe_ = new ItemStack[recipeItems.size()][];
        List<ItemStack> recipe = new ArrayList<>();
        for (int i = 0; i < recipeItems.size(); i++) {
            recipe_[i] = recipeItems.get(i).getItems();
            recipe.add(recipe_[i][0]);
        }
         */


        /*
        // Removes any duplicates from the SimpleContainer
        for (int i = 0; i < simpleContainerNoDuplicates.size() - 1; i++) {
            for (int j = i + 1; j < simpleContainerNoDuplicates.size(); j++) {
                if (simpleContainerNoDuplicates.get(i).getItem().equals(simpleContainerNoDuplicates.get(j).getItem())) {
                    simpleContainerNoDuplicates.remove(j);
                    j--;
                }
            }
        }
         */

        /*
        for (int i = 0; i < recipe.size(); i++) {
            for (int j = 0; j < simpleContainerNoDuplicates.size(); j++) {
                if(recipeItems.get(i).test(simpleContainerNoDuplicates.get(j))){
                    // Is getting called one too many times
                    for (int k = 0; k < recipe.get(i).getCount(); k++) {
                        itemsToRemove.add(simpleContainerNoDuplicates.get(j));

                        // Currently isn't working
                        if(recipe.get(i).getCount() == k + 1)
                            recipe.remove(0);
                    }
                }
            }
        }
         */

        if(requiredFood > 0){
            int food = 0;

            // Checks through all FoodItems on top of the block and adds their food values up
            for (ItemStack itemStack : simpleContainerNoDuplicates) {
                if(itemStack.getItem().isEdible() && !itemStack.getItem().getDescriptionId().contains(CulinaryCultists.MOD_ID)){
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

        int requiredItemMatches = 0;
        for (int i = 0; i < recipe.size(); i++) {
            requiredItemMatches += recipe.get(i).getCount();
        }

        // match needs to match the total amount of required items
        if(match == requiredItemMatches)
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
