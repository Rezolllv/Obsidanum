package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rezolv.obsidanum.block.BlocksObs;


public class ObsidianForge extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public ObsidianForge(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Получаем позицию блока, где происходит установка
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var direction = context.getHorizontalDirection();

        // Проверяем, достаточно ли места для мультиблока (3x3x2)
        if (isSpaceAvailable(level, pos, direction)) {
            // Если места достаточно, ставим центральный блок с правильным направлением
            return this.defaultBlockState().setValue(FACING, direction.getOpposite());
        }

        // Если места недостаточно, возвращаем null, блок не будет поставлен
        return null;
    }


    // Проверка пространства для мультиблока
    private boolean isSpaceAvailable(Level level, BlockPos pos, Direction direction) {
        // Проверяем все блоки в области 3x3x2
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 0; y < 2; y++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    if (!level.getBlockState(checkPos).isAir()) {
                        return false; // Если хотя бы один блок не воздух, места нет
                    }
                }
            }
        }
        return true; // Места достаточно
    }
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        Direction facing = state.getValue(FACING);

        // Определение смещений для вспомогательных блоков в зависимости от направления
        BlockPos[] auxiliaryPositions = switch (facing) {
            case NORTH -> new BlockPos[]{
                    pos.offset(1, 0, 0),  // справа от центрального
                    pos.offset(-1, 0, 0), // слева от центрального
                    pos.offset(0, 0, 1),  // сзади от центрального
                    pos.offset(1, 0, 1),  // справа сзади
                    pos.offset(-1, 0, 1), // слева сзади
                    pos.offset(0, 1, 1),  // сверху центрального
                    pos.offset(1, 0, -1) // спереди от левого вспомогательного
            };
            case SOUTH -> new BlockPos[]{
                    pos.offset(-1, 0, 0),  // справа (если смотреть в сторону South)
                    pos.offset(1, 0, 0),   // слева
                    pos.offset(0, 0, -1),  // сзади
                    pos.offset(-1, 0, -1), // справа сзади
                    pos.offset(1, 0, -1),  // слева сзади
                    pos.offset(0, 1, -1),   // сверху центрального
                    pos.offset(-1, 0, 1)    // спереди от левого вспомогательного
            };
            case EAST -> new BlockPos[]{
                    pos.offset(0, 0, 1),   // справа
                    pos.offset(0, 0, -1),  // слева
                    pos.offset(-1, 0, 0),  // сзади
                    pos.offset(-1, 0, 1),  // справа сзади
                    pos.offset(-1, 0, -1), // слева сзади
                    pos.offset(-1, 1, 0),   // сверху центрального
                    pos.offset(1, 0, 1)    // спереди от левого вспомогательного
            };
            case WEST -> new BlockPos[]{
                    pos.offset(0, 0, -1),  // справа
                    pos.offset(0, 0, 1),   // слева
                    pos.offset(1, 0, 0),   // сзади
                    pos.offset(1, 0, -1),  // справа сзади
                    pos.offset(1, 0, 1),   // слева сзади
                    pos.offset(1, 1, 0),   // сверху центрального
                    pos.offset(-1, 0, -1)  // спереди от левого вспомогательного
            };
            default -> new BlockPos[]{};
        };

        // Установка вспомогательных блоков
        for (BlockPos offsetPos : auxiliaryPositions) {
            if (!offsetPos.equals(pos)) {
                level.setBlock(offsetPos, BlocksObs.ABSTRACT_OBSIDIAN_FORGE.get().defaultBlockState(), 3); // Устанавливаем вспомогательный блок
            }
        }
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            // Удаление всех вспомогательных блоков
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = 0; y < 2; y++) {
                        BlockPos offsetPos = pos.offset(x, y, z);
                        if (!offsetPos.equals(pos)) {
                            level.destroyBlock(offsetPos, false);
                        }
                    }
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
}
