package net.rezolv.obsidanum.item.item_entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.entity.ModEntities;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class ObsidianChakramEntity extends ThrowableItemProjectile {
    private float rotationSpeed; // Поле для скорости вращения

    public ObsidianChakramEntity(EntityType<? extends ObsidianChakramEntity> type, Level world) {
        super(type, world);
        this.rotationSpeed = 20.0F; // Инициализация скорости вращения
        this.setYRot(this.getYRot() + 90); // Поворот на 90 градусов
    }

    public ObsidianChakramEntity(Level world, double x, double y, double z) {
        super(ModEntities.OBSIDIAN_CHAKRAM.get(), x, y, z, world);
        this.rotationSpeed = 20.0F; // Инициализация скорости вращения
        this.setYRot(this.getYRot() + 90); // Поворот на 90 градусов
    }

    public ObsidianChakramEntity(Level world, LivingEntity owner) {
        super(ModEntities.OBSIDIAN_CHAKRAM.get(), owner, world);
        this.rotationSpeed = 20.0F; // Инициализация скорости вращения
        this.setYRot(owner.getYRot() + 90); // Поворот на 90 градусов
    }

    @Override
    protected Item getDefaultItem() {
        return ItemsObs.OBSIDIAN_CHAKRAM.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        Entity owner = this.getOwner();
        if (target != null && owner != null) {
            target.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK)), 7);
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        // Вращение сущности
        if (this.isInWater()) {
            this.rotationSpeed = 5.0F; // Замедление вращения в воде
        } else {
            this.rotationSpeed = 20.0F; // Восстановление обычной скорости вращения
        }
        this.setYRot(this.getYRot() + this.rotationSpeed); // Вращение на заданную скорость за тик
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }
}
