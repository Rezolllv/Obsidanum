package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.particle.ParticlesObs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SmolderingAxe extends AxeItem {
    public SmolderingAxe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
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

        ServerLevel serverLevel = (world instanceof ServerLevel) ? (ServerLevel) world : null;
        if (serverLevel == null) return retval;

        List<ItemStack> drops = Block.getDrops(blockstate, serverLevel, pos, world.getBlockEntity(pos), entity, itemstack);

        if (!drops.isEmpty()) {
            List<ItemStack> results = new ArrayList<>();
            int totalExp = 0;

            for (ItemStack drop : drops) {
                Optional<SmeltingRecipe> recipeOpt = serverLevel.getRecipeManager()
                        .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(drop), serverLevel);

                if (recipeOpt.isPresent()) {
                    // Добавление частиц
                    for (int i = 0; i < 5; i++) {
                        double offsetX = world.random.nextDouble() * 0.5 - 0.25;
                        double offsetY = world.random.nextDouble() * 0.5 - 0.25;
                        double offsetZ = world.random.nextDouble() * 0.5 - 0.25;
                        serverLevel.sendParticles(ParticlesObs.NETHER_FLAME2_PARTICLES.get(),
                                pos.getX() + 0.5 + offsetX,
                                pos.getY() + 0.5 + offsetY,
                                pos.getZ() + 0.5 + offsetZ,
                                1, 0.0, 0.0, 0.0, 0.0);
                    }

                    ItemStack smeltedResult = recipeOpt.get().getResultItem(serverLevel.registryAccess()).copy();
                    smeltedResult.setCount(drop.getCount());
                    results.add(smeltedResult);
                    totalExp += recipeOpt.get().getExperience() * 2;
                } else {
                    results.add(drop);
                }
            }

            // Спавн предметов с правильным смещением и движением
            for (ItemStack result : results) {
                double x = pos.getX() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5;
                double y = pos.getY() + 0.25 + world.random.nextDouble() * 0.5;
                double z = pos.getZ() + 0.5 + (world.random.nextDouble() - 0.5) * 0.5;

                ItemEntity entityToSpawn = new ItemEntity(serverLevel, x, y, z, result);
                entityToSpawn.setPickUpDelay(10);

                // Добавляем естественное движение как при обычном дропе
                entityToSpawn.setDeltaMovement(
                        world.random.nextGaussian() * 0.05,
                        world.random.nextGaussian() * 0.05 + 0.2,
                        world.random.nextGaussian() * 0.05
                );

                serverLevel.addFreshEntity(entityToSpawn);
            }

            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

            // Опыт от блока
            int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, itemstack);
            int silkTouchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack);
            int exp = blockstate.getBlock().getExpDrop(blockstate, serverLevel, serverLevel.getRandom(), pos, fortuneLevel, silkTouchLevel);

            if (exp > 0) {
                blockstate.getBlock().popExperience(serverLevel, pos, exp);
            }

            // Опыт за переплавку
            if (totalExp > 0) {
                while (totalExp > 0) {
                    int expToDrop = ExperienceOrb.getExperienceValue(totalExp);
                    totalExp -= expToDrop;
                    serverLevel.addFreshEntity(new ExperienceOrb(serverLevel,
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            expToDrop));
                }
            }
        }

        return retval;
    }
}