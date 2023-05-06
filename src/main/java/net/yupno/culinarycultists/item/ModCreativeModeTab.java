package net.yupno.culinarycultists.item;


import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.yupno.culinarycultists.CulinaryCultists;

@Mod.EventBusSubscriber(modid = CulinaryCultists.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTab {
    public static CreativeModeTab CULINARYCULTISTS_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event){
        CULINARYCULTISTS_TAB = event.registerCreativeModeTab(new ResourceLocation(CulinaryCultists.MOD_ID, "culinarycultists_tab"),
                builder -> builder.icon(() -> new ItemStack(ModItems.ZIRCON.get())).title(Component.translatable("culinarycultists.creative.tab")).build());
    }
}
