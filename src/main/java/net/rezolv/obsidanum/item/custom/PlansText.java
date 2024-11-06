package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class PlansText extends Item {
    public PlansText(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("RecipesPlans")) {
            MutableComponent recipePath = Component.literal(tag.getString("RecipesPlans")).withStyle(ChatFormatting.AQUA);
            tooltip.add(recipePath);
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
