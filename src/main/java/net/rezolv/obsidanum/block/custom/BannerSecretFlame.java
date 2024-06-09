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
                        Block.box(1, 28, 14.6, 15, 29.4, 16), // stand top horizontal
                        Block.box(7.3, 0, 14.6, 8.7, 28.1, 16), // stand vertical
                        Block.box(1, 1.4, 13.9, 15, 29.4, 14.6) // banner
                );
            case SOUTH:
                return Shapes.or(
                        Block.box(1, 28, 0, 15, 29.4, 1.4), // stand top horizontal
                        Block.box(7.3, 0, 0, 8.7, 28.1, 1.4), // stand vertical
                        Block.box(1, 1.4, 1.4, 15, 29.4, 2.1) // banner
                );
            case WEST:
                return Shapes.or(
                        Block.box(14.6, 28, 1, 16, 29.4, 15), // stand top horizontal
                        Block.box(14.6, 0, 7.3, 16, 28.1, 8.7), // stand vertical
                        Block.box(13.9, 1.4, 1, 14.6, 29.4, 15) // banner
                );
            case EAST:
                return Shapes.or(
                        Block.box(0, 28, 1, 1.4, 29.4, 15), // stand top horizontal
                        Block.box(0, 0, 7.3, 1.4, 28.1, 8.7), // stand vertical
                        Block.box(1.4, 1.4, 1, 2.1, 29.4, 15) // banner
                );
        }
    }
}
