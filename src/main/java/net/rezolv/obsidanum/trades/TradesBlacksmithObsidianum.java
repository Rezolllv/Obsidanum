package net.rezolv.obsidanum.trades;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.item.ItemsObs;

import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TradesBlacksmithObsidianum {
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.TOOLSMITH) {
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3),
                    new ItemStack(ItemsObs.CRUCIBLE.get()), 15, 5, 0.05f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 5),
                    new ItemStack(ItemsObs.CRUCIBLE.get()), 15, 5, 0.05f));
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 7),
                    new ItemStack(ItemsObs.CRUCIBLE.get()), 15, 5, 0.05f));
        }
    }
}