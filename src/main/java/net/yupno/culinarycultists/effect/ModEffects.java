package net.yupno.culinarycultists.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.yupno.culinarycultists.CulinaryCultists;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CulinaryCultists.MOD_ID);

    public static final RegistryObject<MobEffect> HEALTH_LOSS = MOB_EFFECTS.register("health_loss",
            () -> new HealthLossEffect(MobEffectCategory.HARMFUL, 7475473)
                    .addAttributeModifier(Attributes.MAX_HEALTH, "9c0556be-a33d-4f15-bbc2-1b73fbe50c49", -0.5D, AttributeModifier.Operation.MULTIPLY_BASE));

    public static final RegistryObject<MobEffect> FLIGHT = MOB_EFFECTS.register("flight",
            () -> new FlightEffect(MobEffectCategory.BENEFICIAL, 15395562));

    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }

}
