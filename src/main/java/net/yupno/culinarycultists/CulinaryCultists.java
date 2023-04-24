package net.yupno.culinarycultists;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
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
import net.yupno.culinarycultists.item.ModCreativeModeTab;
import net.yupno.culinarycultists.item.ModItems;
import org.slf4j.Logger;

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
        ModBlockEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Put items in here to add them to a Creative Mode Tab
    private void addCreative(CreativeModeTabEvent.BuildContents event){
        if(event.getTab() == ModCreativeModeTab.CULINARYCULTISTS_TAB){
            // Items
            event.accept(ModItems.ZIRCON);

            // Blocks
            event.accept(ModBlocks.ZIRCON_BLOCK);
            event.accept(ModBlocks.SACRIFICIAL_ALTAR);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
