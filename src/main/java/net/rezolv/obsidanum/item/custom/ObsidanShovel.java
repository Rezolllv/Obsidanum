package net.rezolv.obsidanum.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3d;

import java.util.List;


public class ObsidanShovel extends HoeItem {

    private boolean activated = false;
    private long lastActivationTime = 0;
    private static final long COOLDOWN_DURATION = 40 * 20; // 60 seconds in ticks
    private static final double BREAK_RADIUS_SQUARED = 20 * 20; // Radius of 20 blocks squared


    public ObsidanShovel(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        long currentTime = worldIn.getGameTime();
        if (activated || currentTime - lastActivationTime >= COOLDOWN_DURATION) {
            if (!worldIn.isClientSide) {
                if (activated) {
                    deactivate();
                    // Находим все существа вокруг игрока в радиусе BREAK_RADIUS

                } else {
                    activate();
                    // Код для активации, например, создание частиц и т.д.
                    lastActivationTime = currentTime;
                }


            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, playerIn.getItemInHand(handIn));
        }
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Проверяем, активен ли предмет
        if (activated) {
            // Проверяем, является ли цель существом
            if (target instanceof Mob) {
                // Определяем вектор направления удара
                Vec3 lookVector = attacker.getLookAngle();
                double knockbackX = lookVector.x * -10.0;
                double knockbackY = lookVector.y * 4.0;
                double knockbackZ = lookVector.z * -10.0;
                // Отталкиваем цель в сторону удара
                target.knockback(knockbackX, knockbackY, knockbackZ);
                deactivate();
            }
        }
        // Вызываем базовую реализацию метода для нанесения урона существу
        return super.hurtEnemy(stack, target, attacker);
    }
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if (activated) {
            list.add(Component.translatable("item.obsidan.description.enabled"));
        } else {
            list.add(Component.translatable("item.obsidan.description.disabled"));
        }
        long currentTime = world != null ? world.getGameTime() : 0;
        long timeLeft = lastActivationTime + COOLDOWN_DURATION - currentTime;
        if (timeLeft > 0) {
            int secondsLeft = (int) (timeLeft / 20); // Перевод времени из тиков в секунды
            list.add(Component.translatable("item.obsidan.description.cooldown"));
            list.add(Component.keybind("" + secondsLeft));
        }
    }

    public void activate() {
        activated = true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return activated; // Возвращает true только когда инструмент активирован

    }

    public void deactivate() {
        activated = false;
    }
}