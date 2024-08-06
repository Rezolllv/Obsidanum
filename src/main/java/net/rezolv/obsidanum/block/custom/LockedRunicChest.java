package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rezolv.obsidanum.chests.SCRegistry;
import net.rezolv.obsidanum.chests.blocks.EnumStoneChest;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.sound.SoundsObs;

import java.util.Optional;

public class LockedRunicChest extends Block {
    public LockedRunicChest(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
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

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        // Check if the player is using the right hand and if the item in hand is the obsidian shard key
        if (pHand == InteractionHand.MAIN_HAND && pPlayer.getItemInHand(pHand).is(ItemsObs.OBSIDIAN_SHARD_KEY.get())) {
            // Get the current block's facing direction
            Direction currentFacing = pState.getValue(LockedRunicChest.FACING);

            // Change the block to RUNIC with the same facing direction
            BlockState newState = SCRegistry.chests[EnumStoneChest.RUNIC.ordinal()].get().defaultBlockState()
                    .setValue(LockedRunicChest.FACING, currentFacing);

            pLevel.setBlock(pPos, newState, 3);

            // Play the beacon activation sound
            pLevel.playSound(null, pPos, SoundsObs.LOCK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

            // Remove one obsidian shard key from the player's inventory
            if (!pPlayer.isCreative()) {
                pPlayer.getItemInHand(pHand).shrink(1); // Remove one key
            }

            return InteractionResult.SUCCESS;
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

}
