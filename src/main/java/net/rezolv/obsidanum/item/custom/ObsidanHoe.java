package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;


public class ObsidanHoe extends HoeItem {

    private boolean activated = false;
    private long lastActivationTime = 0;
    private static final long COOLDOWN_DURATION = 10 * 20; // 60 seconds in ticks
    private static final double BREAK_RADIUS_SQUARED = 20 * 20; // Radius of 20 blocks squared
    private static final long ACTIVATION_DURATION = 5 * 20; // 5 seconds in ticks


    public ObsidanHoe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    public boolean isActivated() {
        return activated;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        long currentTime = worldIn.getGameTime();
        if (activated || currentTime - lastActivationTime >= COOLDOWN_DURATION) {
            if (!worldIn.isClientSide) {
                if (activated) {
                    playerIn.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION); // Устанавливаем визуальный кулдаун

                    deactivate(playerIn);
                    int radiusSquared = (int) Math.sqrt(BREAK_RADIUS_SQUARED);
                    // Получение позиции игрока
                    BlockPos playerPos = playerIn.blockPosition();
                    // Список блоков травы
                    BlockState[] grassBlocks = {
                            Blocks.GRASS.defaultBlockState(),
                            Blocks.TALL_GRASS.defaultBlockState(),
                            Blocks.FERN.defaultBlockState(),
                            Blocks.DEAD_BUSH.defaultBlockState(),
                            Blocks.CRIMSON_ROOTS.defaultBlockState(),
                            Blocks.WARPED_ROOTS.defaultBlockState(),
                            Blocks.FIRE.defaultBlockState(),
                            Blocks.FERN.defaultBlockState(),
                            Blocks.LARGE_FERN.defaultBlockState(),
                            Blocks.NETHER_SPROUTS.defaultBlockState()
                    };

                    // Проверка каждого блока в радиусе вокруг игрока
                    for (int x = -radiusSquared; x <= radiusSquared; x++) {
                        for (int y = -radiusSquared; y <= radiusSquared; y++) {
                            for (int z = -radiusSquared; z <= radiusSquared; z++) {
                                BlockPos blockPos = playerPos.offset(x, y, z);
                                // Проверка, находится ли блок в пределах мира
                                if (worldIn.isLoaded(blockPos)) {
                                    // Проверка, является ли блок одним из блоков травы
                                    for (BlockState grassBlock : grassBlocks) {
                                        if (worldIn.getBlockState(blockPos).getBlock() == grassBlock.getBlock()) {
                                            // Уничтожение блока
                                            worldIn.destroyBlock(blockPos, true);
                                            break; // Прерывание цикла после первого совпадения
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Код для деактивации, например, удаление частиц и т.д.
                } else {

                    activate();
                    // Код для активации, например, создание частиц и т.д.
                    lastActivationTime = currentTime;
                }

            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, playerIn.getItemInHand(handIn));
        }
    }

    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if(Screen.hasShiftDown()) {
            list.add(Component.translatable("obsidanum.press_shift2").withStyle(ChatFormatting.DARK_GRAY));
            list.add(Component.translatable("item.obsidan.description.hoe").withStyle(ChatFormatting.DARK_PURPLE));
        } else {
            list.add(Component.translatable("obsidanum.press_shift").withStyle(ChatFormatting.DARK_GRAY));
        }


    }

    public void activate() {
        activated = true;
    }


    @Override
    public void inventoryTick(ItemStack stack, Level world, net.minecraft.world.entity.Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClientSide && activated && world.getGameTime() - lastActivationTime >= ACTIVATION_DURATION) {
            if (entity instanceof Player) {
                deactivate((Player) entity);
            }
        }
    }
    public void deactivate(Player player) {
        activated = false;
        player.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION); // Устанавливаем визуальный кулдаун для общего кулдауна
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