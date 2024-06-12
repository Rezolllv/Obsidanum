package net.rezolv.obsidanum.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.ModEntities;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElemental;
@Mod.EventBusSubscriber(modid = Obsidanum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.OBSIDIAN_ELEMENTAL.get(), ObsidianElemental.createAttributes().build());
    }
}
