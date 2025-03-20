package net.rezolv.obsidanum.chests.client.model;

import net.minecraft.resources.ResourceLocation;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.chests.block.ObsidanumChestsTypes;
import org.jetbrains.annotations.NotNull;

public class ObsidanumChestsModels {

  public static final ResourceLocation AZURE_OBSIDIAN_CHEST_LOCATION = new ResourceLocation(Obsidanum.MOD_ID, "model/azure_obsidian_chest");
  public static final ResourceLocation OBSIDIAN_CHEST_LOCATION = new ResourceLocation(Obsidanum.MOD_ID, "model/obsidian_chest");
  public static final ResourceLocation RUNIC_OBSIDIAN_CHEST_LOCATION = new ResourceLocation(Obsidanum.MOD_ID, "model/runic_obsidian_chest");
  public static final ResourceLocation VANILLA_CHEST_LOCATION = new ResourceLocation("entity/chest/normal");


  public static final ResourceLocation TRAPPED_VANILLA_CHEST_LOCATION = new ResourceLocation("entity/chest/trapped");

  public static ResourceLocation chooseChestTexture(ObsidanumChestsTypes type) {

      return getResourceLocation(type, AZURE_OBSIDIAN_CHEST_LOCATION, OBSIDIAN_CHEST_LOCATION, RUNIC_OBSIDIAN_CHEST_LOCATION,  VANILLA_CHEST_LOCATION);

  }

  @NotNull
  private static ResourceLocation getResourceLocation(ObsidanumChestsTypes type, ResourceLocation azureObsidianChestLocation, ResourceLocation obsidianChestLocation, ResourceLocation runicObsidianChestLocation, ResourceLocation vanillaChestLocation) {
    return switch (type) {
      case AZURE_OBSIDIAN -> azureObsidianChestLocation;
      case OBSIDIAN -> obsidianChestLocation;
      case RUNIC_OBSIDIAN -> runicObsidianChestLocation;
      default -> vanillaChestLocation;
    };
  }
}
