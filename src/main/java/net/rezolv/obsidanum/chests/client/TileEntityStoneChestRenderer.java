package net.rezolv.obsidanum.chests.client;


import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.chests.blocks.EnumStoneChest;
import net.rezolv.obsidanum.chests.tileentities.TileEntityStoneChest;

import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class TileEntityStoneChestRenderer extends ChestRenderer<TileEntityStoneChest> {
    public static Material[] single = new Material[EnumStoneChest.VALUES.length];
    public static Material[] left = new Material[EnumStoneChest.VALUES.length];
    public static Material[] right = new Material[EnumStoneChest.VALUES.length];

    static {
        for (EnumStoneChest type : EnumStoneChest.VALUES) {
            single[type.ordinal()] = getChestMaterial(type.name().toLowerCase(Locale.ENGLISH));
            left[type.ordinal()] = getChestMaterial(type.name().toLowerCase(Locale.ENGLISH) + "_left");
            right[type.ordinal()] = getChestMaterial(type.name().toLowerCase(Locale.ENGLISH) + "_right");
        }
    }

    public TileEntityStoneChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Material getMaterial(TileEntityStoneChest blockEntity, ChestType chestType) {
        return getChestMaterial(blockEntity, chestType);
    }

    private static Material getChestMaterial(String path) {
        return new Material(Sheets.CHEST_SHEET, new ResourceLocation(Obsidanum.MOD_ID, "entity/chest/" + path));
    }

    private static Material getChestMaterial(TileEntityStoneChest tile, ChestType type) {
        switch(type) {
            case LEFT:
                return single[tile.getChestType().ordinal()];
            case RIGHT:
                return single[tile.getChestType().ordinal()];
            case SINGLE:
            default:
                return single[tile.getChestType().ordinal()];
        }
    }
}
