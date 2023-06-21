package net.yupno.culinarycultists;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.yupno.culinarycultists.block.ModBlocks;
import net.yupno.culinarycultists.block.entity.ModBlockEntities;
import net.yupno.culinarycultists.effect.ModEffects;
import net.yupno.culinarycultists.entity.ModEntityTypes;
import net.yupno.culinarycultists.entity.client.SeethRenderer;
import net.yupno.culinarycultists.item.ModCreativeModeTab;
import net.yupno.culinarycultists.item.ModItems;
import net.yupno.culinarycultists.recipe.ModRecipes;
import net.yupno.culinarycultists.sound.ModSounds;
import net.yupno.culinarycultists.villager.ModVillagers;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CulinaryCultists.MOD_ID)
public class CulinaryCultists
{
    public static final String MOD_ID = "culinarycultists";
    private static final Logger LOGGER = LogUtils.getLogger();
    public CulinaryCultists() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModVillagers.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipes.register(modEventBus);

        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);

        ModEntityTypes.register(modEventBus);

        GeckoLib.initialize();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            //ModMessages.register();
            ModVillagers.registerPOIs();
        });
    }

    // Put items in here to add them to a Creative Mode Tab
    private void addCreative(CreativeModeTabEvent.BuildContents event){
        if(event.getTab() == ModCreativeModeTab.CULINARYCULTISTS_TAB){
            // Items
            event.accept(ModItems.ZIRCON);
            event.accept(ModItems.CORRUPTED_BREAD);
            event.accept(ModItems.CORRUPTED_CHICKEN);
            event.accept(ModItems.CORRUPTED_MUTTON);
            event.accept(ModItems.CORRUPTED_CARROT);
            event.accept(ModItems.CORRUPTED_POTATO);
            event.accept(ModItems.SEETH_SPAWN_EGG);

            // Blocks
            event.accept(ModBlocks.SACRIFICE_BLOCK);
            event.accept(ModBlocks.SACRIFICIAL_ALTAR);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntityTypes.SEETH.get(), SeethRenderer::new);
        }
    }
}
