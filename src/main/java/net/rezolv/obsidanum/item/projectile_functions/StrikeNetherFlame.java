package net.rezolv.obsidanum.item.projectile_functions;


import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.entity.ModItemEntities;
import net.rezolv.obsidanum.entity.projectile_entity.NetherFlameEntity;
import net.rezolv.obsidanum.item.ItemsObs;

@Mod.EventBusSubscriber
public class StrikeNetherFlame {

    public static void execute(Entity entity, LevelAccessor world, double x, double y, double z) {
        // Получаем предмет из любой руки
        ItemStack mainHandItem = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
        ItemStack offHandItem = (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY);

        // Проверяем, что хотя бы в одной руке есть Robin Stick
        if (isStrikeNetherFlame(mainHandItem) || isStrikeNetherFlame(offHandItem)) {
            // Уменьшаем прочность на 1 и восстанавливаем
            if (mainHandItem.getDamageValue() < 69) {
                ItemStack _ist = (isStrikeNetherFlame(mainHandItem)) ? mainHandItem : offHandItem;
                if (_ist.hurt(1, RandomSource.create(), null)) {
                    _ist.shrink(1);
                    _ist.setDamageValue(0);
                }
            }

            // Стреляем проектилем
            if (entity instanceof LivingEntity shooter) {
                Level projectileLevel = shooter.level();
                if (!projectileLevel.isClientSide()) {
                    projectileLevel.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
                            SoundEvents.LAVA_POP, // Звук лавы, можно заменить на кастомный
                            SoundSource.PLAYERS,  // Категория звука
                            1.0f,                 // Громкость
                            0.8f + projectileLevel.random.nextFloat() * 0.4f); // Высота звука с небольшим рандомом
                    Projectile projectile = new NetherFlameEntity(ModItemEntities.NETHER_FLAME_ENTITY.get(), projectileLevel);
                    projectile.setOwner(shooter);
                    projectile.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
                    Vec3 lookVector = shooter.getLookAngle();
                    projectile.shoot(lookVector.x, lookVector.y, lookVector.z, 1.5f, 1.0f);
                    projectileLevel.addFreshEntity(projectile);
                }
            }
        }
    }

    // Проверка, является ли предмет ROBIN_STICK
    private static boolean isStrikeNetherFlame(ItemStack itemStack) {
        return itemStack.getItem() == ItemsObs.NETHER_FLAME.get();
    }
}
