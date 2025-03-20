package net.rezolv.obsidanum.chests.inventory;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;

public class ObsidanumChestsContainerTypes {

  public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Obsidanum.MOD_ID);



  public static final RegistryObject<MenuType<ObsidanumChestMenu>> AZURE_OBSIDIAN_CHEST = CONTAINERS.register("azure_obsidian_chest", () -> new MenuType<>(ObsidanumChestMenu::createAzureObsidianContainer, FeatureFlags.REGISTRY.allFlags()));
  public static final RegistryObject<MenuType<ObsidanumChestMenu>> OBSIDIAN_CHEST = CONTAINERS.register("obsidian_chest", () -> new MenuType<>(ObsidanumChestMenu::createObsidianContainer, FeatureFlags.REGISTRY.allFlags()));
  public static final RegistryObject<MenuType<ObsidanumChestMenu>> RUNIC_OBSIDIAN_CHEST = CONTAINERS.register("runic_obsidian_chest", () -> new MenuType<>(ObsidanumChestMenu::createRunicObsidianContainer, FeatureFlags.REGISTRY.allFlags()));
}
