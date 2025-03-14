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
    public static final BooleanProperty ALL_ACTIVE = BooleanProperty.create("all_active");
    public static final EnumProperty<DoorPart> PART = EnumProperty.create("part", DoorPart.class);
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 4);

    public ObsidianDoor(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(ACTIVE, false)
                .setValue(ALL_ACTIVE, false)
                .setValue(PART, DoorPart.BC));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, ACTIVE, ALL_ACTIVE, PART);
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
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(OPEN)) {
            return Shapes.empty(); // Пустой коллайдер, если дверь открыта
        }

        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case NORTH -> Block.box(0, 0, 6, 16, 16, 10);
            case SOUTH -> Block.box(0, 0, 6, 16, 16, 10);
            case EAST -> Block.box(6, 0, 0, 10, 16, 16);
            case WEST -> Block.box(6, 0, 0, 10, 16, 16);
            default -> SHAPE;
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case NORTH -> Block.box(0, 0, 6, 16, 16, 10);
            case SOUTH -> Block.box(0, 0, 6, 16, 16, 10);
            case EAST -> Block.box(6, 0, 0, 10, 16, 16);
            case WEST -> Block.box(6, 0, 0, 10, 16, 16);
            default -> SHAPE;
        };
    }

    private void createDoorStructure(Level level, BlockPos basePos, Direction facing) {
        // Создаем 3x3 структуру относительно базового блока BC
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                BlockPos placePos = basePos.relative(facing.getClockWise(), dx).above(dy);

                // Пропускаем не воздушные блоки
                if (!level.getBlockState(placePos).isAir()) continue;

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
        DoorPart part = state.getValue(PART);
        Direction facing = state.getValue(FACING);

        if (player.getItemInHand(hand).getItem() == ItemsObs.OBSIDIAN_KEY.get()) {
            if ((part == DoorPart.C || part == DoorPart.CL || part == DoorPart.CR) && !state.getValue(ACTIVE)) {
                player.getItemInHand(hand).shrink(1);
                world.setBlock(pos, state.setValue(ACTIVE, true), 3);
                world.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                checkAndUpdateAllActive(world, pos, facing); // Обновляем ALL_ACTIVE
                return InteractionResult.SUCCESS;
            }
        }

        if (!world.isClientSide()) {
            BlockPos basePos = findBaseFromAnyPart(pos, facing, part);

            if (checkCenterPartsActive(world, basePos, facing)) {
                boolean newOpenState = !state.getValue(OPEN);
                updateAllParts(world, basePos, facing, newOpenState);

                SoundEvent sound = newOpenState ?
                        SoundsObs.OPEN_OBSIDIAN_DOOR.get() :
                        SoundsObs.CLOSE_OBSIDIAN_DOOR.get();
                world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

        return InteractionResult.SUCCESS;
    }

    private void checkAndUpdateAllActive(Level level, BlockPos pos, Direction facing) {
        BlockPos basePos = findBaseFromAnyPart(pos, facing, level.getBlockState(pos).getValue(PART));
        BlockPos cPos  = basePos.above(1);
        BlockPos clPos = cPos.relative(facing.getCounterClockWise());
        BlockPos crPos = cPos.relative(facing.getClockWise());

        boolean allActive = isPartActive(level, cPos, facing) &&
                isPartActive(level, clPos, facing) &&
                isPartActive(level, crPos, facing);

        // Обновляем состояние ALL_ACTIVE для всех частей двери
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                BlockPos partPos = basePos.relative(facing.getClockWise(), dx).above(dy);
                BlockState partState = level.getBlockState(partPos);

                if (partState.getBlock() == this) {
                    level.setBlock(partPos, partState.setValue(ALL_ACTIVE, allActive), Block.UPDATE_ALL);
                }
            }
        }
    }
    // Находим позицию базового блока BC относительно TL
    private BlockPos findBaseFromAnyPart(BlockPos anyPartPos, Direction facing, DoorPart part) {
        // Определяем смещение относительно BC
        int dx = switch(part) {
            case BL, CL, TL -> -1;
            case BC, C, TC -> 0;
            case BR, CR, TR -> 1;
        };

        int dy = switch(part) {
            case BL, BC, BR -> 0;
            case CL, C, CR -> 1;
            case TL, TC, TR -> 2;
        };

        return anyPartPos
                .relative(facing.getClockWise(), -dx)
                .below(dy);
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock()) {
            // Если это не базовая часть (BC), находим базовый блок
            if (state.getValue(PART) != DoorPart.BC) {
                BlockPos basePos = findBaseFromAnyPart(pos, state.getValue(FACING), state.getValue(PART));
                BlockState baseState = level.getBlockState(basePos);

                // Убедимся, что базовый блок существует и является частью двери
                if (baseState.getBlock() == this) {
                    breakDoorStructure(level, basePos, state.getValue(FACING));
                }
            } else {
                // Если это базовая часть, сразу ломаем всю структуру
                breakDoorStructure(level, pos, state.getValue(FACING));
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
    private void breakDoorStructure(Level level, BlockPos basePos, Direction facing) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                BlockPos partPos = basePos.relative(facing.getClockWise(), dx).above(dy);
                BlockState partState = level.getBlockState(partPos);

                // Удаляем только части двери
                if (partState.getBlock() == this) {
                    level.destroyBlock(partPos, true); // true - дропает предметы
                }
            }
        }
    }
    // Проверяем активность C/CL/CR
    private boolean checkCenterPartsActive(Level level, BlockPos basePos, Direction facing) {
        BlockPos cPos  = basePos.above(1);
        BlockPos clPos = cPos.relative(facing.getCounterClockWise());
        BlockPos crPos = cPos.relative(facing.getClockWise());

        return isPartActive(level, cPos, facing) &&
                isPartActive(level, clPos, facing) &&
                isPartActive(level, crPos, facing);
    }

    // Проверка активности конкретной части
    private boolean isPartActive(Level level, BlockPos pos, Direction facing) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() == this &&
                state.getValue(ACTIVE) &&
                state.getValue(FACING) == facing;
    }
    // Обновляем все части двери
    private void updateAllParts(Level level, BlockPos basePos, Direction facing, boolean open) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                BlockPos partPos = basePos.relative(facing.getClockWise(), dx).above(dy);
                BlockState partState = level.getBlockState(partPos);

                if (partState.getBlock() == this) {
                    level.setBlock(partPos, partState.setValue(OPEN, open), Block.UPDATE_ALL);
                }
            }
        }
    }
}