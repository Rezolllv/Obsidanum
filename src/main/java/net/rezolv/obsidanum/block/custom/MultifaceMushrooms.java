package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

public class MultifaceMushrooms extends MultifaceBlock implements BonemealableBlock {
    public static final IntegerProperty GROWTH_STAGE = IntegerProperty.create("growth_stage", 0, 1);
    public static final BooleanProperty HAS_UP = BooleanProperty.create("has_up");
    public static final BooleanProperty HAS_DOWN = BooleanProperty.create("has_down");
    public static final BooleanProperty HAS_NORTH = BooleanProperty.create("has_north");
    public static final BooleanProperty HAS_SOUTH = BooleanProperty.create("has_south");
    public static final BooleanProperty HAS_EAST = BooleanProperty.create("has_east");
    public static final BooleanProperty HAS_WEST = BooleanProperty.create("has_west");

    private static final VoxelShape MUSHROOM_SHAPE_DOWN = Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_UP = Block.box(0.0, 11.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_NORTH = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 5.0);
    private static final VoxelShape MUSHROOM_SHAPE_SOUTH = Block.box(0.0, 0.0, 11.0, 16.0, 16.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_EAST = Block.box(11.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape MUSHROOM_SHAPE_WEST = Block.box(0.0, 0.0, 0.0, 5.0, 16.0, 16.0);

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
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(GROWTH_STAGE) == 0; // Блок случайно обновляется только на первой стадии роста
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) < 50) { // Вероятность роста 50%
            grow(level, pos, state);
        }
    }

    public void grow(ServerLevel world, BlockPos pos, BlockState state) {
        int currentStage = state.getValue(GROWTH_STAGE);
        if (currentStage < 1 )  {
            // Увеличиваем стадию роста
            world.setBlock(pos, state.setValue(GROWTH_STAGE, currentStage + 1), 2);
        }
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape baseShape = super.getShape(state, world, pos, context);

        // Проверяем, что грибы могут расти только на верхней грани
        if (state.getValue(HAS_DOWN) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_DOWN);
        }
       else if (state.getValue(HAS_UP) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_UP);
        }
        else if (state.getValue(HAS_NORTH) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_NORTH);
        }
        else if (state.getValue(HAS_SOUTH) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_SOUTH);
        }
        else if (state.getValue(HAS_EAST) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_EAST);
        }
        else if (state.getValue(HAS_WEST) && state.getValue(GROWTH_STAGE) == 1) {
            return Shapes.or(baseShape, MUSHROOM_SHAPE_WEST);
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
            }
           else if (hasFace(newState, Direction.UP) && !newState.getValue(HAS_UP)
                    && !hasFace(newState, Direction.NORTH)
                    && !hasFace(newState, Direction.SOUTH)
                    && !hasFace(newState, Direction.EAST)
                    && !hasFace(newState, Direction.WEST)
                    && !hasFace(newState, Direction.DOWN)
            ) {
                newState = newState.setValue(HAS_UP, true);
            }
            else if (hasFace(newState, Direction.NORTH) && !newState.getValue(HAS_NORTH)
                    && !hasFace(newState, Direction.DOWN)
                    && !hasFace(newState, Direction.SOUTH)
                    && !hasFace(newState, Direction.EAST)
                    && !hasFace(newState, Direction.WEST)
                    && !hasFace(newState, Direction.UP)
            ) {
                newState = newState.setValue(HAS_NORTH, true);
            }
            else if (hasFace(newState, Direction.SOUTH) && !newState.getValue(HAS_SOUTH)
                    && !hasFace(newState, Direction.NORTH)
                    && !hasFace(newState, Direction.DOWN)
                    && !hasFace(newState, Direction.EAST)
                    && !hasFace(newState, Direction.WEST)
                    && !hasFace(newState, Direction.UP)
            ) {
                newState = newState.setValue(HAS_SOUTH, true);
            }
            else if (hasFace(newState, Direction.EAST) && !newState.getValue(HAS_EAST)
                    && !hasFace(newState, Direction.NORTH)
                    && !hasFace(newState, Direction.SOUTH)
                    && !hasFace(newState, Direction.DOWN)
                    && !hasFace(newState, Direction.WEST)
                    && !hasFace(newState, Direction.UP)
            ) {
                newState = newState.setValue(HAS_EAST, true);
            }
            else if (hasFace(newState, Direction.WEST) && !newState.getValue(HAS_WEST)
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
        return blockState.getValue(GROWTH_STAGE) == 0;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        // Вероятность успеха костной муки возможна только на первой стадии роста
        return blockState.getValue(GROWTH_STAGE) == 0;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        grow(serverLevel, blockPos, blockState); // Применяем рост при использовании костной муки
    }
}