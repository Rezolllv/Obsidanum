package net.rezolv.obsidanum.item.item_entity.arrows.netherite_bolt;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.item_entity.arrows.EntityTypeInit;

public class NetheriteBolt extends AbstractArrow {

    // Конструктор с типом сущности и уровнем
    public NetheriteBolt(EntityType<? extends NetheriteBolt> entityType, Level level) {
        super(entityType, level);
    }

    // Конструктор для выстрела из живого существа
    public NetheriteBolt(Level level, LivingEntity shooter) {
        super(EntityTypeInit.NETHERITE_BOLT.get(), shooter, level);
    }

    // Конструктор для выстрела из позиции (x,y,z)
    public NetheriteBolt(Level level, double x, double y, double z) {
        super(EntityTypeInit.NETHERITE_BOLT.get(), x, y, z, level);
    }

    // Конструктор для создания из сетевого пакета
    public NetheriteBolt(PlayMessages.SpawnEntity spawnPacket, Level world) {
        this(EntityTypeInit.NETHERITE_BOLT.get(), world);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ItemsObs.NETHERITE_BOLT.get());
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity target = hitResult.getEntity();
        target.hurt(this.damageSources().arrow(this, this), 7.0F); // Наносим 7 единиц урона
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (!this.level().isClientSide) {
            // Отправляем событие для визуальных эффектов (например, частиц)
            this.level().broadcastEntityEvent(this, (byte) 3);
        }
    }
}