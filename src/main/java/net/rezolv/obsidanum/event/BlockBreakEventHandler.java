package net.rezolv.obsidanum.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.item.item_entity.obsidan_chakram.ObsidianChakramEntity;

@Mod.EventBusSubscriber(modid = "obsidanum")
public class BlockBreakEventHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        ServerLevel world = (ServerLevel) event.getLevel();
        for (Entity entity : world.getEntities(null, new AABB(pos))) {
            if (entity instanceof ObsidianChakramEntity) {
                ((ObsidianChakramEntity) entity).dropAsItem();
            }
        }
    }
}