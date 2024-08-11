package net.rezolv.obsidanum.mixin;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.rezolv.obsidanum.block.BlocksObs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;

@Mixin(value = BaseFireBlock.class, priority = 1001)
public class BaseFireBlockMixin {
    @ModifyVariable(method = "isPortal", at = @At(value = "INVOKE", target="Lnet/minecraft/core/Direction;values()[Lnet/minecraft/core/Direction;"))
    private static boolean BaseFireBlock_isPortal(boolean bl, Level level, BlockPos blockPos, Direction direction) {
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
        Direction[] var5 = Direction.values();
        int var6 = var5.length;

        for (Direction direction2 : var5) {
            if (level.getBlockState(mutableBlockPos.set(blockPos).move(direction2)).is(Tags.Blocks.OBSIDIAN)) {
                return true;
            }
        }

        return false;
    }

}