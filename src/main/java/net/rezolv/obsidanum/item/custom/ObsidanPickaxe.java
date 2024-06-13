package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;



public class ObsidanPickaxe extends PickaxeItem {

    private boolean activated = false;
    private long lastActivationTime = 0;
    private static final long COOLDOWN_DURATION = 120 * 20; // 30 seconds in ticks
    private static final long ACTIVATION_DURATION = 5 * 20; // 5 seconds in ticks

    public ObsidanPickaxe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClientSide && activated && world.getGameTime() - lastActivationTime >= ACTIVATION_DURATION) {

            deactivate((Player) entity);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        long currentTime = worldIn.getGameTime();
        ItemStack itemStack = playerIn.getItemInHand(handIn);

        if (!activated && currentTime - lastActivationTime >= COOLDOWN_DURATION) {
            if (!worldIn.isClientSide) {
                activate(playerIn);
                lastActivationTime = currentTime;
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        }
        else {
            return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
        }
    }
    public boolean isActivated() {
        return activated;
    }
    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && activated) {
            Block block = pState.getBlock();

            if (isInstantBreakBlock(block)) {
                // Немедленно разрушаем блок
                pLevel.destroyBlock(pPos, false);

                // Шанс выпадения алмаза
                if (pLevel.random.nextFloat() < 0.15f) {
                    ItemStack diamond = new ItemStack(Items.DIAMOND);
                    Block.popResource(pLevel, pPos, diamond);
                }

                // Деактивируем кирку после разрушения блока
                deactivate((Player) pEntityLiving);
            }
        }

        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }




    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if(Screen.hasShiftDown()) {
            list.add(Component.translatable("obsidanum.press_shift2").withStyle(ChatFormatting.DARK_GRAY));
            list.add(Component.translatable("item.obsidan.description.pickaxe").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            list.add(Component.translatable("obsidanum.press_shift").withStyle(ChatFormatting.DARK_GRAY));
        }

    }

    public void activate(Player player) {
        activated = true;
    }

    public void deactivate(Player player) {
        activated = false;
        player.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION); // Устанавливаем визуальный кулдаун для общего кулдауна
    }
    private static final Block[] INSTANT_BREAK_BLOCKS = {
            Blocks.STONE,
            Blocks.COBBLESTONE,
            Blocks.DIORITE,
            Blocks.GRANITE,
            Blocks.ANDESITE,
            Blocks.DEEPSLATE,
            Blocks.COBBLED_DEEPSLATE
            // Добавьте другие блоки по вашему усмотрению
    };

    private boolean isInstantBreakBlock(Block block) {
        for (Block instantBreakBlock : INSTANT_BREAK_BLOCKS) {
            if (block == instantBreakBlock) {
                return true;
            }
        }
        return false;
    }
}