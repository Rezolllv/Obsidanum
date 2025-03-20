package net.rezolv.obsidanum.world.features;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.BlocksObs;

import java.util.List;
import java.util.OptionalInt;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OBSIDAN_TREE = registerKey("obsidan_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ONYX_KEY = registerKey("onyx");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        OreConfiguration.target(deepslateReplaceables, BlocksObs.ONYX.get().defaultBlockState());

        List<OreConfiguration.TargetBlockState> overworldOnyxOres = List.of(
                OreConfiguration.target(deepslateReplaceables, BlocksObs.ONYX.get().defaultBlockState()));

        register(context, OVERWORLD_ONYX_KEY, Feature.ORE, new OreConfiguration(overworldOnyxOres, 60));

        register(context, OBSIDAN_TREE, Feature.TREE, createFancyOak().build());
    }
    private static TreeConfiguration.TreeConfigurationBuilder createFancyOak() {
        return (
                new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlocksObs.OBSIDAN_WOOD_LOG.get()),
                new FancyTrunkPlacer(3, 11, 0), BlockStateProvider.simple(BlocksObs.OBSIDAN_WOOD_LEAVES.get()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))).ignoreVines();
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Obsidanum.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}