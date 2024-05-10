package net.rezolv.obsidanum.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.item.ItemsObs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SmolderingHoe extends HoeItem {
    public SmolderingHoe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        boolean retval = super.onLeftClickEntity(stack, player, target);

        // Определяем 20% вероятность поджечь цель
        double chanceToIgnite = 0.20; // 20% вероятность

        // Генерируем случайное число от 0 до 1
        Random random = new Random();
        double roll = random.nextDouble();

        // Если выпало значение меньше или равно 20%, поджигаем цель
        if (roll <= chanceToIgnite && target instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity) target;
            livingTarget.setSecondsOnFire(6); // Поджигаем цель на 5 секунд
        }

        return retval;
    }

}