package net.rezolv.obsidanum.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.block.custom.GloomyMycelium2;
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