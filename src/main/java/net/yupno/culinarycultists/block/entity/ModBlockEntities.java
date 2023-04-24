package net.yupno.culinarycultists.block.entity;


import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.yupno.culinarycultists.CulinaryCultists;
import net.yupno.culinarycultists.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CulinaryCultists.MOD_ID);

    public static final RegistryObject<BlockEntityType<SacrificialAltarBlockEntity>> SACRIFICIAL_ALTAR =
            BLOCK_ENTITIES.register("sacrificial_altar", () ->
                    BlockEntityType.Builder.of(SacrificialAltarBlockEntity::new,
                            ModBlocks.SACRIFICIAL_ALTAR.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
