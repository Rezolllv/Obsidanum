package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.block.BlocksObs;

public class HymeniumMushroom extends GrowingPlantBodyBlock implements BonemealableBlock, CaveVines {
    public HymeniumMushroom(BlockBehaviour.Properties p_153000_) {
        super(p_153000_, Direction.DOWN, SHAPE, false);
    }

    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) BlocksObs.HEAD_HYMENIUM_STEM_GLOOMY_MUSHROOM.get();
    }



    public boolean isBonemealSuccess(Level p_220943_, RandomSource p_220944_, BlockPos p_220945_, BlockState p_220946_) {
        return true;
    }


}