package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlameBannerBaggel extends Block {
    public FlameBannerBaggel(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TOP, false)
                .setValue(TOP_BELOW, false)
                .setValue(MIDDLE, false)
                .setValue(BOTTOM, false));
    }
    // VoxelShapes for each direction
    private static final VoxelShape SHAPE_NORTH = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape SHAPE_WEST = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SHAPE_EAST = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        switch (state.getValue(FACING)) {
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            case EAST:
                return SHAPE_EAST;
            default:
                return SHAPE_NORTH;
        }
    }
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty TOP_BELOW = BooleanProperty.create("top_below");
    public static final BooleanProperty MIDDLE = BooleanProperty.create("middle");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TOP, TOP_BELOW, MIDDLE, BOTTOM);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState belowState = level.getBlockState(pos.below());
        Block thisBlock = this;

        boolean hasBlockBelow = belowState.is(thisBlock);

        if (hasBlockBelow) {
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(TOP, true)
                    .setValue(TOP_BELOW, true)
                    .setValue(MIDDLE, false)
                    .setValue(BOTTOM, false);
        } else {
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(TOP, true)
                    .setValue(TOP_BELOW, false)
                    .setValue(MIDDLE, false)
                    .setValue(BOTTOM, false);
        }
    }
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Block thisBlock = this;
        boolean hasBlockAbove = level.getBlockState(pos.above()).is(thisBlock);
        boolean hasBlockBelow = level.getBlockState(pos.below()).is(thisBlock);

        // Если есть блок выше и ниже, это средний блок
        if (hasBlockAbove && hasBlockBelow) {
            return state.setValue(MIDDLE, true)
                    .setValue(TOP, false)
                    .setValue(TOP_BELOW, false)
                    .setValue(BOTTOM, false);
        }
        // Если есть только блок выше, это нижний блок
        else if (hasBlockAbove) {
            return state.setValue(BOTTOM, true)
                    .setValue(MIDDLE, false)
                    .setValue(TOP, false)
                    .setValue(TOP_BELOW, false);
        }
        // Если есть только блок ниже, это верхний блок с состоянием "top-below"
        else if (hasBlockBelow) {
            return state.setValue(TOP, true)
                    .setValue(TOP_BELOW, true)
                    .setValue(MIDDLE, false)
                    .setValue(BOTTOM, false);
        }
        // Если нет блоков ни сверху, ни снизу, это обычный верхний блок
        else {
            return state.setValue(TOP, true)
                    .setValue(TOP_BELOW, false)
                    .setValue(MIDDLE, false)
                    .setValue(BOTTOM, false);
        }
    }
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

}
