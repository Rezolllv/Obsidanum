package net.rezolv.obsidanum.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.gart.Gart;
import net.rezolv.obsidanum.entity.meat_beetle.MeetBeetle;
import net.rezolv.obsidanum.entity.mutated_gart.MutatedGart;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElemental;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Obsidanum.MOD_ID);

    public static final RegistryObject<EntityType<ObsidianElemental>> OBSIDIAN_ELEMENTAL =
            ENTITY_TYPES.register("obsidian_elemental", () -> EntityType.Builder.of(ObsidianElemental::new, MobCategory.MONSTER)
                    .sized(1f, 2f).build("obsidian_elemental"));
    public static final RegistryObject<EntityType<MeetBeetle>> MEET_BEETLE =
            ENTITY_TYPES.register("meet_beetle", () -> EntityType.Builder.of(MeetBeetle::new, MobCategory.AMBIENT)
                    .sized(0.8f, 0.4f).build("meet_beetle"));
    public static final RegistryObject<EntityType<Gart>> GART =
            ENTITY_TYPES.register("gart", () -> EntityType.Builder.of(Gart::new, MobCategory.MONSTER)
                    .sized(1f, 1.2f).build("gart"));
    public static final RegistryObject<EntityType<MutatedGart>> MUTATED_GART =
            ENTITY_TYPES.register("mutated_gart", () -> EntityType.Builder.of(MutatedGart::new, MobCategory.MONSTER)
                    .sized(1.2f, 2f).build("mutated_gart"));

}