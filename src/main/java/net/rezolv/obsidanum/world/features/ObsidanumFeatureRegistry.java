package net.rezolv.obsidanum.world.features;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;

public class ObsidanumFeatureRegistry {
    public static final DeferredRegister<Feature<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.FEATURES, Obsidanum.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PRANA_CRYSTALL = DEF_REG.register("prana_crystall", () -> new PranaCrystallFeature(NoneFeatureConfiguration.CODEC));

}
