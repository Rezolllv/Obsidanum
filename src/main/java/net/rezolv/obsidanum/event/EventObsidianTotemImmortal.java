package net.rezolv.obsidanum.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.network.PacketDistributor;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.damage_source.ObsidianTotemDamageSource;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.sound.SoundsObs;

import java.util.List;

@Mod.EventBusSubscriber
public class EventObsidianTotemImmortal {
    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            if (player.getHealth() - event.getAmount() <= 0) {
                // Отменяем урон, чтобы игрок не умер

                // Проверяем наличие обсиданового тотема
                ItemStack totem = getObsidianTotem(player);
                if (!totem.isEmpty()) {
                    // Восстанавливаем здоровье игрока до 50%
                    event.setCanceled(true);

                    revivePlayer(player);
                    // Воспроизводим анимацию тотема
                    Obsidanum.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new TotemAnimationMessage());
                    playSound(player.level(), player.getX(), player.getY(), player.getZ(), SoundsObs.OBSIDIAN_TOTEM.get());
                    // Применяем эффекты тотема
                    applyTotemEffects(player);
                    // Удаляем один тотем из инвентаря
                    totem.shrink(1);
                    // Отталкиваем и наносим урон ближайшим сущностям
                    pushAndDamageNearbyEntities(player);
                    spawnPortalParticles(player.level(), player.getX(), player.getY(), player.getZ());

                }
            }
        }
    }
    public static void spawnPortalParticles(LevelAccessor world, double x, double y, double z) {
        if (world instanceof ServerLevel level)
            level.sendParticles(ParticleTypes.PORTAL, x, y, z, 80, 2, 2, 2, 0.2);
    }

    public static void playSound(LevelAccessor world, double x, double y, double z, SoundEvent sound) {
        world.playSound(null, BlockPos.containing(x, y, z), sound, SoundSource.NEUTRAL, 1.0F, 2.0F);
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
                // Получаем Holder<DamageType> для вашего кастомного урона
                Holder<DamageType> damageTypeHolder = player.level().registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("obsidanum:obsidian_totem")));

                // Передаём Holder<DamageType> в конструктор
                livingEntity.hurt(new ObsidianTotemDamageSource(damageTypeHolder, player), 10);


                // Отталкиваем сущность от игрока
                double dx = entity.getX() - player.getX();
                double dz = entity.getZ() - player.getZ();
                double distance = Math.sqrt(dx * dx + dz * dz);
                // Добавляем небольшое значение для предотвращения деления на 0
                if (distance != 0) {
                    // Отталкиваем с коэффициентом силы
                    double strength = 7;
                    entity.setDeltaMovement(dx / distance * strength, 0.5, dz / distance * strength);
                }
            }
        }
    }



    private static void applyTotemEffects(Player player) {
        // Наложение сильного замедления и сопротивления
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10 * 20, 3)); // Замедление
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 35 * 20, 3)); // Сопротивление
    }
}
