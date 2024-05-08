package net.rezolv.obsidanum.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.rezolv.obsidanum.item.ItemsObs;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class DrillingCrystallizer extends Item {
    private static final Random RANDOM = new Random();

    public DrillingCrystallizer(Properties properties) {
        super(properties);
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
            level.sendParticles(ParticleTypes.SMOKE, pos.getX(), pos.getY(), pos.getZ(), 10, 0.1D, 0.1D, 0.1D, 0.0D);
            if (RANDOM.nextInt(100) < 30) {
                Block.popResource(level, pos, new ItemStack(ItemsObs.OBSIDIAN_TEAR.get()));
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
                Blocks.ANCIENT_DEBRIS,
                Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
                Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
                Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE
        };

        for (Block ore : ores) {
            if (blockState.is(ore)) {
                // Воспроизведение звука шипения и частиц дыма
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.sendParticles(ParticleTypes.SMOKE, pos.getX(), pos.getY(), pos.getZ(), 10, 0.1D, 0.1D, 0.1D, 0.0D);

// Удаление блока руды и всех прилегающих блоков руды
                Queue<BlockPos> queue = new LinkedList<>();
                queue.add(pos);
                while (!queue.isEmpty()) {
                    BlockPos currentPos = queue.poll();
                    BlockState currentBlockState = level.getBlockState(currentPos);
                    if (currentBlockState.is(ore)) {
                        level.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 3);

                        // Выпадение от 1 до 3 кристаллизированных руд
                        int itemsToDrop = RANDOM.nextInt(3) + 1; // Выпадает от 1 до 3 предметов
                        int itemsToDropSix = RANDOM.nextInt(6) + 1; // Выпадает от 1 до 6 предметов
                        for (int i = 0; i < itemsToDrop; i++) {
                            // Выбор кристаллизированной руды в зависимости от типа руды
                            ItemStack crystallizedOre;
                            if (ore == Blocks.IRON_ORE || ore == Blocks.DEEPSLATE_IRON_ORE) {
                                crystallizedOre = new ItemStack(ItemsObs.CRYSTALLIZED_IRON_ORE.get());
                            } else if (ore == Blocks.GOLD_ORE || ore == Blocks.DEEPSLATE_GOLD_ORE) {
                                crystallizedOre = new ItemStack(ItemsObs.CRYSTALLIZED_GOLD_ORE.get());
                            } else if (ore == Blocks.COPPER_ORE || ore == Blocks.DEEPSLATE_COPPER_ORE) {
                                crystallizedOre = new ItemStack(ItemsObs.CRYSTALLIZED_COPPER_ORE.get());
                            } else if (ore == Blocks.DIAMOND_ORE || ore == Blocks.DEEPSLATE_DIAMOND_ORE) {
                                crystallizedOre = new ItemStack(Items.DIAMOND);
                            } else if (ore == Blocks.EMERALD_ORE || ore == Blocks.DEEPSLATE_EMERALD_ORE) {
                                crystallizedOre = new ItemStack(Items.EMERALD);
                            } else if (ore == Blocks.ANCIENT_DEBRIS ) {
                                crystallizedOre = new ItemStack(Items.NETHERITE_SCRAP);
                            } else {
                                // Здесь можно добавить обработку других типов руд
                                continue;
                            }
                            Block.popResource(level, currentPos, crystallizedOre);
                            level.sendParticles(ParticleTypes.SMOKE, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 10, 0.1D, 0.1D, 0.1D, 0.0D);

                        }
                        for (int i = 0; i < itemsToDropSix; i++) {
                            // Выбор кристаллизированной руды в зависимости от типа руды
                            ItemStack crystallizedOre;
                            if (ore == Blocks.COAL_ORE || ore == Blocks.DEEPSLATE_COAL_ORE) {
                                crystallizedOre = new ItemStack(Items.COAL);
                            } else if (ore == Blocks.LAPIS_ORE || ore == Blocks.DEEPSLATE_LAPIS_ORE) {
                                crystallizedOre = new ItemStack(Items.LAPIS_LAZULI);
                            } else if (ore == Blocks.REDSTONE_ORE || ore == Blocks.DEEPSLATE_REDSTONE_ORE) {
                                crystallizedOre = new ItemStack(Items.REDSTONE);
                            } else {
                                // Здесь можно добавить обработку других типов руд
                                continue;
                            }
                            Block.popResource(level, currentPos, crystallizedOre);
                            level.sendParticles(ParticleTypes.SMOKE, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 10, 0.1D, 0.1D, 0.1D, 0.0D);
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
                if (context.getPlayer()!= null) {
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