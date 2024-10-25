package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rezolv.obsidanum.item.ItemsObs;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

public class MultifaceMushrooms extends MultifaceBlock implements BonemealableBlock {
    public static final IntegerProperty GROWTH_STAGE = IntegerProperty.create("growth_stage", 0, 2);
    public static final BooleanProperty HAS_UP = BooleanProperty.create("has_up");
    public static final BooleanProperty HAS_DOWN = BooleanProperty.create("has_down");
    public static final BooleanProperty HAS_NORTH = BooleanProperty.create("has_north");
    public static final BooleanProperty HAS_SOUTH = BooleanProperty.create("has_south");
    public static final BooleanProperty HAS_EAST = BooleanProperty.create("has_east");
    public static final BooleanProperty HAS_WEST = BooleanProperty.create("has_west");

    private static final VoxelShape MUSHROOM_SHAPE_DOWN = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_UP = Block.box(0.0, 4.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_NORTH = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 9.0);
    private static final VoxelShape MUSHROOM_SHAPE_SOUTH = Block.box(0.0, 0.0, 7.0, 16.0, 16.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_EAST = Block.box(7.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_WEST = Block.box(0.0, 0.0, 0.0, 9.0, 16.0, 16.0);

    private static final VoxelShape MUSHROOM_SHAPE_DOWN_1 = Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_UP_1 = Block.box(0.0, 11.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_NORTH_1 = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 5.0);
    private static final VoxelShape MUSHROOM_SHAPE_SOUTH_1 = Block.box(0.0, 0.0, 11.0, 16.0, 16.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_EAST_1 = Block.box(11.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_WEST_1 = Block.box(0.0, 0.0, 0.0, 5.0, 16.0, 16.0);

    public MultifaceMushrooms(Properties properties) {
        super(properties);
        // Инициализация начального состояния блока
        this.registerDefaultState(this.defaultBlockState()
                .setValue(GROWTH_STAGE, 0)
                .setValue(HAS_UP, false)
                .setValue(HAS_DOWN, false)
                .setValue(HAS_NORTH, false)
                .setValue(HAS_SOUTH, false)
                .setValue(HAS_EAST, false)
                .setValue(HAS_WEST, false)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            if (player.getItemInHand(hand).getItem() instanceof ShearsItem) {
                // Проверяем стадию роста
                if (state.getValue(GROWTH_STAGE) == 2) {
                    // Сбрасываем стадию роста до 0
                    level.setBlock(pos, state.setValue(GROWTH_STAGE, 0), 2);

                    // Выпадение предмета GLOOMY_MUSHROOM
                    popResource(level, pos, ItemsObs.GLOOMY_MUSHROOM.get().getDefaultInstance());
                    ItemStack itemStack = player.getItemInHand(hand);

                    // Добавляем повреждение ножницам
                    itemStack.hurtAndBreak(1, player, (p) -> {
                        p.broadcastBreakEvent(hand);
                    });
                    // Звук стрижки
                    level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

                    // Движение руки
                    player.swing(hand, true);

                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return null; // Пока не используется
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        // Регистрируем свойства блока
        builder.add(GROWTH_STAGE, HAS_DOWN, HAS_UP, HAS_NORTH, HAS_SOUTH, HAS_EAST, HAS_WEST);
    }


    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int growthStage = state.getValue(GROWTH_STAGE);
        if (random.nextInt(100) < 1) {
            grow(level, pos, state);
        }
        if (level.isDay() && level.canSeeSky(pos)) {
            if (random.nextInt(100) < 30) {
                if (growthStage == 0) {
                    // На первой стадии роста блок ломается при дневном свете
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
                    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                if (growthStage == 1) {
                    // На второй стадии роста блок возвращается к первой стадии
                    BlockState newState = state.setValue(GROWTH_STAGE, 0);
                    level.setBlock(pos, newState, 3);

                    // Воспроизводим эффекты
                    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
                }
                if (growthStage == 2) {
                    // На второй стадии роста блок возвращается к первой стадии
                    BlockState newState = state.setValue(GROWTH_STAGE, 1);
                    level.setBlock(pos, newState, 3);

                    // Воспроизводим эффекты
                    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }
    }

    public void grow(ServerLevel world, BlockPos pos, BlockState state) {
        int currentStage = state.getValue(GROWTH_STAGE);
        if (currentStage < 2) {
            // Увеличиваем стадию роста
            world.setBlock(pos, state.setValue(GROWTH_STAGE, currentStage + 1), 2);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape baseShape = super.getShape(state, world, pos, context);

        // Проверяем, что грибы могут расти только на верхней грани
        if (state.getValue(HAS_DOWN) && state.getValue(GROWTH_STAGE) == 2) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_DOWN);
        } else if (state.getValue(HAS_UP) && state.getValue(GROWTH_STAGE) == 2) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_UP);
        } else if (state.getValue(HAS_NORTH) && state.getValue(GROWTH_STAGE) == 2) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_NORTH);
        } else if (state.getValue(HAS_SOUTH) && state.getValue(GROWTH_STAGE) == 2) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_SOUTH);
        } else if (state.getValue(HAS_EAST) && state.getValue(GROWTH_STAGE) == 2) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_EAST);
        } else if (state.getValue(HAS_WEST) && state.getValue(GROWTH_STAGE) == 2) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_WEST);
        } else if (state.getValue(HAS_DOWN) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_DOWN_1);
        } else if (state.getValue(HAS_UP) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_UP_1);
        } else if (state.getValue(HAS_NORTH) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_NORTH_1);
        } else if (state.getValue(HAS_SOUTH) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_SOUTH_1);
        } else if (state.getValue(HAS_EAST) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_EAST_1);
        } else if (state.getValue(HAS_WEST) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_WEST_1);
        }

        return baseShape;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        // Логика обновления состояния блока
        if (state.getValue(HAS_DOWN)) {
            // Здесь можно добавить логику для изменения хитбокса гриба в зависимости от стороны
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(GROWTH_STAGE) == 0; // Костная мука работает только на первой стадии
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        BlockState currentState = level.getBlockState(pos);

        // Попробуем найти подходящее направление для установки блока
        BlockState newState = Arrays.stream(pContext.getNearestLookingDirections())
                .map(direction -> this.getStateForPlacement(currentState, level, pos, direction))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        // Если новое состояние не null и оно не имеет установленного HAS_DOWN
        if (newState != null) {
            // Устанавливаем HAS_DOWN в true, если направление "down" (вниз) было выбрано и HAS_DOWN не установлено
            if (hasFace(newState, Direction.DOWN) && !newState.getValue(HAS_DOWN)
                    && !hasFace(newState, Direction.NORTH)
                    && !hasFace(newState, Direction.SOUTH)
                    && !hasFace(newState, Direction.EAST)
                    && !hasFace(newState, Direction.WEST)
                    && !hasFace(newState, Direction.UP)
            ) {
                newState = newState.setValue(HAS_DOWN, true);
            } else if (hasFace(newState, Direction.UP) && !newState.getValue(HAS_UP)
                    && !hasFace(newState, Direction.NORTH)
                    && !hasFace(newState, Direction.SOUTH)
                    && !hasFace(newState, Direction.EAST)
                    && !hasFace(newState, Direction.WEST)
                    && !hasFace(newState, Direction.DOWN)
            ) {
                newState = newState.setValue(HAS_UP, true);
            } else if (hasFace(newState, Direction.NORTH) && !newState.getValue(HAS_NORTH)
                    && !hasFace(newState, Direction.DOWN)
                    && !hasFace(newState, Direction.SOUTH)
                    && !hasFace(newState, Direction.EAST)
                    && !hasFace(newState, Direction.WEST)
                    && !hasFace(newState, Direction.UP)
            ) {
                newState = newState.setValue(HAS_NORTH, true);
            } else if (hasFace(newState, Direction.SOUTH) && !newState.getValue(HAS_SOUTH)
                    && !hasFace(newState, Direction.NORTH)
                    && !hasFace(newState, Direction.DOWN)
                    && !hasFace(newState, Direction.EAST)
                    && !hasFace(newState, Direction.WEST)
                    && !hasFace(newState, Direction.UP)
            ) {
                newState = newState.setValue(HAS_SOUTH, true);
            } else if (hasFace(newState, Direction.EAST) && !newState.getValue(HAS_EAST)
                    && !hasFace(newState, Direction.NORTH)
                    && !hasFace(newState, Direction.SOUTH)
                    && !hasFace(newState, Direction.DOWN)
                    && !hasFace(newState, Direction.WEST)
                    && !hasFace(newState, Direction.UP)
            ) {
                newState = newState.setValue(HAS_EAST, true);
            } else if (hasFace(newState, Direction.WEST) && !newState.getValue(HAS_WEST)
                    && !hasFace(newState, Direction.NORTH)
                    && !hasFace(newState, Direction.SOUTH)
                    && !hasFace(newState, Direction.EAST)
                    && !hasFace(newState, Direction.DOWN)
                    && !hasFace(newState, Direction.UP)
            ) {
                newState = newState.setValue(HAS_WEST, true);
            }
        }

        return newState;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean b) {
        // Костная мука работает только на первой стадии роста
        return blockState.getValue(GROWTH_STAGE) == 0 || blockState.getValue(GROWTH_STAGE) == 1;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        // Вероятность успеха костной муки возможна только на первой стадии роста
        return blockState.getValue(GROWTH_STAGE) == 0 || blockState.getValue(GROWTH_STAGE) == 1 && randomSource.nextInt(100) < 25;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        grow(serverLevel, blockPos, blockState); // Применяем рост при использовании костной муки
    }
}