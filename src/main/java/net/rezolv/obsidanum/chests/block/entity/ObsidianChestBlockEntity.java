package net.rezolv.obsidanum.chests.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.chests.block.ObsidanumChestsTypes;
import net.rezolv.obsidanum.chests.inventory.ObsidanumChestMenu;

public class ObsidianChestBlockEntity extends AbstractObsidanumChestBlockEntity {

  public ObsidianChestBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ObsidanumChestsBlockEntityTypes.OBSIDIAN_CHEST.get(), blockPos, blockState, ObsidanumChestsTypes.OBSIDIAN, BlocksObs.OBSIDIAN_CHEST::get);
  }

  @Override
  protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
    return ObsidanumChestMenu.createObsidianContainer(containerId, playerInventory, this);
  }
}
