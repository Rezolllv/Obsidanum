package net.rezolv.obsidanum.recipes;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;

public class ObsidanRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Obsidanum.MOD_ID);

  public static final RegistryObject<RecipeSerializer<ForgeScrollNetherRecipe>> FORGE_SCROLL_NETHER_SERIALIZER =
            SERIALIZERS.register("forge_scroll_nether", () -> ForgeScrollNetherRecipe.Serializer.FORGE_SCROOL_NETHER);
    public static final RegistryObject<RecipeSerializer<ForgeScrollOrderRecipe>> FORGE_SCROLL_ORDER_SERIALIZER =
            SERIALIZERS.register("forge_scroll_order", () -> ForgeScrollOrderRecipe.Serializer.FORGE_SCROOL_ORDER);
    public static final RegistryObject<RecipeSerializer<ForgeScrollCatacombsRecipe>> FORGE_SCROLL_CATACOMBS_SERIALIZER =
            SERIALIZERS.register("forge_scroll_catacombs", () -> ForgeScrollCatacombsRecipe.Serializer.FORGE_SCROOL_CATACOMBS);

}
