package net.rezolv.obsidanum.item.custom;

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


    public ObsidanHoe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        long currentTime = worldIn.getGameTime();
        if (activated || currentTime - lastActivationTime >= COOLDOWN_DURATION) {
            if (!worldIn.isClientSide) {
                if (activated) {
                    deactivate(playerIn, worldIn);
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
        return activated; // Возвращает true только когда инструмент активирован

    }

    public void deactivate(Player playerIn, Level worldIn) {
        activated = false;
        // Определение радиуса в квадратных блоках
        int radiusSquared = (int) Math.sqrt(BREAK_RADIUS_SQUARED);
        // Получение позиции игрока
        BlockPos playerPos = playerIn.blockPosition();
        // Список блоков травы
        BlockState[] grassBlocks = {
                Blocks.GRASS.defaultBlockState(),
                Blocks.TALL_GRASS.defaultBlockState(),
                Blocks.FERN.defaultBlockState(),
                Blocks.DEAD_BUSH.defaultBlockState()
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
    }
}