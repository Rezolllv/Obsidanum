package net.rezolv.obsidanum.chests.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.chests.block.entity.ObsidanumChestsBlockEntityTypes;
import net.rezolv.obsidanum.chests.block.entity.RunicObsidianChestBlockEntity;

import javax.annotation.Nullable;

public class RunicObsidianChestBlock extends AbstractObsidanumChestBlock {

  public RunicObsidianChestBlock(Properties properties) {
    super(properties, ObsidanumChestsBlockEntityTypes.RUNIC_OBSIDIAN_CHEST::get, ObsidanumChestsTypes.RUNIC_OBSIDIAN);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new RunicObsidianChestBlockEntity(blockPos, blockState);
  }
}
