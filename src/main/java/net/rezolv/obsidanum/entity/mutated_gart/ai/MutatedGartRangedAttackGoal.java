package net.rezolv.obsidanum.entity.mutated_gart.ai;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.rezolv.obsidanum.entity.mutated_gart.MutatedGart;

import java.util.EnumSet;

public class MutatedGartRangedAttackGoal extends Goal {
    private final MutatedGart entity;
    private final double minDistance; // Минимальное расстояние для дальней атаки
    private int attackCooldown = 15;

    public MutatedGartRangedAttackGoal(MutatedGart entity, double minDistance) {
        this.entity = entity;

        this.minDistance = minDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }
    @Override
    public void start() {
        super.start();
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

        if (--attackCooldown <= 0) {
            attackCooldown = 15;

            // Начинаем анимацию дальнего выстрела

            // Создаем и запускаем снаряд
            SmallFireball projectile = new SmallFireball(EntityType.SMALL_FIREBALL, level);
            projectile.setOwner(this.entity);
            projectile.setPos(this.entity.getX(), this.entity.getEyeY() - 0.2, this.entity.getZ());
            projectile.shoot(target.getX() - this.entity.getX(),
                    target.getEyeY() - projectile.getY(),
                    target.getZ() - this.entity.getZ(),
                    1.5F, 1.0F);

            level.addFreshEntity(projectile);

            // Останавливаем анимацию дальнего выстрела после выстрела
        }
    }
}
