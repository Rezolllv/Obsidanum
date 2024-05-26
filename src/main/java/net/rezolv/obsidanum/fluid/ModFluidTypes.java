package net.rezolv.obsidanum.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import org.joml.Vector3f;

public class ModFluidTypes {
    public static final ResourceLocation LAVA_STILL_RL = new ResourceLocation("block/nether_fire_still");
    public static final ResourceLocation LAVA_FLOWING_RL = new ResourceLocation("block/nether_fire_flow");
    public static final ResourceLocation NETHER_FIRE_OVERLAY_RL = new ResourceLocation(Obsidanum.MOD_ID, "misc/in_nether_fire");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Obsidanum.MOD_ID);

    public static final RegistryObject<FluidType> NETHER_FIRE_LAVA_FLUID_TYPE = register("nether_fire_fluid",
            FluidType.Properties.create()
                    .lightLevel(15) // Устанавливает уровень света, излучаемого лавой
                    .density(2) // Устанавливает плотность жидкости
                    .viscosity(2) // Устанавливает вязкость жидкости
                    .fallDistanceModifier(1)
                    .canDrown(true) // Устанавливает, можно ли утонуть в жидкости
                    .canHydrate(false) // Устанавливает, может ли жидкость увлажнять блоки
                    .sound(SoundAction.get("drink"),
                    SoundEvents.LAVA_POP)
                    .sound(SoundAction.get("swim"),

                            SoundEvents.LAVA_POP)
    );



    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(LAVA_STILL_RL, LAVA_FLOWING_RL, NETHER_FIRE_OVERLAY_RL,
                0xA1FF8800, new Vector3f(224f / 255f, 56f / 255f, 208f / 255f), properties));
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}