package net.rezolv.obsidanum.entity;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.projectile_entity.MagicArrow;
import net.rezolv.obsidanum.entity.projectile_entity.NetherFlameEntity;
import net.rezolv.obsidanum.entity.projectile_entity.NetherFlameEntityMini;

@Mod.EventBusSubscriber(modid = Obsidanum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModItemEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Obsidanum.MOD_ID);
    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
        return ENTITY_TYPES.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
    }

    public static final RegistryObject<EntityType<NetherFlameEntity>> NETHER_FLAME_ENTITY = register("projectile_nether_flame_entity",
            EntityType.Builder.<NetherFlameEntity>of(NetherFlameEntity::new, MobCategory.MISC).setCustomClientFactory(NetherFlameEntity::new)
                    .setShouldReceiveVelocityUpdates(true).setTrackingRange(20).setUpdateInterval(1).sized(0.2f, 0.2f));


    public static final RegistryObject<EntityType<MagicArrow>> MAGIC_ARROW_NETHER_FLAME_ENTITY = register("magic_arrow_entity",
            EntityType.Builder.<MagicArrow>of(MagicArrow::new, MobCategory.MISC).setCustomClientFactory(MagicArrow::new)
                    .setShouldReceiveVelocityUpdates(true).setTrackingRange(20).setUpdateInterval(1).sized(0.2f, 0.2f));

    public static final RegistryObject<EntityType<NetherFlameEntityMini>> NETHER_FLAME_ENTITY_MINI = register("projectile_nether_flame_entity_mini",
            EntityType.Builder.<NetherFlameEntityMini>of(NetherFlameEntityMini::new, MobCategory.MISC).setCustomClientFactory(NetherFlameEntityMini::new)
                    .setShouldReceiveVelocityUpdates(true).setTrackingRange(20).setUpdateInterval(1).sized(0.2f, 0.2f));

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
    }
}