package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rezolv.obsidanum.block.BlocksObs;

import javax.annotation.Nullable;

public class LargeUrn extends HalfTransparentBlock {
    public static final BooleanProperty VOID_URN = BooleanProperty.create("void_urn");

    public static final BooleanProperty TOP_FOUR_BLOCK_URN = BooleanProperty.create("top_four_block_urn");
    public static final BooleanProperty TOP_THREE_BLOCK_URN = BooleanProperty.create("top_three_block_urn");
    public static final BooleanProperty TOP_TWO_BLOCK_URN = BooleanProperty.create("top_two_block_urn");
    public static final BooleanProperty TOP_ONE_BLOCK_URN = BooleanProperty.create("top_one_block_urn");

    public static final BooleanProperty BOTTOM_FOUR_BLOCK_URN = BooleanProperty.create("bottom_four_block_urn");
    public static final BooleanProperty BOTTOM_THREE_BLOCK_URN = BooleanProperty.create("bottom_three_block_urn");
    public static final BooleanProperty BOTTOM_TWO_BLOCK_URN = BooleanProperty.create("bottom_two_block_urn");
    public static final BooleanProperty BOTTOM_ONE_BLOCK_URN = BooleanProperty.create("bottom_one_block_urn");

    public LargeUrn(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(TOP_FOUR_BLOCK_URN,false)
                .setValue(TOP_THREE_BLOCK_URN,false)
                .setValue(TOP_TWO_BLOCK_URN,false)
                .setValue(TOP_ONE_BLOCK_URN,false)

                .setValue(BOTTOM_FOUR_BLOCK_URN,false)
                .setValue(BOTTOM_THREE_BLOCK_URN,false)
                .setValue(BOTTOM_TWO_BLOCK_URN,false)
                .setValue(BOTTOM_ONE_BLOCK_URN,false)

                .setValue(VOID_URN, false));
    }
    private static final VoxelShape NORMAL_SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    private static final VoxelShape TOP_FOUR_SHAPE = Shapes.or(
            box(0, 0, 0, 16, 8, 16),
            box(4, 8, 4, 16, 10, 16),
            box(2, 10, 2, 16, 14, 16));
    private static final VoxelShape TOP_THREE_SHAPE = Shapes.or(
            box(0, 0, 0, 16, 8, 16),
            box(0, 8, 4, 12, 10, 16),
            box(0, 10, 2, 14, 14, 16));
    private static final VoxelShape TOP_TWO_SHAPE = Shapes.or(
            box(0, 0, 0, 16, 8, 16),
            box(4, 8, 0, 16, 10, 12),
            box(2, 10, 0, 16, 14, 14));
    private static final VoxelShape TOP_ONE_SHAPE = Shapes.or(
            box(0, 0, 0, 16, 8, 16),
            box(0, 8, 0, 12, 10, 12),
            box(0, 10, 0, 14, 14, 14));



    private static final VoxelShape BOTTOM_FOUR_SHAPE = Shapes.or(
            box(2, 0, 2, 16, 2, 16),
            box(0, 2, 0, 16, 16, 16));
    private static final VoxelShape BOTTOM_THREE_SHAPE = Shapes.or(
            box(0, 0, 2, 14, 2, 16),
            box(0, 2, 0, 16, 16, 16));
    private static final VoxelShape BOTTOM_TWO_SHAPE = Shapes.or(
            box(2, 0, 0, 16, 2, 14),
            box(0, 2, 0, 16, 16, 16));
    private static final VoxelShape BOTTOM_ONE_SHAPE = Shapes.or(
            box(0, 0, 0, 14, 2, 14),
            box(0, 2, 0, 16, 16, 16));
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(TOP_FOUR_BLOCK_URN)) {
            return TOP_FOUR_SHAPE; // Заданный хитбокс
        }
        else if (state.getValue(TOP_THREE_BLOCK_URN)) {
            return TOP_THREE_SHAPE; // Заданный хитбокс
        }
        else if (state.getValue(TOP_TWO_BLOCK_URN)) {
            return TOP_TWO_SHAPE; // Заданный хитбокс
        }
        else if (state.getValue(TOP_ONE_BLOCK_URN)) {
            return TOP_ONE_SHAPE; // Заданный хитбокс
        }

        else if (state.getValue(BOTTOM_FOUR_BLOCK_URN)) {
            return BOTTOM_FOUR_SHAPE; // Заданный хитбокс
        }
        else if (state.getValue(BOTTOM_THREE_BLOCK_URN)) {
            return BOTTOM_THREE_SHAPE; // Заданный хитбокс
        }
        else if (state.getValue(BOTTOM_TWO_BLOCK_URN)) {
            return BOTTOM_TWO_SHAPE; // Заданный хитбокс
        }
        else if (state.getValue(BOTTOM_ONE_BLOCK_URN)) {
            return BOTTOM_ONE_SHAPE; // Заданный хитбокс
        }
        else {
            return NORMAL_SHAPE; // Стандартный хитбокс 16x16x16
        }
    }
    // Регистрация свойства VOID_URN
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VOID_URN);
        builder.add(TOP_FOUR_BLOCK_URN);
        builder.add(TOP_THREE_BLOCK_URN);
        builder.add(TOP_TWO_BLOCK_URN);
        builder.add(TOP_ONE_BLOCK_URN);
        builder.add(BOTTOM_FOUR_BLOCK_URN);
        builder.add(BOTTOM_THREE_BLOCK_URN);
        builder.add(BOTTOM_TWO_BLOCK_URN);
        builder.add(BOTTOM_ONE_BLOCK_URN);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!state.getValue(TOP_FOUR_BLOCK_URN)) {
            BlockState topFourUrnState = BlocksObs.LARGE_URN.get().defaultBlockState().setValue(BOTTOM_FOUR_BLOCK_URN, true); // Замените на ваш блок
            level.setBlockAndUpdate(pos.offset(0, 0, 0), topFourUrnState);
        }
        // Проверяем состояние VOID_URN
        if (!state.getValue(VOID_URN)) {
            // Устанавливаем блоки вокруг
            BlockState voidUrnState = BlocksObs.LARGE_URN.get().defaultBlockState().setValue(VOID_URN, true); // Замените на ваш блок

            if (!state.getValue(TOP_THREE_BLOCK_URN)) {
                BlockState topThreeUrnState = BlocksObs.LARGE_URN.get().defaultBlockState().setValue(VOID_URN, true).setValue(BOTTOM_THREE_BLOCK_URN, true);
                level.setBlockAndUpdate(pos.offset(1, 0, 0), topThreeUrnState);
            }
            if (!state.getValue(TOP_TWO_BLOCK_URN)) {
                BlockState topTwoUrnState = BlocksObs.LARGE_URN.get().defaultBlockState().setValue(VOID_URN, true).setValue(BOTTOM_TWO_BLOCK_URN, true);
                level.setBlockAndUpdate(pos.offset(0, 0, 1), topTwoUrnState);
            }
            if (!state.getValue(TOP_ONE_BLOCK_URN)) {
                BlockState topOneUrnState = BlocksObs.LARGE_URN.get().defaultBlockState().setValue(VOID_URN, true).setValue(BOTTOM_ONE_BLOCK_URN, true);
                level.setBlockAndUpdate(pos.offset(1, 0, 1), topOneUrnState);
            }



            if (!state.getValue(TOP_FOUR_BLOCK_URN)) {
                BlockState topFourUrnState = BlocksObs.LARGE_URN.get().defaultBlockState().setValue(VOID_URN, true).setValue(TOP_FOUR_BLOCK_URN, true); // Замените на ваш блок
                // Сверху
                level.setBlockAndUpdate(pos.offset(0, 1, 0), topFourUrnState);
            }
                // Второй ряд сверху


            if (!state.getValue(TOP_THREE_BLOCK_URN)) {
                BlockState topThreeUrnState = BlocksObs.LARGE_URN.get().defaultBlockState().setValue(VOID_URN, true).setValue(TOP_THREE_BLOCK_URN, true); // Замените на ваш блок

                level.setBlockAndUpdate(pos.offset(1, 1, 0), topThreeUrnState);
            }
            if (!state.getValue(TOP_TWO_BLOCK_URN)) {
                BlockState topTwoUrnState = BlocksObs.LARGE_URN.get().defaultBlockState().setValue(VOID_URN, true).setValue(TOP_TWO_BLOCK_URN, true); // Замените на ваш блок

                level.setBlockAndUpdate(pos.offset(0, 1, 1), topTwoUrnState);
            }
            if (!state.getValue(TOP_ONE_BLOCK_URN)) {
                BlockState topOneUrnState = BlocksObs.LARGE_URN.get().defaultBlockState().setValue(VOID_URN, true).setValue(TOP_ONE_BLOCK_URN, true); // Замените на ваш блок

                level.setBlockAndUpdate(pos.offset(1, 1, 1), topOneUrnState);
            }
        }
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        // Удаляем блоки вокруг, независимо от их состояния
        BlockPos[] positionsToRemove = new BlockPos[] {
                pos.offset(-1, 0, 0), // Справа
                pos.offset(0, 0, -1), // Сзади

                pos.offset(1, 0, 0), // Справа
                pos.offset(0, 0, 1), // Сзади

                pos.offset(-1, 0, -1), // Справа-сзади
                pos.offset(0, -1, 0), // Сверху

                pos.offset(1, 0, 1), // Справа-сзади
                pos.offset(0, 1, 0), // Сверху


                pos.offset(-1, -1, -1), // Второй ряд сверху
                pos.offset(-1, -1, 0), // Второй ряд сверху
                pos.offset(0, -1, -1),  // Второй ряд сверху

                pos.offset(1, 1, 1), // Второй ряд сверху
                pos.offset(1, 1, 0), // Второй ряд сверху
                pos.offset(0, 1, 1)  // Второй ряд сверху
        };

        for (BlockPos position : positionsToRemove) {
            // Удаляем блок и получаем результаты
            BlockState blockState = level.getBlockState(position);
            if (blockState.getBlock() == BlocksObs.LARGE_URN.get().defaultBlockState().setValue(VOID_URN, true).getBlock()) {
                level.destroyBlock(position, false); // true - означает, что ресурсы блока будут выброшены
            } else if (blockState.getBlock() == BlocksObs.LARGE_URN.get().defaultBlockState().setValue(VOID_URN, false).getBlock()) {
                level.destroyBlock(position, true);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}