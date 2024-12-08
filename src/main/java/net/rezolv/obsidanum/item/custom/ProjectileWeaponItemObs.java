package net.rezolv.obsidanum.item.custom;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.rezolv.obsidanum.item.ItemsObs;

import java.util.function.Predicate;

public abstract class ProjectileWeaponItemObs extends Item {

    public static final Predicate<ItemStack> ARROW_ONLY = (itemStack) -> {
        return itemStack.is(ItemsObs.FLAME_BOLT.get());
    };
    public static final Predicate<ItemStack> ARROW_OR_FIREWORK;

    public ProjectileWeaponItemObs(Properties pProperties) {
        super(pProperties);
    }
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return this.getAllSupportedProjectiles();
    }

    public abstract Predicate<ItemStack> getAllSupportedProjectiles();

    public static ItemStack getHeldProjectile(LivingEntity pShooter, Predicate<ItemStack> pIsAmmo) {
        if (pIsAmmo.test(pShooter.getItemInHand(InteractionHand.OFF_HAND))) {
            return pShooter.getItemInHand(InteractionHand.OFF_HAND);
        } else {
            return pIsAmmo.test(pShooter.getItemInHand(InteractionHand.MAIN_HAND)) ? pShooter.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
        }
    }

    public int getEnchantmentValue() {
        return 1;
    }

    public abstract int getDefaultProjectileRange();

    static {
        ARROW_OR_FIREWORK = ARROW_ONLY.or((p_43015_) -> {
            return p_43015_.is(Items.FIREWORK_ROCKET);
        });
    }
}
