package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class GloomyMycelium2 extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    // Хитбоксы для каждой стороны
    private static final VoxelShape FLAT_SHAPE_DOWN = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);  // Верх блока
    private static final VoxelShape FLAT_SHAPE_UP = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);     // Низ блока
    private static final VoxelShape FLAT_SHAPE_NORTH = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D); // Южная сторона
    private static final VoxelShape FLAT_SHAPE_SOUTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);   // Северная сторона
    private static final VoxelShape FLAT_SHAPE_WEST = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);  // Восточная сторона
    private static final VoxelShape FLAT_SHAPE_EAST = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);    // Западная сторона
    private static final VoxelShape FLAT_BWN = Shapes.or(FLAT_SHAPE_UP, FLAT_SHAPE_NORTH);
    private static final VoxelShape FLAT_BWS = Shapes.or(FLAT_SHAPE_UP, FLAT_SHAPE_SOUTH);
    private static final VoxelShape FLAT_BWE = Shapes.or(FLAT_SHAPE_UP, FLAT_SHAPE_EAST);
    private static final VoxelShape FLAT_BWW = Shapes.or(FLAT_SHAPE_UP, FLAT_SHAPE_WEST);
    public static final BooleanProperty BWN = BooleanProperty.create("b_w_n");  // Новое свойство
    public static final BooleanProperty BWS = BooleanProperty.create("b_w_s");  // Новое свойство
    public static final BooleanProperty BWE = BooleanProperty.create("b_w_e");  // Новое свойство
    public static final BooleanProperty BWW = BooleanProperty.create("b_w_w");  // Новое свойство

    public GloomyMycelium2(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(BWN, false)
                .setValue(BWE, false)
                .setValue(BWW, false)
                .setValue(BWS, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite())
                    .setValue(BWN, false)
                    .setValue(BWE, false)
                    .setValue(BWW, false)
                    .setValue(BWS, false); // Устанавливаем начальное значение

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BWN, BWS, BWE, BWW); // Добавляем новое состояние в блок
    }

    // Возвращаем соответствующий хитбокс в зависимости от стороны установки
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (state.getValue(BWN)) return FLAT_BWN;
        if (state.getValue(BWS)) return FLAT_BWS;
        if (state.getValue(BWE)) return FLAT_BWE;
        if (state.getValue(BWW)) return FLAT_BWW;
        switch (state.getValue(FACING)) {
            case DOWN:
                return FLAT_SHAPE_DOWN;
            case UP:
                return FLAT_SHAPE_UP;
            case NORTH:
                return FLAT_SHAPE_NORTH;
            case SOUTH:
                return FLAT_SHAPE_SOUTH;
            case WEST:
                return FLAT_SHAPE_WEST;
            case EAST:
                return FLAT_SHAPE_EAST;
            default:
                return FLAT_SHAPE_DOWN;
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING); // Направление блока
        BlockPos blockBelow = pos.relative(facing.getOpposite()); // Позиция блока, на который ставится грибница

        // Проверяем, является ли блок под грибницей твёрдым и может ли блок стоять на этой стороне
        return level.getBlockState(blockBelow).isFaceSturdy(level, blockBelow, facing);
    }
}