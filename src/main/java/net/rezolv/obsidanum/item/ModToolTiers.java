package net.rezolv.obsidanum.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.tags.TagsObs;

import java.util.List;

public class ModToolTiers {
    public static final Tier OBSIDIANUM = TierSortingRegistry.registerTier(
            new ForgeTier(2, 950, 10f, 4f, 2,
                    TagsObs.Blocks.NEEDS_OBSIDAN_TOOL, () -> Ingredient.of(Items.OBSIDIAN)),
            new ResourceLocation(Obsidanum.MOD_ID, "obsidianum"), List.of(Tiers.IRON), List.of());
    public static final Tier SMOLDERING = TierSortingRegistry.registerTier(
            new ForgeTier(2, 550, 10f, 4f, 2,
                    TagsObs.Blocks.NEEDS_SMOLDERING_TOOL, () -> Ingredient.of(Items.OBSIDIAN)),
            new ResourceLocation(Obsidanum.MOD_ID, "smoldering"), List.of(Tiers.IRON), List.of());

}