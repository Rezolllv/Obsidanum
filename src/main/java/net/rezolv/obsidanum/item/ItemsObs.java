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
    public static final RegistryObject<Item> OBSIDAN_APPLE = ITEMS.register("obsidan_apple",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
