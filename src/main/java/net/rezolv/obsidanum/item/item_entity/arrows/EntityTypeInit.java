package net.rezolv.obsidanum.item.item_entity.arrows;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.item.item_entity.arrows.flame_bolt.FlameBolt;
import net.rezolv.obsidanum.item.item_entity.arrows.netherite_bolt.NetheriteBolt;
import net.rezolv.obsidanum.item.item_entity.arrows.obsidian_arrow.ObsidianArrow;
public class EntityTypeInit {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Obsidanum.MOD_ID);

    public static final RegistryObject<EntityType<ObsidianArrow>> OBSIDIAN_ARROW = ENTITY_TYPES.register("obsidian_arrow",
            () -> EntityType.Builder.<ObsidianArrow>of(ObsidianArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).setCustomClientFactory(ObsidianArrow::new)
                    .build(new ResourceLocation(Obsidanum.MOD_ID, "obsidian_arrow").toString()));

    public static final RegistryObject<EntityType<FlameBolt>> FLAME_ARROW = ENTITY_TYPES.register("flame_bolt",
            () -> EntityType.Builder.<FlameBolt>of(FlameBolt::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).setCustomClientFactory(FlameBolt::new)
                    .build(new ResourceLocation(Obsidanum.MOD_ID, "flame_bolt").toString()));

    public static final RegistryObject<EntityType<NetheriteBolt>> NETHERITE_BOLT = ENTITY_TYPES.register("netherite_bolt",
            () -> EntityType.Builder.<NetheriteBolt>of(NetheriteBolt::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).setCustomClientFactory(NetheriteBolt::new)
                    .build(new ResourceLocation(Obsidanum.MOD_ID, "netherite_bolt").toString()));
}
