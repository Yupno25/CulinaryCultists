package net.yupno.culinarycultists.event;


import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.yupno.culinarycultists.CulinaryCultists;
import net.yupno.culinarycultists.entity.ModEntityTypes;
import net.yupno.culinarycultists.entity.custom.SeethEntity;
import net.yupno.culinarycultists.item.ModItems;
import net.yupno.culinarycultists.sacrificepoints.PlayerSacrificePoints;
import net.yupno.culinarycultists.sacrificepoints.PlayerSacrificePointsProvider;
import net.yupno.culinarycultists.villager.ModVillagers;

import java.util.List;


public class ModEvents {
    @Mod.EventBusSubscriber(modid = CulinaryCultists.MOD_ID)
    public static class ForgeEvents{
        @SubscribeEvent
        public static void  addCustomTrades(VillagerTradesEvent event){
            if(event.getType() == ModVillagers.CULTIST_VILLAGER.get()){
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.ZIRCON.get(), 5);
                int villagerLevel = 1;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 5),
                        stack, 10, 8, 0.02F));
            }
        }

        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            // Makes sure entity is a player and that player doesn't have capability yet, then adds it
            if(event.getObject() instanceof Player) {
                if(!event.getObject().getCapability(PlayerSacrificePointsProvider.PLAYER_SACRIFICE_POINTS).isPresent()) {
                    event.addCapability(new ResourceLocation(CulinaryCultists.MOD_ID, "properties"), new PlayerSacrificePointsProvider());
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            // Makes sure the data doesn't get lost on death
            if(event.isWasDeath()) {
                event.getOriginal().reviveCaps();
                event.getOriginal().getCapability(PlayerSacrificePointsProvider.PLAYER_SACRIFICE_POINTS).ifPresent(oldStore -> {
                    event.getEntity().getCapability(PlayerSacrificePointsProvider.PLAYER_SACRIFICE_POINTS).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
                event.getOriginal().invalidateCaps();
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(PlayerSacrificePoints.class);
        }
    }

    @Mod.EventBusSubscriber(modid = CulinaryCultists.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents{
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event){
            event.put(ModEntityTypes.SEETH.get(), SeethEntity.setAttributes());
        }
    }
}
