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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class ObsidanAxe extends AxeItem {
    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }
    private static final TagKey<Block> MINEABLE_LOGS_TAG = BlockTags.create(new ResourceLocation("minecraft", "logs"));
    private static final TagKey<Block> MINEABLE_LEAVES_TAG = BlockTags.create(new ResourceLocation("minecraft", "leaves"));

    private static final long COOLDOWN_DURATION = 40 * 20; // 40 секунд в тиках
    private static final long ACTIVATION_DURATION = 5 * 20;  // 5 секунд в тиках

    public ObsidanAxe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClientSide && isActivated(stack)) {
            long lastActivationTime = getLastActivationTime(stack);
            if (world.getGameTime() - lastActivationTime >= ACTIVATION_DURATION) {
                if (entity instanceof Player) {
                    deactivate(stack, (Player) entity);
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        long currentTime = worldIn.getGameTime();

        if (!isActivated(stack) && currentTime - getLastActivationTime(stack) >= COOLDOWN_DURATION) {
            if (!worldIn.isClientSide) {
                activate(stack);
                setLastActivationTime(stack, currentTime);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }
    }

    public boolean isActivated(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Activated");
    }

    private long getLastActivationTime(ItemStack stack) {
        return stack.getOrCreateTag().getLong("LastActivationTime");
    }

    private void setLastActivationTime(ItemStack stack, long time) {
        stack.getOrCreateTag().putLong("LastActivationTime", time);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("obsidanum.press_shift2").withStyle(ChatFormatting.DARK_GRAY));
            list.add(Component.translatable("item.obsidan.description.axe").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            list.add(Component.translatable("obsidanum.press_shift").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public void activate(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("Activated", true);
        stack.getOrCreateTag().putInt("CustomModelData", 1);
        stack.getOrCreateTag().putBoolean("DurabilityLost", false);
    }

    public void deactivate(ItemStack stack, Player player) {
        stack.getOrCreateTag().putBoolean("Activated", false);
        stack.getOrCreateTag().putInt("CustomModelData", 0);
        stack.getOrCreateTag().putBoolean("DurabilityLost", false);
        player.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!world.isClientSide && isActivated(stack) && entity instanceof Player) {
            // Если блок является обычным стволом, листьями или незерским стволом – выполняем цепное разрушение
            if (state.is(MINEABLE_LOGS_TAG) || state.is(MINEABLE_LEAVES_TAG) || isNetherLog(state) || isNetherFungus(state)) {
                chainBreak(world, pos, (Player) entity, stack, state);
                deactivate(stack, (Player) entity);
                return true;
            }
        }
        return super.mineBlock(stack, world, state, pos, entity);
    }

    /**
     * Определяем, является ли блок незерским стволом (Crimson или Warped)
     */
    private boolean isNetherLog(BlockState state) {
        return state.getBlock() == Blocks.CRIMSON_STEM || state.getBlock() == Blocks.WARPED_STEM;
    }

    /**
     * Определяем, является ли блок незерским наростом или светогрибом.
     */
    private boolean isNetherFungus(BlockState state) {
        return state.getBlock() == Blocks.NETHER_WART_BLOCK ||
                state.getBlock() == Blocks.WARPED_WART_BLOCK ||
                state.getBlock() == Blocks.SHROOMLIGHT;
    }

    /**
     * Для незерских блоков при добыче: разрушаем ВСЁ, что относится к дереву (и ствол, и наросты, и светогриб)
     */
    private void chainBreak(Level world, BlockPos pos, Player player, ItemStack stack, BlockState state) {
        if (isNetherLog(state) || isNetherFungus(state)) {
            breakNetherTree(world, pos, player, stack);
        }
        else if (state.is(MINEABLE_LOGS_TAG)) {
            breakTree(world, pos, player, stack);
        }
        else if (state.is(MINEABLE_LEAVES_TAG)) {
            breakPlants(world, pos, player, stack);
        }
    }

    /**
     * Обработка обычных деревьев (сбор ствола и листьев)
     */
    private void breakTree(Level world, BlockPos startPos, Player player, ItemStack stack) {
        Set<BlockPos> logs = new HashSet<>();
        Set<BlockPos> leaves = new HashSet<>();

        findConnectedBlocks(world, startPos, logs, MINEABLE_LOGS_TAG, 1024);

        for (BlockPos logPos : logs) {
            findLeaves(world, logPos, leaves, 2048);
        }

        destroyBlocks(world, logs, player, stack);
        destroyBlocks(world, leaves, player, stack);
    }

    /**
     * Обработка незерских деревьев – собираем ВСЕ связанные блоки, относящиеся к дереву (и ствол, и наросты, и светогриб)
     */
    private void breakNetherTree(Level world, BlockPos startPos, Player player, ItemStack stack) {
        Set<BlockPos> netherBlocks = new HashSet<>();
        findConnectedNetherTreeBlocks(world, startPos, netherBlocks, 2048);
        destroyBlocks(world, netherBlocks, player, stack);
    }

    /**
     * Рекурсивно ищем связанные незерские блоки (как ствол, так и наросты/светогриб)
     */
    private void findConnectedNetherTreeBlocks(Level world, BlockPos pos, Set<BlockPos> result, int max) {
        Deque<BlockPos> queue = new ArrayDeque<>();
        queue.add(pos);

        while (!queue.isEmpty() && result.size() < max) {
            BlockPos current = queue.poll();
            if (result.contains(current)) continue;
            BlockState state = world.getBlockState(current);
            if (isNetherLog(state) || isNetherFungus(state)) {
                result.add(current);
                queue.add(current.offset(1, 0, 0));
                queue.add(current.offset(-1, 0, 0));
                queue.add(current.offset(0, 0, 1));
                queue.add(current.offset(0, 0, -1));
                queue.add(current.offset(0, 1, 0));
                queue.add(current.offset(0, -1, 0));
            }
        }
    }

    private void findConnectedBlocks(Level world, BlockPos pos, Set<BlockPos> result, TagKey<Block> tag, int max) {
        Deque<BlockPos> queue = new ArrayDeque<>();
        queue.add(pos);

        while (!queue.isEmpty() && result.size() < max) {
            BlockPos current = queue.poll();
            if (result.contains(current)) continue;

            BlockState state = world.getBlockState(current);
            if (state.is(tag)) {
                result.add(current);
                queue.add(current.offset(1, 0, 0));
                queue.add(current.offset(-1, 0, 0));
                queue.add(current.offset(0, 0, 1));
                queue.add(current.offset(0, 0, -1));
                queue.add(current.offset(0, 1, 0));
                queue.add(current.offset(0, -1, 0));
            }
        }
    }

    private void findLeaves(Level world, BlockPos pos, Set<BlockPos> result, int max) {
        BlockPos.betweenClosedStream(pos.offset(-5, -3, -5), pos.offset(5, 3, 5))
                .filter(p -> world.getBlockState(p).is(MINEABLE_LEAVES_TAG))
                .limit(max)
                .forEach(p -> result.add(p.immutable()));
    }

    private void breakPlants(Level world, BlockPos pos, Player player, ItemStack stack) {
        AtomicInteger counter = new AtomicInteger(0);
        BlockPos.betweenClosedStream(pos.offset(-7, -3, -7), pos.offset(7, 3, 7))
                .filter(p -> world.getBlockState(p).is(MINEABLE_LEAVES_TAG))
                .limit(300)
                .forEach(p -> {
                    if (counter.getAndIncrement() < 300) {
                        world.destroyBlock(p, true);
                    }
                });
    }

    private void destroyBlocks(Level world, Collection<BlockPos> positions, Player player, ItemStack stack) {
        boolean durabilityLost = stack.getOrCreateTag().getBoolean("DurabilityLost");

        positions.forEach(p -> {
            if (p.distSqr(player.blockPosition()) < 4096) {
                world.destroyBlock(p, true);
                if (!durabilityLost) {
                    stack.getOrCreateTag().putBoolean("DurabilityLost", true);
                }
            }
        });
    }
}