package net.rezolv.obsidanum.tab;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.item.ItemsObs;

public class CreativeTabObs extends CreativeModeTab {

    protected CreativeTabObs(Builder builder) {
        super(builder);
    }
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Obsidanum.MOD_ID);

    public static final RegistryObject<CreativeModeTab> OBSIDANUM_TAB = CREATIVE_MODE_TABS.register("obsidanum_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemsObs.OBSIDIAN_TEAR.get()))
                    .title(Component.translatable("creativetab.obsidanum"))
                    .displayItems((pParameters, pOutput) -> {
                        //Items
                        pOutput.accept(ItemsObs.OBSIDIAN_TEAR.get());
                        pOutput.accept(ItemsObs.OBSIDAN.get());
                        pOutput.accept(ItemsObs.DRILLING_CRYSTALLIZER.get());
                        pOutput.accept(ItemsObs.OBSIDAN_AXE.get());
                        pOutput.accept(ItemsObs.OBSIDAN_SHOVEL.get());
                        pOutput.accept(ItemsObs.OBSIDAN_SWORD.get());
                        pOutput.accept(ItemsObs.REZOLV_THE_TALE_OF_THE_VANISHED_ORDER_DISC.get());
                        pOutput.accept(ItemsObs.OBSIDAN_HOE.get());
                        pOutput.accept(ItemsObs.OBSIDAN_APPLE.get());
                        pOutput.accept(ItemsObs.OBSIDAN_PICKAXE.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_ARROW.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_AXE.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_PICKAXE.get());
                        pOutput.accept(ItemsObs.SMOLDERING_OBSIDIAN_PICKAXE.get());
                        pOutput.accept(ItemsObs.SMOLDERING_OBSIDIAN_AXE.get());
                        pOutput.accept(ItemsObs.SMOLDERING_OBSIDIAN_SHOVEL.get());
                        pOutput.accept(ItemsObs.SMOLDERING_OBSIDIAN_HOE.get());
                        pOutput.accept(ItemsObs.SMOLDERING_OBSIDIAN_SWORD.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_SHOVEL.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_SWORD.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_HOE.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_CHAKRAM.get());
                        pOutput.accept(ItemsObs.CRYSTALLIZED_IRON_ORE.get());
                        pOutput.accept(ItemsObs.CRYSTALLIZED_GOLD_ORE.get());
                        pOutput.accept(ItemsObs.CRYSTALLIZED_COPPER_ORE.get());
                        pOutput.accept(ItemsObs.BAGELL_FUEL.get());
                        pOutput.accept(ItemsObs.OBSIDAN_SIGN.get());
                        pOutput.accept(ItemsObs.OBSIDAN_HANGING_SIGN.get());
                        pOutput.accept(ItemsObs.OBSIDAN_BOAT.get());
                        pOutput.accept(ItemsObs.OBSIDAN_CHEST_BOAT.get());
                        pOutput.accept(ItemsObs.NETHER_FLAME.get());
                        pOutput.accept(ItemsObs.CRUCIBLE.get());
                        pOutput.accept(ItemsObs.RELICT_AMETHYST_SHARD.get());
                        pOutput.accept(ItemsObs.CRUCIBLE_WITH_NETHER_FLAME.get());



                        //Blocks
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_TABLET.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_INLAID_COLUMN.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_INLAID_COLUMN.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_COLUMN.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_COLUMN.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_POLISHED.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_POLISHED.get());
                        pOutput.accept(BlocksObs.BANNER_SECRET_FLAME.get());
                        pOutput.accept(BlocksObs.CARVED_OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.CRACKED_CARVED_OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.MYSTERIOUS_CARVED_OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.CRACKED_MYSTERIOUS_CARVED_OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.OBSIDAN_WOOD_DOOR.get());
                        pOutput.accept(BlocksObs.OBSIDAN_WOOD_TRAPDOOR.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_BUTTON.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_PRESSURE_PLATE.get());
                        pOutput.accept(ItemsObs.OBSIDAN_SAPLING.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_STAIRS.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_SLAB.get());

                        pOutput.accept(BlocksObs.OBSIDAN_WOOD_LOG.get());
                        pOutput.accept(BlocksObs.OBSIDAN_WOOD.get());
                        pOutput.accept(BlocksObs.STRIPPED_OBSIDAN_WOOD_LOG.get());
                        pOutput.accept(BlocksObs.STRIPPED_OBSIDAN_WOOD.get());
                        pOutput.accept(ItemsObs.OBSIDAN_WOOD_LEAVES.get());

                    })
                    .build());




    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}