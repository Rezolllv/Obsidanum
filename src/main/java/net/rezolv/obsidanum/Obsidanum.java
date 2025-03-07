package net.rezolv.obsidanum;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.ComposterBlock;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.block.entity.ModBlockEntities;
import net.rezolv.obsidanum.chests.block.entity.IronChestsBlockEntityTypes;
import net.rezolv.obsidanum.chests.client.render.IronChestRenderer;
import net.rezolv.obsidanum.chests.client.screen.IronChestScreen;
import net.rezolv.obsidanum.chests.inventory.IronChestsContainerTypes;
import net.rezolv.obsidanum.effect.EffectsObs;
import net.rezolv.obsidanum.entity.ModEntities;
import net.rezolv.obsidanum.entity.ModItemEntities;
import net.rezolv.obsidanum.entity.gart.GartRenderer;
import net.rezolv.obsidanum.entity.meat_beetle.MeetBeetleRenderer;
import net.rezolv.obsidanum.entity.mutated_gart.MutatedGartRenderer;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElementalRenderer;
import net.rezolv.obsidanum.event.BlockBreakEventHandler;
import net.rezolv.obsidanum.event.TotemAnimationMessage;
import net.rezolv.obsidanum.fluid.ModFluidTypes;
import net.rezolv.obsidanum.fluid.ModFluids;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.ModItemProperties;
import net.rezolv.obsidanum.item.entity.ModEntitiesItem;
import net.rezolv.obsidanum.item.entity.client.ModBoatRenderer;
import net.rezolv.obsidanum.item.item_entity.arrows.DispenserRegistry;
import net.rezolv.obsidanum.item.item_entity.arrows.EntityTypeInit;
import net.rezolv.obsidanum.item.item_entity.arrows.flame_arrow.FlameArrowRenderer;
import net.rezolv.obsidanum.item.item_entity.arrows.obsidian_arrow.ObsidianArrowRenderer;
import net.rezolv.obsidanum.particle.ParticlesObs;
import net.rezolv.obsidanum.recipes.ObsidanRecipes;
import net.rezolv.obsidanum.sound.SoundsObs;
import net.rezolv.obsidanum.structures.WDAStructures;
import net.rezolv.obsidanum.structures.processors.RSProcessors;
import net.rezolv.obsidanum.tab.CreativeTabObs;
import net.rezolv.obsidanum.world.wood.ModWoodTypes;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Obsidanum.MOD_ID)
public class Obsidanum {
    public static final String MOD_ID = "obsidanum";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Сеть для анимации тотема
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "main"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    public Obsidanum() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RSProcessors.STRUCTURE_PROCESSOR.register(modEventBus);
        // Канал для анимации тотема
        CHANNEL.messageBuilder(TotemAnimationMessage.class, 0)
                .decoder(TotemAnimationMessage::decode)
                .encoder(TotemAnimationMessage::encode)
                .consumerMainThread(TotemAnimationMessage::handle)
                .add();
        WDAStructures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);
        ObsidanRecipes.SERIALIZERS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        ItemsObs.ITEMS.register(modEventBus);
        BlocksObs.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModEntitiesItem.ENTITIES.register(modEventBus);
        ParticlesObs.PARTICLE_TYPES.register(modEventBus);
        EntityTypeInit.ENTITY_TYPES.register(modEventBus);
        SoundsObs.register(modEventBus);
        EffectsObs.MOB_EFFECTS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModItemEntities.ENTITY_TYPES.register(modEventBus);
        CreativeTabObs.CREATIVE_MODE_TABS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        NeoForge.EVENT_BUS.register(new BlockBreakEventHandler());

        IronChestsBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);
        IronChestsContainerTypes.CONTAINERS.register(modEventBus);


    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        DispenserRegistry.registerBehaviors();

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ItemsObs.GART_SPANW_EGG);
            event.accept(ItemsObs.MUTATED_GART_SPANW_EGG);
            event.accept(ItemsObs.MEET_BEETLE_SPANW_EGG);
            event.accept(ItemsObs.OBSIDIAN_ELEMENTAL_SPANW_EGG);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {



            MenuScreens.register(IronChestsContainerTypes.OBSIDIAN_CHEST.get(), IronChestScreen::new);
            BlockEntityRenderers.register(IronChestsBlockEntityTypes.OBSIDIAN_CHEST.get(), IronChestRenderer::new);
            MenuScreens.register(IronChestsContainerTypes.RUNIC_OBSIDIAN_CHEST.get(), IronChestScreen::new);
            BlockEntityRenderers.register(IronChestsBlockEntityTypes.RUNIC_OBSIDIAN_CHEST.get(), IronChestRenderer::new);

            event.enqueueWork(() -> ModItemProperties.register());

            EntityRenderers.register(EntityTypeInit.OBSIDIAN_ARROW.get(), ObsidianArrowRenderer::new);
            EntityRenderers.register(EntityTypeInit.FLAME_ARROW.get(), FlameArrowRenderer::new);
            EntityRenderers.register(ModEntities.OBSIDIAN_ELEMENTAL.get(), ObsidianElementalRenderer::new);
            EntityRenderers.register(ModEntities.MEET_BEETLE.get(), MeetBeetleRenderer::new);
            EntityRenderers.register(ModEntities.GART.get(), GartRenderer::new);
            EntityRenderers.register(ModEntities.MUTATED_GART.get(), MutatedGartRenderer::new);
            EntityRenderers.register(ModEntitiesItem.MOD_BOAT.get(), pContext -> new ModBoatRenderer(pContext, false));
            EntityRenderers.register(ModEntitiesItem.MOD_CHEST_BOAT.get(), pContext -> new ModBoatRenderer(pContext, true));
            event.enqueueWork(() -> {
                ComposterBlock.COMPOSTABLES.put(ItemsObs.OBSIDAN_WOOD_LEAVES.get(), 0.3f);
                ComposterBlock.COMPOSTABLES.put(ItemsObs.OBSIDAN_SAPLING.get(), 0.2f);
                ComposterBlock.COMPOSTABLES.put(ItemsObs.GLOOMY_MUSHROOM.get(), 0.2f);
                ComposterBlock.COMPOSTABLES.put(ItemsObs.THE_GLOOMY_MYCELIUM.get(), 0.2f);
                ComposterBlock.COMPOSTABLES.put(ItemsObs.STEM_GLOOMY_MUSHROOM.get(), 0.4f);
                ComposterBlock.COMPOSTABLES.put(ItemsObs.CAP_GLOOMY_MUSHROOM.get(), 0.4f);
                ComposterBlock.COMPOSTABLES.put(ItemsObs.SPORES_OF_THE_GLOOMY_MUSHROOM.get(), 0.4f);
            });
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_NETHER_FIRE_LAVA.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_NETHER_FIRE_LAVA.get(), RenderType.solid());
            Sheets.addWoodType(ModWoodTypes.OBSIDAN);
        }
    }
}