package net.yupno.culinarycultists.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.yupno.culinarycultists.CulinaryCultists;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CulinaryCultists.MOD_ID);

    public static final RegistryObject<RecipeSerializer<SacrificialAltarRecipe>> SACRIFICIAL_ALTAR_SERIALIZER =
            SERIALIZERS.register("sacrificial_altar", () -> SacrificialAltarRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}

