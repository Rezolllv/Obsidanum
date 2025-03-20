package net.rezolv.obsidanum.chests.block.entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.BlocksObs;

public class ObsidanumChestsBlockEntityTypes {

  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Obsidanum.MOD_ID);



  public static final RegistryObject<BlockEntityType<AzureObsidianChestBlockEntity>> AZURE_OBSIDIAN_CHEST = BLOCK_ENTITIES.register(
    "azure_obsidian_chest", () -> typeOf(AzureObsidianChestBlockEntity::new, BlocksObs.AZURE_OBSIDIAN_CHEST.get()));

  public static final RegistryObject<BlockEntityType<ObsidianChestBlockEntity>> OBSIDIAN_CHEST = BLOCK_ENTITIES.register(
          "obsidian_chest", () -> typeOf(ObsidianChestBlockEntity::new, BlocksObs.OBSIDIAN_CHEST.get()));
  public static final RegistryObject<BlockEntityType<RunicObsidianChestBlockEntity>> RUNIC_OBSIDIAN_CHEST = BLOCK_ENTITIES.register(
          "runic_obsidian_chest", () -> typeOf(RunicObsidianChestBlockEntity::new, BlocksObs.RUNIC_OBSIDIAN_CHEST.get()));


  /**
   * Helper method to avoid having a NonNull error on each line
   */
  private static <T extends BlockEntity> BlockEntityType<T> typeOf(BlockEntityType.BlockEntitySupplier<T> entity, Block... blocks) {
    return BlockEntityType.Builder.of(entity, blocks).build(null);
  }
}
