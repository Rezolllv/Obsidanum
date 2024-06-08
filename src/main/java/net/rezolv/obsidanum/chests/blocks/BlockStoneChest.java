package net.rezolv.obsidanum.chests.blocks;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.rezolv.obsidanum.chests.SCRegistry;
import net.rezolv.obsidanum.chests.tileentities.TileEntityStoneChest;

import java.util.function.BiPredicate;

public class BlockStoneChest extends ChestBlock {
    private final EnumStoneChest chestType;

    public BlockStoneChest(EnumStoneChest chestType, Properties properties) {
        super(properties, () -> SCRegistry.CHEST_TILE_TYPE.get());
        this.chestType = chestType;
    }
    public static Direction getConnectedDirection(BlockState p_51585_) {
        Direction direction = (Direction)p_51585_.getValue(FACING);
        // Предотвращаем соединение, возвращая направление, противоположное соединению
        return direction.getOpposite(); // Возвращает направление, противоположное текущему
    }
    private Direction candidatePartnerFacing(BlockPlaceContext p_51493_, Direction opposite) {
        // Получаем позицию клика и уровень
        BlockPos clickedPos = p_51493_.getClickedPos();
        Level level = p_51493_.getLevel();

        // Ищем соседний блок в направлении, противоположном opposite
        BlockPos neighborPos = clickedPos.relative(opposite.getOpposite());

        // Проверяем, существует ли соседний блок и является ли он блоком сундука
        if (level.getBlockState(neighborPos).is(this)) {
            // Возвращаем направление к соседнему блоку
            return opposite;
        }

        // Если соседний блок не найден или не является сундуком, возвращаем null
        return null;
    }
    public BlockState getStateForPlacement(BlockPlaceContext p_51493_) {
        ChestType chesttype = ChestType.SINGLE;
        Direction direction = p_51493_.getHorizontalDirection().getOpposite();
        FluidState fluidstate = p_51493_.getLevel().getFluidState(p_51493_.getClickedPos());
        boolean flag = p_51493_.isSecondaryUseActive();
        Direction direction1 = p_51493_.getClickedFace();

        // Измененная логика выбора ChestType
        if (direction1.getAxis().isHorizontal() && flag) {
            Direction direction2 = this.candidatePartnerFacing(p_51493_, direction1.getOpposite());
            if (direction2!= null && direction2.getAxis()!= direction1.getAxis()) {
                direction = direction2;
                // Здесь можно оставить выбор между LEFT и RIGHT, если это необходимо
                chesttype = direction2.getCounterClockWise() == direction1.getOpposite()? ChestType.RIGHT : ChestType.LEFT;
            }
        }

        // Теперь всегда устанавливаем ChestType.SINGLE, если это не двойной сундук
        if (!flag || chesttype == ChestType.SINGLE) {
            chesttype = ChestType.SINGLE;
        }

        return (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, direction)).setValue(TYPE, chesttype)).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }
    public static DoubleBlockCombiner.BlockType getBlockType(BlockState p_51583_) {
        // Всегда возвращаем SINGLE, чтобы предотвратить соединение сундуков
        return DoubleBlockCombiner.BlockType.SINGLE;
    }
    public BlockState updateShape(BlockState p_51555_, Direction p_51556_, BlockState p_51557_, LevelAccessor p_51558_, BlockPos p_51559_, BlockPos p_51560_) {
        if ((Boolean)p_51555_.getValue(WATERLOGGED)) {
            p_51558_.scheduleTick(p_51559_, Fluids.WATER, Fluids.WATER.getTickDelay(p_51558_));
        }

        // Запретить соединение сундуков
        if (p_51557_.is(this) && p_51556_.getAxis().isHorizontal()) {
            return p_51555_; // Возвращаем исходное состояние без изменений
        }

        return super.updateShape(p_51555_, p_51556_, p_51557_, p_51558_, p_51559_, p_51560_);
    }
    @Override
    public DoubleBlockCombiner.NeighborCombineResult<? extends TileEntityStoneChest> combine(BlockState p_51544_, Level p_51545_, BlockPos p_51546_, boolean p_51547_) {
        // Изменяем тип bipredicate на LevelAccessor и BlockPos
        BiPredicate<LevelAccessor, BlockPos> bipredicate = (world, pos) -> false;

        return DoubleBlockCombiner.combineWithNeigbour(
                (BlockEntityType)this.blockEntityType.get(),
                BlockStoneChest::getBlockType,
                BlockStoneChest::getConnectedDirection,
                FACING,
                p_51544_,
                p_51545_,
                p_51546_,
                bipredicate
        );
    }
    public BlockStoneChest(EnumStoneChest chestType) {
        super(Properties.of().strength(20F,500).requiresCorrectToolForDrops().sound(SoundType.STONE), () -> SCRegistry.CHEST_TILE_TYPE.get());
        this.chestType = chestType;
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityStoneChest(this.chestType, pos, state);
    }

    public EnumStoneChest getChestType() {
        return this.chestType;
    }
}
