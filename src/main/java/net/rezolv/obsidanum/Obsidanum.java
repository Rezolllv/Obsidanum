package net.rezolv.obsidanum;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.block.entity.ModBlockEntities;
import net.rezolv.obsidanum.chests.SCRegistry;
import net.rezolv.obsidanum.effect.EffectsObs;
import net.rezolv.obsidanum.entity.ModEntities;
import net.rezolv.obsidanum.entity.ModItemEntities;
import net.rezolv.obsidanum.entity.gart.GartRenderer;
import net.rezolv.obsidanum.entity.meat_beetle.MeetBeetleRenderer;
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
            new ResourceLocation(MOD_ID, "main"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    public Obsidanum() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Канал для анимации тотема
        CHANNEL.messageBuilder(TotemAnimationMessage.class, 0)
                .decoder(TotemAnimationMessage::decode)
                .encoder(TotemAnimationMessage::encode)
                .consumerMainThread(TotemAnimationMessage::handle)
                .add();
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
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventHandler());
        SCRegistry.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener((BuildCreativeModeTabContentsEvent e) -> {
            if (e.getTabKey() == CreativeTabObs.OBSIDANUM_TAB.getKey()) {
                SCRegistry.ITEMS.getEntries()
                        .stream()
                        .map(RegistryObject::get)
                        .forEach(e::accept);
            }
        });
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        DispenserRegistry.registerBehaviors();

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ItemsObs.GART_SPANW_EGG);
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

            event.enqueueWork(() -> ModItemProperties.register());

            EntityRenderers.register(EntityTypeInit.OBSIDIAN_ARROW.get(), ObsidianArrowRenderer::new);
            EntityRenderers.register(EntityTypeInit.FLAME_ARROW.get(), FlameArrowRenderer::new);
            EntityRenderers.register(ModEntities.OBSIDIAN_ELEMENTAL.get(), ObsidianElementalRenderer::new);
            EntityRenderers.register(ModEntities.MEET_BEETLE.get(), MeetBeetleRenderer::new);
            EntityRenderers.register(ModEntities.GART.get(), GartRenderer::new);
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