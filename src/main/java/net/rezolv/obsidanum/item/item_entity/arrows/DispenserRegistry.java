package net.rezolv.obsidanum.item.item_entity.arrows;

import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.item_entity.arrows.flame_bolt.FlameBolt;
import net.rezolv.obsidanum.item.item_entity.arrows.netherite_bolt.NetheriteBolt;
import net.rezolv.obsidanum.item.item_entity.arrows.obsidian_arrow.ObsidianArrow;

public class DispenserRegistry {

    /**
     * Регистрирует поведения для всех специальных стрел при выстреле из раздатчика
     */
    public static void registerBehaviors() {
        // Регистрация поведения для обсидиановой стрелы
        DispenserBlock.registerBehavior(ItemsObs.OBSIDIAN_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                AbstractArrow arrow = new ObsidianArrow(level, position.x(), position.y(), position.z());
                arrow.pickup = AbstractArrow.Pickup.ALLOWED; // Разрешить подбирать стрелу
                return arrow;
            }
        });

        // Регистрация поведения для огненного снаряда
        DispenserBlock.registerBehavior(ItemsObs.FLAME_BOLT.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                AbstractArrow arrow = new FlameBolt(level, position.x(), position.y(), position.z());
                arrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return arrow;
            }
        });

        // Регистрация поведения для незеритового снаряда
        DispenserBlock.registerBehavior(ItemsObs.NETHERITE_BOLT.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                AbstractArrow arrow = new NetheriteBolt(level, position.x(), position.y(), position.z());
                arrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return arrow;
            }
        });
    }
}