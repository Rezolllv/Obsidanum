package net.rezolv.obsidanum.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.ForgeEventFactory;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.particle.ParticlesObs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static net.minecraft.world.level.block.EnchantmentTableBlock.BOOKSHELF_OFFSETS;
import static net.minecraft.world.level.block.EnchantmentTableBlock.isValidBookShelf;

@Mixin(EnchantmentTableBlock.class)
public abstract class EnchantmentTableBlockMixin extends BaseEntityBlock {

    protected EnchantmentTableBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        Iterator var5 = BOOKSHELF_OFFSETS.iterator();

        while (var5.hasNext()) {
            BlockPos blockpos = (BlockPos) var5.next();
            if (pRandom.nextInt(16) == 0 && isValidBookShelf(pLevel, pPos, blockpos) && !isValidObsidianTablet(pLevel, pPos, blockpos)) {
                pLevel.addParticle(ParticleTypes.ENCHANT, (double) pPos.getX() + 0.5, (double) pPos.getY() + 2.0, (double) pPos.getZ() + 0.5, (double) ((float) blockpos.getX() + pRandom.nextFloat()) - 0.5, (double) ((float) blockpos.getY() - pRandom.nextFloat() - 1.0F), (double) ((float) blockpos.getZ() + pRandom.nextFloat()) - 0.5);
            } else if (pRandom.nextInt(16) == 0 && isValidBookShelf(pLevel, pPos, blockpos) && isValidObsidianTablet(pLevel, pPos, blockpos)) {
                for (int i = 0; i < 6; i++) { // Генерируем частицы 6 раз
                    pLevel.addParticle(ParticlesObs.BAGELL_TABLE_PARTICLES.get(),
                            (double) pPos.getX() + 0.5,
                            (double) pPos.getY() + 2.0,
                            (double) pPos.getZ() + 0.5,
                            (double) ((float) blockpos.getX() + pRandom.nextFloat()) - 0.5,
                            (double) ((float) blockpos.getY() - pRandom.nextFloat() - 1.0F),
                            (double) ((float) blockpos.getZ() + pRandom.nextFloat()) - 0.5);
                }
            }
            if (pRandom.nextInt(16) == 0 && isValidBookShelf(pLevel, pPos, blockpos) && !isValidAzureObsidianTablet(pLevel, pPos, blockpos)) {
                pLevel.addParticle(ParticleTypes.ENCHANT, (double) pPos.getX() + 0.5, (double) pPos.getY() + 2.0, (double) pPos.getZ() + 0.5, (double) ((float) blockpos.getX() + pRandom.nextFloat()) - 0.5, (double) ((float) blockpos.getY() - pRandom.nextFloat() - 1.0F), (double) ((float) blockpos.getZ() + pRandom.nextFloat()) - 0.5);
            } else if (pRandom.nextInt(16) == 0 && isValidBookShelf(pLevel, pPos, blockpos) && isValidAzureObsidianTablet(pLevel, pPos, blockpos)) {
                for (int i = 0; i < 6; i++) { // Генерируем частицы 6 раз
                    pLevel.addParticle(ParticlesObs.BAGELL_TABLE_PARTICLES.get(),
                            (double) pPos.getX() + 0.5,
                            (double) pPos.getY() + 2.0,
                            (double) pPos.getZ() + 0.5,
                            (double) ((float) blockpos.getX() + pRandom.nextFloat()) - 0.5,
                            (double) ((float) blockpos.getY() - pRandom.nextFloat() - 1.0F),
                            (double) ((float) blockpos.getZ() + pRandom.nextFloat()) - 0.5);
                }
            }
        }
    }

    private boolean isValidObsidianTablet(Level level, BlockPos enchantmentTablePos, BlockPos offset) {
        BlockPos targetPos = enchantmentTablePos.offset(offset);
        return level.getBlockState(targetPos).is(BlocksObs.OBSIDIAN_TABLET.get());
    }
    private boolean isValidAzureObsidianTablet(Level level, BlockPos enchantmentTablePos, BlockPos offset) {
        BlockPos targetPos = enchantmentTablePos.offset(offset);
        return level.getBlockState(targetPos).is(BlocksObs.AZURE_OBSIDIAN_TABLET.get());
    }
}