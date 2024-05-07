package net.rezolv.obsidanum.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;

public class ItemsObs {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Obsidanum.MOD_ID);

    public static final RegistryObject<Item> OBSIDIAN_TEAR = ITEMS.register("obsidian_tear",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN = ITEMS.register("obsidan",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DRILLING_CRYSTALLIZER = ITEMS.register("drilling_crystallizer",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_APPLE = ITEMS.register("obsidan_apple",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_SWORD = ITEMS.register("obsidan_sword",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_AXE = ITEMS.register("obsidan_axe",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_PICKAXE = ITEMS.register("obsidan_pickaxe",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_SHOVEL = ITEMS.register("obsidan_shovel",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDAN_HOE = ITEMS.register("obsidan_hoe",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_ARROW = ITEMS.register("obsidian_arrow",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_AXE = ITEMS.register("obsidian_axe",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_CHAKRAM = ITEMS.register("obsidian_chakram",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_HOE = ITEMS.register("obsidian_hoe",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_PICKAXE = ITEMS.register("obsidian_pickaxe",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SHOVEL = ITEMS.register("obsidian_shovel",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OBSIDIAN_SWORD = ITEMS.register("obsidian_sword",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
