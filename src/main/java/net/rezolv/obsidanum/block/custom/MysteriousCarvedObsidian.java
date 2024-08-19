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
import net.minecraft.world.phys.Vec3;

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
    private boolean isBlockInView(Player player, BlockPos blockPos) {
        Vec3 playerEyePos = player.getEyePosition(0.1F);
        Vec3 blockPosVec = Vec3.atCenterOf(blockPos);

        // Направление взгляда игрока
        Vec3 playerLookVec = player.getViewVector(0.1F);

        // Направление от игрока к блоку
        Vec3 toBlockVec = blockPosVec.subtract(playerEyePos).normalize();

        // Проверка, находится ли блок в пределах видимости игрока
        double angle = playerLookVec.dot(toBlockVec);

        // Порог для определения "смотрит ли игрок на блок". Чем ближе к 1.0, тем точнее наведение.
        double viewThreshold = 0.98D; // Более узкий порог для видимости
        double viewDistance = 5.0D; // Определите расстояние видимости по вашему усмотрению
        return angle > viewThreshold && playerEyePos.distanceToSqr(blockPosVec) <= viewDistance * viewDistance;
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
        if (currentState.getValue(MYSTERIOUS) && level.players().stream().anyMatch(player -> isBlockInView(player, pos))) {
            level.removeBlock(pos, false);
        }
    }
}