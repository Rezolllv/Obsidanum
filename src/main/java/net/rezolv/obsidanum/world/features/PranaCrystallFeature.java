package net.rezolv.obsidanum.world.features;


import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.block.custom.PranaCrystall;

public class PranaCrystallFeature extends Feature<NoneFeatureConfiguration> {

    public PranaCrystallFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();

        for (blockpos = context.origin(); blockpos.getY() >= worldgenlevel.getMaxBuildHeight() - 3; blockpos = blockpos.above()) {
            if (!worldgenlevel.isEmptyBlock(blockpos.above())) {
                break;
            }
        }
        BlockPos copy = blockpos;
        if (blockpos.getY() >= worldgenlevel.getMaxBuildHeight() - 3) {
            return false;
        } else {
            worldgenlevel.setBlock(blockpos, BlocksObs.PRANA_CRYSTALL.get().defaultBlockState(), 3);
            PranaCrystall.fillWithLights(blockpos, worldgenlevel);
            return true;
        }
    }

    private static boolean canReplace(BlockState state) {
        return state.isAir() || state.canBeReplaced();
    }

    private static void drawOrb(WorldGenLevel level, BlockPos center, RandomSource random, BlockState blockState, int radiusX, int radiusY, int radiusZ) {
        double equalRadius = (radiusX + radiusY + radiusZ) / 3.0D;
        for (int x = -radiusX; x <= radiusX; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusZ; z <= radiusZ; z++) {
                    BlockPos fill = center.offset(x, y, z);
                    if (fill.distToLowCornerSqr(center.getX(), center.getY(), center.getZ()) <= equalRadius * equalRadius - random.nextFloat() * 4) {
                        if (canReplace(level.getBlockState(fill))) {
                            level.setBlock(fill, blockState, 2);
                        }
                    }
                }
            }
        }
    }
}