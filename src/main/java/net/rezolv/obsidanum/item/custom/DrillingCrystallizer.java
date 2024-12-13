package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.entity.ModEntities;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElemental;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.particle.ParticlesObs;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class DrillingCrystallizer extends Item {
    private static final Random RANDOM = new Random();
    private static final int MAX_CHAIN_LENGTH = 20; // Максимальная длина цепочки

    public DrillingCrystallizer(Properties properties) {
        super(properties);
    }
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false; 
    }
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if(Screen.hasShiftDown()) {
            list.add(Component.translatable("obsidanum.press_shift2").withStyle(ChatFormatting.DARK_GRAY));
            list.add(Component.translatable("item.obsidan.description.drilling_crystallizer").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            list.add(Component.translatable("obsidanum.press_shift").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!(context.getLevel() instanceof ServerLevel)) {
            return InteractionResult.FAIL;
        }

        ServerLevel level = (ServerLevel) context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        if (blockState.is(Blocks.CRYING_OBSIDIAN)) {
            level.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3);
            // Воспроизведение звука шипения и частиц дыма
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
            double particleDistance = 0.5;
            for (Direction direction : Direction.values()) {
                double offsetX = direction.getStepX() * particleDistance;
                double offsetY = direction.getStepY() * particleDistance;
                double offsetZ = direction.getStepZ() * particleDistance;
                level.sendParticles(ParticlesObs.BAGELL_FLAME_PARTICLES.get(), pos.getX() + 0.5 + offsetX, pos.getY() + 0.5 + offsetY, pos.getZ() + 0.5 + offsetZ, 10, 0.1D, 0.1D, 0.1D, 0.0D);
                level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5 + offsetX, pos.getY() + 0.5 + offsetY, pos.getZ() + 0.5 + offsetZ, 10, 0.1D, 0.1D, 0.1D, 0.0D);
            }
            if (RANDOM.nextInt(100) < 40) {
                Block.popResource(level, pos, new ItemStack(ItemsObs.OBSIDIAN_TEAR.get()));
            }
            if (RANDOM.nextInt(100) < 15) {
                // Генерация случайного направления
                double angle = RANDOM.nextDouble() * 2.0D * Math.PI;
                double distance = 2.0D + RANDOM.nextDouble() * 3.0D; // Расстояние от 2 до 5 блоков

                // Вычисление целевой позиции
                double targetX = pos.getX() + 0.5D + distance * Math.cos(angle);
                double targetY = pos.getY() + 1;
                double targetZ = pos.getZ() + 0.5D + distance * Math.sin(angle);

                // Округление координат до целых чисел и создание объекта BlockPos
                BlockPos targetPos = new BlockPos((int) Math.round(targetX), (int) Math.round(targetY), (int) Math.round(targetZ));

                // Проверка на наличие воздуха
                if (level.getBlockState(targetPos).isAir()) {
                    ObsidianElemental obsidianElemental = new ObsidianElemental(ModEntities.OBSIDIAN_ELEMENTAL.get(), level);
                    obsidianElemental.moveTo(targetX, targetY, targetZ, level.random.nextFloat() * 360.0F, 0.0F);
                    level.addFreshEntity(obsidianElemental);
                }
            }
            ItemStack itemStack = context.getItemInHand();
            if (context.getPlayer() != null) {
                itemStack.hurtAndBreak(1, context.getPlayer(), player -> {
                    player.broadcastBreakEvent(context.getHand());
                });
            }
            return InteractionResult.SUCCESS;
        }

        // Список блоков руд для обработки
        Block[] ores = {
                Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
                Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
                Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
                Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
                Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
                Blocks.NETHER_QUARTZ_ORE,
                Blocks.GLOWSTONE,
                Blocks.AMETHYST_BLOCK,
                Blocks.ANCIENT_DEBRIS,
                BlocksObs.ONYX.get(),
                Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
                Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE,
                Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE
        };

        for (Block ore : ores) {
            if (blockState.is(ore)) {
                // Воспроизведение звука шипения и частиц дыма
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.sendParticles(ParticlesObs.BAGELL_FLAME_PARTICLES.get(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, 0.2D, 0.2D, 0.2D, 0.0D);
                level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.1D, 0.1D, 0.1D, 0.0D);

                // Удаление блока руды и всех прилегающих блоков руды до 20 блоков
                Queue<BlockPos> queue = new LinkedList<>();
                queue.add(pos);
                int chainLength = 0;
                while (!queue.isEmpty() && chainLength < MAX_CHAIN_LENGTH) {
                    BlockPos currentPos = queue.poll();
                    BlockState currentBlockState = level.getBlockState(currentPos);
                    if (currentBlockState.is(ore)) {
                        level.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 3);
                        chainLength++;

                        // Выпадение от 1 до 3 кристаллизированных руд
                        int itemsToDrop = RANDOM.nextInt(3) + 1; // Выпадает от 1 до 3 предметов
                        int itemsToDropFour = RANDOM.nextInt(4) + 1; // Выпадает от 1 до 4 предметов
                        int itemsToDropSix = RANDOM.nextInt(6) + 1; // Выпадает от 1 до 6 предметов

                        for (int i = 0; i < itemsToDrop; i++) {
                            // Выбор кристаллизированной руды в зависимости от типа руды
                            ItemStack crystallizedOre;
                            if (ore == Blocks.IRON_ORE || ore == Blocks.DEEPSLATE_IRON_ORE) {
                                crystallizedOre = new ItemStack(ItemsObs.CRYSTALLIZED_IRON_ORE.get());
                            } else if (ore == Blocks.GOLD_ORE || ore == Blocks.DEEPSLATE_GOLD_ORE || ore == Blocks.NETHER_GOLD_ORE) {
                                crystallizedOre = new ItemStack(ItemsObs.CRYSTALLIZED_GOLD_ORE.get());
                            } else if (ore == Blocks.COPPER_ORE || ore == Blocks.DEEPSLATE_COPPER_ORE) {
                                crystallizedOre = new ItemStack(ItemsObs.CRYSTALLIZED_COPPER_ORE.get());
                            } else if (ore == Blocks.DIAMOND_ORE || ore == Blocks.DEEPSLATE_DIAMOND_ORE) {
                                crystallizedOre = new ItemStack(Items.DIAMOND);
                            } else if (ore == Blocks.EMERALD_ORE || ore == Blocks.DEEPSLATE_EMERALD_ORE) {
                                crystallizedOre = new ItemStack(Items.EMERALD);
                            } else if (ore == Blocks.ANCIENT_DEBRIS) {
                                crystallizedOre = new ItemStack(Items.NETHERITE_SCRAP);
                            } else {
                                // Здесь можно добавить обработку других типов руд
                                continue;
                            }
                            Block.popResource(level, currentPos, crystallizedOre);
                            level.sendParticles(ParticlesObs.BAGELL_FLAME_PARTICLES.get(), currentPos.getX() + 0.5, currentPos.getY() + 0.5, currentPos.getZ() + 0.5, 5, 0.2D, 0.2D, 0.2D, 0.0D);
                            level.sendParticles(ParticleTypes.SMOKE, currentPos.getX() + 0.5, currentPos.getY() + 0.5, currentPos.getZ() + 0.5, 10, 0.1D, 0.1D, 0.1D, 0.0D);
                        }

                        for (int i = 0; i < itemsToDropSix; i++) {
                            // Выбор кристаллизированной руды в зависимости от типа руды
                            ItemStack crystallizedOre;
                            if (ore == Blocks.COAL_ORE || ore == Blocks.DEEPSLATE_COAL_ORE) {
                                crystallizedOre = new ItemStack(Items.COAL);
                                // 20% chance to drop Bagell Fuel
                                if (RANDOM.nextInt(100) < 5) {
                                    level.addFreshEntity(new ExperienceOrb(level, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 6));
                                    Block.popResource(level, currentPos, new ItemStack(ItemsObs.BAGELL_FUEL.get()));
                                }
                            } else if (ore == Blocks.LAPIS_ORE || ore == Blocks.DEEPSLATE_LAPIS_ORE) {
                                crystallizedOre = new ItemStack(Items.LAPIS_LAZULI);
                            } else if (ore == Blocks.REDSTONE_ORE || ore == Blocks.DEEPSLATE_REDSTONE_ORE) {
                                crystallizedOre = new ItemStack(Items.REDSTONE);
                            } else if (ore == Blocks.NETHER_QUARTZ_ORE) {
                                crystallizedOre = new ItemStack(Items.QUARTZ);
                            } else if (ore == BlocksObs.ONYX.get()&& RANDOM.nextInt(100) < 1) {
                                crystallizedOre = new ItemStack(ItemsObs.FACETED_ONYX.get());
                            } else if (ore == Blocks.GLOWSTONE) {
                                crystallizedOre = new ItemStack(Items.GLOWSTONE_DUST);
                            } else {
                                // Здесь можно добавить обработку других типов руд
                                continue;
                            }
                            Block.popResource(level, currentPos, crystallizedOre);
                            level.sendParticles(ParticlesObs.BAGELL_FLAME_PARTICLES.get(), currentPos.getX() + 0.5, currentPos.getY() + 0.5, currentPos.getZ() + 0.5, 5, 0.2D, 0.2D, 0.2D, 0.0D);
                            level.sendParticles(ParticleTypes.SMOKE, currentPos.getX() + 0.5, currentPos.getY() + 0.5, currentPos.getZ() + 0.5, 10, 0.1D, 0.1D, 0.1D, 0.0D);
                        }

                        for (int i = 0; i < itemsToDropFour; i++) {
                            // Выбор кристаллизированной руды в зависимости от типа руды
                            ItemStack crystallizedOre;
                            if (ore == Blocks.AMETHYST_BLOCK) {
                                crystallizedOre = new ItemStack(Items.AMETHYST_SHARD);
                                // 1% chance to drop Relict Amethyst Shard
                                if (RANDOM.nextInt(100) < 1) {
                                    Block.popResource(level, currentPos, new ItemStack(ItemsObs.RELICT_AMETHYST_SHARD.get()));
                                    level.addFreshEntity(new ExperienceOrb(level, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 6));
                                }
                            } else {
                                // Здесь можно добавить обработку других типов руд
                                continue;
                            }
                            Block.popResource(level, currentPos, crystallizedOre);
                            level.sendParticles(ParticlesObs.BAGELL_FLAME_PARTICLES.get(), currentPos.getX() + 0.5, currentPos.getY() + 0.5, currentPos.getZ() + 0.5, 5, 0.2D, 0.2D, 0.2D, 0.0D);
                            level.sendParticles(ParticleTypes.SMOKE, currentPos.getX() + 0.5, currentPos.getY() + 0.5, currentPos.getZ() + 0.5, 10, 0.1D, 0.1D, 0.1D, 0.0D);
                        }

                        // Добавление опыта за каждый разрушенный блок руды
                        if (ore == Blocks.DIAMOND_ORE || ore == Blocks.DEEPSLATE_DIAMOND_ORE) {
                            level.addFreshEntity(new ExperienceOrb(level, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 3));
                        } else if (ore == Blocks.EMERALD_ORE || ore == Blocks.DEEPSLATE_EMERALD_ORE) {
                            level.addFreshEntity(new ExperienceOrb(level, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 3));
                        } else if (ore == Blocks.ANCIENT_DEBRIS) {
                            level.addFreshEntity(new ExperienceOrb(level, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 3));
                        } else if (ore == Blocks.COAL_ORE || ore == Blocks.DEEPSLATE_COAL_ORE) {
                            level.addFreshEntity(new ExperienceOrb(level, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 1));
                        } else if (ore == Blocks.LAPIS_ORE || ore == Blocks.DEEPSLATE_LAPIS_ORE) {
                            level.addFreshEntity(new ExperienceOrb(level, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 2));
                        } else if (ore == Blocks.REDSTONE_ORE || ore == Blocks.DEEPSLATE_REDSTONE_ORE) {
                            level.addFreshEntity(new ExperienceOrb(level, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 1));
                        } else if (ore == Blocks.NETHER_QUARTZ_ORE) {
                            level.addFreshEntity(new ExperienceOrb(level, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 1));
                        }

                        // Добавление соседних блоков руды в очередь для обработки
                        for (Direction direction : Direction.values()) {
                            BlockPos neighborPos = currentPos.relative(direction);
                            BlockState neighborBlockState = level.getBlockState(neighborPos);
                            if (neighborBlockState.is(ore)) {
                                queue.add(neighborPos);
                            }
                        }
                    }
                }

                // Уменьшение прочности предмета на 1
                ItemStack itemStack = context.getItemInHand();
                if (context.getPlayer() != null) {
                    itemStack.hurtAndBreak(1, context.getPlayer(), player -> {
                        player.broadcastBreakEvent(context.getHand());
                    });
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }
}