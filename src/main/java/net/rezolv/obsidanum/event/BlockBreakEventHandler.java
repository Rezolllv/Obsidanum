package net.rezolv.obsidanum.event;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
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

        for (Entity entity : world.getEntities(null, new AABB(pos).inflate(1))) {
            if (entity instanceof ObsidianChakramEntity) {
                BlockPos entityPos = entity.blockPosition();
                if (!hasBlockBelowOrAdjacent(world, entityPos)) {
                    ((ObsidianChakramEntity) entity).dropAsItem();
                }
            }
        }
    }

    private static boolean hasBlockBelowOrAdjacent(ServerLevel world, BlockPos pos) {
        // Проверяем блоки снизу и по всем сторонам
        BlockPos[] positionsToCheck = new BlockPos[] {
                pos.below(),
                pos.north(),
                pos.south(),
                pos.east(),
                pos.west()
        };

        for (BlockPos checkPos : positionsToCheck) {
            BlockState blockState = world.getBlockState(checkPos);
            if (!blockState.isAir()) {
                return true;
            }
        }
        return false;
    }
}