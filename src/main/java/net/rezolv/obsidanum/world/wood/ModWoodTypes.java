package net.rezolv.obsidanum.world.wood;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.rezolv.obsidanum.Obsidanum;

public class ModWoodTypes {
    public static final WoodType OBSIDAN = WoodType.register(new WoodType(Obsidanum.MOD_ID + ":obsidan", BlockSetType.OAK));
}