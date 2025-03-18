package net.rezolv.obsidanum.item.item_entity.obsidan_chakram;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.entity.ModEntitiesItem;
import net.rezolv.obsidanum.sound.SoundsObs;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class ObsidianChakramEntity extends ThrowableItemProjectile {

    public static final EntityDataAccessor<Float> STOPPED_YAW = SynchedEntityData.defineId(ObsidianChakramEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> STOPPED_PITCH = SynchedEntityData.defineId(ObsidianChakramEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(ObsidianChakramEntity.class, EntityDataSerializers.BYTE);

    private boolean stopped = false;
    private boolean inGround = false;
    private ItemStack tridentItem;
    private BlockState lastState;
    public int shakeTime;
    public AbstractArrow.Pickup pickup = AbstractArrow.Pickup.ALLOWED;

    public ObsidianChakramEntity(EntityType<? extends ObsidianChakramEntity> type, Level world) {
        super(type, world);
        this.tridentItem = new ItemStack(ItemsObs.OBSIDIAN_CHAKRAM.get());
    }

    public ObsidianChakramEntity(Level world, double x, double y, double z) {
        super(ModEntitiesItem.OBSIDIAN_CHAKRAM.get(), x, y, z, world);
        this.tridentItem = new ItemStack(ItemsObs.OBSIDIAN_CHAKRAM.get());
    }

    public ObsidianChakramEntity(Level world, LivingEntity owner) {
        super(ModEntitiesItem.OBSIDIAN_CHAKRAM.get(), owner, world);
        this.tridentItem = new ItemStack(ItemsObs.OBSIDIAN_CHAKRAM.get());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STOPPED_YAW, 0.0F);
        this.entityData.define(STOPPED_PITCH, 0.0F);
        this.entityData.define(ID_FLAGS, (byte) 0);
    }
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);

        // Обновляем углы при запуске
        Vec3 motion = new Vec3(x, y, z).normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(motion.x, motion.z));
        float pitch = (float) Math.toDegrees(Math.asin(motion.y));

        // Корректируем ориентацию для вертикального броска
        if (Math.abs(motion.y) > 0.9) { // Если игрок смотрит почти вертикально
            yaw += 90.0F; // Поворачиваем чакрам на 90 градусов, чтобы он был ребром
        }

        this.setYRot(yaw);
        this.setXRot(pitch);
        this.yRotO = yaw;
        this.xRotO = pitch;

        // Сохраняем ориентацию в синхронизированных данных
        this.getEntityData().set(STOPPED_YAW, yaw);
        this.getEntityData().set(STOPPED_PITCH, pitch);
    }
    @Override
    protected Item getDefaultItem() {
        return ItemsObs.OBSIDIAN_CHAKRAM.get();
    }

    protected ItemStack getPickupItem() {
        return this.tridentItem.copy();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        Entity owner = this.getOwner();
        if (target != null && owner != null) {
            target.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK)), 10);
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.BLOCK) {
            onHitBlock((BlockHitResult) result);
        } else if (result.getType() == HitResult.Type.ENTITY) {
            onHitEntity((EntityHitResult) result);
        }
    }


    protected void onHitBlock(BlockHitResult pResult) {
        if (this.inGround) return;

        // Смещаем позицию внутрь блока на 2 пикселя
        Direction hitDirection = pResult.getDirection();
        Vec3 newPos = pResult.getLocation().add(
                hitDirection.getNormal().getX() * 0.125,
                hitDirection.getNormal().getY() * 0.125,
                hitDirection.getNormal().getZ() * 0.125
        );
        this.setPos(newPos.x, newPos.y, newPos.z);

        // Сохраняем вращение при ударе
        Vec3 motionBeforeHit = this.getDeltaMovement();
        float horizontalSpeed = (float) Math.sqrt(motionBeforeHit.x * motionBeforeHit.x + motionBeforeHit.z * motionBeforeHit.z);
        float yaw = (horizontalSpeed > 0.001F)
                ? (float) (Math.atan2(motionBeforeHit.x, motionBeforeHit.z) * (180 / Math.PI))
                : this.getYRot();
        float pitch = (horizontalSpeed > 0.001F)
                ? (float) (Math.atan2(motionBeforeHit.y, horizontalSpeed) * (180 / Math.PI))
                : this.getXRot();

        this.getEntityData().set(STOPPED_YAW, yaw);
        this.getEntityData().set(STOPPED_PITCH, pitch);
        this.lastState = this.level().getBlockState(pResult.getBlockPos());

        // Фиксируем чакрам в блоке
        this.setDeltaMovement(Vec3.ZERO);
        this.inGround = true;
        this.stopped = true;
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundsObs.CHAKRAM_HIT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
    }


    public float getStoppedYaw() {
        return this.entityData.get(STOPPED_YAW);
    }

    public float getStoppedPitch() {
        return this.entityData.get(STOPPED_PITCH);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.stopped) {
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            // Проверка на слишком резкое замедление
            if (this.getDeltaMovement().lengthSqr() < 0.01 && !this.inGround) {
                this.setDeltaMovement(this.getDeltaMovement().scale(1.05)); // Ускоряем, если слишком замедлился
            }
        }
    }
    public boolean isStopped() {
        return stopped;
    }

    public void playerTouch(Player pEntity) {
        if (!this.level().isClientSide && (this.inGround) && this.shakeTime <= 0 && this.tryPickup(pEntity)) {
            pEntity.take(this, 1);
            this.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
            this.discard();
        }
    }

    protected boolean tryPickup(Player pPlayer) {
        switch (this.pickup) {
            case ALLOWED:
                return pPlayer.getInventory().add(this.getPickupItem());
            case CREATIVE_ONLY:
                return pPlayer.getAbilities().instabuild;
            default:
                return false;
        }
    }

    public void dropAsItem() {
        if (!this.level().isClientSide) {
            this.spawnAtLocation(this.getPickupItem(), 0.1F);
            this.discard();
        }
    }

}