package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class FlameLeavesBlock extends LeavesBlock {
    public FlameLeavesBlock(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }


    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) == 0) { // Уменьшенная частота появления слёз
            Direction randomDirection = Direction.getRandom(random);
            if (randomDirection != Direction.UP) {
                BlockPos relativePos = pos.relative(randomDirection);
                BlockState relativeState = world.getBlockState(relativePos);
                if (!state.canOcclude() || !relativeState.isFaceSturdy(world, relativePos, randomDirection.getOpposite())) {
                    double xOffset = randomDirection.getStepX() == 0 ? random.nextDouble() : 0.5 + (double) randomDirection.getStepX() * 0.6;
                    double yOffset = randomDirection.getStepY() == 0 ? random.nextDouble() : 0.5 + (double) randomDirection.getStepY() * 0.6;
                    double zOffset = randomDirection.getStepZ() == 0 ? random.nextDouble() : 0.5 + (double) randomDirection.getStepZ() * 0.6;

                    double xSpeed = 0.0;
                    double ySpeed = -0.15; // Уменьшенная скорость падения
                    double zSpeed = 0.0;

                    world.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, xSpeed, ySpeed, zSpeed);
                }
            }
        }
    }
}
