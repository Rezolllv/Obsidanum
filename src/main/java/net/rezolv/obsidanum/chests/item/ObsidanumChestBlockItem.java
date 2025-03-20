package net.rezolv.obsidanum.chests.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.DistExecutor;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.chests.block.ObsidanumChestsTypes;
import net.rezolv.obsidanum.chests.block.entity.AzureObsidianChestBlockEntity;
import net.rezolv.obsidanum.chests.block.entity.ObsidianChestBlockEntity;
import net.rezolv.obsidanum.chests.block.entity.RunicObsidianChestBlockEntity;
import net.rezolv.obsidanum.chests.client.model.inventory.ObsidanumChestItemStackRenderer;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObsidanumChestBlockItem extends BlockItem {

  protected Supplier<ObsidanumChestsTypes> type;

  protected Supplier<Boolean> trapped;

  public ObsidanumChestBlockItem(Block block, Properties properties, Supplier<Callable<ObsidanumChestsTypes>> type) {
    super(block, properties);

    ObsidanumChestsTypes tempType = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, type);

    this.type = tempType == null ? null : () -> tempType;
  }

  @Override
  public void initializeClient(Consumer<IClientItemExtensions> consumer) {
    super.initializeClient(consumer);

    consumer.accept(new IClientItemExtensions() {
      @Override
      public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        Supplier<BlockEntity> modelToUse;

          switch (type.get()) {
            case RUNIC_OBSIDIAN -> modelToUse = () -> new RunicObsidianChestBlockEntity(BlockPos.ZERO, BlocksObs.RUNIC_OBSIDIAN_CHEST.get().defaultBlockState());
            case AZURE_OBSIDIAN -> modelToUse = () -> new AzureObsidianChestBlockEntity(BlockPos.ZERO, BlocksObs.AZURE_OBSIDIAN_CHEST.get().defaultBlockState());

            default -> modelToUse = () -> new ObsidianChestBlockEntity(BlockPos.ZERO, BlocksObs.OBSIDIAN_CHEST.get().defaultBlockState());
          }
        return new ObsidanumChestItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), modelToUse);
      }
    });
  }
}
