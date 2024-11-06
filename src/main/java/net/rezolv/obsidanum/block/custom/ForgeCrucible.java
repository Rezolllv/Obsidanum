package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;
import net.rezolv.obsidanum.block.entity.RightForgeScrollEntity;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class ForgeCrucible extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public ForgeCrucible(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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



    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        // Получаем направление, на которое смотрит ForgeCrucible
        Direction facing = state.getValue(FACING);

        // Определяем позицию блока справа от ForgeCrucible в зависимости от ориентации
        BlockPos rightPos;
        switch (facing) {
            case NORTH -> rightPos = pos.west();
            case SOUTH -> rightPos = pos.east();
            case EAST -> rightPos = pos.north();
            case WEST -> rightPos = pos.south();
            default -> {
                System.out.println("Unknown facing direction!");
                return;
            }
        }

        // Проверка, что блок справа — это RightForgeScroll
        BlockEntity rightEntity = level.getBlockEntity(rightPos);
        if (rightEntity instanceof RightForgeScrollEntity rightForgeScrollEntity) {
            // Получаем NBT данные из RightForgeScroll
            CompoundTag scrollNBT = rightForgeScrollEntity.getScrollNBT();

            // Проверка, пуст ли NBT
            if (!scrollNBT.isEmpty()) {
                System.out.println("RightForgeScroll NBT Data at " + rightPos + ": " + scrollNBT);

                // Обновление данных ForgeCrucibleEntity
                BlockEntity crucibleEntity = level.getBlockEntity(pos);
                if (crucibleEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
                    forgeCrucibleEntity.updateFromScrollNBT(scrollNBT);
                    forgeCrucibleEntity.setChanged();
                    // Инициируем обновление рендера на клиенте
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            } else {
                System.out.println("RightForgeScroll NBT Data is empty at " + rightPos);
            }
        }
    }


    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ForgeCrucibleEntity(pPos, pState);
    }
}
