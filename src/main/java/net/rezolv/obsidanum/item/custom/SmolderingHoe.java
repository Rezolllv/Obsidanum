package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
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
    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if(Screen.hasShiftDown()) {
            list.add(Component.translatable("obsidanum.press_shift2").withStyle(ChatFormatting.DARK_GRAY));
            list.add(Component.translatable("item.smoldering_obsidian.description.instrument").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            list.add(Component.translatable("obsidanum.press_shift").withStyle(ChatFormatting.DARK_GRAY));
        }

    }
    @Override
    public boolean mineBlock(ItemStack itemstack, Level world, BlockState blockstate, BlockPos pos, LivingEntity entity) {
        boolean retval = super.mineBlock(itemstack, world, blockstate, pos, entity);

        // Определяем уровень сервера
        ServerLevel serverLevel = (world instanceof ServerLevel) ? (ServerLevel) world : null;
        // Добавляем огненные частицы
        if (world instanceof ServerLevel) {
            for (int i = 0; i < 5; i++) {
                double offsetX = world.random.nextDouble() * 0.5 - 0.25;
                double offsetY = world.random.nextDouble() * 0.5 - 0.25;
                double offsetZ = world.random.nextDouble() * 0.5 - 0.25;
                serverLevel.sendParticles(ParticleTypes.FLAME, pos.getX() + 0.5 + offsetX, pos.getY() + 0.5 + offsetY, pos.getZ() + 0.5 + offsetZ, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        // Получаем дропы с учетом инструмента и зачарования удачи
        List<ItemStack> drops = Block.getDrops(blockstate, serverLevel, pos, world.getBlockEntity(pos), entity, itemstack);

        // Создаем список для хранения результатов (переплавленные или исходные предметы)
        List<ItemStack> results = new ArrayList<>();
        int totalExp = 0;

        // Перебираем все дропы
        for (ItemStack drop : drops) {
            // Проверяем, можно ли дроп переплавить
            Optional<SmeltingRecipe> recipeOpt = serverLevel.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(drop), serverLevel);

            if (recipeOpt.isPresent()) {
                // Получаем результат переплавки
                ItemStack smeltedResult = recipeOpt.get().getResultItem(serverLevel.registryAccess()).copy();
                smeltedResult.setCount(drop.getCount());  // Сохраняем количество исходного дропа
                results.add(smeltedResult);
                // Добавляем опыт за переплавку
                totalExp += recipeOpt.get().getExperience() * 2;
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

        // Получаем уровень зачарования Удача
        int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, itemstack);
        int silkTouchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack);

        // Получаем количество опыта, которое должен был бы выпустить блок
        int exp = blockstate.getBlock().getExpDrop(blockstate, serverLevel, serverLevel.getRandom(), pos, fortuneLevel, silkTouchLevel);

        // Спавним опыт в мире
        if (exp > 0) {
            blockstate.getBlock().popExperience(serverLevel, pos, exp);
        }

        // Спавним опыт за переплавку
        if (totalExp > 0) {
            while (totalExp > 0) {
                int expToDrop = ExperienceOrb.getExperienceValue(totalExp);
                totalExp -= expToDrop;
                serverLevel.addFreshEntity(new ExperienceOrb(serverLevel, pos.getX(), pos.getY(), pos.getZ(), expToDrop));
            }
        }

        return retval;
    }
}