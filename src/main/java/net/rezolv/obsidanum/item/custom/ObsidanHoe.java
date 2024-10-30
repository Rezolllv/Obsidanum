package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;


public class ObsidanHoe extends HoeItem {

    private static final long COOLDOWN_DURATION = 10 * 20; // 10 seconds in ticks
    private static final long ACTIVATION_DURATION = 5 * 20; // 5 seconds in ticks
    private static final String TAG_ACTIVATED = "Activated";
    private static final String TAG_LAST_ACTIVATION_TIME = "LastActivationTime";

    public ObsidanHoe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public boolean isActivated(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(TAG_ACTIVATED);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        long currentTime = worldIn.getGameTime();
        ItemStack itemStack = playerIn.getItemInHand(handIn);

        // Get the last activation time from the item stack
        long lastActivationTime = itemStack.getOrCreateTag().getLong(TAG_LAST_ACTIVATION_TIME);

        if (!isActivated(itemStack) && currentTime - lastActivationTime >= COOLDOWN_DURATION) {
            if (!worldIn.isClientSide) {
                activate(itemStack, currentTime);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, itemStack);
        }
    }

    private static final BlockState[] TARGET_BLOCKS = {
            Blocks.GRASS.defaultBlockState(),
            Blocks.TALL_GRASS.defaultBlockState(),
            Blocks.FERN.defaultBlockState(),
            Blocks.DEAD_BUSH.defaultBlockState(),
            Blocks.CRIMSON_ROOTS.defaultBlockState(),
            Blocks.WARPED_ROOTS.defaultBlockState(),
            Blocks.FIRE.defaultBlockState(),
            Blocks.LARGE_FERN.defaultBlockState(),
            Blocks.NETHER_SPROUTS.defaultBlockState(),
            Blocks.SCULK.defaultBlockState(),
            Blocks.SCULK_VEIN.defaultBlockState(),
            Blocks.SCULK_SENSOR.defaultBlockState(),
            Blocks.SCULK_SHRIEKER.defaultBlockState(),
            Blocks.KELP.defaultBlockState(),
            Blocks.KELP_PLANT.defaultBlockState(),
            Blocks.LILY_PAD.defaultBlockState(),
            Blocks.MOSS_BLOCK.defaultBlockState(),
            Blocks.CAVE_VINES.defaultBlockState(),
            Blocks.CAVE_VINES_PLANT.defaultBlockState(),
            Blocks.MOSS_CARPET.defaultBlockState(),
            Blocks.SPORE_BLOSSOM.defaultBlockState(),
            Blocks.GLOW_LICHEN.defaultBlockState()
    };

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, net.minecraft.world.entity.LivingEntity entity) {
        if (!level.isClientSide && isActivated(stack)) {
            for (BlockState targetBlock : TARGET_BLOCKS) {
                if (state.is(targetBlock.getBlock())) {
                    BlockPos playerPos = entity.blockPosition();
                    for (BlockPos blockPos : BlockPos.betweenClosed(playerPos.offset(-20, -20, -20), playerPos.offset(20, 20, 20))) {
                        BlockState targetState = level.getBlockState(blockPos);
                        for (BlockState targetBlockInner : TARGET_BLOCKS) {
                            if (targetState.is(targetBlockInner.getBlock())) {
                                level.destroyBlock(blockPos, true);
                                break;
                            }
                        }
                    }
                    deactivate(stack, (Player) entity);
                    break;
                }
            }
        }
        return super.mineBlock(stack, level, state, pos, entity);
    }

    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("obsidanum.press_shift2").withStyle(ChatFormatting.DARK_GRAY));
            list.add(Component.translatable("item.obsidan.description.hoe").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            list.add(Component.translatable("obsidanum.press_shift").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public void activate(ItemStack stack, long currentTime) {
        stack.getOrCreateTag().putBoolean(TAG_ACTIVATED, true);
        stack.getOrCreateTag().putLong(TAG_LAST_ACTIVATION_TIME, currentTime); // Save the activation time
        stack.getOrCreateTag().putInt("CustomModelData", 1); // Update model
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, net.minecraft.world.entity.Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClientSide && isActivated(stack)) {
            long currentTime = world.getGameTime();
            long lastActivationTime = stack.getOrCreateTag().getLong(TAG_LAST_ACTIVATION_TIME);

            if (currentTime - lastActivationTime >= ACTIVATION_DURATION) {
                if (entity instanceof Player) {
                    deactivate(stack, (Player) entity);
                }
            }
        }
    }

    public void deactivate(ItemStack stack, Player player) {
        stack.getOrCreateTag().putBoolean(TAG_ACTIVATED, false);
        stack.getOrCreateTag().putInt("CustomModelData", 0); // Return to default model
        player.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION); // Set cooldown
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        ItemStack stack = context.getItemInHand();
        BlockState state = world.getBlockState(pos);

        if (player == null) {
            return InteractionResult.FAIL;
        }

        if (!player.mayUseItemAt(pos.relative(context.getClickedFace()), context.getClickedFace(), stack)) {
            return InteractionResult.FAIL;
        }

        if (state.is(BlockTags.DIRT) || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT_PATH) || state.is(Blocks.COARSE_DIRT)) {
            world.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide) {
                for (BlockPos targetPos : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 0, 1))) {
                    BlockState targetState = world.getBlockState(targetPos);
                    if (targetState.is(BlockTags.DIRT) || targetState.is(Blocks.GRASS_BLOCK) || targetState.is(Blocks.DIRT_PATH) || targetState.is(Blocks.COARSE_DIRT)) {
                        world.setBlock(targetPos, Blocks.FARMLAND.defaultBlockState(), 11);
                        world.levelEvent(2001, targetPos, Block.getId(targetState));
                    }
                }
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}