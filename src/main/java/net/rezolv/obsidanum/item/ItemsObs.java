package net.rezolv.obsidanum.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.entity.ModEntities;
import net.rezolv.obsidanum.item.custom.*;
import net.rezolv.obsidanum.item.entity.ModBoatEntity;
import net.rezolv.obsidanum.sound.SoundsObs;

import java.util.List;

public class ItemsObs {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Obsidanum.MOD_ID);

    public static final RegistryObject<Item> POT_GRENADE = ITEMS.register("pot_grenade",
            () -> new PotGrenadeItem(new Item.Properties()));
    public static final RegistryObject<Item> VELNARIUM_ORE = ITEMS.register("velnarium_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ORDER_SWORD = ITEMS.register("order_sword",
            () -> new VelnariumSword(ModToolTiers.VELNARIUM, 1, -1.8f, new Item.Properties()));

    // Свитки для печи
    public static final RegistryObject<Item> ORDER_PLAN = ITEMS.register("order_plan",
            () -> new ScrollText(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> NETHER_PLAN = ITEMS.register("nether_plan",
            () -> new ScrollText(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CATACOMBS_PLAN = ITEMS.register("catacombs_plan",
            () -> new ScrollText(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> UPGRADE_PLAN = ITEMS.register("upgrade_plan",
            () -> new UpgradeScrollsText(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<Item> OBSIDAN_ESSENCE = ITEMS.register("obsidan_essence",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_TOTEM_OF_IMMORTALITY = ITEMS.register("obsidian_totem_of_immortality",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ELEMENTAL_CRUSHER = ITEMS.register("elemental_crusher",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VELNARIUM_MACE = ITEMS.register("velnarium_mace",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DEMONIC_BONECRUSHER = ITEMS.register("demonic_bonecrusher",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPORES_OF_THE_GLOOMY_MUSHROOM = ITEMS.register("spores_of_the_gloomy_mushroom",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FLAME_CROSSBOW = ITEMS.register("flame_crossbow",
            () -> new FlameCrossbowItem(new Item.Properties().stacksTo(1).durability(1001)));

    public static final RegistryObject<Item> FLAME_BOLT = ITEMS.register("flame_bolt",
            () -> new FlameBoltItem(new Item.Properties()));

    public static final RegistryObject<Item> NETHERITE_BOLT = ITEMS.register("netherite_bolt",
            () -> new NetheriteBoltItem(new Item.Properties()));

    public static final RegistryObject<Item> PRANA_CRYSTALL_SHARD = ITEMS.register("prana_crystall_shard",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LEAF_LIVE = ITEMS.register("leaf_live",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(1).saturationMod(0.5f).alwaysEat()
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1F)
                            .build()
            )));


    public static final RegistryObject<Item> OBSIDIAN_TEAR = ITEMS.register("obsidian_tear",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN = ITEMS.register("obsidan",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_MEET_BEETLE = ITEMS.register("raw_meet_beetle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).build()
                    )));
    public static final RegistryObject<Item> COCKED_MEET_BEETLE = ITEMS.register("cocked_meet_beetle",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.5f).build()
                    )));
    public static final RegistryObject<Item> RELICT_AMETHYST_SHARD = ITEMS.register("relict_amethyst_shard",
            () -> new RelictAmethyst(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BAGELL_FUEL = ITEMS.register("bagell_fuel",
            () -> new BagellFuel(new Item.Properties(), 24000));
    public static final RegistryObject<Item> NETHER_FLAME = ITEMS.register("nether_flame",
            () -> new NetherFlame(new Item.Properties().durability(25)));
    public static final RegistryObject<Item> NETHER_FLAME_ENTITY = ITEMS.register("nether_flame_entity",
            () -> new NetherFlame(new Item.Properties()));
    public static final RegistryObject<Item> NETHER_FLAME_ENTITY_MINI = ITEMS.register("nether_flame_entity_mini",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGIC_ARROW = ITEMS.register("magic_arrow",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CRYSTALLIZED_COPPER_ORE = ITEMS.register("crystallized_copper_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CRYSTALLIZED_IRON_ORE = ITEMS.register("crystallized_iron_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CRYSTALLIZED_GOLD_ORE = ITEMS.register("crystallized_gold_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CRUCIBLE = ITEMS.register("crucible",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VELNARIUM_INGOT = ITEMS.register("velnarium_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GLOOMY_MUSHROOM = ITEMS.register("gloomy_mushroom",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 120, 0), 0.5F)
                    .effect(() -> new MobEffectInstance(MobEffects.WITHER, 80, 0), 0.1F)
                    .build())));
    public static final RegistryObject<Item> OBSIDIAN_SHARD_KEY = ITEMS.register("obsidian_shard_key",
            () -> new ObsidianShardKey(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SHARD_ARROW = ITEMS.register("obsidian_shard_arrow",
            () -> new ObsidianShardArrow(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SHARD_INVIOLABILITY = ITEMS.register("obsidian_shard_inviolability",
            () -> new ObsidianShardInviolability(new Item.Properties()));
    public static final RegistryObject<Item> FACETED_ONYX = ITEMS.register("faceted_onyx",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ONYX_PENDANT = ITEMS.register("onyx_pendant",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EYE_GART = ITEMS.register("eye_gart",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1F)
                    .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 1200, 0), 1.0F) // Ночное зрение
                    .effect(() -> new MobEffectInstance(MobEffects.POISON, 60, 0), 0.5F) // Отравление
                    .alwaysEat().build())));

    public static final RegistryObject<Item> OBSIDIAN_KEY = ITEMS.register("obsidian_key",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> REZOLV_THE_TALE_OF_THE_VANISHED_ORDER_DISC = ITEMS.register("rezolv_the_tale_of_the_vanished_order_disc",
            () -> new RecordItem(12, SoundsObs.REZOLV_THE_TALE_OF_THE_VANISHED_ORDER, new Item.Properties().stacksTo(1), 2520));
    public static final RegistryObject<Item> CRUCIBLE_WITH_NETHER_FLAME = ITEMS.register("crucible_with_nether_flame",
            () -> new CrucibleNetherFlame(new Item.Properties().durability(25)));
    public static final RegistryObject<Item> DRILLING_CRYSTALLIZER = ITEMS.register("drilling_crystallizer",
            () -> new DrillingCrystallizer(new Item.Properties().durability(5)));
    public static final RegistryObject<Item> OBSIDAN_APPLE = ITEMS.register("obsidan_apple",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.4F)
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400, 2), 1.0F) // Защита
                    .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2400, 2), 1.0F) // Огнестойкость
                    .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 2), 1.0F) // Абсорбция
                    .alwaysEat().build())));
    public static final RegistryObject<Item> OBSIDAN_SWORD = ITEMS.register("obsidan_sword",
            () -> new ObsidanSword(ModToolTiers.OBSIDAN, 2, -2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_AXE = ITEMS.register("obsidan_axe",
            () -> new ObsidanAxe(ModToolTiers.OBSIDAN, 3, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_PICKAXE = ITEMS.register("obsidan_pickaxe",
            () -> new ObsidanPickaxe(ModToolTiers.OBSIDAN, -1, -2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_SHOVEL = ITEMS.register("obsidan_shovel",
            () -> new ObsidanShovel(ModToolTiers.OBSIDAN, 1, -2.6F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_HOE = ITEMS.register("obsidan_hoe",
            () -> new ObsidanHoe(ModToolTiers.OBSIDAN, -4, 2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_ARROW = ITEMS.register("obsidian_arrow",
            () -> new ObsidianArrowItem(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_CHAKRAM = ITEMS.register("obsidian_chakram",
            () -> new Chakram(new Item.Properties()));
    public static final RegistryObject<Item> SMOLDERING_OBSIDIAN_PICKAXE = ITEMS.register("smoldering_obsidian_pickaxe",
            () -> new SmolderingPickaxe(ModToolTiers.SMOLDERING, 0, -2.9F, new Item.Properties()));
    public static final RegistryObject<Item> SMOLDERING_OBSIDIAN_HOE = ITEMS.register("smoldering_obsidian_hoe",
            () -> new SmolderingHoe(ModToolTiers.SMOLDERING, -3, -2F, new Item.Properties()));
    public static final RegistryObject<Item> SMOLDERING_OBSIDIAN_AXE = ITEMS.register("smoldering_obsidian_axe",
            () -> new SmolderingAxe(ModToolTiers.SMOLDERING, 4, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> SMOLDERING_OBSIDIAN_SWORD = ITEMS.register("smoldering_obsidian_sword",
            () -> new SmolderingSword(ModToolTiers.SMOLDERING, 2, -2.7f, new Item.Properties()));
    public static final RegistryObject<Item> SMOLDERING_OBSIDIAN_SHOVEL = ITEMS.register("smoldering_obsidian_shovel",
            () -> new SmolderingShovel(ModToolTiers.SMOLDERING, -1, -3.1F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_AXE = ITEMS.register("obsidian_axe",
            () -> new ObsAxe(ModToolTiers.OBSIDIANUM, 5, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_HOE = ITEMS.register("obsidian_hoe",
            () -> new ObsHoe(ModToolTiers.OBSIDIANUM, -1, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_PICKAXE = ITEMS.register("obsidian_pickaxe",
            () -> new ObsPickaxe(ModToolTiers.OBSIDIANUM, 1, -3F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SHOVEL = ITEMS.register("obsidian_shovel",
            () -> new ObsShovel(ModToolTiers.OBSIDIANUM, 1.5F, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SWORD = ITEMS.register("obsidian_sword",
            () -> new ObsSword(ModToolTiers.OBSIDIANUM, 3, -3F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_SIGN = ITEMS.register("obsidan_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), BlocksObs.OBSIDAN_SIGN.get(), BlocksObs.OBSIDAN_WALL_SIGN.get()));
    public static final RegistryObject<Item> OBSIDAN_HANGING_SIGN = ITEMS.register("obsidan_hanging_sign",
            () -> new HangingSignItem(BlocksObs.OBSIDAN_HANGING_SIGN.get(), BlocksObs.OBSIDAN_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> OBSIDIAN_BRICKS = ITEMS.register("obsidian_bricks",
            () -> new ItemNameBlockItem(BlocksObs.OBSIDIAN_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> AZURE_OBSIDIAN_BRICKS = ITEMS.register("azure_obsidian_bricks",
            () -> new ItemNameBlockItem(BlocksObs.AZURE_OBSIDIAN_BRICKS.get(), new Item.Properties()));

    public static final RegistryObject<Item> OBSIDAN_WOOD_LEAVES = ITEMS.register("obsidan_wood_leaves",
            () -> new ItemNameBlockItem(BlocksObs.OBSIDAN_WOOD_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> THE_GLOOMY_MYCELIUM = ITEMS.register("the_gloomy_mycelium",
            () -> new ItemNameBlockItem(BlocksObs.THE_GLOOMY_MYCELIUM.get(), new Item.Properties()));
    public static final RegistryObject<Item> STEM_GLOOMY_MUSHROOM = ITEMS.register("stem_gloomy_mushroom",
            () -> new ItemNameBlockItem(BlocksObs.STEM_GLOOMY_MUSHROOM.get(), new Item.Properties()));
    public static final RegistryObject<Item> CAP_GLOOMY_MUSHROOM = ITEMS.register("cap_gloomy_mushroom",
            () -> new ItemNameBlockItem(BlocksObs.CAP_GLOOMY_MUSHROOM.get(), new Item.Properties()));

    public static final RegistryObject<Item> FIERY_INFUSION_SMITHING_TEMPLATE = ITEMS.register(
            "fiery_infusion_smithing_template",
            () -> new ObsFieryInfusionTemplateItem(
                    Component.translatable("item.obsidanum.fiery_infusion_apply_to").withStyle(ChatFormatting.BLUE), // displayName
                    Component.translatable("item.obsidanum.fiery_infusion_smithing_template.base_slot").withStyle(ChatFormatting.BLUE), // baseSlotDescription
                    Component.translatable("item.obsidanum.fiery_infusion_smithing_template.add_slot").withStyle(ChatFormatting.GRAY), // addSlotDescription
                    Component.translatable("item.obsidanum.fiery_infusion_smithing_template.base_tooltip"), // baseSlotTooltip
                    Component.translatable("item.obsidanum.fiery_infusion_smithing_template.add_tooltip"), // addSlotTooltip
                    List.of( // baseSlotIcons
                                new ResourceLocation("minecraft:item/empty_slot_sword"),
                            new ResourceLocation("minecraft:item/empty_slot_pickaxe"),
                            new ResourceLocation("minecraft:item/empty_slot_shovel"),
                            new ResourceLocation("minecraft:item/empty_slot_axe")
                    ),
                    List.of(new ResourceLocation("obsidanum:item/empty_slot_crucible")) // addSlotIcons
            )
    );


    public static final RegistryObject<Item> OBSIDAN_SAPLING = ITEMS.register("obsidan_sapling",
            () -> new FuelItemBlock(BlocksObs.OBSIDAN_SAPLING.get(), new Item.Properties(), 450));
    public static final RegistryObject<Item> OBSIDAN_BOAT = ITEMS.register("obsidan_boat",
            () -> new ModBoatItem(false, ModBoatEntity.Type.OBSIDAN, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_CHEST_BOAT = ITEMS.register("obsidan_chest_boat",
            () -> new ModBoatItem(true, ModBoatEntity.Type.OBSIDAN, new Item.Properties()));


    public static final RegistryObject<Item> GART_SPANW_EGG = ITEMS.register("gart_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.GART, 0x240935, 0x008000, new Item.Properties()));
    public static final RegistryObject<Item> MUTATED_GART_SPANW_EGG = ITEMS.register("mutated_gart_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MUTATED_GART, 0x1faee9, 0x483d8b, new Item.Properties()));

    public static final RegistryObject<Item> MEET_BEETLE_SPANW_EGG = ITEMS.register("meet_beetle_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MEET_BEETLE, 0x613613, 0xf2e8c9, new Item.Properties()));

    public static final RegistryObject<Item> OBSIDIAN_ELEMENTAL_SPANW_EGG = ITEMS.register("obsidian_elemental_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.OBSIDIAN_ELEMENTAL, 0x240935, 0xdeb6f3, new Item.Properties()));


}
