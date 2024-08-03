package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.sound.SoundsObs;


public class ObsidianDoor extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
    public static final BooleanProperty ACTIVE_1 = BooleanProperty.create("active_1");
    public static final BooleanProperty ACTIVE_2 = BooleanProperty.create("active_2");
    public static final BooleanProperty ACTIVE_3= BooleanProperty.create("active_3");
    public static final BooleanProperty ACTIVE_4 = BooleanProperty.create("active_4");
    public static final BooleanProperty ACTIVE_TOP = BooleanProperty.create("active_top");
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    private static final VoxelShape SHAPE_NORTH = Block.box(0, 0, 6, 16, 16, 10);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0, 0, 6, 16, 16, 10);
    private static final VoxelShape SHAPE_EAST = Block.box(6, 0, 0, 10, 16, 16);
    private static final VoxelShape SHAPE_WEST = Block.box(6, 0, 0, 10, 16, 16);

    public ObsidianDoor(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, Part.CENTER)
                .setValue(ACTIVE_1, false)
                .setValue(ACTIVE_2, false)
                .setValue(ACTIVE_3, false)
                .setValue(ACTIVE_4, false)
                .setValue(OPEN, false));
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(OPEN) ? Shapes.empty() : super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return !state.getValue(OPEN);
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
        builder.add(FACING, PART, ACTIVE_1, ACTIVE_2, ACTIVE_3, ACTIVE_4, ACTIVE_TOP, OPEN);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Direction facing = state.getValue(FACING);

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    BlockPos partPos = getPartPos(pos, facing, x, y);
                    BlockState partState = level.getBlockState(partPos);

                    if (partState.getBlock() == this) {
                        level.destroyBlock(partPos, false);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                BlockPos partPos = getPartPos(pos, facing, x, y);
                Part part = Part.getPart(x, y);
                BlockState partState = this.defaultBlockState().setValue(FACING, facing)
                        .setValue(PART, part)
                        .setValue(ACTIVE_1, false)
                        .setValue(ACTIVE_2, false)
                        .setValue(ACTIVE_3, false)
                        .setValue(ACTIVE_4, false)
                        .setValue(ACTIVE_TOP, false)
                        .setValue(OPEN, false);
                level.setBlock(partPos, partState, 3);
                level.sendBlockUpdated(partPos, partState, partState, 3);
            }
        }
        return this.defaultBlockState().setValue(FACING, facing)
                .setValue(PART, Part.CENTER)
                .setValue(ACTIVE_1, false)
                .setValue(ACTIVE_2, false)
                .setValue(ACTIVE_3, false)
                .setValue(ACTIVE_4, false)
                .setValue(ACTIVE_TOP, false)
                .setValue(OPEN, false);
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

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(PART) == Part.CENTER) {
            if (!state.getValue(ACTIVE_1) && player.getItemInHand(hand).getItem() == ItemsObs.OBSIDIAN_DOOR_KEY_1.get()) {
                world.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                world.setBlock(pos, state.setValue(ACTIVE_1, true), 3);
                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                }
                if (state.getValue(ACTIVE_4) && state.getValue(ACTIVE_2) && state.getValue(ACTIVE_3)) {
                    Direction facing = state.getValue(FACING);
                    BlockPos topCenterPos = getPartPos(pos, facing, 0, 1);
                    BlockState topCenterState = world.getBlockState(topCenterPos);
                    if (topCenterState.getBlock() == this && topCenterState.getValue(PART) == Part.TOP_CENTER && !topCenterState.getValue(ACTIVE_TOP)) {
                        world.setBlock(topCenterPos, topCenterState.setValue(ACTIVE_TOP, true), 3);
                        world.playSound(null, topCenterPos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                return InteractionResult.SUCCESS;
            }
           else if (!state.getValue(ACTIVE_2) && player.getItemInHand(hand).getItem() == ItemsObs.OBSIDIAN_DOOR_KEY_2.get()) {
                world.setBlock(pos, state.setValue(ACTIVE_2, true), 3);
                world.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                }
                if (state.getValue(ACTIVE_1) && state.getValue(ACTIVE_3) && state.getValue(ACTIVE_4)) {
                    Direction facing = state.getValue(FACING);
                    BlockPos topCenterPos = getPartPos(pos, facing, 0, 1);
                    BlockState topCenterState = world.getBlockState(topCenterPos);
                    if (topCenterState.getBlock() == this && topCenterState.getValue(PART) == Part.TOP_CENTER && !topCenterState.getValue(ACTIVE_TOP)) {
                        world.setBlock(topCenterPos, topCenterState.setValue(ACTIVE_TOP, true), 3);
                        world.playSound(null, topCenterPos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            else if (!state.getValue(ACTIVE_3) && player.getItemInHand(hand).getItem() == ItemsObs.OBSIDIAN_DOOR_KEY_3.get()) {
                world.setBlock(pos, state.setValue(ACTIVE_3, true), 3);
                world.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                }if (state.getValue(ACTIVE_1) && state.getValue(ACTIVE_2) && state.getValue(ACTIVE_4)) {
                    Direction facing = state.getValue(FACING);
                    BlockPos topCenterPos = getPartPos(pos, facing, 0, 1);
                    BlockState topCenterState = world.getBlockState(topCenterPos);
                    if (topCenterState.getBlock() == this && topCenterState.getValue(PART) == Part.TOP_CENTER && !topCenterState.getValue(ACTIVE_TOP)) {
                        world.setBlock(topCenterPos, topCenterState.setValue(ACTIVE_TOP, true), 3);
                        world.playSound(null, topCenterPos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            else if (!state.getValue(ACTIVE_4) && player.getItemInHand(hand).getItem() == ItemsObs.OBSIDIAN_DOOR_KEY_4.get()) {
                world.setBlock(pos, state.setValue(ACTIVE_4, true), 3);
                world.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                }if (state.getValue(ACTIVE_1) && state.getValue(ACTIVE_2) && state.getValue(ACTIVE_3)) {
                    Direction facing = state.getValue(FACING);
                    BlockPos topCenterPos = getPartPos(pos, facing, 0, 1);
                    BlockState topCenterState = world.getBlockState(topCenterPos);
                    if (topCenterState.getBlock() == this && topCenterState.getValue(PART) == Part.TOP_CENTER && !topCenterState.getValue(ACTIVE_TOP)) {
                        world.setBlock(topCenterPos, topCenterState.setValue(ACTIVE_TOP, true), 3);
                        world.playSound(null, topCenterPos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            else if (state.getValue(ACTIVE_1) && state.getValue(ACTIVE_2) && state.getValue(ACTIVE_3) && state.getValue(ACTIVE_4)) {
                // Set ACTIVE_TOP to true for the TOP_CENTER part
                Direction facing = state.getValue(FACING);
                BlockPos topCenterPos = getPartPos(pos, facing, 0, 1);
                BlockState topCenterState = world.getBlockState(topCenterPos);
                if (topCenterState.getBlock() == this && topCenterState.getValue(PART) == Part.TOP_CENTER && !topCenterState.getValue(ACTIVE_TOP)) {
                    world.setBlock(topCenterPos, topCenterState.setValue(ACTIVE_TOP, true), 3);
                    world.playSound(null, topCenterPos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                }

                boolean isOpen = state.getValue(OPEN);
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        BlockPos partPos = getPartPos(pos, facing, x, y);
                        BlockState partState = world.getBlockState(partPos);
                        if (partState.getBlock() == this) {
                            BlockState newState = partState.setValue(OPEN, !isOpen);
                            world.setBlock(partPos, newState, 3);
                        }
                    }
                }
                SoundEvent soundEvent = isOpen ? SoundsObs.CLOSE_OBSIDIAN_DOOR.get() : SoundsObs.OPEN_OBSIDIAN_DOOR.get();
                world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
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