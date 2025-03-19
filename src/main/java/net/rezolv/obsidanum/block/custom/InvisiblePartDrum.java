package net.rezolv.obsidanum.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class InvisiblePartDrum extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING; // Поддержка всех 6 направлений

    public InvisiblePartDrum(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Direction facing = state.getValue(FACING);
        BlockPos mainPos = pos.relative(facing.getOpposite()); // Находим главный блок

        if (level.getBlockState(mainPos).getBlock() instanceof RitualDrum) {
            // Перенаправляем взаимодействие на основной блок
            BlockHitResult newHit = new BlockHitResult(
                    hit.getLocation(),
                    hit.getDirection(),
                    mainPos,
                    hit.isInside()
            );
            return level.getBlockState(mainPos).use(level, player, hand, newHit);
        }
        return InteractionResult.PASS;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING); // Добавляем свойство FACING
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case DOWN -> Block.box(0, 12, 0, 16, 16, 16);  // Верхняя грань (под потолком)
            case UP -> Block.box(0, 0, 0, 16, 14, 16);  // Нижняя грань (на полу)
            case SOUTH -> Block.box(0, 0, 0, 16, 16, 4);  // К стене с севера
            case NORTH -> Block.box(0, 0, 12, 16, 16, 16); // К стене с юга
            case EAST -> Block.box(0, 0, 0, 4, 16, 16);  // К стене с запада
            case WEST -> Block.box(12, 0, 0, 16, 16, 16); // К стене с востока
        };
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }
}