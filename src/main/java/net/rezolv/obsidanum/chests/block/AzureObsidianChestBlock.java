package net.rezolv.obsidanum.chests.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.chests.block.entity.AzureObsidianChestBlockEntity;
import net.rezolv.obsidanum.chests.block.entity.ObsidanumChestsBlockEntityTypes;

import javax.annotation.Nullable;

public class AzureObsidianChestBlock extends AbstractObsidanumChestBlock {

  public AzureObsidianChestBlock(Properties properties) {
    super(properties, ObsidanumChestsBlockEntityTypes.AZURE_OBSIDIAN_CHEST::get, ObsidanumChestsTypes.AZURE_OBSIDIAN);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new AzureObsidianChestBlockEntity(blockPos, blockState);
  }
}
