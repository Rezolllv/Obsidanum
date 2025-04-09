package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.block.BlocksObs;

public class ObsidianSapling extends SaplingBlock {
    public ObsidianSapling(AbstractTreeGrower pTreeGrower, Properties pProperties) {
        super(pTreeGrower, pProperties);
    }
    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        // Разрешаем размещение на земле (как в оригинале) и на камне
        return pState.is(Blocks.GRASS_BLOCK) ||
                pState.is(Blocks.DIRT) ||
                pState.is(Blocks.PODZOL) ||
                pState.is(Blocks.COARSE_DIRT) ||
                pState.is(Blocks.MYCELIUM) ||
                pState.is(BlocksObs.ALCHEMICAL_DIRT.get()) ||
                pState.is(BlocksObs.CRIMSON_GRASS_BLOCK.get()) ||
                pState.is(Blocks.ROOTED_DIRT);
    }
}
