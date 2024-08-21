package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.rezolv.obsidanum.effect.EffectsObs;
import net.rezolv.obsidanum.item.ItemsObs;

import java.util.List;

public class ObsidianShardInviolability extends Item {
    public ObsidianShardInviolability(Properties pProperties) {
        super(pProperties);
    }
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
            list.add(Component.translatable("item.obsidan.description.obsidian_shard_inviolability").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        // Получаем предмет в руке
        ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);

        // Проверяем, если предмет в руке - это OBSIDIAN_SHARD_INVIOLABILITY
        if (itemInHand.getItem() == ItemsObs.OBSIDIAN_SHARD_INVIOLABILITY.get()) {
            // Проверка, если не в мире
            if (!pLevel.isClientSide) {
                MobEffectInstance existingEffect = pPlayer.getEffect(EffectsObs.INVIOLABILITY.get());
                if (existingEffect != null) {
                    // Если эффект уже активен, создаем новый эффект с обновленным временем
                    int newDuration = existingEffect.getDuration() + 1200; // Увеличиваем на 1200 тиков
                    pPlayer.addEffect(new MobEffectInstance(EffectsObs.INVIOLABILITY.get(), newDuration, 0,false,false,true)); // Устанавливаем обновленное время
                } else {
                    // Если эффекта нет, накладываем новый с длительностью 1200 тиков
                    pPlayer.addEffect(new MobEffectInstance(EffectsObs.INVIOLABILITY.get(), 1200, 0,false,false,true)); // Длительность 1200 тиков, уровень 0
                }

                // Убираем 1 предмет из инвентаря
                itemInHand.shrink(1);
            }

            return InteractionResultHolder.sidedSuccess(itemInHand, pLevel.isClientSide);
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
