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

    private static final long COOLDOWN_DURATION = 80 * 20; // 60 seconds in ticks
    private static final long ACTIVATION_DURATION = 5 * 20; // 5 seconds in ticks

    public ObsidanSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    // Метод для проверки активации через NBT
    public boolean isActivated(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Activated");
    }

    // Метод для получения времени последней активации через NBT
    public long getLastActivationTime(ItemStack stack) {
        return stack.getOrCreateTag().getLong("LastActivationTime");
    }

    // Метод для установки времени последней активации через NBT
    public void setLastActivationTime(ItemStack stack, long time) {
        stack.getOrCreateTag().putLong("LastActivationTime", time);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level world, Player player) {
        super.onCraftedBy(stack, world, player);
        stack.getOrCreateTag().putBoolean("Activated", false); // По умолчанию меч не активирован
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClientSide && isActivated(stack)) {
            long lastActivationTime = getLastActivationTime(stack);
            if (world.getGameTime() - lastActivationTime >= ACTIVATION_DURATION) {
                deactivate(stack, (Player) entity);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        long currentTime = worldIn.getGameTime();

        if (!isActivated(stack) && !playerIn.getCooldowns().isOnCooldown(this)) {
            if (!worldIn.isClientSide) {
                activate(stack, currentTime);
                playerIn.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION); // Кулдаун уникален для каждого игрока
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }
    }

    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("obsidanum.press_shift2").withStyle(ChatFormatting.DARK_GRAY));
            list.add(Component.translatable("item.obsidan.description.sword").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            list.add(Component.translatable("obsidanum.press_shift").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public void activate(ItemStack stack, long currentTime) {
        stack.getOrCreateTag().putBoolean("Activated", true);
        setLastActivationTime(stack, currentTime);
        stack.getOrCreateTag().putInt("CustomModelData", 1);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!player.isLocalPlayer() && entity instanceof LivingEntity livingEntity) {
            if (isActivated(stack)) {
                float baseDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                float additionalDamage = 4f;

                livingEntity.hurt(new DamageSource(player.getCommandSenderWorld().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK)), baseDamage + additionalDamage);
                deactivate(stack, player);
                return true; // Отмена стандартного поведения
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    public void deactivate(ItemStack stack, Player player) {
        stack.getOrCreateTag().putBoolean("Activated", false);
        stack.getOrCreateTag().putInt("CustomModelData", 0);

        player.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION); // Назначаем кулдаун для игрока
    }
}