package net.rezolv.obsidanum.item.item_entity.arrows;


import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.item_entity.arrows.flame_arrow.FlameArrow;
import net.rezolv.obsidanum.item.item_entity.arrows.obsidian_arrow.ObsidianArrow;
public class DispenserRegistry {
    public static void registerBehaviors() {

        DispenserBlock.registerBehavior(ItemsObs.OBSIDIAN_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level p_123456_, Position p_123457_, ItemStack p_123458_) {
                AbstractArrow abstractarrow = new ObsidianArrow(p_123456_, p_123457_.x(), p_123457_.y(), p_123457_.z());
                abstractarrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrow;
            }
        });
        DispenserBlock.registerBehavior(ItemsObs.FLAME_BOLT.get(), new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level p_123456_, Position p_123457_, ItemStack p_123458_) {
                AbstractArrow abstractarrow = new FlameArrow(p_123456_, p_123457_.x(), p_123457_.y(), p_123457_.z());
                abstractarrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return abstractarrow;
            }
        });

    }
}
