package net.rezolv.obsidanum.item.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.item.item_entity.obsidan_chakram.ObsidianChakramEntity;
import net.rezolv.obsidanum.item.item_entity.pot_grenade.PotGrenade;
import net.rezolv.obsidanum.item.item_entity.pot_grenade.fog.PotGrenadeFog;

public class ModEntitiesItem {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Obsidanum.MOD_ID);

    public static final RegistryObject<EntityType<ObsidianChakramEntity>> OBSIDIAN_CHAKRAM = ENTITIES.register("obsidian_chakram",
            () -> EntityType.Builder.<ObsidianChakramEntity>of(ObsidianChakramEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build(new ResourceLocation(Obsidanum.MOD_ID, "obsidian_chakram").toString()));

    public static final RegistryObject<EntityType<PotGrenade>> POT_GRENADE = ENTITIES.register("pot_grenade",
            () -> EntityType.Builder.<PotGrenade>of(PotGrenade::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build(new ResourceLocation(Obsidanum.MOD_ID, "pot_grenade").toString()));

    public static final RegistryObject<EntityType<PotGrenadeFog>> POT_GRENADE_FOG = ENTITIES.register("pot_grenade_fog",
            () -> EntityType.Builder.<PotGrenadeFog>of(PotGrenadeFog::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build(new ResourceLocation(Obsidanum.MOD_ID, "pot_grenade_fog").toString()));

    public static final RegistryObject<EntityType<ModBoatEntity>> MOD_BOAT =
            ENTITIES.register("mod_boat", () -> EntityType.Builder.<ModBoatEntity>of(ModBoatEntity::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("mod_boat"));
    public static final RegistryObject<EntityType<ModChestBoatEntity>> MOD_CHEST_BOAT =
            ENTITIES.register("mod_chest_boat", () -> EntityType.Builder.<ModChestBoatEntity>of(ModChestBoatEntity::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("mod_chest_boat"));



}