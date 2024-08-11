package net.rezolv.obsidanum.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraftforge.common.Tags;
import net.rezolv.obsidanum.block.BlocksObs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PortalShape.class, priority = 1001)
public class PortalShapeMixin {
    @Shadow private static @Final @Mutable BlockBehaviour.StatePredicate FRAME;

    @Inject(method = "<init>(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction$Axis;)V", at = @At(value = "TAIL"))
    private void PortalShape(LevelAccessor p_77695_, BlockPos p_77696_, Direction.Axis p_77697_, CallbackInfo ci) {
        FRAME = (BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) -> {
            return blockState.is(Tags.Blocks.OBSIDIAN); // Используйте тег блоков
        };
    }

}