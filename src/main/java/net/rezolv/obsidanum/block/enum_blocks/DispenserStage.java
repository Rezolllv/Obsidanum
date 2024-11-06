package net.rezolv.obsidanum.block.enum_blocks;

import net.minecraft.util.StringRepresentable;

public enum DispenserStage implements StringRepresentable {
    NONE,
    NETHER,
    ORDER,
    CATACOMBS;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
