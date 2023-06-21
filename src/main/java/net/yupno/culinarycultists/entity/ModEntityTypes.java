package net.yupno.culinarycultists.entity;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.yupno.culinarycultists.CulinaryCultists;
import net.yupno.culinarycultists.entity.custom.SeethEntity;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CulinaryCultists.MOD_ID);

    public static final RegistryObject<EntityType<SeethEntity>> SEETH =
            ENTITY_TYPES.register("seeth",
                    () -> EntityType.Builder.of(SeethEntity::new, MobCategory.MONSTER)
                            .sized(0.4f, 0.4f)
                            .build(new ResourceLocation(CulinaryCultists.MOD_ID, "seeth").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}