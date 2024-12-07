package net.rezolv.obsidanum.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;

public class GlowingThrownItemRenderer<T extends Entity & ItemSupplier> extends ThrownItemRenderer<T> {

    public GlowingThrownItemRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLightLevel(T pEntity, BlockPos pPos) {
        return 15;
    }
}