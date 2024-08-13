package net.rezolv.obsidanum.entity.meat_beetle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.rezolv.obsidanum.entity.meat_beetle.ai.PanicByLightGoal;
import org.jetbrains.annotations.Nullable;

public class MeetBeetle extends Animal {

    public MeetBeetle(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    @Override
    public void tick() {
        super.tick();
        Level level1 = this.level();


        boolean isDaytime = level1.isDay();
        boolean canSeeSky = level1.canSeeSky(blockPosition());

        // Если день и сущность под открытым небом, замедляем движение
        if (isDaytime && canSeeSky) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.15D); // Замедляем движение
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.27D); // Восстанавливаем обычную скорость
        }

        if(this.level().isClientSide()) {
            setupAnimationStates();
        }
    }
    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
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
    private boolean isMoving() {
        return this.getDeltaMovement().lengthSqr() > 0.01; // Arbitrary threshold for movement
    }

    @Override
    protected void dropExperience() {
        super.dropExperience();
        this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY(), this.getZ(), 0));
    }
    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.MOVEMENT_SPEED, 0.27D)
                .add(Attributes.ARMOR, 0.0);
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new PanicByLightGoal(this)); // Добавьте новую цель паники
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 5.0F));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(4, new FloatGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D)); // Added random movement


    }

    // Добавьте методы для управления состоянием паники
    private boolean isPanicking = false;

    public boolean isPanicking() {
        return isPanicking;
    }

    public void setPanicking(boolean panicking) {
        this.isPanicking = panicking;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
