package net.rezolv.obsidanum.chests.tileentities;


import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.chests.SCRegistry;
import net.rezolv.obsidanum.chests.blocks.EnumStoneChest;

public class TileEntityStoneChest extends ChestBlockEntity {
    protected Component getDefaultName() {
        return Component.translatable("container.chest_obsidian");
    }
    private EnumStoneChest chestType;

    public TileEntityStoneChest(BlockPos pos, BlockState state) {
        this(EnumStoneChest.OBSIDIAN, pos, state);
    }


    public TileEntityStoneChest(EnumStoneChest chestType, BlockPos pos, BlockState state) {
        super(SCRegistry.CHEST_TILE_TYPE.get(), pos, state);
        this.chestType = chestType;
    }

    public EnumStoneChest getChestType() {
        return chestType;
    }
}
