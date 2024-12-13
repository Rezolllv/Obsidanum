package net.rezolv.obsidanum.item.item_entity.arrows.obsidian_arrow;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.item_entity.arrows.EntityTypeInit;

public class ObsidianArrow extends AbstractArrow {

    public ObsidianArrow(EntityType<? extends ObsidianArrow> p_37411_, Level p_37412_) {
        super(p_37411_, p_37412_);

      }

    public ObsidianArrow(Level p_37419_, LivingEntity p_37420_) {
        super(EntityTypeInit.OBSIDIAN_ARROW.get(), p_37420_, p_37419_);
        this.setBaseDamage(2.0f);
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.21, 0.21, 0.21)); // увеличьте начальную скорость на 12,5%
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide &&!this.inGround) {
            this.level().addParticle(ParticleTypes.FALLING_OBSIDIAN_TEAR, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.2, 1.2, 1.2)); // снижение скорости с течением времени на 12,5%
    }


    public ObsidianArrow(Level p_37414_, double p_37415_, double p_37416_, double p_37417_) {
        super(EntityTypeInit.OBSIDIAN_ARROW.get(), p_37415_, p_37416_, p_37417_, p_37414_);

    }

    public ObsidianArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(EntityTypeInit.OBSIDIAN_ARROW.get(), world);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ItemsObs.OBSIDIAN_ARROW.get());
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity targetEntity = result.getEntity();

        if (targetEntity instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity) targetEntity;

            // Генерируем случайное число от 0 до 100
            int chance = this.random.nextInt(100);

            // 50% шанс повесить слепоту на 8 секунд
            if (chance < 50) {
                livingTarget.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 8 * 20)); // 8 секунд
            }
            // 30% шанс отравления на 5 секунд
            else if (chance < 80) {
                livingTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 5 * 20)); // 5 секунд
            }
            livingTarget.hurt(new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.ARROW)), 7);

        }
    }
}