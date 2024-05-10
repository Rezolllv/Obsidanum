package net.rezolv.obsidanum.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SmolderingAxe extends AxeItem {
    public SmolderingAxe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        boolean retval = super.onLeftClickEntity(stack, player, target);

        // Определяем 20% вероятность поджечь цель
        double chanceToIgnite = 0.30; // 20% вероятность

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
    @Override
    public boolean mineBlock(ItemStack itemstack, Level world, BlockState blockstate, BlockPos pos, LivingEntity entity) {
        boolean retval = super.mineBlock(itemstack, world, blockstate, pos, entity);

        // Определяем уровень сервера
        ServerLevel serverLevel = (world instanceof ServerLevel) ? (ServerLevel) world : null;

        // Получаем дропы с учетом инструмента и зачарования удачи
        List<ItemStack> drops = Block.getDrops(blockstate, serverLevel, pos, world.getBlockEntity(pos), entity, itemstack);

        // Создаем список для хранения результатов (переплавленные или исходные предметы)
        List<ItemStack> results = new ArrayList<>();

        // Перебираем все дропы
        for (ItemStack drop : drops) {
            // Проверяем, можно ли дроп переплавить
            Optional<? extends Recipe<?>> recipeOpt = serverLevel.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(drop), serverLevel);

            if (recipeOpt.isPresent()) {
                // Получаем результат переплавки
                ItemStack smeltedResult = recipeOpt.get().getResultItem(serverLevel.registryAccess()).copy();
                smeltedResult.setCount(drop.getCount());  // Сохраняем количество исходного дропа
                results.add(smeltedResult);
            } else {
                // Если переплавка невозможна, добавляем исходный дроп
                results.add(drop);
            }
        }

        // Спавним каждый предмет из списка результатов
        for (ItemStack result : results) {
            ItemEntity entityToSpawn = new ItemEntity(serverLevel, pos.getX(), pos.getY(), pos.getZ(), result);
            entityToSpawn.setPickUpDelay(10);
            serverLevel.addFreshEntity(entityToSpawn);
        }

        // Удаляем блок, так как дроп уже обработан
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

        return retval;
    }

}