package net.rezolv.obsidanum.funtions;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.item.ItemsObs;

@Mod.EventBusSubscriber(modid = "obsidanum", bus = Mod.EventBusSubscriber.Bus.FORGE)

public class AliveObsidian {
    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        InteractionHand hand = event.getHand();

        BlockState state = world.getBlockState(pos);

        if (state.is(Blocks.OBSIDIAN) && itemStack.is(ItemsObs.OBSIDIAN_TEAR.get())) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = world.getBlockState(abovePos);

            if (aboveState.is(Blocks.FIRE)) {
                if (!world.isClientSide) {
                    // Transform obsidian to crying obsidian
                    world.setBlock(pos, Blocks.CRYING_OBSIDIAN.defaultBlockState(), 3);

                    // Create portal particles around the block
                    if (world instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 50, 0.5, 0.5, 0.5, 0.1);
                    }

                    // Play beacon activation sound
                    world.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);

                    // Consume the item
                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }

                    // Notify neighbors
                    world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    // Grant the advancement
                    if (player instanceof ServerPlayer serverPlayer) {
                        Advancement advancement = serverPlayer.getServer().getAdvancements().getAdvancement(new ResourceLocation("obsidanum:resurrected"));
                        if (advancement != null) {
                            AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
                            if (!progress.isDone()) {
                                for (String criteria : progress.getRemainingCriteria()) {
                                    serverPlayer.getAdvancements().award(advancement, criteria);
                                }
                            }
                        }
                    }

                }
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}
