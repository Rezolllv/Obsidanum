package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.rezolv.obsidanum.block.entity.ModHangingSignBlockEntity;

import java.util.Random;


public class ModHangingSignBlock extends CeilingHangingSignBlock {
    // Сначала определите MAX_FRAMES
    public static final int MAX_FRAMES = 5;  // Максимальное количество кадров

    // Теперь используйте MAX_FRAMES при создании IntegerProperty
    public static final IntegerProperty FRAME = IntegerProperty.create("frame", 0, MAX_FRAMES - 1);

    public ModHangingSignBlock(Properties properties, WoodType type) {
        super(properties, type);
        this.registerDefaultState(this.stateDefinition.any().setValue(FRAME, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FRAME);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ModHangingSignBlockEntity(pos, state);
    }

    // Добавляем код для обновления анимации
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);
        // Запланируйте обновление блока через 20 тиков
        world.scheduleTick(pos, this, 20);
    }

    public void tick(BlockState state, Level world, BlockPos pos, Random random) {
        // Получаем текущий кадр анимации
        int frame = state.getValue(FRAME);
        // Увеличиваем кадр
        frame = (frame + 1) % MAX_FRAMES;
        // Обновляем состояние блока с новым кадром
        world.setBlock(pos, state.setValue(FRAME, frame), 3);
        // Запланируйте следующий тик через 20 тиков
        world.scheduleTick(pos, this, 20);
    }
}