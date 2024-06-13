package net.rezolv.obsidanum.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;

public class ParticlesObs {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Obsidanum.MOD_ID);

    public static final RegistryObject<SimpleParticleType> NETHER_FLAME_PARTICLES =
            PARTICLE_TYPES.register("nether_flame_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BAGELL_FLAME_PARTICLES =
            PARTICLE_TYPES.register("bagell_flame_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> NETHER_FLAME2_PARTICLES =
            PARTICLE_TYPES.register("nether_flame2_particles", () -> new SimpleParticleType(true));
    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

}
