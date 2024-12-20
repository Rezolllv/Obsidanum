package net.rezolv.obsidanum.entity.mutated_gart;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Pose;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.rezolv.obsidanum.entity.mutated_gart.ai.MutatedGartAttackGoal;
import net.rezolv.obsidanum.entity.mutated_gart.ai.MutatedGartRangedAttackGoal;

public class MutatedGart extends Monster {
    private final ServerBossEvent BOSS_INFO = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.YELLOW, ServerBossEvent.BossBarOverlay.PROGRESS);
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(MutatedGart.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MAGIC_ARROW_ATTACKING =
            SynchedEntityData.defineId(MutatedGart.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;
    public final AnimationState attackAnimationMagicArrowState = new AnimationState();
    public int attackAnimationMagicArrowTimeout = 0;
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public MutatedGart(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            setupAnimationStates();
        }
    }
    public int getAttackAnimationMagicArrowTimeout() {
        return this.attackAnimationMagicArrowTimeout;
    }
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 13;
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }

        if (!this.isAttacking()) {
            attackAnimationState.stop();
        }

        if (this.isAttackingMagicArrow() && attackAnimationMagicArrowTimeout <= 0) {
            attackAnimationMagicArrowTimeout = 35;
            attackAnimationMagicArrowState.start(this.tickCount);
        } else {
            --this.attackAnimationMagicArrowTimeout;
        }

        if (!this.isAttackingMagicArrow()) {
            attackAnimationMagicArrowState.stop();
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.BOSS_INFO.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.BOSS_INFO.removePlayer(player);
    }
    @Override
    public boolean fireImmune() {
        return true;
    }
    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.BOSS_INFO.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttackingMagicArrow(boolean attacking) {
        this.entityData.set(MAGIC_ARROW_ATTACKING, attacking);
    }

    public boolean isAttackingMagicArrow() {
        return this.entityData.get(MAGIC_ARROW_ATTACKING);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(MAGIC_ARROW_ATTACKING, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MutatedGartAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new MutatedGartRangedAttackGoal(this,7));       // Дальняя атака, если дальше 7 блоков

        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, 10, true, false,
                target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, 10, true, false,
                target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, SnowGolem.class, 10, true, false,
                target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Witch.class, 10, true, false,
                target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ZombifiedPiglin.class, 10, true, false,
                target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

//    protected SoundEvent getAmbientSound() {
//        return SoundsObs.OBSIDIAN_ELEMENTAL_AMBIENT.get();
//    }
//
//    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
//        return SoundsObs.OBSIDIAN_ELEMENTAL_HURT.get();
//    }
//
//    protected SoundEvent getDeathSound() {
//        return SoundsObs.OBSIDIAN_ELEMENTAL_DEATH.get();
//    }


    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.8D)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_DAMAGE, 16)
                .add(Attributes.ATTACK_KNOCKBACK, 5)
                .add(Attributes.ATTACK_SPEED, 4)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
                .add(Attributes.ARMOR, 0.0);
    }
    @Override
    protected void dropExperience() {
        super.dropExperience();
        this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY(), this.getZ(), 40));
    }


    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
    }

}
