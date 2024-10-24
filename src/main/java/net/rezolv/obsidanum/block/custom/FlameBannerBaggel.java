package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Block blockInHand = player.getItemInHand(hand).getItem() instanceof BlockItem ? ((BlockItem)player.getItemInHand(hand).getItem()).getBlock() : null;

        if (blockInHand != this) {
            return InteractionResult.FAIL; // Если в руке не тот же блок, выходим
        }

        // Начинаем проверку сверху
        BlockPos currentPos = pos.below();

        // Цикл для поиска первого свободного места сверху
        while (level.getBlockState(currentPos).is(this)) {
            currentPos = currentPos.below(); // Поднимаемся выше, если блок тот же
        }

        // Проверяем, можно ли установить блок
        if (level.isEmptyBlock(currentPos)) {
            // Устанавливаем блок выше
            level.setBlock(currentPos, this.defaultBlockState().setValue(FACING, state.getValue(FACING)), 3);

            // Уменьшаем количество блоков в руке игрока
            if (!player.isCreative()) {
                player.getItemInHand(hand).shrink(1);
            }
            level.playSound(null, pos, SoundEvents.WOOL_PLACE,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        } else {
            // Если выше твёрдый блок, возвращаем неудачный результат
            return InteractionResult.FAIL;
        }
    }

// Проверка на прикрепление к стене или потолку
    private boolean isAttachedToWallOrCeiling(LevelAccessor level, BlockPos pos, Direction facing) {
        BlockPos offsetPos = pos.relative(facing.getOpposite()); // Проверка по направлению FACING
        BlockState sideState = level.getBlockState(offsetPos);
        BlockState aboveState = level.getBlockState(pos.above());

        // Проверяем наличие крепления на стене (согласно FACING) или потолке
        boolean isAttachedToWall = sideState.isFaceSturdy(level, offsetPos, facing);
        boolean isAttachedToCeiling = aboveState.isFaceSturdy(level, pos.above(), Direction.DOWN);

        // Возвращаем true, если блок прикреплён к стене или потолку
        return isAttachedToWall || isAttachedToCeiling;
    }

    // Рекурсивное удаление блоков
    private void destroyBlockChain(LevelAccessor level, BlockPos pos) {
        BlockPos currentPos = pos;

        // Удаляем блоки вниз по цепочке с дропом
        while (level.getBlockState(currentPos).is(this)) {
            BlockState blockState = level.getBlockState(currentPos);

            // Проверяем, не прикреплён ли блок к стене или потолку
            if (isAttachedToWallOrCeiling(level, currentPos, blockState.getValue(FACING))) {
                // Прерываем цепочку, если блок прикреплён к стене или потолку
                break;
            }

            if (level instanceof Level) {
                // Вызываем метод для дропа предметов
                Block.popResource((Level) level, currentPos, this.asItem().getDefaultInstance());
            }
            level.destroyBlock(currentPos, false); // Удаляем блок без дропа стандартных ресурсов
            currentPos = currentPos.below();
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
            // Проверяем, прикреплён ли блок к стене или потолку
            if (isAttachedToWallOrCeiling(level, pos, state.getValue(FACING))) {
                return state.setValue(TOP, true)
                        .setValue(TOP_BELOW, true)
                        .setValue(MIDDLE, false)
                        .setValue(BOTTOM, false);
            } else {
                // Удаляем этот и нижние блоки
                destroyBlockChain(level, pos);
                return Blocks.AIR.defaultBlockState();
            }
        }
        // Если нет блоков ни сверху, ни снизу, это обычный верхний блок
        else {
            if (isAttachedToWallOrCeiling(level, pos, state.getValue(FACING))) {
                return state.setValue(TOP, true)
                        .setValue(TOP_BELOW, false)
                        .setValue(MIDDLE, false)
                        .setValue(BOTTOM, false);
            } else {
                // Удаляем этот блок
                level.destroyBlock(pos, false);
                return Blocks.AIR.defaultBlockState();
            }
        }
    }
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        // Получаем направление, на которое указывает FACING
        Direction facing = state.getValue(FACING);
        BlockPos offsetPos = pos.relative(facing.getOpposite());

        // Получаем состояние блока в направлении FACING и проверяем, является ли он полным блоком
        BlockState wallState = level.getBlockState(offsetPos);

        // Блок должен быть полным, чтобы блок можно было установить на стену
        boolean canAttachToWall = wallState.isSolidRender(level, offsetPos);

        // Проверка на потолок (если блок прикрепляется сверху)
        BlockPos ceilingPos = pos.above();
        BlockState ceilingState = level.getBlockState(ceilingPos);
        boolean canAttachToCeiling = ceilingState.isFaceSturdy(level, ceilingPos, Direction.DOWN);

        // Блок может быть установлен либо на стене, либо на потолке
        return canAttachToWall || canAttachToCeiling;
    }
}
