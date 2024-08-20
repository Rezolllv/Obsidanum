package net.rezolv.obsidanum.trades;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.item.ItemsObs;

import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TradesBlacksmithObsidianum {
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.TOOLSMITH) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3+pRandom.nextInt(4)),
                    new ItemStack(ItemsObs.CRUCIBLE.get()),
                    3, 1, 0.00f));
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3+pRandom.nextInt(4)),
                    new ItemStack(ItemsObs.CRUCIBLE.get()),
                    3, 1, 0.00f));
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3+pRandom.nextInt(4)),
                    new ItemStack(ItemsObs.CRUCIBLE.get()),
                    3, 1, 0.00f));
        }
    }
}