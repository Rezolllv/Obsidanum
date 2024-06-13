package net.rezolv.obsidanum.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.rezolv.obsidanum.Obsidanum;

public class TagsObs {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_OBSIDAN_TOOL = tag("needs_obsidan_tool");
        public static final TagKey<Block> NEEDS_OBSIDIAN_TOOL = tag("needs_obsidian_tool");
        public static final TagKey<Block> NEEDS_SMOLDERING_TOOL = tag("needs_smoldering_tool");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Obsidanum.MOD_ID, name));
        }
    }

    public static class Items {

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Obsidanum.MOD_ID, name));
        }
    }
}