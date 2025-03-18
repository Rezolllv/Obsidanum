package net.rezolv.obsidanum.item.custom;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.rezolv.obsidanum.item.item_entity.obsidan_chakram.ObsidianChakramEntity;
import net.rezolv.obsidanum.sound.SoundsObs;

public class Chakram extends Item {
    public Chakram(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
        getDamage(entity.level(), entity);
        return retval;
    }
    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }
    public static void getDamage(LevelAccessor world, Entity entity) {
        if (entity == null)
            return;
        entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK)), 3);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        entity.startUsingItem(hand);
        ItemStack itemStack = entity.getItemInHand(hand);

        // Server-side only logic
        if (!world.isClientSide) {
            // Create the projectile entity
            ObsidianChakramEntity chakramEntity = new ObsidianChakramEntity(world, entity);

            // Уменьшение количества предметов на 1
            if (!entity.isCreative()) {
                itemStack.shrink(1);
            }

            // Play throw sound
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundsObs.CHAKRAM_TROWE.get(), SoundSource.PLAYERS, 1.0F,
                    0.8F + world.random.nextFloat() * 0.4F);

            // Set the position and motion of the projectile
            Vec3 lookVec = entity.getLookAngle();
            chakramEntity.setPos(
                    entity.getX(),
                    entity.getEyeY() - (double) 0.1F,
                    entity.getZ()
            );

            // Рассчитываем начальную ориентацию чакрама
            float yaw = entity.getYRot();
            float pitch = entity.getXRot();

            // Корректируем ориентацию для вертикального броска
            if (Math.abs(lookVec.y) > 0.9) { // Если игрок смотрит почти вертикально
                yaw += 90.0F; // Поворачиваем чакрам на 90 градусов, чтобы он был ребром
            }

            // Устанавливаем начальную ориентацию чакрама
            chakramEntity.setYRot(yaw);
            chakramEntity.setXRot(pitch);

            // Сохраняем ориентацию через методы
            chakramEntity.getEntityData().set(ObsidianChakramEntity.STOPPED_YAW, yaw);
            chakramEntity.getEntityData().set(ObsidianChakramEntity.STOPPED_PITCH, pitch);

            // Запускаем чакрам
            chakramEntity.shoot(lookVec.x, lookVec.y, lookVec.z, 1.5F, 1.0F);

            // Add the projectile entity to the world
            world.addFreshEntity(chakramEntity);
        }

        // Кулдаун
        entity.getCooldowns().addCooldown(this, 30);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, entity.getItemInHand(hand));
    }
}
