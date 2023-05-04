package net.yupno.culinarycultists.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.yupno.culinarycultists.CulinaryCultists;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CulinaryCultists.MOD_ID);

    public static final RegistryObject<SoundEvent> SACRIFICE_SOUND = registerSoundEvent("sacrifice_sound");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name){
        ResourceLocation id = new ResourceLocation(CulinaryCultists.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}
