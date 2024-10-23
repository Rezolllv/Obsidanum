package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class MultifaceMushrooms extends MultifaceBlock implements BonemealableBlock {
    public static final IntegerProperty GROWTH_STAGE = IntegerProperty.create("growth_stage", 0, 1);

    // Хитбоксы для второй стадии роста
    private static final VoxelShape UP_AABB_STAGE_2 = Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape DOWN_AABB_STAGE_2 = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    private static final VoxelShape WEST_AABB_STAGE_2 = Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 16.0);
    private static final VoxelShape EAST_AABB_STAGE_2 = Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape NORTH_AABB_STAGE_2 = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 2.0);
    private static final VoxelShape SOUTH_AABB_STAGE_2 = Block.box(0.0, 0.0, 14.0, 16.0, 16.0, 16.0);

    public MultifaceMushrooms(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(GROWTH_STAGE, 0)); // Начальная стадия
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return null; // Пока не используется
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(GROWTH_STAGE);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true; // Включаем случайные обновления для роста
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(100) < 100) { // Вероятность роста 100%
            grow(pLevel, pPos, pState);
        }
    }

    public void grow(ServerLevel world, BlockPos pos, BlockState state) {
        int currentStage = state.getValue(GROWTH_STAGE);
        if (currentStage < 1) {
            world.setBlock(pos, state.setValue(GROWTH_STAGE, currentStage + 1), 2); // Увеличиваем стадию
        }
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(GROWTH_STAGE) == 0; // Костная мука работает только на первой стадии
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean b) {
        // Костная мука работает только на первой стадии роста
        return blockState.getValue(GROWTH_STAGE) == 0;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        // Успех костной муки возможен только на первой стадии роста
        return blockState.getValue(GROWTH_STAGE) == 0;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        grow(serverLevel, blockPos, blockState); // Применяем рост при использовании костной муки
    }

    // Метод для возврата формы хитбокса
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        int stage = state.getValue(GROWTH_STAGE);

        if (stage == 1) {
            // На второй стадии возвращаем увеличенные хитбоксы на основе стороны установки блока
            for (Direction direction : Direction.values()) {
                if (MultifaceBlock.hasFace(state, direction)) {
                    return switch (direction) {
                        case UP -> UP_AABB_STAGE_2;
                        case DOWN -> DOWN_AABB_STAGE_2;
                        case WEST -> WEST_AABB_STAGE_2;
                        case EAST -> EAST_AABB_STAGE_2;
                        case NORTH -> NORTH_AABB_STAGE_2;
                        case SOUTH -> SOUTH_AABB_STAGE_2;
                        default -> Shapes.empty();
                    };
                }
            }
        }
        // На первой стадии стандартные хитбоксы
        return super.getShape(state, level, pos, context);
    }
}