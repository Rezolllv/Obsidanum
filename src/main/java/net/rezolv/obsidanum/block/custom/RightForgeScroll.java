package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.rezolv.obsidanum.block.entity.RightForgeScrollEntity;
import net.rezolv.obsidanum.block.enum_blocks.ScrollType;
import net.rezolv.obsidanum.item.ItemsObs;

public class RightForgeScroll extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<ScrollType> TYPE_SCROLL = EnumProperty.create("type_scroll", ScrollType.class);

    public RightForgeScroll(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TYPE_SCROLL, ScrollType.NONE));

    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE_SCROLL);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
        ScrollType newType = null;
        ScrollType currentType = pState.getValue(TYPE_SCROLL);

        if (itemInHand.is(ItemsObs.NETHER_PLAN.get())) {
            newType = ScrollType.NETHER;
        } else if (itemInHand.is(ItemsObs.ORDER_PLAN.get())) {
            newType = ScrollType.ORDER;
        } else if (itemInHand.is(ItemsObs.CATACOMBS_PLAN.get())) {
            newType = ScrollType.CATACOMBS;
        }

        // Добавить свиток и NBT
        if (newType != null && currentType == ScrollType.NONE) {
            pLevel.setBlock(pPos, pState.setValue(TYPE_SCROLL, newType), 3);
            CompoundTag itemTag = itemInHand.getTag();

            if (itemTag != null) {
                BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
                if (blockEntity instanceof RightForgeScrollEntity) {
                    ((RightForgeScrollEntity) blockEntity).setScrollNBT(itemTag);
                }
            }
            if (!pPlayer.isCreative()) {
                itemInHand.shrink(1);
            }

            // Обновление соседних блоков
            pLevel.updateNeighborsAt(pPos, this);
            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }

        // Убрать свиток и NBT
        if (pPlayer.isShiftKeyDown() && itemInHand.isEmpty() && currentType != ScrollType.NONE) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

            if (blockEntity instanceof RightForgeScrollEntity) {
                RightForgeScrollEntity scrollEntity = (RightForgeScrollEntity) blockEntity;
                CompoundTag storedNBT = scrollEntity.getScrollNBT();

                if (!storedNBT.isEmpty()) {
                    ItemStack scrollItem = null;
                    switch (currentType) {
                        case NETHER:
                            scrollItem = ItemsObs.NETHER_PLAN.get().getDefaultInstance();
                            break;
                        case ORDER:
                            scrollItem = ItemsObs.ORDER_PLAN.get().getDefaultInstance();
                            break;
                        case CATACOMBS:
                            scrollItem = ItemsObs.CATACOMBS_PLAN.get().getDefaultInstance();
                            break;
                    }

                    if (scrollItem != null) {
                        scrollItem.setTag(storedNBT);

                        if (!pPlayer.addItem(scrollItem)) {
                            pPlayer.drop(scrollItem, false);
                        }
                        scrollEntity.setScrollNBT(new CompoundTag()); // Стираем NBT
                        pLevel.setBlock(pPos, pState.setValue(TYPE_SCROLL, ScrollType.NONE), 3);

                        // Обновление соседних блоков
                        pLevel.updateNeighborsAt(pPos, this);
                        return InteractionResult.sidedSuccess(pLevel.isClientSide());
                    }
                }
            }
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
    return RenderShape.MODEL;
}

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }


    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RightForgeScrollEntity(pPos, pState);
    }
}
