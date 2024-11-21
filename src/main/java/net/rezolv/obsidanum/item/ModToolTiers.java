package net.rezolv.obsidanum.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.tags.TagsObs;

import java.util.List;

public class ModToolTiers {
    public static final Tier OBSIDIANUM = TierSortingRegistry.registerTier(
            new ForgeTier(2, 950, 10, 4f, 2,
                    TagsObs.Blocks.NEEDS_OBSIDIAN_TOOL, () -> Ingredient.of(Items.OBSIDIAN)),
            new ResourceLocation(Obsidanum.MOD_ID, "obsidianum"), List.of(Tiers.STONE), List.of());
    public static final Tier OBSIDAN = TierSortingRegistry.registerTier(
            new ForgeTier(3, 1000, 9, 4f, 6,
                    TagsObs.Blocks.NEEDS_OBSIDAN_TOOL, () -> Ingredient.of(Items.OBSIDIAN)),
            new ResourceLocation(Obsidanum.MOD_ID, "obsidan"), List.of(Tiers.DIAMOND), List.of());
    public static final Tier SMOLDERING = TierSortingRegistry.registerTier(
            new ForgeTier(2, 550, 6, 4f, 1,
                    TagsObs.Blocks.NEEDS_OBSIDIAN_TOOL, () -> Ingredient.of(Items.OBSIDIAN)),
            new ResourceLocation(Obsidanum.MOD_ID, "smoldering"), List.of(Tiers.IRON), List.of());

    public static final Tier VELNARIUM = TierSortingRegistry.registerTier(
            new ForgeTier(2, 400, 10, 4f, 30,
                    Tags.Blocks.NEEDS_GOLD_TOOL, () -> Ingredient.of(ItemsObs.VELNARIUM_INGOT.get())),
            new ResourceLocation(Obsidanum.MOD_ID, "velnarium"), List.of(Tiers.IRON), List.of());

}