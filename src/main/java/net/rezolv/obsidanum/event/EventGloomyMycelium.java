package net.rezolv.obsidanum.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.block.custom.GloomyMycelium2;

@Mod.EventBusSubscriber
public class EventGloomyMycelium {

    @SubscribeEvent
    public static void onRightClickBW(PlayerInteractEvent.RightClickBlock event) {
        // Получаем данные о событии
        Player player = event.getEntity();
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState clickedState = world.getBlockState(pos);
        InteractionHand hand = event.getHand();
        Direction clickedFace = event.getFace(); // Грань, на которую кликнули

        // Проверяем, что это не клиентская сторона и клик выполнен основной рукой
        if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            // Проверяем, что клик был по верхней грани блока
            if (clickedFace == Direction.UP) {
                // Получаем позицию блока выше текущего
                BlockPos blockAbovePos = pos.above();
                BlockState blockAboveState = world.getBlockState(blockAbovePos);

                // Проверяем, что блок выше — это GloomyMycelium2
                if (blockAboveState.getBlock() instanceof GloomyMycelium2) {
                    // Проверяем, что в руке игрока нужный блок
                    if (player.getItemInHand(hand).getItem() == BlocksObs.THE_GLOOMY_MYCELIUM.get().asItem()) {
                        // Определяем направление блока и соответствующее свойство BW
                        Direction facing = blockAboveState.getValue(GloomyMycelium2.FACING);
                        BooleanProperty bwProperty = getBWProperty(facing);

                        // Проверяем: свойство BW не установлено и текущий блок надёжный
                        if (!blockAboveState.getValue(bwProperty) && clickedState.isFaceSturdy(world, pos, Direction.UP)) {
                            // Проигрываем звук установки
                            world.playSound(null, pos, net.minecraft.sounds.SoundEvents.VINE_PLACE,
                                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);

                            // Уменьшаем предмет в руке (если игрок не в креативе)
                            if (!player.isCreative()) {
                                player.getItemInHand(hand).shrink(1);
                            }

                            // Устанавливаем состояние BW на блоке выше
                            world.setBlock(blockAbovePos, blockAboveState.setValue(bwProperty, true), 3);

                            // Отменяем дальнейшую обработку события
                            event.setCancellationResult(InteractionResult.SUCCESS);
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    // Метод для получения соответствующего свойства BW на основе направления блока
    // BW - Bottom Wall
    private static BooleanProperty getBWProperty(Direction facing) {
        switch (facing) {
            case NORTH: return GloomyMycelium2.BWN;
            case SOUTH: return GloomyMycelium2.BWS;
            case EAST: return GloomyMycelium2.BWE;
            case WEST: return GloomyMycelium2.BWW;
            default: throw new IllegalArgumentException("Unexpected direction: " + facing);
        }
    }
}