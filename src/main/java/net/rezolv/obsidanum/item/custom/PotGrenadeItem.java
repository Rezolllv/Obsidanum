package net.rezolv.obsidanum.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.rezolv.obsidanum.item.item_entity.pot_grenade.PotGrenade;

public class PotGrenadeItem extends Item {
    public PotGrenadeItem(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        // Получаем предмет в руке игрока
        ItemStack itemStack = player.getItemInHand(hand);

        // Проигрываем звук броска снежка
        world.playSound(
                null,                    // Источник звука (null - глобальный звук)
                player.getX(),           // X координата
                player.getY(),          // Y координата
                player.getZ(),          // Z координата
                SoundEvents.SNOWBALL_THROW,  // Звук броска снежка
                SoundSource.NEUTRAL,    // Категория звука
                0.5F,                   // Громкость
                0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)  // Высота тона (рандомизированная)
        );

        // Выполняем только на серверной стороне
        if (!world.isClientSide) {
            // Создаем сущность снежка
            PotGrenade potGrenade = new PotGrenade(world, player);
            potGrenade.setItem(itemStack);  // Устанавливаем предмет для снежка
            potGrenade.shootFromRotation(
                    player,                   // Игрок, который бросает
                    player.getXRot(),         // Угол наклона по X
                    player.getYRot(),         // Угол поворота по Y
                    0.0F,                    // Наклон
                    1.5F,                    // Скорость
                    1.0F                     // Разброс
            );
            world.addFreshEntity(potGrenade);  // Добавляем снежок в мир
        }

        // Увеличиваем статистику использования предмета
        player.awardStat(Stats.ITEM_USED.get(this));

        // Уменьшаем количество предметов, если игрок не в творческом режиме
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        // Возвращаем результат с оставшимся предметом
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }
}
