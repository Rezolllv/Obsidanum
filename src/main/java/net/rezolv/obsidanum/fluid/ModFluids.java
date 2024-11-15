package net.rezolv.obsidanum.fluid;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.item.ItemsObs;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, Obsidanum.MOD_ID);

    public static final RegistryObject<FlowingFluid> SOURCE_NETHER_FIRE_LAVA = FLUIDS.register("nether_fire_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.NETHER_FIRE_LAVA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_NETHER_FIRE_LAVA = FLUIDS.register("flowing_nether_fire",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.NETHER_FIRE_LAVA_FLUID_PROPERTIES));


    public static final ForgeFlowingFluid.Properties NETHER_FIRE_LAVA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.NETHER_FIRE_LAVA_FLUID_TYPE, SOURCE_NETHER_FIRE_LAVA, FLOWING_NETHER_FIRE_LAVA)
            .slopeFindDistance(1).levelDecreasePerBlock(1).tickRate(15).block(BlocksObs.NETHER_FLAME_BLOCK);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}