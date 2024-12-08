package net.rezolv.obsidanum.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.item.custom.FlameCrossbowItem;

public class ModItemProperties {

    public static void register() {
        // And finally, register the model predicates for the crossbow...




        //Copied from vanilla to mimic normal crossbow
        ItemProperties.register(ItemsObs.FLAME_CROSSBOW.get(), new ResourceLocation(Obsidanum.MOD_ID, "pull"), (p_239427_0_, p_239427_1_, p_239427_2_, intIn) -> {
            if (p_239427_2_ == null) {
                return 0.0F;
            } else {
                return FlameCrossbowItem.isCharged(p_239427_0_) ? 0.0F : (float)(p_239427_0_.getUseDuration() - p_239427_2_.getUseItemRemainingTicks()) / (float)FlameCrossbowItem.getChargeDuration(p_239427_0_);
            }
        });
        ItemProperties.register(ItemsObs.FLAME_CROSSBOW.get(), new ResourceLocation(Obsidanum.MOD_ID, "pulling"), (p_239426_0_, p_239426_1_, p_239426_2_, intIn) -> {
            return p_239426_2_ != null && p_239426_2_.isUsingItem() && p_239426_2_.getUseItem() == p_239426_0_ && !FlameCrossbowItem.isCharged(p_239426_0_) ? 1.0F : 0.0F;
        });
        ItemProperties.register(ItemsObs.FLAME_CROSSBOW.get(), new ResourceLocation(Obsidanum.MOD_ID, "charged"), (p_239425_0_, p_239425_1_, p_239425_2_, intIn) -> {
            return p_239425_2_ != null && FlameCrossbowItem.isCharged(p_239425_0_) ? 1.0F : 0.0F;
        });
        ItemProperties.register(ItemsObs.FLAME_CROSSBOW.get(), new ResourceLocation(Obsidanum.MOD_ID, "firework"), (p_239424_0_, p_239424_1_, p_239424_2_, intIn) -> {
            return p_239424_2_ != null && FlameCrossbowItem.isCharged(p_239424_0_) && FlameCrossbowItem.containsChargedProjectile(p_239424_0_, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });
    }
}
