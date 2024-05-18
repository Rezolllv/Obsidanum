package net.rezolv.obsidanum.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class RelictAmethyst extends Item {
    public RelictAmethyst(Properties pProperties) {
        super(pProperties);
    }
    private static final int TICK_INTERVAL = 12000; // 10 минут в тиках (20 тиков = 1 секунда)

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);

        if (!pLevel.isClientSide && pEntity instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) pEntity;

            if (pStack.getOrCreateTag().contains("tickCount")) {
                int tickCount = pStack.getTag().getInt("tickCount") + 1;
                if (tickCount >= TICK_INTERVAL) {
                    // Выдаем опыт
                    int experience = 5 + RandomSource.create().nextInt(11);
                    player.giveExperiencePoints(experience);

                    // Воспроизведение звука получения опыта
                    pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);

                    // Сбрасываем счетчик тиков
                    tickCount = 0;
                }
                pStack.getTag().putInt("tickCount", tickCount);
            } else {
                pStack.getOrCreateTag().putInt("tickCount", 1);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player instanceof ServerPlayer) {
            // Получение случайного количества опыта от 400 до 600
            int experience = 400 + RandomSource.create().nextInt(201);
            player.giveExperiencePoints(experience);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);

            // Уменьшение количества предмета
            stack.shrink(1);

            // Если предмет закончился, удаление его из инвентаря
            if (stack.isEmpty()) {
                player.getInventory().removeItem(stack);
            }

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }
}
