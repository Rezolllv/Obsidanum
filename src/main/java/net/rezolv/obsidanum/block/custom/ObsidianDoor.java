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
import net.rezolv.obsidanum.block.custom.obsidian_door.DoorPart;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.sound.SoundsObs;


public class ObsidianDoor extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final EnumProperty<DoorPart> PART = EnumProperty.create("part", DoorPart.class);
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 4);

    public ObsidianDoor(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(ACTIVE, false)
                .setValue(PART, DoorPart.C));
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, ACTIVE, PART);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Убираем getOpposite() для правильного направления
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        if (!level.isClientSide && !state.getValue(ACTIVE)) {
            Direction facing = state.getValue(FACING);
            BlockPos basePos = pos; // Используем текущую позицию как базовую
                createDoorStructure(level, basePos, facing);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case NORTH -> Block.box(0, 0, 0, 16, 16, 4);
            case SOUTH -> Block.box(0, 0, 12, 16, 16, 16);
            case EAST -> Block.box(12, 0, 0, 16, 16, 16);
            case WEST -> Block.box(0, 0, 0, 4, 16, 16);
            default -> SHAPE;
        };
    }
    private boolean canFormDoor(Level level, BlockPos basePos, Direction facing) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                BlockPos checkPos = basePos.relative(facing.getClockWise(), dx).above(dy);
                if (!level.getBlockState(checkPos).canBeReplaced()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void createDoorStructure(Level level, BlockPos basePos, Direction facing) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                BlockPos placePos = basePos.relative(facing.getClockWise(), dx).above(dy);
                DoorPart part = determinePart(dx, dy);
                level.setBlock(placePos, this.defaultBlockState()
                        .setValue(FACING, facing)
                        .setValue(ACTIVE, true)
                        .setValue(PART, part), 3);
            }
        }
    }
    private DoorPart determinePart(int dx, int dy) {
        if (dy == 0) { // Нижний ряд
            switch (dx) {
                case -1:
                    return DoorPart.BL;
                case 0:
                    return DoorPart.BC;
                case 1:
                    return DoorPart.BR;
                default:
                    return DoorPart.C;
            }
        } else if (dy == 1) { // Средний ряд
            switch (dx) {
                case -1:
                    return DoorPart.CL;
                case 0:
                    return DoorPart.C;
                case 1:
                    return DoorPart.CR;
                default:
                    return DoorPart.C;
            }
        } else if (dy == 2) { // Верхний ряд
            switch (dx) {
                case -1:
                    return DoorPart.TL;
                case 0:
                    return DoorPart.TC;
                case 1:
                    return DoorPart.TR;
                default:
                    return DoorPart.C;
            }
        } else {
            return DoorPart.C;
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

        return InteractionResult.PASS;
    }

}