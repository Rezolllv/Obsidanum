package net.rezolv.obsidanum.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class ObsSword extends SwordItem {

    public ObsSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        // Создаем экземпляр Random
        Random random = new Random();
        // Генерируем случайное число от 0 до 10
        int damage = random.nextInt(11); // Верхний предел 11, чтобы получить число от 0 до 10

        pStack.hurtAndBreak(damage, pAttacker, (p_43296_) -> {
            p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            // Создаем экземпляр Random
            Random random = new Random();
            // Генерируем случайное число от 0 до 10
            int damage = random.nextInt(11); // Верхний предел 11, чтобы получить число от 0 до 10

            pStack.hurtAndBreak(damage, pEntityLiving, (p_40992_) -> {
                p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }
}
