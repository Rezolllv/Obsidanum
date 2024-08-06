package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ObsidianShardKey extends Item {
    public ObsidianShardKey(Properties pProperties) {
        super(pProperties);
    }
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
            list.add(Component.translatable("item.obsidan.description.obsidian_shard_key").withStyle(ChatFormatting.GRAY));
    }

}
