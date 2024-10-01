package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class ObsidanAxe extends AxeItem {

    private static final TagKey<Block> MINEABLE_LOGS_TAG = BlockTags.create(new ResourceLocation("minecraft", "logs"));
    private static final TagKey<Block> MINEABLE_LEAVES_TAG = BlockTags.create(new ResourceLocation("minecraft", "leaves"));

    private boolean activated = false;
    private long lastActivationTime = 0;
    private static final long COOLDOWN_DURATION = 40 * 20; // 60 seconds in ticks
    private static final long ACTIVATION_DURATION = 5 * 20; // 5 seconds in ticks


    public ObsidanAxe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }


    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClientSide && activated && world.getGameTime() - lastActivationTime >= ACTIVATION_DURATION) {
            if (entity instanceof Player) {
                deactivate(stack, (Player) entity); // Передаем stack и player в метод деактивации
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn); // Получаем предмет
        long currentTime = worldIn.getGameTime();

        if (!activated && currentTime - lastActivationTime >= COOLDOWN_DURATION) {
            if (!worldIn.isClientSide) {
                activate(stack); // Передаем stack в метод активации
                lastActivationTime = currentTime;
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }
    }

    public boolean isActivated() {
        return activated;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if(Screen.hasShiftDown()) {
            list.add(Component.translatable("obsidanum.press_shift2").withStyle(ChatFormatting.DARK_GRAY));
            list.add(Component.translatable("item.obsidan.description.axe").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            list.add(Component.translatable("obsidanum.press_shift").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public void activate(ItemStack stack) {
        activated = true;
        // Сохраняем состояние активации в NBT
        stack.getOrCreateTag().putBoolean("Activated", true);
        stack.getOrCreateTag().putInt("CustomModelData", 1); // Обновляем модель
    }



    public void deactivate(ItemStack stack, Player player) {
        activated = false;
        // Сохраняем состояние деактивации в NBT
        stack.getOrCreateTag().putBoolean("Activated", false);
        stack.getOrCreateTag().putInt("CustomModelData", 0); // Возвращаем обычную модель

        player.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION); // Устанавливаем кулдаун
    }
    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!world.isClientSide && activated && entity instanceof Player) {
            Block block = state.getBlock();
            if (block.defaultBlockState().is(MINEABLE_LOGS_TAG) || block.defaultBlockState().is(MINEABLE_LEAVES_TAG)) {
                chainBreak(world, pos, (Player) entity, stack); // Передаем stack
                deactivate(stack, (Player) entity); // Передаем stack и player в метод деактивации

                return true;
            }
        }
        return super.mineBlock(stack, world, state, pos, entity);
    }

    private void chainBreak(Level world, BlockPos pos, Player player, ItemStack stack) {
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        AtomicInteger blockBreakCount = new AtomicInteger(0);

        queue.offer(pos);
        visited.add(pos);

        while (!queue.isEmpty() && blockBreakCount.get() < 300) {
            BlockPos currentPos = queue.poll();
            breakBlock(world, currentPos, player, stack, visited, queue, blockBreakCount);
        }
    }

    private void breakBlock(Level world, BlockPos pos, Player player, ItemStack stack, Set<BlockPos> visited, Queue<BlockPos> queue, AtomicInteger blockBreakCount) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block.defaultBlockState().is(MINEABLE_LOGS_TAG) || block.defaultBlockState().is(MINEABLE_LEAVES_TAG) || block == Blocks.NETHER_WART_BLOCK || block == Blocks.WARPED_WART_BLOCK || block == Blocks.SHROOMLIGHT) {
            world.destroyBlock(pos, true);
            blockBreakCount.incrementAndGet();

            // Добавляем соседние блоки
            for (BlockPos offset : getNeighbors(pos)) {
                if (!visited.contains(offset)) {
                    visited.add(offset);
                    queue.offer(offset);
                }
            }
        }
    }

    private Iterable<BlockPos> getNeighbors(BlockPos pos) {
        List<BlockPos> neighbors = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx != 0 || dy != 0 || dz != 0) {
                        neighbors.add(pos.offset(dx, dy, dz));
                    }
                }
            }
        }
        return neighbors;
    }
}