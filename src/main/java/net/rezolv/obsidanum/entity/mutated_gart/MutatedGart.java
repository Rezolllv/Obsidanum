package net.rezolv.obsidanum.entity.mutated_gart;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.rezolv.obsidanum.entity.mutated_gart.ai.MutatedGartAttackGoal;
import net.rezolv.obsidanum.entity.mutated_gart.ai.MutatedGartRangedAttackGoal;

public class MutatedGart extends Monster implements RangedAttackMob {
    // Синхронизация данных для атак
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(MutatedGart.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MAGIC_ATTACKING = SynchedEntityData.defineId(MutatedGart.class, EntityDataSerializers.BOOLEAN);

    // Состояния анимаций
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState magicAttackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    // Цель для дальнего боя
    private MutatedGartRangedAttackGoal rangedAttackGoal;

    // Полоса здоровья босса
    private ServerBossEvent bossInfo;

    // Конструктор
    public MutatedGart(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // Реализация дальнего боя
    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        if (!this.level().isClientSide) {
            Snowball snowball = new Snowball(this.level(), this);
            double dX = target.getX() - this.getX();
            double dY = target.getY(0.5) - snowball.getY();
            double dZ = target.getZ() - this.getZ();
            double distance = Math.sqrt(dX * dX + dZ * dZ) * 0.2;
            snowball.shoot(dX, dY + distance, dZ, 1.6F, 12.0F);
            this.level().addFreshEntity(snowball);
            this.setMagicAttacking(true); // Запуск анимации магической атаки
        }
    }

    // Проверка, движется ли моб
    public boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
    }
    public final AnimationState appearanceAnimationState = new AnimationState();

    // Обновление состояния моба
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.tickCount == 1) {
            // Запуск анимации при первом тике на клиенте
            this.appearanceAnimationState.start(this.tickCount);
        }
        if (this.level().isClientSide()) {
            setupAnimationStates(); // Настройка состояний анимации
        }
    }

    // Определение синхронизированных данных
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(MAGIC_ATTACKING, false);
    }

    // Установка состояния магической атаки
    public void setMagicAttacking(boolean attacking) {
        this.entityData.set(MAGIC_ATTACKING, attacking);
    }

    // Проверка, выполняется ли магическая атака
    public boolean isMagicAttacking() {
        return this.entityData.get(MAGIC_ATTACKING);
    }

    // Настройка состояний анимации
    private void setupAnimationStates() {
        // Анимация бездействия
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            this.idleAnimationTimeout--;
        }

        // Анимация ближней атаки
        if (this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 13; // Длительность анимации
            attackAnimationState.start(this.tickCount);
        } else if (!this.isAttacking()) {
            attackAnimationState.stop();
        }

        // Анимация дальней атаки
        if (this.isMagicAttacking()) {
            if (attackAnimationTimeout <= 0) {
                attackAnimationTimeout = 40; // Длительность анимации
                magicAttackAnimationState.start(this.tickCount);
            }
        } else {
            magicAttackAnimationState.stop();
        }

        // Уменьшение таймера анимации
        if (attackAnimationTimeout > 0) {
            attackAnimationTimeout--;
        }
    }

    // Обновление AI
    @Override
    public void aiStep() {
        super.aiStep();
    }

    // Добавление игрока в полосу здоровья босса
    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (this.bossInfo == null) {
            this.bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.YELLOW, ServerBossEvent.BossBarOverlay.PROGRESS);
        }
        this.bossInfo.addPlayer(player);
    }

    // Удаление игрока из полосы здоровья босса
    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        if (this.bossInfo != null) {
            this.bossInfo.removePlayer(player);
        }
    }

    // Иммунитет к огню
    @Override
    public boolean fireImmune() {
        return true;
    }

    // Обновление полосы здоровья босса
    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        if (this.bossInfo == null) {
            this.bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.YELLOW, ServerBossEvent.BossBarOverlay.PROGRESS);
        }
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    // Установка состояния атаки
    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    // Проверка, выполняется ли атака
    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    // Регистрация целей AI
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MutatedGartAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.2D));
        this.goalSelector.addGoal(1, new MoveTowardsRestrictionGoal(this, 1.0));

        // Инициализация цели дальнего боя
        this.rangedAttackGoal = new MutatedGartRangedAttackGoal(this, 1.0, 10.0F);
        this.goalSelector.addGoal(3, this.rangedAttackGoal);

        // Цели для атаки
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, 10, true, false, target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, 10, true, false, target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, SnowGolem.class, 10, true, false, target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Witch.class, 10, true, false, target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ZombifiedPiglin.class, 10, true, false, target -> this.distanceTo(target) <= 18.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    // Создание атрибутов моба
    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.8D)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.ATTACK_KNOCKBACK, 0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
                .add(Attributes.ARMOR, 0.0);
    }

    // Выпадение опыта при смерти
    @Override
    protected void dropExperience() {
        super.dropExperience();
        this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY(), this.getZ(), 500));
    }

    // Обновление анимации ходьбы
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