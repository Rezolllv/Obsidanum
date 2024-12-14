package net.rezolv.obsidanum.block.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
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
import org.spongepowered.asm.mixin.Mixin;

public class DecorativeUrn extends Block {
    public DecorativeUrn(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    private static final VoxelShape SHAPE = Shapes.or(
            // Основная часть вазы (первая часть вашей модели)
            Shapes.box(0.1875, 0.0, 0.1875, 0.8125, 0.125, 0.8125), // первая часть
            // Вторая часть вазы
            Shapes.box(0.125, 0.125, 0.125, 0.875, 0.5625, 0.875), // вторая часть
            // Третья часть вазы
            Shapes.box(0.25, 0.5625, 0.25, 0.75, 0.625, 0.75), // третья часть
            // Четвертая часть вазы
            Shapes.box(0.1875, 0.625, 0.1875, 0.8125, 0.8125, 0.8125) // четвертая часть
    );

    @Override
    public void stepOn(Level pLevel, BlockPos pos, BlockState pState, Entity entity) {
        super.stepOn(pLevel, pos, pState, entity);
        if (!pLevel.isClientSide && entity instanceof Entity) {
            // Сломать блок при шаге игрока
            pLevel.destroyBlock(pos, true);
            // Проверяем, бегает ли игрок
            if (entity.isSprinting() || entity.onGround() && entity.getDeltaMovement().y < 0) {
                // Сломать блок при шаге игрока или беге
                pLevel.destroyBlock(pos, true); // true указывает на то, что блок должен быть сломан с выпадением предметов
            }
        }
    }

    @Override
    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        super.onProjectileHit(pLevel, pState, pHit, pProjectile);
        pLevel.destroyBlock(pHit.getBlockPos(), true);
    }
}
