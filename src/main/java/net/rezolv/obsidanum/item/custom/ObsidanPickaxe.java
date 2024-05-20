package net.rezolv.obsidanum.item.custom;

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
        } else {
            return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
        }
    }
    public boolean isActivated() {
        return activated;
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (!world.isClientSide && activated && player != null) {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (isInstantBreakBlock(block)) {
                // Немедленно разрушаем блок
                world.destroyBlock(pos, false);

                // Шанс выпадения алмаза
                if (world.random.nextFloat() < 0.3f) {
                    ItemStack diamond = new ItemStack(Items.DIAMOND);
                    Block.popResource(world, pos, diamond);
                }

                deactivate(player);
                player.getCooldowns().addCooldown(this, (int) COOLDOWN_DURATION); // Устанавливаем визуальный кулдаун

                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
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
            list.add(Component.translatable("item.obsidan.description.cooldown", secondsLeft));
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