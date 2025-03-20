package net.rezolv.obsidanum.chests.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.chests.Util;
import net.rezolv.obsidanum.chests.block.entity.AbstractObsidanumChestBlockEntity;
import net.rezolv.obsidanum.chests.block.entity.ObsidianChestBlockEntity;
import net.rezolv.obsidanum.chests.block.entity.RunicObsidianChestBlockEntity;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum ObsidanumChestsTypes implements StringRepresentable {

    OBSIDIAN(27, 9, 184, 168, new ResourceLocation("obsidanum", "textures/gui/obsidian_container.png"), 256, 256),
    AZURE_OBSIDIAN(27, 9, 184, 168, new ResourceLocation("obsidanum", "textures/gui/obsidian_container.png"), 256, 256),
    RUNIC_OBSIDIAN(81, 9, 184, 276, new ResourceLocation("obsidanum", "textures/gui/runic_obsidian_container.png"), 256, 276),
    WOOD(0, 0, 0, 0, null, 0, 0);

    private final String name;
    public final int size;
    public final int rowLength;
    public final int xSize;
    public final int ySize;
    public final ResourceLocation guiTexture;
    public final int textureXSize;
    public final int textureYSize;

    ObsidanumChestsTypes(int size, int rowLength, int xSize, int ySize, ResourceLocation guiTexture, int textureXSize, int textureYSize) {
        this(null, size, rowLength, xSize, ySize, guiTexture, textureXSize, textureYSize);
    }

    ObsidanumChestsTypes(@Nullable String name, int size, int rowLength, int xSize, int ySize, ResourceLocation guiTexture, int textureXSize, int textureYSize) {
        this.name = name == null ? Util.toEnglishName(this.name()) : name;
        this.size = size;
        this.rowLength = rowLength;
        this.xSize = xSize;
        this.ySize = ySize;
        this.guiTexture = guiTexture;
        this.textureXSize = textureXSize;
        this.textureYSize = textureYSize;
    }

    public String getId() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String getEnglishName() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.getEnglishName();
    }

    public int getRowCount() {
        return this.size / this.rowLength;
    }

    public static List<Block> get(ObsidanumChestsTypes type) {
        return switch (type) {

            case RUNIC_OBSIDIAN -> Arrays.asList(BlocksObs.RUNIC_OBSIDIAN_CHEST.get());
            case OBSIDIAN -> Arrays.asList(BlocksObs.OBSIDIAN_CHEST.get());
            case AZURE_OBSIDIAN -> Arrays.asList(BlocksObs.AZURE_OBSIDIAN_CHEST.get());
            default -> List.of(Blocks.CHEST);
        };
    }

    @Nullable
    public AbstractObsidanumChestBlockEntity makeEntity(BlockPos blockPos, BlockState blockState, boolean trapped) {

        return switch (this) {
            case RUNIC_OBSIDIAN -> new ObsidianChestBlockEntity(blockPos, blockState);
            case OBSIDIAN -> new RunicObsidianChestBlockEntity(blockPos, blockState);
            case AZURE_OBSIDIAN -> new RunicObsidianChestBlockEntity(blockPos, blockState);
            default -> null;
        };
    }
}
