package net.rezolv.obsidanum.item.item_entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.rezolv.obsidanum.item.ItemsObs;

public class ObsidianChakramEntity extends ThrowableItemProjectile {
    public ObsidianChakramEntity(EntityType<? extends ObsidianChakramEntity> type, Level world) {
        super(type, world);
    }

    public ObsidianChakramEntity(Level world, double x, double y, double z) {
        super(EntityType.SNOWBALL, x, y, z, world);
    }

    public ObsidianChakramEntity(Level world, LivingEntity owner) {
        super(EntityType.SNOWBALL, owner, world);
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
        }
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
