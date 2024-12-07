package net.rezolv.obsidanum.item.custom;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.entity.ModItemEntities;
import net.rezolv.obsidanum.entity.projectile_entity.NetherFlameEntityMini;
import net.rezolv.obsidanum.item.projectile_functions.StrikeNetherFlame;

@Mod.EventBusSubscriber
public class NetherFlame extends Item {
    public NetherFlame(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    @Override
    public boolean isRepairable(ItemStack itemstack) {
        return false;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
        ItemStack retval = new ItemStack(this);
        retval.setDamageValue(itemstack.getDamageValue() + 1);
        if (retval.getDamageValue() >= retval.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return retval;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player entity, InteractionHand hand) {
        entity.startUsingItem(hand);


        if (!level.isClientSide) {
            Vec3 hitDirection = new Vec3(0, 1, 0);
            if (level.random.nextFloat() < 0.1) {
                // Первая группа снарядов (поблизости, по кругу)
                int miniProjectileCount = 10 + entity.getRandom().nextInt(25);
                for (int i = 0; i < miniProjectileCount; i++) {
                    NetherFlameEntityMini miniProjectile = new NetherFlameEntityMini(ModItemEntities.NETHER_FLAME_ENTITY_MINI.get(), level);
                    miniProjectile.setOwner(entity);
                    miniProjectile.setPos(entity.getX(), entity.getY(), entity.getZ());
                    entity.hurt(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.IN_FIRE)), 10);
                    entity.setSecondsOnFire(6); // Поджигаем на 6 секунд
                    double angle = 2 * Math.PI * i / miniProjectileCount;
                    double offsetX = Math.cos(angle) * 0.5;
                    double offsetZ = Math.sin(angle) * 0.5;
                    double offsetY = 0.2 + entity.getRandom().nextDouble() * 0.3;

                    Vec3 scatterDirection = new Vec3(offsetX, offsetY, offsetZ).add(hitDirection);
                    scatterDirection = scatterDirection.normalize().scale(0.6 + entity.getRandom().nextDouble() * 0.4);

                    miniProjectile.shoot(scatterDirection.x, scatterDirection.y, scatterDirection.z, 0.45f, 0.1f);
                    level.addFreshEntity(miniProjectile);
                }
                level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 4.0f, Level.ExplosionInteraction.NONE);
                entity.setItemInHand(hand, ItemStack.EMPTY);
                // Вторая группа снарядов (дальше, тоже по кругу)
                int outerProjectileCount = 30 + entity.getRandom().nextInt(40);
                for (int i = 0; i < outerProjectileCount; i++) {
                    NetherFlameEntityMini outerProjectile = new NetherFlameEntityMini(ModItemEntities.NETHER_FLAME_ENTITY_MINI.get(), level);
                    outerProjectile.setOwner(entity);
                    outerProjectile.setPos(entity.getX(), entity.getY(), entity.getZ());

                    double angle = 2 * Math.PI * i / outerProjectileCount;
                    double offsetX = Math.cos(angle) * 1.5; // Увеличиваем радиус
                    double offsetZ = Math.sin(angle) * 1.5;
                    double offsetY = 0.1 + entity.getRandom().nextDouble() * 0.2;

                    Vec3 outerDirection = new Vec3(offsetX, offsetY, offsetZ);
                    outerDirection = outerDirection.normalize().scale(1.0 + entity.getRandom().nextDouble() * 0.5); // Дальше и чуть больше разброс

                    outerProjectile.shoot(outerDirection.x, outerDirection.y, outerDirection.z, 0.7f, 0.2f); // Немного быстрее
                    level.addFreshEntity(outerProjectile);
                }

                // Звук
                level.playSound(
                        null,
                        entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.LAVA_POP,
                        SoundSource.PLAYERS,
                        1.0f,
                        0.8f + entity.getRandom().nextFloat() * 0.4f
                );
            }
            else {
                StrikeNetherFlame.execute(entity, level, entity.getX(), entity.getY(), entity.getZ());
            }
        }
        // Кулдаун
        entity.getCooldowns().addCooldown(this, 100);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, entity.getItemInHand(hand));
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        // Проверяем каждый слот инвентаря
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof NetherFlame) {
                // Проверяем, прошло ли уже минута с последнего поджога
                if (player.tickCount % (20 * 60) == 0) { // 20 тиков в секунду * 60 секунд = 1 минута
                    // Генерируем случайное число от 0 до 99
                    int chance = player.getRandom().nextInt(100);
                    // Если случайное число меньше 20, поджигаем игрока
                    if (chance < 20) {
                        player.setSecondsOnFire(5); // Поджигаем игрока на 5 секунд
                    }
                }
            }
        }
    }
}
