package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class VelnariumSword extends SwordItem {
    public VelnariumSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (isNetherEntity(target)) {
            // Наносим дополнительный урон существам из Незера
            target.hurt(attacker.damageSources().playerAttack((Player) attacker), 10);
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if(Screen.hasShiftDown()) {
            list.add(Component.translatable("obsidanum.press_shift2").withStyle(ChatFormatting.DARK_GRAY));
            if (world.dimension() == Level.NETHER) {
                // Игрок в Незере
                list.add(Component.translatable("item.velnarium_sword.description.in_nether").withStyle(ChatFormatting.RED));
            } else {
                // Игрок в обычном мире или другом измерении
                list.add(Component.translatable("item.velnarium_sword.description.not_in_nether").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            list.add(Component.translatable("obsidanum.press_shift").withStyle(ChatFormatting.DARK_GRAY));
        }

    }
    private boolean isNetherEntity(LivingEntity entity) {
        return entity.level().dimension().equals(Level.NETHER) ||
                entity.getType().equals(EntityType.ZOMBIFIED_PIGLIN) ||
                entity.getType().equals(EntityType.GHAST);
    }
}
