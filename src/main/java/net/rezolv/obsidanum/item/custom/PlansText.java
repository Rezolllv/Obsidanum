package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
        if (tag != null) {
            if (tag.contains("RecipesPlans")) {
                MutableComponent recipePath = Component.literal(tag.getString("RecipesPlans")).withStyle(ChatFormatting.AQUA);
                tooltip.add(recipePath);
            }



            if (tag.contains("RecipeResult")) {
                tooltip.add(Component.translatable("tooltip.scrolls.result").withStyle(ChatFormatting.GOLD));

                ListTag resultList = tag.getList("RecipeResult", Tag.TAG_COMPOUND);
                for (int i = 0; i < resultList.size(); i++) {
                    CompoundTag resultTag = resultList.getCompound(i);
                    ItemStack result = ItemStack.of(resultTag);
                    int count = resultTag.getInt("Count");
                    tooltip.add(Component.literal("- " + result.getHoverName().plainCopy().getString() + " x" + count)
                            .withStyle(ChatFormatting.AQUA));
                }
            }
            // Добавляем информацию об ингредиентах
            if (tag.contains("RecipeIngredients")) {
                tooltip.add(Component.translatable("tooltip.scrolls.ingredients").withStyle(ChatFormatting.GOLD));

                ListTag ingredientList = tag.getList("RecipeIngredients", Tag.TAG_COMPOUND);
                for (int i = 0; i < ingredientList.size(); i++) {
                    CompoundTag ingredientTag = ingredientList.getCompound(i);
                    ItemStack ingredient = ItemStack.of(ingredientTag);
                    int count = ingredientTag.getInt("Count");
                    tooltip.add(Component.literal("- " + ingredient.getHoverName().getString() + " x" + count)
                            .withStyle(ChatFormatting.GRAY));
                }
            }
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
