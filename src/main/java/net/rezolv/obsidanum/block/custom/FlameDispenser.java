package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.rezolv.obsidanum.block.entity.FlameDispenserEntity;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;
import net.rezolv.obsidanum.block.entity.RightForgeScrollEntity;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.custom.NetherFlame;
import org.jetbrains.annotations.Nullable;

public class FlameDispenser extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3); // Стадии от 0 до 2
    public static final BooleanProperty IS_PRESSED = BooleanProperty.create("is_pressed");

    public FlameDispenser(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(STAGE, 0)
                .setValue(IS_PRESSED, false));  // Изначально не нажат

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE, IS_PRESSED);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(STAGE, 0)
                .setValue(IS_PRESSED, false);

    }
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(IS_PRESSED)) {
            level.playSound(null, pos, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);

            // Восстановление состояния нажатия через 10 тиков
            level.setBlock(pos, state.setValue(IS_PRESSED, false), 2);
        }
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack item = player.getItemInHand(hand);

        // Проверка на то, что в главной руке нет NetherFlame и кнопка не нажата
        if ((hand == InteractionHand.MAIN_HAND && item.getItem() != ItemsObs.NETHER_FLAME.get()) && !state.getValue(IS_PRESSED)) {
            // Воспроизводим звук нажатия
            level.playSound(null, pos, SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS, 1.0F, 1.0F);

            // Переключаем состояние нажатия
            level.setBlock(pos, state.setValue(IS_PRESSED, true), 2);

            // Анимация взмаха рукой
            player.swing(InteractionHand.MAIN_HAND, true);

            // Запланировать возврат состояния в false
            level.scheduleTick(pos, this, 40);  // 10 тиков = ~0.5 секунд
        }

        if (!level.isClientSide) {
            if (item.getItem() instanceof NetherFlame) {
                // Проверяем стадию роста
                int currentStage = state.getValue(STAGE);
                if (currentStage < 3) {
                    player.swing(InteractionHand.MAIN_HAND, true);
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);

                    // Увеличиваем стадию роста
                    level.setBlock(pos, state.setValue(STAGE, currentStage + 1), 2);

                    // Уменьшаем прочность предмета
                    item.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                }
            }
        }

        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        int stage = pState.getValue(STAGE);
        return stage * 3;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FlameDispenserEntity(pPos, pState);
    }
}
