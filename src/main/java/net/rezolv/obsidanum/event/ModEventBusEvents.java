package net.rezolv.obsidanum.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.ModEntities;
import net.rezolv.obsidanum.entity.gart.Gart;
import net.rezolv.obsidanum.entity.meat_beetle.MeetBeetle;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElemental;

@Mod.EventBusSubscriber(modid = Obsidanum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributesElemental(EntityAttributeCreationEvent event) {
        event.put(ModEntities.OBSIDIAN_ELEMENTAL.get(), ObsidianElemental.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributesBeetle(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MEET_BEETLE.get(), MeetBeetle.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerAttributesGart(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GART.get(), Gart.createAttributes().build());
    }

}
