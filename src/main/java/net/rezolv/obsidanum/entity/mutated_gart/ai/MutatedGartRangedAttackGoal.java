package net.rezolv.obsidanum.entity.mutated_gart.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.rezolv.obsidanum.entity.ModItemEntities;
import net.rezolv.obsidanum.entity.mutated_gart.MutatedGart;
import net.rezolv.obsidanum.entity.projectile_entity.MagicArrow;

import java.util.EnumSet;

public class MutatedGartRangedAttackGoal extends Goal {
    private final MutatedGart entity;
    private final double minDistance;
    private double attackCooldown = 17.5;

    public MutatedGartRangedAttackGoal(MutatedGart entity, double minDistance) {
        this.entity = entity;

        this.minDistance = minDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public void start() {
        super.start();
        if (entity.attackAnimationMagicArrowTimeout <= 0) {
            this.entity.setAttackingMagicArrow(true);
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        return target != null && target.isAlive() && this.entity.distanceTo(target) > minDistance;
    }


    @Override
    public void tick() {
        LivingEntity target = this.entity.getTarget();
        if (target == null) return;

        Level level = this.entity.level();
        this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Decrease cooldown
        if (--attackCooldown <= 0) {
            attackCooldown = 17.5;

            MagicArrow magicArrow = new MagicArrow(ModItemEntities.MAGIC_ARROW_NETHER_FLAME_ENTITY.get(), level);
            magicArrow.setOwner(this.entity);
            magicArrow.setTarget(target); // Target for homing
            magicArrow.setPos(this.entity.getX(), this.entity.getEyeY() - 0.2, this.entity.getZ());
            magicArrow.shoot(target.getX() - this.entity.getX(),
                    target.getEyeY() - magicArrow.getY(),
                    target.getZ() - this.entity.getZ(),
                    1.5F, 1.0F);

            level.addFreshEntity(magicArrow);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAttackingMagicArrow(false);
    }
}
