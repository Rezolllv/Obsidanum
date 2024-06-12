package net.rezolv.obsidanum.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElemental;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Obsidanum.MOD_ID);

    public static final RegistryObject<EntityType<ObsidianElemental>> OBSIDIAN_ELEMENTAL =
            ENTITY_TYPES.register("obsidian_elemental", () -> EntityType.Builder.of(ObsidianElemental::new, MobCategory.MONSTER)
                    .sized(1f, 2f).build("obsidian_elemental"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}