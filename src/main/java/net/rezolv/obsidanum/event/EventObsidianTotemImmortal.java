package net.rezolv.obsidanum.event;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.item.ItemsObs;

import javax.annotation.Nullable;
import java.util.List;
import java.util.logging.Level;

@Mod.EventBusSubscriber
public class EventObsidianTotemImmortal {
    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            if (player.getHealth() - event.getAmount() <= 0) {
                // Отменяем урон, чтобы игрок не умер
                event.setCanceled(true);

                // Проверяем наличие обсиданового тотема
                ItemStack totem = getObsidianTotem(player);
                if (!totem.isEmpty()) {
                    // Восстанавливаем здоровье игрока до 50%
                    revivePlayer(player);
                    // Воспроизводим анимацию тотема
                    Obsidanum.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new TotemAnimationMessage());
                    playSound(player.level(), player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_BLOCK);
                    // Применяем эффекты тотема
                    applyTotemEffects(player);
                    // Удаляем один тотем из инвентаря
                    totem.shrink(1);
                    // Отталкиваем и наносим урон ближайшим сущностям
                    pushAndDamageNearbyEntities(player);
                }
            }
        }
    }
    public static void playSound(LevelAccessor world, double x, double y, double z, SoundEvent sound) {
        world.playSound(null, BlockPos.containing(x, y, z), sound, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }



    private static ItemStack getObsidianTotem(Player player) {
        // Проверяем наличие обсиданового тотема в руках или инвентаре
        if (player.getOffhandItem().getItem() == ItemsObs.OBSIDIAN_TOTEM_OF_IMMORTALITY.get()) {
            return player.getOffhandItem();
        } else if (player.getMainHandItem().getItem() == ItemsObs.OBSIDIAN_TOTEM_OF_IMMORTALITY.get()) {
            return player.getMainHandItem();
        } else {
            return ItemStack.EMPTY;
        }
    }


    private static void revivePlayer(Player player) {
        player.setHealth(player.getMaxHealth() * 0.5F); // Половина хп
        player.removeAllEffects(); // Удаление всех негативных эффектов
    }

    private static void pushAndDamageNearbyEntities(Player player) {
        // Радиус отталкивания
        double radius = 7.0;

        // Определяем область вокруг игрока в радиусе 7 блоков
        AABB area = new AABB(player.blockPosition()).inflate(radius);

        // Получаем список всех живых сущностей в радиусе
        List<Entity> nearbyEntities = player.level().getEntities(player, area, entity -> entity instanceof LivingEntity);

        // Проходим по всем сущностям и выполняем отталкивание и нанесение урона
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity livingEntity) {
                // Наносим 5 урона (2.5 сердечка)
                livingEntity.hurt(player.damageSources().magic(), 5.0F);

                // Отталкиваем сущность от игрока
                double dx = entity.getX() - player.getX();
                double dz = entity.getZ() - player.getZ();
                double distance = Math.sqrt(dx * dx + dz * dz);
                // Проверяем, если это игрок и он умер от урона
                if (livingEntity instanceof Player victimPlayer && victimPlayer.isDeadOrDying()) {
                    // Создаём кастомное сообщение о смерти
                    Component deathMessage = createDeathMessage(player, victimPlayer);
                    // Отправляем сообщение в чат
                    player.level().getServer().getPlayerList().broadcastSystemMessage(deathMessage, false);
                }
                // Добавляем небольшое значение для предотвращения деления на 0
                if (distance != 0) {
                    // Отталкиваем с коэффициентом силы
                    double strength = 1.5;
                    entity.setDeltaMovement(dx / distance * strength, 0.5, dz / distance * strength);
                }
            }
        }
    }

    // Метод для создания кастомного сообщения о смерти с локализацией
    private static Component createDeathMessage(Player killer, Player victim) {
        // Используем Component.translatable() для поддержки локализации
        return Component.translatable("death.attack.obsidian_totem", victim.getName(), killer.getName());
    }

    private static void applyTotemEffects(Player player) {
        // Наложение сильного замедления и сопротивления
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 35 * 20, 4)); // Замедление
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 35 * 20, 5)); // Сопротивление
        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 3 * 20, 0)); // Тьма
    }
}
