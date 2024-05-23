package net.rezolv.obsidanum.item.item_entity.arrows;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.item.item_entity.arrows.obsidian_arrow.ObsidianArrow;
public class EntityTypeInit {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Obsidanum.MOD_ID);

    public static final RegistryObject<EntityType<ObsidianArrow>> OBSIDIAN_ARROW = ENTITY_TYPES.register("obsidian_arrow",
            () -> EntityType.Builder.<ObsidianArrow>of(ObsidianArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).setCustomClientFactory(ObsidianArrow::new)
                    .build(new ResourceLocation(Obsidanum.MOD_ID, "obsidian_arrow").toString()));
}
