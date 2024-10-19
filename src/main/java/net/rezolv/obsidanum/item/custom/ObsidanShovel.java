package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3d;

import java.util.List;


public class ObsidanShovel extends ShovelItem {

    private static final long COOLDOWN_DURATION = 50 * 20; // 60 секунд в тиках
    private static final long ACTIVATION_DURATION = 5 * 20; // 5 секунд в тиках

    public ObsidanShovel(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClientSide && isActivated(stack)) {
            long currentTime = world.getGameTime();
            long lastActivationTime = stack.getOrCreateTag().getLong("LastActivationTime");

            if (currentTime - lastActivationTime >= ACTIVATION_DURATION) {
                if (entity instanceof Player) {
                    deactivate(stack, (Player) entity, world); // Деактивируем, если прошло время активации
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        long currentTime = worldIn.getGameTime();
        long lastActivationTime = stack.getOrCreateTag().getLong("LastActivationTime");

        if (!isActivated(stack) && currentTime - lastActivationTime >= COOLDOWN_DURATION) {
            if (!worldIn.isClientSide) {
                activate(stack, currentTime); // Активируем лопату
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (isActivated(stack)) {
            double knockbackY = 4.0; // Подбрасываем цель вверх на 4 блока
            target.setDeltaMovement(target.getDeltaMovement().x, knockbackY, target.getDeltaMovement().z);
            if (attacker instanceof Player) {
                deactivate(stack, (Player) attacker, attacker.level()); // Деактивируем после удара
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("obsidanum.press_shift2").withStyle(ChatFormatting.DARK_GRAY));
            list.add(Component.translatable("item.obsidan.description.shovel").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            list.add(Component.translatable("obsidanum.press_shift").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public void activate(ItemStack stack, long currentTime) {
        stack.getOrCreateTag().putBoolean("Activated", true);
        stack.getOrCreateTag().putLong("LastActivationTime", currentTime);
        stack.getOrCreateTag().putInt("CustomModelData", 1); // Изменяем модель на активированную
    }

    public void deactivate(ItemStack stack, Player player, Level world) {
        stack.getOrCreateTag().putBoolean("Activated", false);
        stack.getOrCreateTag().putInt("CustomModelData", 0); // Возвращаем обычную модель

        player.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION); // Устанавливаем кулдаун
    }

    public boolean isActivated(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Activated");
    }
}