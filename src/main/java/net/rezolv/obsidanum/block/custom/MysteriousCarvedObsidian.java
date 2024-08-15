package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class MysteriousCarvedObsidian extends Block {
    public static final BooleanProperty MYSTERIOUS = BooleanProperty.create("mysterious");
    private static final int DETECTION_RADIUS_SQ = 15; // 15 blocks squared

    public MysteriousCarvedObsidian(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MYSTERIOUS, false));
    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        updateMysteriousState(level, pos);
        // Запланировать следующий тик через 20 тиков (1 секунда)
        level.scheduleTick(pos, this, 8);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MYSTERIOUS);
    }

    private void updateMysteriousState(Level level, BlockPos pos) {
        boolean hasNearbyPlayer = level.players().stream()
                .anyMatch(player -> player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= DETECTION_RADIUS_SQ);

        BlockState currentState = level.getBlockState(pos);
        if (currentState.getValue(MYSTERIOUS) != hasNearbyPlayer) {
            BlockState newState = currentState.setValue(MYSTERIOUS, hasNearbyPlayer);
            level.setBlock(pos, newState, 3);
            // Уведомить клиентов об обновлении блока
            level.sendBlockUpdated(pos, currentState, newState, 3);
        }
    }
}