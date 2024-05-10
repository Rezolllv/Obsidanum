package net.rezolv.obsidanum.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.rezolv.obsidanum.fluid.ModFluids;

public class NetherFlame extends Item {
    public NetherFlame(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    @Override
    public boolean isRepairable(ItemStack itemstack) {
        return false;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
        ItemStack retval = new ItemStack(this);
        retval.setDamageValue(itemstack.getDamageValue() + 1);
        if (retval.getDamageValue() >= retval.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return retval;
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        // Получаем необходимые объекты из контекста использования
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        InteractionHand hand = context.getHand();
        ItemStack itemStack = context.getItemInHand();
        Direction clickedFace = context.getClickedFace();

        // Проверяем, есть ли игрок и является ли предмет `NetherFlame`
        if (player != null && itemStack.getItem() == this) {
            // Определяем позицию для установки лавы
            BlockPos posBelow = pos.relative(clickedFace);

            // Проверяем, можно ли установить текучую лаву на указанном месте
            if (world.isEmptyBlock(posBelow) && world instanceof ServerLevel) {
                // Создайте FluidState текучей лавы
                FluidState lavaFluidState = ModFluids.FLOWING_NETHER_FIRE_LAVA.get().getFlowing(3, true);  // 1 - уровень жидкости


                // Преобразуйте FluidState в BlockState
                BlockState lavaBlockState = lavaFluidState.createLegacyBlock();

                // Установите текучую лаву (BlockState) на выбранной позиции
                ((ServerLevel) world).setBlockAndUpdate(posBelow, lavaBlockState);

                // Уменьшите прочность предмета
                itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));

                // Проиграйте анимацию руки игрока
                player.swing(hand);
                world.playSound(null, posBelow, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);

                // Верните успешный результат
                return InteractionResult.SUCCESS;
            }
        }

        // Если ничего не выполнено, возвращаем стандартный результат использования
        return InteractionResult.PASS;
    }
}
