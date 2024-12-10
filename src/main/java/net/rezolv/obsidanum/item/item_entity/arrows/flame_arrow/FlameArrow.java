package net.rezolv.obsidanum.item.item_entity.arrows.flame_arrow;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import net.rezolv.obsidanum.block.custom.NetherFireBlock;
import net.rezolv.obsidanum.entity.ModItemEntities;
import net.rezolv.obsidanum.entity.projectile_entity.NetherFlameEntityMini;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.item_entity.arrows.EntityTypeInit;
import net.rezolv.obsidanum.particle.ParticlesObs;

public class FlameArrow extends AbstractArrow {

    public FlameArrow(EntityType<? extends FlameArrow> p_37411_, Level p_37412_) {
        super(p_37411_, p_37412_);
    }

    public FlameArrow(Level p_37419_, LivingEntity p_37420_) {
        super(EntityTypeInit.FLAME_ARROW.get(), p_37420_, p_37419_);
    }

    public FlameArrow(Level p_37414_, double p_37415_, double p_37416_, double p_37417_) {
        super(EntityTypeInit.FLAME_ARROW.get(), p_37415_, p_37416_, p_37417_, p_37414_);
    }

    public FlameArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(EntityTypeInit.FLAME_ARROW.get(), world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && !this.inGround) {
            this.level().addParticle(ParticlesObs.NETHER_FLAME_PROJECTILE_PARTICLES.get(), this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ItemsObs.FLAME_BOLT.get());
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

        if (random.nextFloat() < 0.12) { // 7% вероятность
            targetEntity.setSecondsOnFire(8); // Поджигаем на 6 секунд
        } else {
            targetEntity.setSecondsOnFire(4);
        }
        targetEntity.hurt(this.damageSources().magic(), 7.0F);
    }


    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (!this.level().isClientSide) {
            Vec3 hitDirection = new Vec3(0, 1, 0);
            if (hitResult instanceof BlockHitResult blockHit) {
                hitDirection = Vec3.atLowerCornerOf(blockHit.getDirection().getNormal());
            }
            this.discard();
            // Первая группа снарядов (поблизости, по кругу)
            int miniProjectileCount = 4 + this.random.nextInt(8);
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

                miniProjectile.shoot(scatterDirection.x, scatterDirection.y, scatterDirection.z, 0.25f, 0.1f);
                this.level().addFreshEntity(miniProjectile);
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

        }
    }
}
