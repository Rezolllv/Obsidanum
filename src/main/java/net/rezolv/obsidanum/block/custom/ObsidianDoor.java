package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class ObsidianDoor extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    private static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 6, 16, 16, 10);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 6, 16, 16, 10);
    private static final VoxelShape SHAPE_EAST = Block.box(6, 0, 0, 10, 16, 16);
    private static final VoxelShape SHAPE_WEST = Block.box(6, 0, 0, 10, 16, 16);

    public ObsidianDoor(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, Part.CENTER)
                .setValue(ACTIVE, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case EAST:
                return SHAPE_EAST;
            case WEST:
                return SHAPE_WEST;
            default:
                return Shapes.block();
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, ACTIVE);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Direction facing = state.getValue(FACING);

            // Iterate through all parts of the door
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    BlockPos partPos = getPartPos(pos, facing, x, y);
                    BlockState partState = level.getBlockState(partPos);

                    // Remove the part if it is part of this door
                    if (partState.getBlock() == this) {
                        level.destroyBlock(partPos, false);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(PART) == Part.CENTER && state.getValue(ACTIVE) == false && player.getItemInHand(hand).getItem() == Items.STICK) {
            world.setBlock(pos, state.setValue(ACTIVE, true), 3);
            if (!player.isCreative()) {
                player.getItemInHand(hand).shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        // Place the entire 3x3 structure horizontally
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                BlockPos partPos = getPartPos(pos, facing, x, y);
                Part part = Part.getPart(x, y);
                BlockState partState = this.defaultBlockState()
                        .setValue(FACING, facing)
                        .setValue(PART, part)
                        .setValue(ACTIVE, part == Part.CENTER ? true : false); // Set ACTIVE to true for CENTER part
                level.setBlock(partPos, partState, 3);
                level.sendBlockUpdated(partPos, partState, partState, 3);
            }
        }
        return this.defaultBlockState().setValue(FACING, facing).setValue(PART, Part.CENTER).setValue(ACTIVE, false);
    }

    private BlockPos getPartPos(BlockPos pos, Direction facing, int x, int y) {
        switch (facing) {
            case NORTH:
                return pos.offset(x, y, 0);
            case SOUTH:
                return pos.offset(-x, y, 0);
            case EAST:
                return pos.offset(0, y, x);
            case WEST:
                return pos.offset(0, y, -x);
            default:
                return pos;
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    public enum Part implements StringRepresentable {
        BOTTOM_RIGHT,
        BOTTOM_CENTER,
        BOTTOM_LEFT,
        CENTER_RIGHT,
        CENTER,
        CENTER_LEFT,
        TOP_RIGHT,
        TOP_CENTER,
        TOP_LEFT;

        public static Part getPart(int x, int y) {
            if (x == -1 && y == -1) return BOTTOM_RIGHT;
            if (x == 0 && y == -1) return BOTTOM_CENTER;
            if (x == 1 && y == -1) return BOTTOM_LEFT;
            if (x == -1 && y == 0) return CENTER_RIGHT;
            if (x == 0 && y == 0) return CENTER;
            if (x == 1 && y == 0) return CENTER_LEFT;
            if (x == -1 && y == 1) return TOP_RIGHT;
            if (x == 0 && y == 1) return TOP_CENTER;
            if (x == 1 && y == 1) return TOP_LEFT;
            return CENTER;
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}