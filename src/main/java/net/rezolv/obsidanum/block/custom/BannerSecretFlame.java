package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BannerSecretFlame extends Block {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public BannerSecretFlame(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        switch (direction) {
            case NORTH:
            default:
                return Shapes.or(
                        Block.box(1, 28, 5.6, 15, 29.4, 7),
                        Block.box(7.3, 0, 5.6, 8.7, 28.1, 7),
                        Block.box(1, 1.4, 4.9, 15, 29.4, 5.6)
                );
            case SOUTH:
                return Shapes.or(
                        Block.box(1, 28, 9, 15, 29.4, 10.4),
                        Block.box(7.3, 0, 9, 8.7, 28.1, 10.4),
                        Block.box(1, 1.4, 9.4, 15, 29.4, 10.1)
                );
            case WEST:
                return Shapes.or(
                        Block.box(4.9, 28, 1, 5.6, 29.4, 15),
                        Block.box(5.6, 0, 7.3, 7, 28.1, 8.7),
                        Block.box(5.6, 1.4, 1, 5.6, 29.4, 15)
                );
            case EAST:
                return Shapes.or(
                        Block.box(9, 28, 1, 10.4, 29.4, 15),
                        Block.box(9, 0, 7.3, 10.4, 28.1, 8.7),
                        Block.box(9.4, 1.4, 1, 10.1, 29.4, 15)
                );
        }
    }
}
