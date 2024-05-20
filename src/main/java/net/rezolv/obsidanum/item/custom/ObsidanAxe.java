package net.rezolv.obsidanum.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class ObsidanAxe extends AxeItem {

    private static final TagKey<Block> MINEABLE_LOGS_TAG = BlockTags.create(new ResourceLocation("minecraft", "logs"));
    private static final TagKey<Block> MINEABLE_LEAVES_TAG = BlockTags.create(new ResourceLocation("minecraft", "leaves"));

    private boolean activated = false;
    private long lastActivationTime = 0;
    private static final long COOLDOWN_DURATION = 15 * 20; // 60 seconds in ticks
    private static final long ACTIVATION_DURATION = 5 * 20; // 5 seconds in ticks


    public ObsidanAxe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }


    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClientSide && activated && world.getGameTime() - lastActivationTime >= ACTIVATION_DURATION) {
            if (entity instanceof Player) {
                deactivate();
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        long currentTime = worldIn.getGameTime();
        if (!activated && currentTime - lastActivationTime >= COOLDOWN_DURATION) {
            if (!worldIn.isClientSide) {
                    activate();
                    lastActivationTime = currentTime;
                playerIn.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION); // Устанавливаем визуальный кулдаун

            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, playerIn.getItemInHand(handIn));
        }
    }



    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if (activated) {
            list.add(Component.translatable("item.obsidan.description.enabled"));
        } else {
            list.add(Component.translatable("item.obsidan.description.disabled"));
        }
        long currentTime = world != null ? world.getGameTime() : 0;
        long timeLeft = lastActivationTime + COOLDOWN_DURATION - currentTime;
        if (timeLeft > 0) {
            int secondsLeft = (int) (timeLeft / 20); // Перевод времени из тиков в секунды
            list.add(Component.translatable("item.obsidan.description.cooldown"));
            list.add(Component.keybind("" + secondsLeft));
        }
    }

    public void activate() {
        activated = true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return activated;
    }

    public void deactivate() {
        activated = false;
        // Здесь можно добавить дополнительный код для деактивации (например, создание частиц)
    }
    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!world.isClientSide && activated && entity instanceof Player) {
            Block block = state.getBlock();
            if (block.defaultBlockState().is(MINEABLE_LOGS_TAG) || block.defaultBlockState().is(MINEABLE_LEAVES_TAG)) {
                chainBreak(world, pos, (Player) entity, stack);
                deactivate();
                return true;
            }
        }
        return super.mineBlock(stack, world, state, pos, entity);
    }

    private void breakBlock(Level world, BlockPos pos, Player player, ItemStack stack, Set<BlockPos> visited, AtomicInteger blockBreakCount) {
        if (visited.contains(pos) || blockBreakCount.get() >= 225) return;
        visited.add(pos);

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block.defaultBlockState().is(MINEABLE_LOGS_TAG) || block.defaultBlockState().is(MINEABLE_LEAVES_TAG) || block == Blocks.NETHER_WART_BLOCK || block == Blocks.WARPED_WART_BLOCK || block == Blocks.SHROOMLIGHT) {
            world.destroyBlock(pos, true);
            blockBreakCount.incrementAndGet(); // Увеличиваем счётчик разрушенных блоков
            for (BlockPos offset : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                breakBlock(world, offset, player, stack, visited, blockBreakCount);
            }
        }
    }

    private void chainBreak(Level world, BlockPos pos, Player player, ItemStack stack) {
        Set<BlockPos> visited = new HashSet<>();
        AtomicInteger blockBreakCount = new AtomicInteger(0);
        breakBlock(world, pos, player, stack, visited, blockBreakCount);
    }
}