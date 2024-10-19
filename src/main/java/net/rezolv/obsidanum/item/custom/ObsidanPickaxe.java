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

    private static final long COOLDOWN_DURATION = 120 * 20; // 120 секунд в тиках
    private static final long ACTIVATION_DURATION = 5 * 20; // 5 секунд в тиках

    public ObsidanPickaxe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!world.isClientSide && isActivated(stack)) {
            long currentTime = world.getGameTime();
            long lastActivationTime = stack.getOrCreateTag().getLong("LastActivationTime");

            if (currentTime - lastActivationTime >= ACTIVATION_DURATION) {
                if (entity instanceof Player) {
                    deactivate(stack, (Player) entity); // Деактивируем кирку после времени активации
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        long currentTime = worldIn.getGameTime();
        ItemStack itemStack = playerIn.getItemInHand(handIn);
        long lastActivationTime = itemStack.getOrCreateTag().getLong("LastActivationTime");

        if (!isActivated(itemStack) && currentTime - lastActivationTime >= COOLDOWN_DURATION) {
            if (!worldIn.isClientSide) {
                activate(itemStack, currentTime);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
        }
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && isActivated(pStack)) {
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
                if (pEntityLiving instanceof Player) {
                    deactivate(pStack, (Player) pEntityLiving);
                }
            }
        }

        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("obsidanum.press_shift2").withStyle(ChatFormatting.DARK_GRAY));
            list.add(Component.translatable("item.obsidan.description.pickaxe").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            list.add(Component.translatable("obsidanum.press_shift").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public void activate(ItemStack stack, long currentTime) {
        stack.getOrCreateTag().putBoolean("Activated", true);
        stack.getOrCreateTag().putLong("LastActivationTime", currentTime);
        stack.getOrCreateTag().putInt("CustomModelData", 1); // Обновляем модель на активированную
    }

    public void deactivate(ItemStack stack, Player player) {
        stack.getOrCreateTag().putBoolean("Activated", false);
        stack.getOrCreateTag().putInt("CustomModelData", 0); // Возвращаем обычную модель
        player.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION); // Устанавливаем кулдаун
    }

    public boolean isActivated(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Activated");
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