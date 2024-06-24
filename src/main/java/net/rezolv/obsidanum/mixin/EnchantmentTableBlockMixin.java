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
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.ForgeEventFactory;
import net.rezolv.obsidanum.block.BlocksObs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

@Mixin(EnchantmentTableBlock.class)
public abstract class EnchantmentTableBlockMixin extends BaseEntityBlock {

    protected EnchantmentTableBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "animateTick", at = @At("HEAD"))
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo ci) {
        if (!pLevel.isClientSide) {
            System.out.println("Running animateTick on server side");

            for (BlockPos blockpos : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
                System.out.println("Checking bookshelf offset: " + blockpos);
                if (pRandom.nextInt(16) == 0 && isValidBookShelf2(pLevel, pPos, blockpos)) {
                    if (pLevel.getBlockState(pPos.offset(blockpos)).is(BlocksObs.OBSIDIAN_TABLET.get())) {
                        System.out.println("Found OBSIDIAN_TABLET, spawning FLAME particles");
                        pLevel.addParticle(ParticleTypes.FLAME,
                                pPos.getX() + 0.5,
                                pPos.getY() + 2.0,
                                pPos.getZ() + 0.5,
                                (blockpos.getX() + pRandom.nextFloat()) - 0.5,
                                (blockpos.getY() - pRandom.nextFloat() - 1.0F),
                                (blockpos.getZ() + pRandom.nextFloat()) - 0.5);
                    } else {
                        System.out.println("Found bookshelf, spawning ENCHANT particles");
                        pLevel.addParticle(ParticleTypes.ENCHANT,
                                pPos.getX() + 0.5,
                                pPos.getY() + 2.0,
                                pPos.getZ() + 0.5,
                                (blockpos.getX() + pRandom.nextFloat()) - 0.5,
                                (blockpos.getY() - pRandom.nextFloat() - 1.0F),
                                (blockpos.getZ() + pRandom.nextFloat()) - 0.5);
                    }
                }
            }
        }
    }


    @Overwrite
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            LightningBolt lightingBolt = EntityType.LIGHTNING_BOLT.create(level);

            if (level instanceof ServerLevel serverLevel && lightingBolt != null) {
                lightingBolt.moveTo(blockPos.getCenter());
                level.addFreshEntity(lightingBolt);
            }

            return InteractionResult.CONSUME;
        }
    }
    public boolean isValidBookShelf2(Level pLevel, BlockPos pTablePos, BlockPos pOffsetPos) {
        return pLevel.getBlockState(pTablePos.offset(pOffsetPos)).getEnchantPowerBonus(pLevel, pTablePos.offset(pOffsetPos)) != 0.0F && pLevel.getBlockState(pTablePos.offset(pOffsetPos.getX() / 2, pOffsetPos.getY(), pOffsetPos.getZ() / 2)).is(BlocksObs.OBSIDIAN_TABLET.get());
    }
    private boolean isValidBookShelf(Level pLevel, BlockPos pPos, BlockPos blockpos) {
        // Ваша логика проверки допустимости книжной полки
        return true; // Пример
    }
}