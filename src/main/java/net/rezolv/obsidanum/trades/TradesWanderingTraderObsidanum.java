package net.rezolv.obsidanum.trades;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.item.ItemsObs;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TradesWanderingTraderObsidanum {
    @SubscribeEvent
    public static void registerWanderingTrades(WandererTradesEvent event) {
        event.getGenericTrades()
                .add(new BasicItemListing(new ItemStack(Items.EMERALD, 25),
                        new ItemStack(ItemsObs.DRILLING_CRYSTALLIZER.get(),1), 8, 20, 0f));
        event.getGenericTrades()
                .add(new BasicItemListing(new ItemStack(Items.EMERALD, 20),
                        new ItemStack(ItemsObs.DRILLING_CRYSTALLIZER.get(),1), 8, 20, 0f));
        event.getGenericTrades()
                .add(new BasicItemListing(new ItemStack(Items.EMERALD, 30),
                        new ItemStack(ItemsObs.DRILLING_CRYSTALLIZER.get(),1), 8, 20, 0f));
        event.getGenericTrades()
                .add(new BasicItemListing(new ItemStack(Items.EMERALD, 35),
                        new ItemStack(ItemsObs.DRILLING_CRYSTALLIZER.get(),1), 8, 20, 0f));
        event.getGenericTrades()
                .add(new BasicItemListing(new ItemStack(Items.EMERALD, 25),
                        new ItemStack(ItemsObs.DRILLING_CRYSTALLIZER.get(),1), 8, 20, 0f));
        event.getGenericTrades()
                .add(new BasicItemListing(new ItemStack(Items.EMERALD, 20),
                        new ItemStack(ItemsObs.DRILLING_CRYSTALLIZER.get(),1), 8, 20, 0f));
        event.getGenericTrades()
                .add(new BasicItemListing(new ItemStack(Items.EMERALD, 30),
                        new ItemStack(ItemsObs.DRILLING_CRYSTALLIZER.get(),1), 8, 20, 0f));
        event.getGenericTrades()
                .add(new BasicItemListing(new ItemStack(Items.EMERALD, 35),
                        new ItemStack(ItemsObs.DRILLING_CRYSTALLIZER.get(),1), 8, 20, 0f));
        event.getGenericTrades()
                .add(new BasicItemListing(new ItemStack(Items.EMERALD, 30),
                        new ItemStack(ItemsObs.DRILLING_CRYSTALLIZER.get(),1), 8, 20, 0f));
        event.getGenericTrades()
                .add(new BasicItemListing(new ItemStack(Items.EMERALD, 35),
                        new ItemStack(ItemsObs.DRILLING_CRYSTALLIZER.get(),1), 8, 20, 0f));
    }
}
