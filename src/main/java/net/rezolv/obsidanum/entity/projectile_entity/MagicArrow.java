package net.rezolv.obsidanum.entity.projectile_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import net.rezolv.obsidanum.block.custom.NetherFireBlock;
import net.rezolv.obsidanum.entity.ModItemEntities;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.particle.ParticlesObs;

import java.util.List;

public class MagicArrow extends ThrowableItemProjectile {

    private LivingEntity target; // Цель для самонаведения
    private static final double HOMING_RADIUS = 40.0; // Радиус поиска цели
    public MagicArrow(EntityType<? extends MagicArrow> entityType, Level level) {
        super(entityType, level);
    }
    public MagicArrow(PlayMessages.SpawnEntity packet, Level world) {
        super(ModItemEntities.NETHER_FLAME_ENTITY.get(), world);

    }
    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }
    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity targetEntity = hitResult.getEntity();
        targetEntity.hurt(this.damageSources().magic(), 1.0F);

    }
    protected Item getDefaultItem() {
        return ItemsObs.MAGIC_ARROW.get();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return; // Логика работает только на стороне сервера
        }

        // Ищем цель, если она не установлена
        if (target == null || !target.isAlive()) {
            findTarget();
        }

        // Если есть цель, корректируем направление
        if (target != null) {
            Vec3 currentPosition = this.position();
            Vec3 targetPosition = target.position();
            Vec3 direction = targetPosition.subtract(currentPosition).normalize();

            // Устанавливаем скорость для корректировки направления
            this.setDeltaMovement(direction.scale(0.3)); // Скорость движения
        }
    }

    private void findTarget() {
        List<LivingEntity> potentialTargets = this.level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(HOMING_RADIUS), // Радиус поиска
                entity -> entity != this.getOwner() // Исключаем владельца стрелы
        );

        if (!potentialTargets.isEmpty()) {
            // Находим ближайшую цель
            potentialTargets.sort((e1, e2) ->
                    Double.compare(e1.distanceToSqr(this), e2.distanceToSqr(this))
            );
            this.target = potentialTargets.get(0);
        }
    }
    protected float getGravity() {
        return 0.00F;
    }

}