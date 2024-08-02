package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;

public class GloomyMycelium extends VineBlock {
    public GloomyMycelium(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.isDay() && pLevel.canSeeSky(pPos) && pRandom.nextFloat() < 0.45) {
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
        } else {
            super.randomTick(pState, pLevel, pPos, pRandom);
        }
    }
}
