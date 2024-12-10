package net.rezolv.obsidanum.entity.projectile_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import net.rezolv.obsidanum.block.custom.NetherFireBlock;
import net.rezolv.obsidanum.entity.ModItemEntities;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.particle.ParticlesObs;

public class NetherFlameEntity extends ThrowableItemProjectile {
    public NetherFlameEntity(EntityType<? extends NetherFlameEntity> entityType, Level level) {
        super(entityType, level);
    }
    public NetherFlameEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(ModItemEntities.NETHER_FLAME_ENTITY.get(), world);
    }




    protected Item getDefaultItem() {
        return ItemsObs.NETHER_FLAME_ENTITY.get();
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
        targetEntity.hurt(this.damageSources().magic(), 10.0F);
        if (random.nextFloat() < 0.12) { // 7% вероятность
            targetEntity.setSecondsOnFire(8); // Поджигаем на 6 секунд
        } else {
            targetEntity.setSecondsOnFire(4);
        }
    }



    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            Vec3 hitDirection = new Vec3(0, 1, 0);
            if (hitResult instanceof BlockHitResult blockHit) {
                hitDirection = Vec3.atLowerCornerOf(blockHit.getDirection().getNormal());
            }

            // Первая группа снарядов (поблизости, по кругу)
            int miniProjectileCount = 10 + this.random.nextInt(25);
            for (int i = 0; i < miniProjectileCount; i++) {
                NetherFlameEntityMini miniProjectile = new NetherFlameEntityMini(ModItemEntities.NETHER_FLAME_ENTITY_MINI.get(), this.level());
                miniProjectile.setOwner(this.getOwner());
                miniProjectile.setPos(this.getX(), this.getY(), this.getZ());

                double angle = 2 * Math.PI * i / miniProjectileCount;
                double offsetX = Math.cos(angle) * 0.5;
                double offsetZ = Math.sin(angle) * 0.5;
                double offsetY = 0.2 + this.random.nextDouble() * 0.3;

                Vec3 scatterDirection = new Vec3(offsetX, offsetY, offsetZ).add(hitDirection);
                scatterDirection = scatterDirection.normalize().scale(0.6 + this.random.nextDouble() * 0.4);

                miniProjectile.shoot(scatterDirection.x, scatterDirection.y, scatterDirection.z, 0.45f, 0.1f);
                this.level().addFreshEntity(miniProjectile);
            }

            // Вторая группа снарядов (дальше, тоже по кругу)
            int outerProjectileCount = 30 + this.random.nextInt(40);
            for (int i = 0; i < outerProjectileCount; i++) {
                NetherFlameEntityMini outerProjectile = new NetherFlameEntityMini(ModItemEntities.NETHER_FLAME_ENTITY_MINI.get(), this.level());
                outerProjectile.setOwner(this.getOwner());
                outerProjectile.setPos(this.getX(), this.getY(), this.getZ());

                double angle = 2 * Math.PI * i / outerProjectileCount;
                double offsetX = Math.cos(angle) * 1.5; // Увеличиваем радиус
                double offsetZ = Math.sin(angle) * 1.5;
                double offsetY = 0.1 + this.random.nextDouble() * 0.2;

                Vec3 outerDirection = new Vec3(offsetX, offsetY, offsetZ);
                outerDirection = outerDirection.normalize().scale(1.0 + this.random.nextDouble() * 0.5); // Дальше и чуть больше разброс

                outerProjectile.shoot(outerDirection.x, outerDirection.y, outerDirection.z, 0.7f, 0.2f); // Немного быстрее
                this.level().addFreshEntity(outerProjectile);
            }

            // Звук и удаление сущности
            this.level().playSound(
                    null,
                    this.getX(), this.getY(), this.getZ(),
                    SoundEvents.LAVA_POP,
                    SoundSource.PLAYERS,
                    1.0f,
                    0.8f + this.random.nextFloat() * 0.4f
            );
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }
}