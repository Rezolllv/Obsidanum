package net.rezolv.obsidanum.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.rezolv.obsidanum.item.ItemsObs;

import java.util.Random;

public class DrillingCrystallizer extends Item {
    private static final Random RANDOM = new Random();

    public DrillingCrystallizer(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // Получение уровня, позиции и состояния блока
        if (!(context.getLevel() instanceof ServerLevel)) {
            return InteractionResult.FAIL;
        }

        ServerLevel level = (ServerLevel) context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);

        // Проверка, что целевой блок является плачущим обсидианом
        if (blockState.is(Blocks.CRYING_OBSIDIAN)) {
            // Преобразование плачущего обсидиана в обсидиан
            level.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3);

            // Шанс выпадения хоруса (30%)
            if (RANDOM.nextInt(100) < 30) {
                // Выпадение хоруса
                Block.popResource(level, pos, new ItemStack(ItemsObs.OBSIDIAN_TEAR.get()));
            }

            // Уменьшение прочности предмета на 1
            ItemStack itemStack = context.getItemInHand();
            if (context.getPlayer() != null) {
                itemStack.hurtAndBreak(1, context.getPlayer(), player -> {
                    player.broadcastBreakEvent(context.getHand());
                });
            }

            // Возвращение результата использования
            return InteractionResult.SUCCESS;
        }

        // Если целевой блок не является плачущим обсидианом
        return InteractionResult.FAIL;
    }
}