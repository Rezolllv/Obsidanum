package net.rezolv.obsidanum.item.item_entity.pot_grenade;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.entity.ModEntitiesItem;
import net.rezolv.obsidanum.item.item_entity.pot_grenade.fog.PotGrenadeFog;
import net.rezolv.obsidanum.particle.ParticlesObs;

import java.util.List;

public class PotGrenade extends ThrowableItemProjectile {

    // Конструкторы
    public PotGrenade(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public PotGrenade(Level level, LivingEntity shooter) {
        super(ModEntitiesItem.POT_GRENADE.get(), shooter, level);
    }

    public PotGrenade(Level level, double x, double y, double z) {
        super(ModEntitiesItem.POT_GRENADE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemsObs.POT_GRENADE.get();
    }

    private ParticleOptions createParticle() {
        ItemStack itemStack = this.getItemRaw();
        return itemStack.isEmpty()
                ? ParticleTypes.ITEM_SNOWBALL
                : new ItemParticleOption(ParticleTypes.ITEM, itemStack);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);

            // Создаем облако эффектов с более заметными параметрами
            PotGrenadeFog cloud = new PotGrenadeFog(this.level(), this.getX(), this.getY(), this.getZ());
            cloud.setRadius(3.0F); // Уменьшим радиус для более концентрированного эффекта
            cloud.setRadiusOnUse(-0.1F); // Медленнее уменьшается
            cloud.setWaitTime(0); // Начинаем действовать сразу
            cloud.setDuration(600); // 30 секунд (20 тиков = 1 секунда)
            cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());

            // Делаем облако более видимым
            cloud.setParticle(ParticlesObs.GLINT_PURPLE_PARTICLES.get()); // Частицы как у зелья
            cloud.setFixedColor(0x8a2be2); // Красный цвет (можно изменить)

            // Добавляем звук при создании облака
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1.0F, 0.8F);
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.EXPLOSION,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        20,  // Количество частиц
                        0.5,  // Разброс по X
                        0.5,  // Разброс по Y
                        0.5,  // Разброс по Z
                        0.1   // Скорость разлета
                );
            }

            this.level().addFreshEntity(cloud);
            this.discard();
        }
    }
}
