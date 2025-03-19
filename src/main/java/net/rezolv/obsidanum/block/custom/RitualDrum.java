package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rezolv.obsidanum.block.BlocksObs;


public class RitualDrum extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public RitualDrum(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    // Пример: можно использовать кастомную фигуру или вернуть Shapes.block()
    @Override
    public VoxelShape getShape(BlockState pState, net.minecraft.world.level.BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.block();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            // Ваша логика взаимодействия (вызывается ТОЛЬКО для основного блока)
            ItemEntity apple = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(Items.APPLE));
            level.addFreshEntity(apple);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    // При размещении основного блока размещаем фейковые части
    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!worldIn.isClientSide()) {
            worldIn.setBlock(pos.north(), BlocksObs.INVISIBLE_PART.get().defaultBlockState().setValue(InvisiblePart.FACING, Direction.NORTH), 3);
            worldIn.setBlock(pos.south(), BlocksObs.INVISIBLE_PART.get().defaultBlockState().setValue(InvisiblePart.FACING, Direction.SOUTH), 3);
            worldIn.setBlock(pos.east(), BlocksObs.INVISIBLE_PART.get().defaultBlockState().setValue(InvisiblePart.FACING, Direction.EAST), 3);
            worldIn.setBlock(pos.west(), BlocksObs.INVISIBLE_PART.get().defaultBlockState().setValue(InvisiblePart.FACING, Direction.WEST), 3);
            worldIn.setBlock(pos.above(), BlocksObs.INVISIBLE_PART.get().defaultBlockState().setValue(InvisiblePart.FACING, Direction.UP), 3);
        }
        super.onPlace(state, worldIn, pos, oldState, isMoving);
    }

    // При удалении основного блока удаляем и фейковые части
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!worldIn.isClientSide()) {
            removePart(worldIn, pos.north());
            removePart(worldIn, pos.south());
            removePart(worldIn, pos.east());
            removePart(worldIn, pos.west());
            removePart(worldIn, pos.above());
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    private void removePart(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof InvisiblePart) {
            world.removeBlock(pos, false);
        }
    }
}