package net.rezolv.obsidanum.item.custom;

import com.google.common.collect.Lists;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.item_entity.arrows.flame_bolt.FlameBolt;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

public class FlameCrossbowItem extends CrossbowItem implements Vanishable {
    protected static final Predicate<ItemStack> PREDICATE_BOLTS = (itemstack) -> {
        return itemstack.getItem() instanceof FlameBoltItem || itemstack.getItem() instanceof NetheriteBoltItem;
    };

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return PREDICATE_BOLTS;
    }
    public FlameCrossbowItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        int i = this.getUseDuration(pStack) - pTimeLeft;
        float f = getPowerForTime(i, pStack);
        if (f >= 1.0F && !isCharged(pStack) && tryLoadProjectiles(pEntityLiving, pStack)) {
            setCharged(pStack, true);
            SoundSource soundsource = pEntityLiving instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            pLevel.playSound((Player)null, pEntityLiving.getX(), pEntityLiving.getY(), pEntityLiving.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundsource, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }
    private static float getPowerForTime(int pUseTime, ItemStack pCrossbowStack) {
        float f = (float)pUseTime / (float)getChargeDuration(pCrossbowStack);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }
    private static boolean tryLoadProjectiles(LivingEntity pShooter, ItemStack pCrossbowStack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, pCrossbowStack);
        int j = i == 0 ? 1 : 3;
        boolean flag = pShooter instanceof Player && ((Player)pShooter).getAbilities().instabuild;

        // Ищем сначала Netherite болты
        ItemStack itemstack = findProjectile(pShooter, ItemsObs.NETHERITE_BOLT.get());
        if (itemstack.isEmpty()) {
            // Если Netherite не найден, ищем Flame болты
            itemstack = pShooter.getProjectile(pCrossbowStack);
        }
        ItemStack itemstack1 = itemstack.copy();

        for(int k = 0; k < j; ++k) {
            if (k > 0) {
                itemstack = itemstack1.copy();
            }

            if (itemstack.isEmpty() && flag) {
                // В креативном режиме создаем Netherite болт по умолчанию
                itemstack = new ItemStack(ItemsObs.NETHERITE_BOLT.get());
                itemstack1 = itemstack.copy();
            }

            if (!loadProjectile(pShooter, pCrossbowStack, itemstack, k > 0, flag)) {
                return false;
            }
        }

        return true;
    }

    // Вспомогательный метод для поиска болтов в инвентаре
    private static ItemStack findProjectile(LivingEntity shooter, Item projectileItem) {
        if (shooter instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() == projectileItem && !stack.isEmpty()) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }
    private static boolean loadProjectile(LivingEntity pShooter, ItemStack pCrossbowStack, ItemStack pAmmoStack, boolean pHasAmmo, boolean pIsCreative) {
        if (pAmmoStack.isEmpty()) {
            return false;
        } else {
            boolean flag = pIsCreative && pAmmoStack.getItem() instanceof ArrowItem;
            ItemStack itemstack;
            if (!flag && !pIsCreative && !pHasAmmo) {
                itemstack = pAmmoStack.split(1);
                if (pAmmoStack.isEmpty() && pShooter instanceof Player) {
                    ((Player)pShooter).getInventory().removeItem(pAmmoStack);
                }
            } else {
                itemstack = pAmmoStack.copy();
            }

            addChargedProjectile(pCrossbowStack, itemstack);
            return true;
        }
    }
    private static void addChargedProjectile(ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        CompoundTag compoundtag = pCrossbowStack.getOrCreateTag();
        ListTag listtag;
        if (compoundtag.contains("ChargedProjectiles", 9)) {
            listtag = compoundtag.getList("ChargedProjectiles", 10);
        } else {
            listtag = new ListTag();
        }

        CompoundTag compoundtag1 = new CompoundTag();
        pAmmoStack.save(compoundtag1);
        listtag.add(compoundtag1);
        compoundtag.put("ChargedProjectiles", listtag);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (isCharged(itemstack)) {
            performShooting(pLevel, pPlayer, pHand, itemstack, getShootingPower(itemstack), 1.0F);
            setCharged(itemstack, false);
            return InteractionResultHolder.consume(itemstack);
        } else if (!pPlayer.getProjectile(itemstack).isEmpty()) {
            if (!isCharged(itemstack)) {
                pPlayer.startUsingItem(pHand);
            }

            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }
    private static float getShootingPower(ItemStack pCrossbowStack) {
        return containsChargedProjectile(pCrossbowStack, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }
    private static List<ItemStack> getChargedProjectiles(ItemStack pCrossbowStack) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag compoundtag = pCrossbowStack.getTag();
        if (compoundtag != null && compoundtag.contains("ChargedProjectiles", 9)) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", 10);
            if (listtag != null) {
                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundtag1 = listtag.getCompound(i);
                    list.add(ItemStack.of(compoundtag1));
                }
            }
        }

        return list;
    }
    private static float[] getShotPitches(RandomSource pRandom) {
        boolean flag = pRandom.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(flag, pRandom), getRandomShotPitch(!flag, pRandom)};
    }
    private static float getRandomShotPitch(boolean pIsHighPitched, RandomSource pRandom) {
        float f = pIsHighPitched ? 0.63F : 0.43F;
        return 1.0F / (pRandom.nextFloat() * 0.5F + 1.8F) + f;
    }
    public static void performShooting(Level pLevel, LivingEntity pShooter, InteractionHand pUsedHand, ItemStack pCrossbowStack, float pVelocity, float pInaccuracy) {
        if (pShooter instanceof Player player) {
            if (ForgeEventFactory.onArrowLoose(pCrossbowStack, pShooter.level(), player, 1, true) < 0) {
                return;
            }
        }

        List<ItemStack> list = getChargedProjectiles(pCrossbowStack);
        float[] afloat = getShotPitches(pShooter.getRandom());

        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = (ItemStack)list.get(i);
            boolean flag = pShooter instanceof Player && ((Player)pShooter).getAbilities().instabuild;
            if (!itemstack.isEmpty()) {
                if (i == 0) {
                    shootProjectile(pLevel, pShooter, pUsedHand, pCrossbowStack, itemstack, afloat[i], flag, pVelocity, pInaccuracy, 0.0F);
                } else if (i == 1) {
                    shootProjectile(pLevel, pShooter, pUsedHand, pCrossbowStack, itemstack, afloat[i], flag, pVelocity, pInaccuracy, -10.0F);
                } else if (i == 2) {
                    shootProjectile(pLevel, pShooter, pUsedHand, pCrossbowStack, itemstack, afloat[i], flag, pVelocity, pInaccuracy, 10.0F);
                }
            }
        }

        onCrossbowShot(pLevel, pShooter, pCrossbowStack);
    }
    private static void onCrossbowShot(Level pLevel, LivingEntity pShooter, ItemStack pCrossbowStack) {
        if (pShooter instanceof ServerPlayer serverplayer) {
            if (!pLevel.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, pCrossbowStack);
            }

            serverplayer.awardStat(Stats.ITEM_USED.get(pCrossbowStack.getItem()));
        }

        clearChargedProjectiles(pCrossbowStack);
    }
    private static void clearChargedProjectiles(ItemStack pCrossbowStack) {
        CompoundTag compoundtag = pCrossbowStack.getTag();
        if (compoundtag != null) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", 9);
            listtag.clear();
            compoundtag.put("ChargedProjectiles", listtag);
        }

    }
    private static void shootProjectile(Level pLevel, LivingEntity pShooter, InteractionHand pHand, ItemStack pCrossbowStack, ItemStack pAmmoStack, float pSoundPitch, boolean pIsCreativeMode, float pVelocity, float pInaccuracy, float pProjectileAngle) {
        if (!pLevel.isClientSide) {
            boolean flag = pAmmoStack.is(Items.FIREWORK_ROCKET);
            Object projectile;
            if (flag) {
                projectile = new FireworkRocketEntity(pLevel, pAmmoStack, pShooter, pShooter.getX(), pShooter.getEyeY() - 0.15000000596046448, pShooter.getZ(), true);
            } else {
                projectile = getArrow(pLevel, pShooter, pCrossbowStack, pAmmoStack);
                if (pIsCreativeMode || pProjectileAngle != 0.0F) {
                    ((FlameBolt)projectile).pickup = FlameBolt.Pickup.CREATIVE_ONLY;
                }
            }

            if (pShooter instanceof CrossbowAttackMob) {
                CrossbowAttackMob crossbowattackmob = (CrossbowAttackMob)pShooter;
                crossbowattackmob.shootCrossbowProjectile(crossbowattackmob.getTarget(), pCrossbowStack, (Projectile)projectile, pProjectileAngle);
            } else {
                Vec3 vec31 = pShooter.getUpVector(1.0F);
                Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(pProjectileAngle * 0.017453292F), vec31.x, vec31.y, vec31.z);
                Vec3 vec3 = pShooter.getViewVector(1.0F);
                Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
                ((Projectile)projectile).shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), pVelocity, pInaccuracy);
            }

            pCrossbowStack.hurtAndBreak(flag ? 3 : 1, pShooter, (p_40858_) -> {
                p_40858_.broadcastBreakEvent(pHand);
            });
            pLevel.addFreshEntity((Entity)projectile);
            pLevel.playSound((Player)null, pShooter.getX(), pShooter.getY(), pShooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, pSoundPitch);
        }

    }
    private static AbstractArrow getArrow(Level pLevel, LivingEntity pLivingEntity, ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        AbstractArrow abstractarrow;

        // Проверяем тип болта и создаем соответствующий снаряд
        if (pAmmoStack.getItem() instanceof NetheriteBoltItem) {
            NetheriteBoltItem boltItem = (NetheriteBoltItem) pAmmoStack.getItem();
            abstractarrow = boltItem.createArrow(pLevel, pAmmoStack, pLivingEntity);
        } else {
            // По умолчанию используем FlameArrowItem
            FlameBoltItem arrowItem = (FlameBoltItem) (pAmmoStack.getItem() instanceof FlameBoltItem ? pAmmoStack.getItem() : ItemsObs.FLAME_BOLT.get());
            abstractarrow = arrowItem.createArrow(pLevel, pAmmoStack, pLivingEntity);
        }

        if (pLivingEntity instanceof Player) {
            abstractarrow.setCritArrow(true);
        }

        abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractarrow.setShotFromCrossbow(true);
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, pCrossbowStack);
        if (i > 0) {
            abstractarrow.setPierceLevel((byte)i);
        }

        return abstractarrow;
    }
}