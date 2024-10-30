package net.rezolv.obsidanum.event;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "obsidanum")
public class NetEventBus {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent pEvent) {
        ItemStack stack = pEvent.getItemStack();
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            if (tag.contains("RecipePath")) {
                pEvent.getToolTip().add(Component.literal(stack.getTag().getString("RecipePath")).withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }
}
