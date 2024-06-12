package net.rezolv.obsidanum.item.item_entity.obsidan_chakram;

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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.entity.ModEntitiesItem;
import net.rezolv.obsidanum.sound.SoundsObs;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class ObsidianChakramEntity extends ThrowableItemProjectile {

    private boolean stopped = false;
    private boolean inGround = false;
    private ItemStack tridentItem;
    private BlockState lastState;
    private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(ObsidianChakramEntity.class, EntityDataSerializers.BYTE);
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
        super.onHit(result);
        if (result.getType() == HitResult.Type.BLOCK) {
            onHitBlock((BlockHitResult) result);
        } else if (result.getType() == HitResult.Type.ENTITY) {
            onHitEntity((EntityHitResult) result);
        }
    }

    protected void onHitBlock(BlockHitResult pResult) {
        this.lastState = this.level().getBlockState(pResult.getBlockPos());
        super.onHitBlock(pResult);
        Vec3 vec3 = pResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale(0.255000000074505806);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.inGround = true;
        this.setNoGravity(false);
        this.stopped = true;
        // Play arrow hit sound
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundsObs.CHAKRAM_HIT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.stopped) {
            this.setDeltaMovement(0, 0, 0); // Останавливаем движение
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

    private void setFlag(int pId, boolean pValue) {
        byte b0 = (Byte) this.entityData.get(ID_FLAGS);
        if (pValue) {
            this.entityData.set(ID_FLAGS, (byte) (b0 | pId));
        } else {
            this.entityData.set(ID_FLAGS, (byte) (b0 & ~pId));
        }
    }
}