package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.entity.ModEntities;
import net.rezolv.obsidanum.entity.mutated_gart.MutatedGart;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import java.util.Random;


public class RitualDrum extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty USES = IntegerProperty.create("uses", 0, 2);

    public RitualDrum(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(USES, 0));
    }

    // Пример: можно использовать кастомную фигуру или вернуть Shapes.block()
    @Override
    public VoxelShape getShape(BlockState pState, net.minecraft.world.level.BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.block();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, USES);
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            int currentUses = state.getValue(USES);

            if (currentUses < 2) {
                switch (currentUses) {
                    case 0 -> {
                        playBeeSound(level, pos, SoundEvents.BEE_LOOP);
                        player.sendSystemMessage(Component.translatable("message.obsidanum.drum_first"));
                    }
                    case 1 -> {
                        playBeeSound(level, pos, SoundEvents.BEE_LOOP_AGGRESSIVE);
                        player.sendSystemMessage(Component.translatable("message.obsidanum.drum_second"));
                    }
                }

                level.setBlock(pos, state.setValue(USES, currentUses + 1), 3);
            } else {
                playBeeSound(level, pos, SoundEvents.BEE_STING);
                player.sendSystemMessage(Component.translatable("message.obsidanum.drum_final"));

                BlockPos spawnPos = null;
                float yRot = player.getYRot();

                // Поиск OBSIDIAN_HOLE_5 в радиусе 20 блоков
                BlockPos holePos = findNearestObsidianHole(level, pos, 50);
                if (holePos != null) {
                    Direction holeFacing = level.getBlockState(holePos).getValue(FACING);
                    // Ищем позицию с противоположной стороны от отверстия
                    spawnPos = findSpawnOppositeHole(level, holePos, holeFacing);

                    // Если нашли позицию для спавна, устанавливаем поворот лицом к отверстию
                    if (spawnPos != null) {
                        yRot = holeFacing.getOpposite().toYRot();
                    }
                }

                // Если не найдено, используем старый алгоритм
                if (spawnPos == null) {
                    spawnPos = findValidSpawnPosition(level, pos, 20);
                }

                if (spawnPos != null) {
                    MutatedGart boss = new MutatedGart(ModEntities.MUTATED_GART.get(), level);
                    boss.moveTo(
                            spawnPos.getX() + 0.5,
                            spawnPos.getY(),
                            spawnPos.getZ() + 0.5,
                            yRot,
                            0
                    );
                    level.addFreshEntity(boss);
                }
                level.destroyBlock(pos, true);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }// Поиск позиции для спауна с противоположной стороны от OBSIDIAN_HOLE_5
    private BlockPos findSpawnOppositeHole(Level level, BlockPos holePos, Direction holeFacing) {
        // Ищем позицию строго в 1 блоке от отверстия (в противоположном направлении)
        BlockPos spawnCandidate = holePos.relative(holeFacing.getOpposite(), 3); // Только 1 блок!
        BlockPos surfacePos = findSurfaceAt(level, spawnCandidate);

        if (surfacePos != null && isValidSpawnArea(level, surfacePos)) {
            return surfacePos;
        }
        return null; // Если не нашлось подходящего места
    }

    // Поиск ближайшего OBSIDIAN_HOLE_5 в радиусе
    private BlockPos findNearestObsidianHole(Level level, BlockPos center, int radius) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        BlockPos closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    mutablePos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    if (level.getBlockState(mutablePos).getBlock() == BlocksObs.OBSIDIAN_HOLE_5.get()) {
                        double distance = center.distSqr(mutablePos);
                        if (distance < closestDistance) {
                            closest = mutablePos.immutable();
                            closestDistance = distance;
                        }
                    }
                }
            }
        }
        return closest;
    }


    private void playBeeSound(Level level, BlockPos pos, SoundEvent sound) {
        level.playSound(
                null, // Для всех игроков
                pos,
                sound,
                SoundSource.BLOCKS,
                1.0f,
                0.8f + level.random.nextFloat() * 0.4f // Случайная высота тона
        );
    }

    private BlockPos findValidSpawnPosition(Level level, BlockPos center, int attempts) {
        Random random = new Random();
        int searchRadius = 20;

        for (int i = 0; i < attempts; i++) {
            // Генерируем случайное смещение
            int xOffset = random.nextInt(searchRadius * 2) - searchRadius;
            int zOffset = random.nextInt(searchRadius * 2) - searchRadius;

            // Пропускаем позиции слишком близко к блоку
            if (Math.abs(xOffset) < 2 && Math.abs(zOffset) < 2) continue;

            BlockPos checkPos = center.offset(xOffset, 0, zOffset);
            BlockPos surfacePos = findSurfaceAt(level, checkPos);

            if (surfacePos != null && isValidSpawnArea(level, surfacePos)) {
                return surfacePos;
            }
        }
        return null;
    }

    private BlockPos findSurfaceAt(Level level, BlockPos pos) {
        // Ищем поверхность на том же Y уровне или ниже
        for (int y = pos.getY(); y >= pos.getY() - 3; y--) {
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            if (isSolidSurface(level, checkPos.below()) &&
                    hasEnoughAir(level, checkPos)) {
                return checkPos;
            }
        }
        return null;
    }

    private boolean isSolidSurface(Level level, BlockPos pos) {
        return level.getBlockState(pos).isSolid();
    }

    private boolean hasEnoughAir(Level level, BlockPos pos) {
        // Проверяем 3 блока в высоту
        return level.getBlockState(pos).isAir() &&
                level.getBlockState(pos.above()).isAir() &&
                level.getBlockState(pos.above(2)).isAir();
    }

    private boolean isValidSpawnArea(Level level, BlockPos pos) {
        // Проверяем пространство 3x3x2 вокруг позиции
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = 0; y <= 1; y++) {
                    BlockPos check = pos.offset(x, y, z);
                    // Игнорируем центральный столб воздуха
                    if (Math.abs(x) < 2 && Math.abs(z) < 2 && y < 2) continue;

                    if (level.getBlockState(check).isSolid()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    // При размещении основного блока размещаем фейковые части
    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!worldIn.isClientSide()) {
            worldIn.setBlock(pos.north(), BlocksObs.INVISIBLE_PART_DRUM.get().defaultBlockState().setValue(InvisiblePartDrum.FACING, Direction.NORTH), 3);
            worldIn.setBlock(pos.south(), BlocksObs.INVISIBLE_PART_DRUM.get().defaultBlockState().setValue(InvisiblePartDrum.FACING, Direction.SOUTH), 3);
            worldIn.setBlock(pos.east(), BlocksObs.INVISIBLE_PART_DRUM.get().defaultBlockState().setValue(InvisiblePartDrum.FACING, Direction.EAST), 3);
            worldIn.setBlock(pos.west(), BlocksObs.INVISIBLE_PART_DRUM.get().defaultBlockState().setValue(InvisiblePartDrum.FACING, Direction.WEST), 3);
            worldIn.setBlock(pos.above(), BlocksObs.INVISIBLE_PART_DRUM.get().defaultBlockState().setValue(InvisiblePartDrum.FACING, Direction.UP), 3);
        }
        super.onPlace(state, worldIn, pos, oldState, isMoving);
    }

    // При удалении основного блока удаляем и фейковые части
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!worldIn.isClientSide()) {
            removePart(worldIn, pos.north());
            removePart(worldIn, pos.south());
            removePart(worldIn, pos.east());
            removePart(worldIn, pos.west());
            removePart(worldIn, pos.above());
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    private void removePart(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof InvisiblePartDrum) {
            world.removeBlock(pos, false);
        }
    }
}