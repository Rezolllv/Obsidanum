package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rezolv.obsidanum.item.ItemsObs;




public class GloomyMycelium extends Block {
    public GloomyMycelium(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(GROWTH_STAGE, 0));

    }
    public static final IntegerProperty GROWTH_STAGE = IntegerProperty.create("growth_stage", 0, 1);

    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, GROWTH_STAGE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING); // Направление блока
        BlockPos blockBelow = pos.relative(facing.getOpposite()); // Позиция блока, на который ставится грибница

        // Проверяем, является ли блок под грибницей твёрдым и может ли блок стоять на этой стороне
        return level.getBlockState(blockBelow).isFaceSturdy(level, blockBelow, facing);
    }
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int growthStage = pState.getValue(GROWTH_STAGE);

        if (pLevel.isDay() && pLevel.canSeeSky(pPos)) {
            if (growthStage == 0) {
                // На первой стадии роста блок ломается при дневном свете
                pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
                ((ServerLevel) pLevel).sendParticles(ParticleTypes.LARGE_SMOKE, pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
                pLevel.playSound(null, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);

            } else if (growthStage == 1) {
                // На второй стадии роста блок возвращается к первой стадии
                BlockState newState = pState.setValue(GROWTH_STAGE, 0);
                pLevel.setBlock(pPos, newState, 3);

                // Воспроизводим эффекты
                pLevel.playSound(null, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                ((ServerLevel) pLevel).sendParticles(ParticleTypes.LARGE_SMOKE, pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
            }
        } else {
            // В остальных случаях, если блок находится на первой стадии, он растет до второй стадии
            if (growthStage == 0 && pRandom.nextFloat() < 0.04) { // Настроить шанс по вашему усмотрению
                BlockState newState = pState.setValue(GROWTH_STAGE, 1);
                pLevel.setBlock(pPos, newState, 3);
            }
        }
    }



    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return state.getValue(GROWTH_STAGE) < 1 && super.canBeReplaced(state, context);
    }





    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
        return state.getValue(GROWTH_STAGE) < 1; // Можно использовать муку только для блока с стадией роста меньше 1
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);

        // Проверяем, является ли предмет костной мукой
        if (hand == InteractionHand.MAIN_HAND && itemStack.getItem() == Items.BONE_MEAL) {
            if (!level.isClientSide) {
                // Генерируем случайное число от 0 до 99
                int randomChance = ((ServerLevel) level).getRandom().nextInt(100);

                // Проверяем, если случайное число меньше 10, то увеличиваем стадию роста

                BlockState newState = state;
                player.swing(InteractionHand.MAIN_HAND, true);

                // Проверяем стадию роста и увеличиваем её, если это возможно
                int growthStage = state.getValue(GROWTH_STAGE);
                if (growthStage < 1) {
                    if (randomChance < 10) {
                        newState = state.setValue(GROWTH_STAGE, growthStage + 1);
                    }
                    level.setBlock(pos, newState, 2); // Обновляем состояние блока

                    // Воспроизводим эффекты
                    ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
                    level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);

                    // Уменьшаем количество костной муки в инвентаре игрока
                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }

                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        // Проверяем, является ли предмет ножницами
        if (hand == InteractionHand.MAIN_HAND && itemStack.getItem() == Items.SHEARS) {
            if (!level.isClientSide) {
                int growthStage = state.getValue(GROWTH_STAGE);
                if (growthStage == 1) {
                    // Сменить стадию роста на 0
                    BlockState newState = state.setValue(GROWTH_STAGE, 0);
                    level.setBlock(pos, newState, 2);

                    // Создать предмет гриба и добавить его в мир
                    ItemStack mushroomStack = new ItemStack(ItemsObs.GLOOMY_MUSHROOM.get()); // Замените на нужный блок или гриб
                    ItemEntity mushroomEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, mushroomStack);
                    level.addFreshEntity(mushroomEntity);                       // Если игрок смог получить гриб, значит инвентарь был изменен
                    // Уменьшить прочность ножниц
                    itemStack.hurtAndBreak(1, player, (p_220037_) -> {
                        p_220037_.broadcastBreakEvent(hand);
                    });

                    // Воспроизвести звук ножниц
                    level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }


        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        int growthStage = state.getValue(GROWTH_STAGE);

        double verticalOffset = (growthStage == 1) ? 12.0 : 0.0; // Для UP и DOWN
        double horizontalOffset = (growthStage == 1) ? 7.0 : 0.0;  // Для SOUTH, NORTH, EAST, WEST

        switch (direction) {
            case UP:
                return Block.box(0.0, 0.0, 0.0, 16.0, 1.0 + verticalOffset, 16.0);
            case DOWN:
                return Block.box(0.0, 15.0 - verticalOffset, 0.0, 16.0, 16.0, 16.0);
            case SOUTH:
                return Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0 + horizontalOffset);
            case NORTH:
                return Block.box(0.0, 0.0, 15.0 - horizontalOffset, 16.0, 16.0, 16.0);
            case EAST:
                return Block.box(0.0, 0.0, 0.0, 1.0 + horizontalOffset, 16.0, 16.0);
            case WEST:
                return Block.box(15.0 - horizontalOffset, 0.0, 0.0, 16.0, 16.0, 16.0);
            default:
                return Block.box(0.0, 15.0 - verticalOffset, 0.0, 16.0, 16.0, 16.0); // По умолчанию
        }
    }
}
