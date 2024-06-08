package net.rezolv.obsidanum.chests;


import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.chests.blocks.BlockStoneChest;
import net.rezolv.obsidanum.chests.blocks.EnumStoneChest;
import net.rezolv.obsidanum.chests.client.TEISRStoneChest;
import net.rezolv.obsidanum.chests.tileentities.TileEntityStoneChest;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

public class SCRegistry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Obsidanum.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Obsidanum.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Obsidanum.MOD_ID);

    public static RegistryObject<BlockStoneChest>[] chests = new RegistryObject[EnumStoneChest.VALUES.length];
    public static RegistryObject<BlockEntityType<TileEntityStoneChest>> CHEST_TILE_TYPE;

    public static void register() {
        for (EnumStoneChest type : EnumStoneChest.VALUES) {

            String name = "chest_" + type.name().toLowerCase(Locale.ENGLISH);

            RegistryObject<BlockStoneChest> chestObject = BLOCKS.register(name, () -> new BlockStoneChest(type));
            chests[type.ordinal()] = chestObject;

            ITEMS.register(name, () -> new BlockItem(chestObject.get(), new Item.Properties()){
                @Override
                public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                    super.initializeClient(consumer);

                    consumer.accept(new IClientItemExtensions() {
                        @Override
                        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                            return TEISRStoneChest.INSTANCE;
                        }
                    });
                }
            });
        }

        CHEST_TILE_TYPE = TILE_ENTITIES.register("chest_tile", () -> BlockEntityType.Builder.of(TileEntityStoneChest::new, Arrays.stream(chests).map(RegistryObject::get).toArray(Block[]::new)).build(null));

        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
