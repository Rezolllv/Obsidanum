package net.rezolv.obsidanum.entity.projectile_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages;
import net.rezolv.obsidanum.block.custom.NetherFireBlock;
import net.rezolv.obsidanum.entity.ModItemEntities;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.particle.ParticlesObs;

public class NetherFlameEntityMini extends ThrowableItemProjectile {
    public NetherFlameEntityMini(EntityType<? extends NetherFlameEntityMini> entityType, Level level) {
        super(entityType, level);
    }

    public NetherFlameEntityMini(PlayMessages.SpawnEntity packet, Level world) {
        super(ModItemEntities.NETHER_FLAME_ENTITY.get(), world);
    }


    @Override
    public void tick() {
        super.tick();

        // Проверяем, что мы на стороне клиента, чтобы создать частицы
        if (this.level().isClientSide) {
            for (int i = 0; i < 3; i++) { // Два партикла за тик для более насыщенного эффекта
                double offsetX = this.random.nextGaussian() * 0.02; // Небольшое случайное смещение по X
                double offsetY = this.random.nextGaussian() * 0.2; // Небольшое случайное смещение по Y
                double offsetZ = this.random.nextGaussian() * 0.02; // Небольшое случайное смещение по Z
                this.level().addParticle(ParticleTypes.SMOKE,
                        this.getX() + offsetX, // Текущее положение снаряда
                        this.getY() + offsetY,
                        this.getZ() + offsetZ,
                        0, 0, 0); // Скорость партикла (0 = партикл "стоит на месте")

            }
            for (int i = 0; i < 1; i++) { // Два партикла за тик для более насыщенного эффекта
                double offsetX = this.random.nextGaussian() * 0.02; // Небольшое случайное смещение по X
                double offsetY = this.random.nextGaussian() * 0.2; // Небольшое случайное смещение по Y
                double offsetZ = this.random.nextGaussian() * 0.02; // Небольшое случайное смещение по Z
                this.level().addParticle(ParticlesObs.NETHER_FLAME_PROJECTILE_PARTICLES.get(),
                        this.getX() + offsetX, // Текущее положение снаряда
                        this.getY() + offsetY,
                        this.getZ() + offsetZ,
                        0, 0, 0); // Скорость партикла (0 = партикл "стоит на месте")
            }
        }
    }

    protected Item getDefaultItem() {
        return ItemsObs.NETHER_FLAME_ENTITY_MINI.get();
    }

    public boolean isOnFire() {
        return false;
    }


    protected void onHitBlock(BlockHitResult p_37384_) {
        super.onHitBlock(p_37384_);
        if (!this.level().isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), entity)) {
                BlockPos blockpos = p_37384_.getBlockPos().relative(p_37384_.getDirection());
                if (this.level().isEmptyBlock(blockpos)) {
                    this.level().setBlockAndUpdate(blockpos, NetherFireBlock.getState(this.level(), blockpos));
                }
            }

        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity targetEntity = hitResult.getEntity();
        targetEntity.hurt(this.damageSources().magic(), 8.0F);
        if (random.nextFloat() < 0.12) { // 7% вероятность
            targetEntity.setSecondsOnFire(4); // Поджигаем на 6 секунд
        } else {
            targetEntity.setSecondsOnFire(2);
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }
}