package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MysteriousCarvedObsidian extends Block {
    public static final BooleanProperty MYSTERIOUS = BooleanProperty.create("mysterious");

    public MysteriousCarvedObsidian(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MYSTERIOUS, false));
    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        updateMysteriousState(level, pos);
        level.scheduleTick(pos, this, 20); // Reschedule tick
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MYSTERIOUS);
    }




    private void updateMysteriousState(Level level, BlockPos pos) {
        boolean hasNearbyPlayer = level.players().stream()
                .anyMatch(player -> player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 15); // Distance squared for 3 blocks

        BlockState newState = this.defaultBlockState().setValue(MYSTERIOUS, hasNearbyPlayer);
        level.setBlock(pos, newState, 3);
    }
}