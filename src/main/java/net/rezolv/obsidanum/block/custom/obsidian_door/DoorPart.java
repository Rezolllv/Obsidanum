package net.rezolv.obsidanum.block.custom.obsidian_door;

import net.minecraft.util.StringRepresentable;

public enum DoorPart implements StringRepresentable {
    C("c"),
    TL("tl"),
    TC("tc"),
    TR("tr"),
    CL("cl"),
    CR("cr"),
    BL("bl"),
    BC("bc"),
    BR("br");

    private final String name;

    DoorPart(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}