package net.yupno.culinarycultists.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.yupno.culinarycultists.CulinaryCultists;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CulinaryCultists.MOD_ID);

    public static final RegistryObject<Item> ZIRCON = ITEMS.register("zircon",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CORRUPTED_BREAD = ITEMS.register("corrupted_bread",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.6F)
                    .effect(new MobEffectInstance(MobEffects.WITHER, 600, 1), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 1), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 1), 1.0F).build())));

    public static final RegistryObject<Item> CORRUPTED_CHICKEN = ITEMS.register("corrupted_chicken",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.6F)
                    .effect(new MobEffectInstance(MobEffects.LEVITATION, 60, 2), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.SLOW_FALLING, 600, 0), 1.0F).meat().build())));

    public static final RegistryObject<Item> CORRUPTED_MUTTON = ITEMS.register("corrupted_mutton",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8F)
                    .effect(new MobEffectInstance(MobEffects.HUNGER, 600, 4), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 500, 1), 1.0F).meat().build())));

    public static final RegistryObject<Item> CORRUPTED_CARROT = ITEMS.register("corrupted_carrot",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.6F).build())));

    public static void register (IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
