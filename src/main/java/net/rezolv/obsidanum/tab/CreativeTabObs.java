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
                        pOutput.accept(ItemsObs.OBSIDAN_HOE.get());
                        pOutput.accept(ItemsObs.OBSIDAN_APPLE.get());
                        pOutput.accept(ItemsObs.OBSIDAN_PICKAXE.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_ARROW.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_AXE.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_PICKAXE.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_SHOVEL.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_SWORD.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_HOE.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_CHAKRAM.get());



                        //Blocks
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_BUTTON.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_PRESSURE_PLATE.get());
                        pOutput.accept(BlocksObs.OBSIDAN_SAPLING.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_STAIRS.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_SLAB.get());

                        pOutput.accept(BlocksObs.OBSIDAN_WOOD_LOG.get());
                        pOutput.accept(BlocksObs.OBSIDAN_WOOD.get());
                        pOutput.accept(BlocksObs.STRIPPED_OBSIDAN_WOOD_LOG.get());
                        pOutput.accept(BlocksObs.STRIPPED_OBSIDAN_WOOD.get());
                        pOutput.accept(BlocksObs.OBSIDAN_WOOD_LEAVES.get());

                    })
                    .build());




    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}