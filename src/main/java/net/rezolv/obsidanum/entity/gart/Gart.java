package net.rezolv.obsidanum.entity.gart;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.rezolv.obsidanum.effect.EffectsObs;

public class Gart extends Monster {
    public Gart(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            setupAnimationStates();
        } else {
            checkPlayerLook();
        }
    }

    private void checkPlayerLook() {
        if (this.level() != null) {
            for (Player player : this.level().players()) {
                if (player.hasLineOfSight(this)) {
                    Vec3 playerLook = player.getLookAngle();
                    Vec3 toEntity = this.position().subtract(player.position()).normalize();
                    double angle = playerLook.dot(toEntity) / (playerLook.length() * toEntity.length());

                    // Преобразуйте угол в радианы
                    double radianAngle = Math.acos(Math.min(Math.max(angle, -1.0), 1.0)); // Убедитесь, что угол в диапазоне [-1, 1]
                    double degreeAngle = Math.toDegrees(radianAngle);

                    // Проверяем, чтобы угол был в пределах 30 градусов (можете изменить порог)
                    if (degreeAngle < 20.0) {
                        this.despawn();
                        this.dealFlashToPlayer();
                        break; // Выход из цикла после действия
                    }
                }
            }
        }
    }


    private void despawn() {
        this.remove(RemovalReason.DISCARDED);
    }

    private void dealFlashToPlayer() {
        this.level().players().forEach(player -> {
            if (player.hasLineOfSight(this)) {
                player.addEffect(new MobEffectInstance(EffectsObs.FLASH.get(), 45)); // 60 тиков = 3 секунды
            }
        });
    }
    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.4D)
                .add(Attributes.FOLLOW_RANGE, 12)
                .add(Attributes.ATTACK_DAMAGE, 8)
                .add(Attributes.ATTACK_KNOCKBACK, 5)
                .add(Attributes.ATTACK_SPEED, 4);
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.2D)); // Случайное передвижение
        this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    private boolean isMoving() {
        return this.getDeltaMovement().lengthSqr() > 0.01; // Arbitrary threshold for movement
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        // Update walk animation state based on movement
        if (this.isMoving()) {
            this.walkAnimationState.start(this.tickCount);
        } else {
            this.walkAnimationState.stop();
        }
    }
}
