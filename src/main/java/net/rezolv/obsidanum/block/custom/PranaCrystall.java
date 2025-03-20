package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.block.entity.HammerForgeEntity;
import net.rezolv.obsidanum.block.entity.PranaCrystallEntity;
import org.jetbrains.annotations.Nullable;

public class PranaCrystall extends BaseEntityBlock {
    // Используем DirectionProperty для всех направлений
    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public PranaCrystall(Properties pProperties) {
        super(pProperties);
        // Устанавливаем направление по умолчанию (например, NORTH)
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING); // Добавляем свойство FACING в определение состояния блока
    }
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Получаем направление, в котором игрок устанавливает блок
        Direction direction = context.getClickedFace();
        // Если игрок устанавливает блок на верхнюю или нижнюю грань, корректируем направление
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return this.defaultBlockState().setValue(FACING, direction);
        } else {
            // Для горизонтальных направлений используем противоположное направление
            return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        // Вращаем блок в зависимости от направления
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        // Отражаем блок в зависимости от направления
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
    public static BlockPos fillWithLights(BlockPos current, LevelAccessor level) {
        current = current.below();
        while (current.getY() > level.getMinBuildHeight() && LightPranaCrystall.testSkylight(level, level.getBlockState(current), current)) {
            if (level.getBlockState(current).isAir()) {
                level.setBlock(current, BlocksObs.LIGHT_PRANA_CRYSTALL.get().defaultBlockState(), 3);
            }
            current = current.below();
        }
        return current;
    }
    public static boolean testSkylight(LevelReader levelReader, BlockState blockState, BlockPos current) {
        return blockState.propagatesSkylightDown(levelReader, current);
    }
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        fillWithLights(blockPos, levelAccessor);
        return super.updateShape(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
        fillWithLights(pos, serverLevel);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        fillWithLights(pos, level);
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity livingEntity, ItemStack stack) {
        fillWithLights(pos, level);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PranaCrystallEntity(blockPos, blockState);
    }
}
