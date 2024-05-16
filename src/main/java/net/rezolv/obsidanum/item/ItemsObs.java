package net.rezolv.obsidanum.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.item.custom.*;
import net.rezolv.obsidanum.item.entity.ModBoatEntity;

public class ItemsObs {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Obsidanum.MOD_ID);

    public static final RegistryObject<Item> OBSIDIAN_TEAR = ITEMS.register("obsidian_tear",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN = ITEMS.register("obsidan",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHER_FLAME = ITEMS.register("nether_flame",
            () -> new NetherFlame(new Item.Properties().durability(25)));
    public static final RegistryObject<Item> CRYSTALLIZED_COPPER_ORE = ITEMS.register("crystallized_copper_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CRYSTALLIZED_IRON_ORE = ITEMS.register("crystallized_iron_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CRYSTALLIZED_GOLD_ORE = ITEMS.register("crystallized_gold_ore",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CRUCIBLE = ITEMS.register("crucible",
            () -> new Item(new Item.Properties()));
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
            () -> new ObsidanSword(ModToolTiers.SMOLDERING,0,-2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_AXE = ITEMS.register("obsidan_axe",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_PICKAXE = ITEMS.register("obsidan_pickaxe",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_SHOVEL = ITEMS.register("obsidan_shovel",
            () -> new ObsidanShovel(ModToolTiers.SMOLDERING,-3,-2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_HOE = ITEMS.register("obsidan_hoe",
            () -> new ObsidanHoe(ModToolTiers.SMOLDERING,-3,-2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_ARROW = ITEMS.register("obsidian_arrow",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_CHAKRAM = ITEMS.register("obsidian_chakram",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SMOLDERING_OBSIDIAN_PICKAXE = ITEMS.register("smoldering_obsidian_pickaxe",
            () -> new SmolderingPickaxe(ModToolTiers.SMOLDERING,0,-2.9F, new Item.Properties()));
    public static final RegistryObject<Item> SMOLDERING_OBSIDIAN_HOE = ITEMS.register("smoldering_obsidian_hoe",
            () -> new SmolderingHoe(ModToolTiers.SMOLDERING,-3,-2F, new Item.Properties()));
    public static final RegistryObject<Item> SMOLDERING_OBSIDIAN_AXE = ITEMS.register("smoldering_obsidian_axe",
            () -> new SmolderingAxe(ModToolTiers.SMOLDERING,4,-3.1F, new Item.Properties()));
    public static final RegistryObject<Item> SMOLDERING_OBSIDIAN_SWORD = ITEMS.register("smoldering_obsidian_sword",
            () -> new SmolderingSword(ModToolTiers.SMOLDERING, 2, -2.7f, new Item.Properties()));
    public static final RegistryObject<Item> SMOLDERING_OBSIDIAN_SHOVEL = ITEMS.register("smoldering_obsidian_shovel",
            () -> new SmolderingShovel(ModToolTiers.SMOLDERING,0,-3.1F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_AXE = ITEMS.register("obsidian_axe",
            () -> new ObsAxe(ModToolTiers.OBSIDIANUM,5,-3.2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_HOE = ITEMS.register("obsidian_hoe",
            () -> new ObsHoe(ModToolTiers.OBSIDIANUM,-1,-3.2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_PICKAXE = ITEMS.register("obsidian_pickaxe",
            () -> new ObsPickaxe(ModToolTiers.OBSIDIANUM,1,-3F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SHOVEL = ITEMS.register("obsidian_shovel",
            () -> new ObsShovel(ModToolTiers.OBSIDIANUM,1.5F,-3.2F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SWORD = ITEMS.register("obsidian_sword",
            () -> new ObsSword(ModToolTiers.OBSIDIANUM,3,-3F, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_SIGN = ITEMS.register("obsidan_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), BlocksObs.OBSIDAN_SIGN.get(), BlocksObs.OBSIDAN_WALL_SIGN.get()));
    public static final RegistryObject<Item> OBSIDAN_HANGING_SIGN = ITEMS.register("obsidan_hanging_sign",
            () -> new HangingSignItem(BlocksObs.OBSIDAN_HANGING_SIGN.get(), BlocksObs.OBSIDAN_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> OBSIDAN_WOOD_LEAVES = ITEMS.register("obsidan_wood_leaves",
            () -> new ItemNameBlockItem(BlocksObs.OBSIDAN_WOOD_LEAVES.get(),new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_SAPLING = ITEMS.register("obsidan_sapling",
            () -> new FuelItemBlock(BlocksObs.OBSIDAN_SAPLING.get(),new Item.Properties(), 450));
    public static final RegistryObject<Item> OBSIDAN_BOAT = ITEMS.register("obsidan_boat",
            () -> new ModBoatItem(false, ModBoatEntity.Type.OBSIDAN, new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_CHEST_BOAT = ITEMS.register("obsidan_chest_boat",
            () -> new ModBoatItem(true, ModBoatEntity.Type.OBSIDAN, new Item.Properties()));
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
