package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class ObsidanSword extends SwordItem {

    private static final long COOLDOWN_DURATION = 80 * 20; // 80 seconds in ticks
    private static final long ACTIVATION_DURATION = 5 * 20; // 5 seconds in ticks

    public ObsidanSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public boolean isActivated(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Activated");
    }

    private long getLastActivationTime(ItemStack stack) {
        return stack.getOrCreateTag().getLong("LastActivationTime");
    }

    private void setLastActivationTime(ItemStack stack, long time) {
        stack.getOrCreateTag().putLong("LastActivationTime", time);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level world, Player player) {
        super.onCraftedBy(stack, world, player);
        // Установка значений по умолчанию
        stack.getOrCreateTag().putBoolean("Activated", false);
        stack.getOrCreateTag().putLong("LastActivationTime", 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClientSide && isActivated(stack) && world.getGameTime() - getLastActivationTime(stack) >= ACTIVATION_DURATION) {
            deactivate(stack, (Player) entity); // Передаем stack и player в метод деактивации
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        long currentTime = worldIn.getGameTime();

        if (!isActivated(stack) && currentTime - getLastActivationTime(stack) >= COOLDOWN_DURATION) {
            if (!worldIn.isClientSide) {
                activate(stack); // Передаем stack в метод активации
                setLastActivationTime(stack, currentTime);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }
    }

    public void activate(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("Activated", true);
        // Обновляем данные модели
        stack.getOrCreateTag().putInt("CustomModelData", 1);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!player.isLocalPlayer()) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                if (stack.getItem() instanceof ObsidanSword) {
                    ObsidanSword sword = (ObsidanSword) stack.getItem();
                    if (sword.isActivated(stack)) {
                        float baseDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                        float additionalDamage = 4f;

                        livingEntity.hurt(new DamageSource(player.getCommandSenderWorld().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK)), baseDamage * additionalDamage);
                        sword.deactivate(stack, player); // Деактивация меча после удара
                        return true; // Отмена стандартного поведения
                    }
                }
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    public void deactivate(ItemStack stack, Player player) {
        stack.getOrCreateTag().putBoolean("Activated", false);
        // Обновляем данные модели
        stack.getOrCreateTag().putInt("CustomModelData", 0);

        player.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION);
    }
}