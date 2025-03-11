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
                .setValue(PART, DoorPart.BC));
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

        if (!level.isClientSide && state.getValue(PART) == DoorPart.BC) {
            Direction facing = state.getValue(FACING);
                createDoorStructure(level, pos, facing);

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

    private void createDoorStructure(Level level, BlockPos basePos, Direction facing) {
        // Создаем 3x3 структуру относительно базового блока BC
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                BlockPos placePos = basePos.relative(facing.getClockWise(), dx).above(dy);

                DoorPart part = determinePart(dx, dy);
                if (part == DoorPart.BC) continue; // Пропускаем базовый блок

                level.setBlock(placePos, this.defaultBlockState()
                        .setValue(FACING, facing)
                        .setValue(PART, part), 3);
            }
        }
    }

    private DoorPart determinePart(int dx, int dy) {
        return switch (dy) {
            case 0 -> switch (dx) { // Нижний ряд
                case -1 -> DoorPart.BL;
                case 0 -> DoorPart.BC;
                case 1 -> DoorPart.BR;
                default -> DoorPart.BC;
            };
            case 1 -> switch (dx) { // Средний ряд
                case -1 -> DoorPart.CL;
                case 0 -> DoorPart.C;
                case 1 -> DoorPart.CR;
                default -> DoorPart.C;
            };
            case 2 -> switch (dx) { // Верхний ряд
                case -1 -> DoorPart.TL;
                case 0 -> DoorPart.TC;
                case 1 -> DoorPart.TR;
                default -> DoorPart.TC;
            };
            default -> DoorPart.BC;
        };
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
        // Логика открытия/закрытия двери
        DoorPart part = state.getValue(PART);

        if (player.getItemInHand(hand).getItem() == ItemsObs.OBSIDIAN_KEY.get()) {
            if (part == DoorPart.C && !state.getValue(ACTIVE)) {
                player.getItemInHand(hand).shrink(1);
                // Логика для открытия/закрытия двери или другие действия
                world.setBlock(pos, state.setValue(ACTIVE, true), 3);
                return InteractionResult.SUCCESS;
            }
            if (part == DoorPart.CR && !state.getValue(ACTIVE)) {
                player.getItemInHand(hand).shrink(1);
                // Логика для открытия/закрытия двери или другие действия
                world.setBlock(pos, state.setValue(ACTIVE, true), 3);
                return InteractionResult.SUCCESS;
            }
            if (part == DoorPart.CL && !state.getValue(ACTIVE)) {
                player.getItemInHand(hand).shrink(1);
                // Логика для открытия/закрытия двери или другие действия
                world.setBlock(pos, state.setValue(ACTIVE, true), 3);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}