package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.rezolv.obsidanum.block.entity.ModHangingSignBlockEntity;
import net.rezolv.obsidanum.block.entity.ModSignBlockEntity;

import java.util.Random;


public class ModHangingSignBlock extends CeilingHangingSignBlock {
    public ModHangingSignBlock(Properties properties, WoodType type) {
        super(properties, type);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ModHangingSignBlockEntity(pPos, pState);
    }

}