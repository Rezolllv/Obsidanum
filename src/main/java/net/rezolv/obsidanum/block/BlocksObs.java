package net.rezolv.obsidanum.block;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.custom.*;
import net.rezolv.obsidanum.chests.block.IronChestsTypes;
import net.rezolv.obsidanum.chests.block.ObsidianChestBlock;
import net.rezolv.obsidanum.chests.block.RunicObsidianChestBlock;
import net.rezolv.obsidanum.chests.item.IronChestBlockItem;
import net.rezolv.obsidanum.fluid.ModFluids;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.world.tree.ObsidanOak;
import net.rezolv.obsidanum.world.wood.ModWoodTypes;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlocksObs {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, Obsidanum.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = ItemsObs.ITEMS;

    public static final DeferredBlock<TallCrimsonGrassBlock> CRIMSON_GRASS = registerBlock("crimson_grass",
            () -> new TallCrimsonGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollission()
                    .instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XYZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<CrimsonGrassBlock> CRIMSON_GRASS_BLOCK = registerBlock("crimson_grass_block",
            () -> new CrimsonGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).randomTicks()
                    .strength(0.6F).sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> ALCHEMICAL_DIRT = registerBlock("alchemical_dirt",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));


    public static final DeferredBlock<FlameDispenser> FLAME_DISPENSER = registerBlock("flame_dispenser",
            () -> new FlameDispenser(BlockBehaviour.Properties.of().noOcclusion().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FlamePipe> FLAME_PIPE = registerBlock("flame_pipe",
            () -> new FlamePipe(BlockBehaviour.Properties.of().noOcclusion().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final DeferredBlock<PranaCrystall> PRANA_CRYSTALL = registerBlock("prana_crystall",
            () -> new PranaCrystall(BlockBehaviour.Properties.of().noOcclusion().strength(-1.0F, 3600000.0F)
                    .sound(SoundType.AMETHYST)));
    public static final DeferredBlock<RitualDrum> RITUAL_DRUM = registerBlock("ritual_drum",
            () -> new RitualDrum(BlockBehaviour.Properties.of().noOcclusion().strength(-1.0F, 3600000.0F)
                    .sound(SoundType.AMETHYST)));

    public static final DeferredBlock<RightForgeScroll> RIGHT_FORGE_SCROLL = registerBlock("right_forge_scroll",
            () -> new RightForgeScroll(BlockBehaviour.Properties.of().noOcclusion().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<ForgeCrucible> FORGE_CRUCIBLE = registerBlock("forge_crucible",
            () -> new ForgeCrucible(BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<LeftCornerLevel> LEFT_CORNER_LEVEL = registerBlock("left_corner_level",
            () -> new LeftCornerLevel(BlockBehaviour.Properties.of()
                    .noOcclusion().strength(-1.0F, 3600000.0F).randomTicks().sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallForge> WALL_FORGE_L_CORNER = registerBlock("wall_forge_l_corner",
            () -> new WallForge(BlockBehaviour.Properties.of().noOcclusion().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallForge> SIDE_FORGE = registerBlock("side_forge",
            () -> new WallForge(BlockBehaviour.Properties.of().noOcclusion().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallForge> WALL_FORGE_R_CORNER = registerBlock("wall_forge_r_corner",
            () -> new WallForge(BlockBehaviour.Properties.of().noOcclusion().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<HammerForge> HAMMER_FORGE = registerBlock("hammer_forge",
            () -> new HammerForge(BlockBehaviour.Properties.of().noOcclusion().randomTicks()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<BagelAnvilBlock> BAGEL_ANVIL_BLOCK = registerBlock("bagel_anvil_block",
            () -> new BagelAnvilBlock(BlockBehaviour.Properties.of().noOcclusion().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final DeferredRegister<NetherFlameBlock> NETHER_FLAME_BLOCK = BLOCKS.register("nether_flame_block",
            () -> new NetherFlameBlock(ModFluids.SOURCE_NETHER_FIRE_LAVA, BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)));
    public static final DeferredHolder<Block, FlameLeavesBlock> OBSIDAN_WOOD_LEAVES = BLOCKS.register("obsidan_wood_leaves",
            () -> new FlameLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).lightLevel((p_152680_) -> {
                return 5;
            })));
    public static final DeferredHolder<Block, MultifaceMushrooms> THE_GLOOMY_MYCELIUM = BLOCKS.register("the_gloomy_mycelium",
            () -> new MultifaceMushrooms(BlockBehaviour.Properties.ofFullCopy(Blocks.VINE).noOcclusion()));
    public static final DeferredRegister<Block> OBSIDAN_SAPLING = BLOCKS.register("obsidan_sapling",
            () -> new SaplingBlock(new ObsidanOak(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final DeferredBlock<ModFlammableRotatedPillarBlock> OBSIDAN_WOOD_LOG = registerBlock("obsidan_wood_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).strength(3f)));
    public static final DeferredBlock<ModFlammableRotatedPillarBlock> OBSIDAN_WOOD = registerBlock("obsidan_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).strength(3f)));
    public static final DeferredBlock<ModFlammableRotatedPillarBlock> STRIPPED_OBSIDAN_WOOD_LOG = registerBlock("stripped_obsidan_wood_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).strength(3f)));
    public static final DeferredBlock<ModFlammableRotatedPillarBlock> STRIPPED_OBSIDAN_WOOD = registerBlock("stripped_obsidan_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).strength(3f)));

    public static final DeferredBlock<ObsidianTablet> OBSIDIAN_TABLET = registerBlock("obsidian_tablet",
            () -> new ObsidianTablet(BlockBehaviour.Properties.of().mapColor(MapColor.METAL)
                    .strength(20, 500).sound(SoundType.CHERRY_WOOD)
                    .mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().noOcclusion()));


    public static final DeferredRegister<ObsidianChestBlock> OBSIDIAN_CHEST = registerChests("obsidian_chest",
            () -> new ObsidianChestBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F)),
            IronChestsTypes.OBSIDIAN);

    public static final DeferredRegister<RunicObsidianChestBlock> RUNIC_OBSIDIAN_CHEST = registerChests("runic_obsidian_chest",
            () -> new RunicObsidianChestBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F)),
            IronChestsTypes.RUNIC_OBSIDIAN);


    public static final DeferredBlock<RotatedPillarBlock> OBSIDIAN_INLAID_COLUMN = registerBlock("obsidian_inlaid_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<RotatedPillarBlock> CRACKED_OBSIDIAN_INLAID_COLUMN = registerBlock("cracked_obsidian_inlaid_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<RotatedPillarBlock> MOLDY_OBSIDIAN_INLAID_COLUMN = registerBlock("moldy_obsidian_inlaid_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<RotatedPillarBlock> OBSIDIAN_COLUMN = registerBlock("obsidian_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<RotatedPillarBlock> CRACKED_OBSIDIAN_COLUMN = registerBlock("cracked_obsidian_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<RotatedPillarBlock> MOLDY_OBSIDIAN_COLUMN = registerBlock("moldy_obsidian_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> OBSIDIAN_POLISHED = registerBlock("obsidian_polished",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops().forceSolidOn()));
    public static final DeferredBlock<WallBlock> OBSIDIAN_POLISHED_WALL = registerBlock("obsidian_polished_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> CRACKED_OBSIDIAN_POLISHED_WALL = registerBlock("cracked_obsidian_polished_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> MOLDY_OBSIDIAN_POLISHED_WALL = registerBlock("moldy_obsidian_polished_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> OBSIDIAN_BRICKS_WALL = registerBlock("obsidian_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> CRACKED_OBSIDIAN_BRICKS_WALL = registerBlock("cracked_obsidian_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> MOLDY_OBSIDIAN_BRICKS_WALL = registerBlock("moldy_obsidian_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> OBSIDIAN_POLISHED_FENCE = registerBlock("obsidian_polished_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> CRACKED_OBSIDIAN_POLISHED_FENCE = registerBlock("cracked_obsidian_polished_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> MOLDY_OBSIDIAN_POLISHED_FENCE = registerBlock("moldy_obsidian_polished_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CRACKED_OBSIDIAN_POLISHED = registerBlock("cracked_obsidian_polished",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MOLDY_OBSIDIAN_POLISHED = registerBlock("moldy_obsidian_polished",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));
    public static final DeferredBlock<MysteriousCarvedObsidian> CARVED_OBSIDIAN_BRICKS = registerBlock("carved_obsidian_bricks",
            () -> new MysteriousCarvedObsidian(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F)
                    .randomTicks().sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CARVED_OBSIDIAN_BRICKS_E = registerBlock("carved_obsidian_bricks_e",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F)
                    .randomTicks().sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<NetherFireBlock> NETHER_FIRE = registerBlock("nether_fire",
            () -> new NetherFireBlock(BlockBehaviour.Properties.of().replaceable().noCollission().instabreak().lightLevel((p_152607_) -> {
                return 15;
            }).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<NetherFireSoulBlock> NETHER_FIRE_SOUL = registerBlock("nether_fire_soul",
            () -> new NetherFireSoulBlock(BlockBehaviour.Properties.of().replaceable().noCollission().instabreak().lightLevel((p_152607_) -> {
                return 15;
            }).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<IronBarsBlock> VELNARIUM_GRID = registerBlock("velnarium_grid",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().strength(5.0F, 1200.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CARVED_OBSIDIAN_BRICKS_D = registerBlock("carved_obsidian_bricks_d",
            () -> new Block(BlockBehaviour.Properties.of().strength(50.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<RotatedPillarBlock> OBSIDIAN_INLAID_COLUMN_D = registerBlock("obsidian_inlaid_column_d",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(50.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<RotatedPillarBlock> CRACKED_OBSIDIAN_INLAID_COLUMN_D = registerBlock("cracked_obsidian_inlaid_column_d",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<RotatedPillarBlock> MOLDY_OBSIDIAN_INLAID_COLUMN_D = registerBlock("moldy_obsidian_inlaid_column_d",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(40.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<RotatedPillarBlock> OBSIDIAN_COLUMN_D = registerBlock("obsidian_column_d",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(50.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<RotatedPillarBlock> CRACKED_OBSIDIAN_COLUMN_D = registerBlock("cracked_obsidian_column_d",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<RotatedPillarBlock> MOLDY_OBSIDIAN_COLUMN_D = registerBlock("moldy_obsidian_column_d",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(40.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> OBSIDIAN_POLISHED_D = registerBlock("obsidian_polished_d",
            () -> new Block(BlockBehaviour.Properties.of().strength(50.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops().forceSolidOn()));
    public static final DeferredBlock<WallBlock> OBSIDIAN_POLISHED_WALL_D = registerBlock("obsidian_polished_wall_d",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> CRACKED_OBSIDIAN_POLISHED_WALL_D = registerBlock("cracked_obsidian_polished_wall_d",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> MOLDY_OBSIDIAN_POLISHED_WALL_D = registerBlock("moldy_obsidian_polished_wall_d",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> OBSIDIAN_BRICKS_WALL_D = registerBlock("obsidian_bricks_wall_d",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> CRACKED_OBSIDIAN_BRICKS_WALL_D = registerBlock("cracked_obsidian_bricks_wall_d",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> MOLDY_OBSIDIAN_BRICKS_WALL_D = registerBlock("moldy_obsidian_bricks_wall_d",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> OBSIDIAN_POLISHED_FENCE_D = registerBlock("obsidian_polished_fence_d",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> CRACKED_OBSIDIAN_POLISHED_FENCE_D = registerBlock("cracked_obsidian_polished_fence_d",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> MOLDY_OBSIDIAN_POLISHED_FENCE_D = registerBlock("moldy_obsidian_polished_fence_d",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CRACKED_OBSIDIAN_POLISHED_D = registerBlock("cracked_obsidian_polished_d",
            () -> new Block(BlockBehaviour.Properties.of().strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MOLDY_OBSIDIAN_POLISHED_D = registerBlock("moldy_obsidian_polished_d",
            () -> new Block(BlockBehaviour.Properties.of().strength(40.0F, 1200.0F).sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));


    public static final DeferredHolder<Block, ModFlammableRotatedPillarBlock> STEM_GLOOMY_MUSHROOM = BLOCKS.register("stem_gloomy_mushroom",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.BASS).strength(0.2F)
                    .sound(SoundType.WOOD).ignitedByLava()));
    public static final DeferredHolder<Block, HugeMushroomBlock> CAP_GLOOMY_MUSHROOM = BLOCKS.register("cap_gloomy_mushroom",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.BASS).strength(0.2F)
                    .sound(SoundType.WOOD).ignitedByLava()));


    public static final DeferredBlock<HeadHymeniumMushroom> HEAD_HYMENIUM_STEM_GLOOMY_MUSHROOM = registerBlock("head_hymenium_gloomy_mushroom",
            () -> new HeadHymeniumMushroom(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).randomTicks().noCollission()
                    .instabreak().sound(SoundType.CAVE_VINES).pushReaction(PushReaction.DESTROY).lightLevel((p_152629_) -> {
                        return 7;
                    })));
    public static final DeferredBlock<HymeniumMushroom> HYMENIUM_STEM_GLOOMY_MUSHROOM = registerBlock("hymenium_gloomy_mushroom",
            () -> new HymeniumMushroom(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).randomTicks().noCollission()
                    .instabreak().sound(SoundType.CAVE_VINES).pushReaction(PushReaction.DESTROY).lightLevel((p_152629_) -> {
                        return 7;
                    })));
    
    public static final DeferredBlock<ObsidianDoor> OBSIDIAN_DOOR = registerBlock("obsidian_door",
            () -> new ObsidianDoor(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F)
                    .noLootTable().sound(SoundType.GLASS)));



    // Blue OBSIDIAN
    public static final DeferredHolder<Block, Block> AZURE_OBSIDIAN_BRICKS = BLOCKS.register("azure_obsidian_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> AZURE_OBSIDIAN_BRICKS_SLAB = registerBlock("azure_obsidian_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredRegister<Block> AZURE_OBSIDIAN_BRICKS_STAIRS = registerBlock("azure_obsidian_bricks_stairs",
            () -> new StairBlock(() -> BlocksObs.AZURE_OBSIDIAN_BRICKS.get().defaultBlockState(),BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CHISELED_AZURE_OBSIDIAN_BRICKS = registerBlock("chiseled_azure_obsidian_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<ConnectedPillar> AZURE_OBSIDIAN_COLUMN = registerBlock("azure_obsidian_column",
            () -> new ConnectedPillar(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));


    public static final DeferredBlock<Block> ONYX = registerBlock("onyx",
            () -> new Block(BlockBehaviour.Properties.of().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> ONYX_SLAB = registerBlock("onyx_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> ONYX_STAIRS = registerBlock("onyx_stairs",
            () -> new StairBlock(BlocksObs.ONYX.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)
                            .requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> ONYX_BRICKS = registerBlock("onyx_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> ONYX_BRICKS_SLAB = registerBlock("onyx_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> ONYX_BRICKS_STAIRS = registerBlock("onyx_bricks_stairs",
            () -> new StairBlock(BlocksObs.ONYX_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> POLISHED_ONYX = registerBlock("polished_onyx",
            () -> new Block(BlockBehaviour.Properties.of().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> POLISHED_ONYX_SLAB = registerBlock("polished_onyx_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> POLISHED_ONYX_STAIRS = registerBlock("polished_onyx_stairs",
            () -> new StairBlock(BlocksObs.POLISHED_ONYX.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<MysteriousCarvedObsidian> MOLDY_CARVED_OBSIDIAN_BRICKS = registerBlock("moldy_carved_obsidian_bricks",
            () -> new MysteriousCarvedObsidian(BlockBehaviour.Properties.of().randomTicks().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<MysteriousCarvedObsidian> CRACKED_CARVED_OBSIDIAN_BRICKS = registerBlock("cracked_carved_obsidian_bricks",
            () -> new MysteriousCarvedObsidian(BlockBehaviour.Properties.of().randomTicks().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MOLDY_CARVED_OBSIDIAN_BRICKS_E = registerBlock("moldy_carved_obsidian_bricks_e",
            () -> new Block(BlockBehaviour.Properties.of().randomTicks().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CRACKED_CARVED_OBSIDIAN_BRICKS_E = registerBlock("cracked_carved_obsidian_bricks_e",
            () -> new Block(BlockBehaviour.Properties.of().randomTicks().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));




    public static final DeferredHolder<Block, Block> OBSIDIAN_BRICKS = BLOCKS.register("obsidian_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> OBSIDIAN_BRICKS_FENCE = registerBlock("obsidian_bricks_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> CRACKED_OBSIDIAN_BRICKS_FENCE = registerBlock("cracked_obsidian_bricks_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> MOLDY_OBSIDIAN_BRICKS_FENCE = registerBlock("moldy_obsidian_bricks_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CRACKED_OBSIDIAN_BRICKS = registerBlock("cracked_obsidian_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MOLDY_OBSIDIAN_BRICKS = registerBlock("moldy_obsidian_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> OBSIDIAN = registerBlock("obsidian",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> AZURE_OBSIDIAN = registerBlock("azure_obsidian",
            () -> new Block(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredRegister<Block> OBSIDIAN_STAIRS = registerBlock("obsidian_stairs",
            () -> new StairBlock(() -> BlocksObs.OBSIDIAN.get().defaultBlockState(),BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> OBSIDIAN_SLAB = registerBlock("obsidian_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> OBSIDIAN_WALL = registerBlock("obsidian_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> OBSIDIAN_FENCE = registerBlock("obsidian_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final DeferredRegister<Block> OBSIDIAN_STAIRS_D = registerBlock("obsidian_stairs_d",
            () -> new StairBlock(() -> BlocksObs.OBSIDIAN.get().defaultBlockState(),BlockBehaviour.Properties.of()
                    .strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> OBSIDIAN_SLAB_D = registerBlock("obsidian_slab_d",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<WallBlock> OBSIDIAN_WALL_D = registerBlock("obsidian_wall_d",
            () -> new WallBlock(BlockBehaviour.Properties.of()
                    .strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> OBSIDIAN_FENCE_D = registerBlock("obsidian_fence_d",
            () -> new FenceBlock(BlockBehaviour.Properties.of()
                    .strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MOLDY_CARVED_OBSIDIAN_BRICKS_D = registerBlock("moldy_carved_obsidian_bricks_d",
            () -> new Block(BlockBehaviour.Properties.of().strength(40.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CRACKED_CARVED_OBSIDIAN_BRICKS_D = registerBlock("cracked_carved_obsidian_bricks_d",
            () -> new Block(BlockBehaviour.Properties.of().strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> OBSIDIAN_BRICKS_D = registerBlock("obsidian_bricks_d",
            () -> new Block(BlockBehaviour.Properties.of().strength(50.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> OBSIDIAN_BRICKS_FENCE_D = registerBlock("obsidian_bricks_fence_d",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> CRACKED_OBSIDIAN_BRICKS_FENCE_D = registerBlock("cracked_obsidian_bricks_fence_d",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<FenceBlock> MOLDY_OBSIDIAN_BRICKS_FENCE_D = registerBlock("moldy_obsidian_bricks_fence_d",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(20.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CRACKED_OBSIDIAN_BRICKS_D = registerBlock("cracked_obsidian_bricks_d",
            () -> new Block(BlockBehaviour.Properties.of().strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MOLDY_OBSIDIAN_BRICKS_D = registerBlock("moldy_obsidian_bricks_d",
            () -> new Block(BlockBehaviour.Properties.of().strength(40.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));


    public static final DeferredBlock<Block> MYSTERIOUS_CARVED_OBSIDIAN_BRICKS = registerBlock("mysterious_carved_obsidian_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(50.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> CRACKED_MYSTERIOUS_CARVED_OBSIDIAN_BRICKS = registerBlock("cracked_mysterious_carved_obsidian_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MOLDY_MYSTERIOUS_CARVED_OBSIDIAN_BRICKS = registerBlock("moldy_mysterious_carved_obsidian_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(40.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));


    public static final DeferredBlock<DecorativeUrn> DECORATIVE_URN = registerBlock("decorative_urn",
            () -> new DecorativeUrn(BlockBehaviour.Properties.of().noOcclusion().strength(0.1F, 1.0F).sound(SoundType.GLASS)
                    .lightLevel((p_152629_) -> {
                return 5;
            })));
    public static final DeferredBlock<DecorativeUrn> UNUSUAL_DECORATIVE_URN = registerBlock("unusual_decorative_urn",
            () -> new DecorativeUrn(BlockBehaviour.Properties.of().noOcclusion().strength(0.1F, 1.0F).sound(SoundType.GLASS)
                    .lightLevel((p_152629_) -> {
                return 5;
            })));
    public static final DeferredBlock<DecorativeUrn> RARE_DECORATIVE_URN = registerBlock("rare_decorative_urn",
            () -> new DecorativeUrn(BlockBehaviour.Properties.of().noOcclusion().strength(0.1F, 1.0F).sound(SoundType.GLASS)
                    .lightLevel((p_152629_) -> {
                return 5;
            })));

    public static final DeferredBlock<LargeUrn> LARGE_URN = registerBlock("large_urn",
            () -> new LargeUrn(BlockBehaviour.Properties.of().noOcclusion().strength(0.2F, 1.0F)
                    .sound(SoundType.DECORATED_POT_CRACKED).lightLevel((p_152680_) -> {
                        return 5;
                    })));

    public static final DeferredBlock<FlameBannerBaggel> FLAME_BANNER_BAGGEL = registerBlock("flame_banner_baggel",
            () -> new FlameBannerBaggel(BlockBehaviour.Properties.of().noOcclusion().noCollission().instabreak()
                    .sound(SoundType.WOOL)));

    public static final DeferredBlock<ObsidianLectern> OBSIDIAN_LECTERN = registerBlock("obsidian_lectern",
            () -> new ObsidianLectern(BlockBehaviour.Properties.of().randomTicks().noOcclusion().strength(20F, 10000.0F)
                    .sound(SoundType.STONE)));


    public static final DeferredBlock<LargeAlchemicalTank> LARGE_ALCHEMICAL_TANK = registerBlock("large_alchemical_tank",
            () -> new LargeAlchemicalTank(BlockBehaviour.Properties.of().randomTicks().noOcclusion().strength(0.1F, 10000.0F)
                    .sound(SoundType.GLASS)));
    public static final DeferredBlock<LargeAlchemicalTankBroken> LARGE_ALCHEMICAL_TANK_BROKEN = registerBlock("large_alchemical_tank_broken",
            () -> new LargeAlchemicalTankBroken(BlockBehaviour.Properties.of().randomTicks().noOcclusion().strength(-1F, 3600000.0F)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<LockedRunicChest> LOCKED_CHEST_RUNIC = registerBlock("locked_chest_runic",
            () -> new LockedRunicChest(BlockBehaviour.Properties.of().noOcclusion()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));
    public static final DeferredBlock<FlameBlock> OBSIDAN_PLANKS = registerBlock("obsidan_planks",
            () -> new FlameBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS).sound(SoundType.CHERRY_WOOD)));
    public static final DeferredBlock<FenceBlock> OBSIDAN_FENCE = registerBlock("obsidan_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_FENCE).sound(SoundType.CHERRY_WOOD)));
    public static final DeferredBlock<FenceGateBlock> OBSIDAN_FENCE_GATE = registerBlock("obsidan_fence_gate",
            () -> new FenceGateBlock(ModWoodTypes.OBSIDAN,BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_FENCE).sound(SoundType.CHERRY_WOOD)));
    public static final DeferredBlock<DoorBlock> OBSIDAN_WOOD_DOOR = registerBlock("obsidan_wood_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_DOOR).sound(SoundType.CHERRY_WOOD)));
    public static final DeferredBlock<TrapDoorBlock> OBSIDAN_WOOD_TRAPDOOR = registerBlock("obsidan_wood_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK,BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_DOOR).sound(SoundType.CHERRY_WOOD)));
    public static final DeferredRegister<Block> OBSIDAN_PLANKS_PRESSURE_PLATE = registerBlock("obsidan_planks_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS).sound(SoundType.CHERRY_WOOD),
                    BlockSetType.IRON));
    public static final DeferredBlock<FlameButton> OBSIDAN_PLANKS_BUTTON = registerBlock("obsidan_planks_button",
            () -> new FlameButton(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_BUTTON).sound(SoundType.CHERRY_WOOD),
                    BlockSetType.OAK, 10, true));
    public static final DeferredBlock<FlameStairs> OBSIDAN_PLANKS_STAIRS = registerBlock("obsidan_planks_stairs",
            () -> new FlameStairs(() -> BlocksObs.OBSIDAN_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS).sound(SoundType.CHERRY_WOOD)));
    public static final DeferredBlock<StairBlock> OBSIDIAN_POLISHED_STAIRS = registerBlock("obsidian_polished_stairs",
            () -> new StairBlock(BlocksObs.OBSIDIAN_POLISHED.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> CRACKED_OBSIDIAN_POLISHED_STAIRS = registerBlock("cracked_obsidian_polished_stairs",
            () -> new StairBlock(BlocksObs.CRACKED_OBSIDIAN_POLISHED.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> MOLDY_OBSIDIAN_POLISHED_STAIRS = registerBlock("moldy_obsidian_polished_stairs",
            () -> new StairBlock(BlocksObs.MOLDY_OBSIDIAN_POLISHED.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> OBSIDIAN_BRICKS_STAIRS = registerBlock("obsidian_bricks_stairs",
            () -> new StairBlock(BlocksObs.OBSIDIAN_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> CRACKED_OBSIDIAN_BRICKS_STAIRS = registerBlock("cracked_obsidian_bricks_stairs",
            () -> new StairBlock(BlocksObs.CRACKED_OBSIDIAN_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> MOLDY_OBSIDIAN_BRICKS_STAIRS = registerBlock("moldy_obsidian_bricks_stairs",
            () -> new StairBlock(BlocksObs.MOLDY_OBSIDIAN_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> OBSIDIAN_BRICKS_SLAB = registerBlock("obsidian_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> CRACKED_OBSIDIAN_BRICKS_SLAB = registerBlock("cracked_obsidian_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> MOLDY_OBSIDIAN_BRICKS_SLAB = registerBlock("moldy_obsidian_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> OBSIDIAN_POLISHED_SLAB = registerBlock("obsidian_polished_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> CRACKED_OBSIDIAN_POLISHED_SLAB = registerBlock("cracked_obsidian_polished_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> MOLDY_OBSIDIAN_POLISHED_SLAB = registerBlock("moldy_obsidian_polished_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final DeferredBlock<StairBlock> OBSIDIAN_POLISHED_STAIRS_D = registerBlock("obsidian_polished_stairs_d",
            () -> new StairBlock(BlocksObs.OBSIDIAN_POLISHED_D.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(50.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> CRACKED_OBSIDIAN_POLISHED_STAIRS_D = registerBlock("cracked_obsidian_polished_stairs_d",
            () -> new StairBlock(BlocksObs.CRACKED_OBSIDIAN_POLISHED_D.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> MOLDY_OBSIDIAN_POLISHED_STAIRS_D = registerBlock("moldy_obsidian_polished_stairs_d",
            () -> new StairBlock(BlocksObs.MOLDY_OBSIDIAN_POLISHED_D.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(40.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> OBSIDIAN_BRICKS_STAIRS_D = registerBlock("obsidian_bricks_stairs_d",
            () -> new StairBlock(BlocksObs.OBSIDIAN_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(50.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> CRACKED_OBSIDIAN_BRICKS_STAIRS_D = registerBlock("cracked_obsidian_bricks_stairs_d",
            () -> new StairBlock(BlocksObs.CRACKED_OBSIDIAN_BRICKS_D.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<StairBlock> MOLDY_OBSIDIAN_BRICKS_STAIRS_D = registerBlock("moldy_obsidian_bricks_stairs_d",
            () -> new StairBlock(BlocksObs.MOLDY_OBSIDIAN_BRICKS_D.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(40.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> OBSIDIAN_BRICKS_SLAB_D = registerBlock("obsidian_bricks_slab_d",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(50.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> CRACKED_OBSIDIAN_BRICKS_SLAB_D = registerBlock("cracked_obsidian_bricks_slab_d",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> MOLDY_OBSIDIAN_BRICKS_SLAB_D = registerBlock("moldy_obsidian_bricks_slab_d",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(40.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> OBSIDIAN_POLISHED_SLAB_D = registerBlock("obsidian_polished_slab_d",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(50.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> CRACKED_OBSIDIAN_POLISHED_SLAB_D = registerBlock("cracked_obsidian_polished_slab_d",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(35.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<SlabBlock> MOLDY_OBSIDIAN_POLISHED_SLAB_D= registerBlock("moldy_obsidian_polished_slab_d",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(40.0F, 1200.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));




    public static final DeferredBlock<FlameSlab> OBSIDAN_PLANKS_SLAB = registerBlock("obsidan_planks_slab",
            () -> new FlameSlab(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS).sound(SoundType.CHERRY_WOOD)));
    public static final DeferredHolder<Block, ModStandingSignBlock> OBSIDAN_SIGN = BLOCKS.register("obsidan_sign",
            () -> new ModStandingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN), ModWoodTypes.OBSIDAN));
    public static final DeferredHolder<Block, ModWallSignBlock> OBSIDAN_WALL_SIGN = BLOCKS.register("obsidan_wall_sign",
            () -> new ModWallSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN), ModWoodTypes.OBSIDAN));

    public static final DeferredHolder<Block, ModHangingSignBlock> OBSIDAN_HANGING_SIGN = BLOCKS.register("obsidan_hanging_sign",
            () -> new ModHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN), ModWoodTypes.OBSIDAN));
    public static final DeferredHolder<Block, ModWallHangingSignBlock> OBSIDAN_WALL_HANGING_SIGN = BLOCKS.register("obsidan_wall_hanging_sign",
            () -> new ModWallHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN), ModWoodTypes.OBSIDAN));



    //Метод регистрации блоков
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ItemsObs.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    private static <T extends Block> DeferredRegister<T> registerChests(String name, Supplier<? extends T> sup, IronChestsTypes chestType) {
        return registerChestItem(name, sup, block -> item(block, () -> () -> chestType));
    }

    private static <T extends Block> DeferredRegister<T> registerChestItem(String name, Supplier<? extends T> sup, Function<DeferredRegister<T>, Supplier<? extends Item>> itemCreator) {
        DeferredRegister<T> ret = registerNoItem(name, sup);
        ITEMS.register(name, itemCreator.apply(ret));
        return ret;
    }

    private static <T extends Block> DeferredRegister<T> registerNoItem(String name, Supplier<? extends T> sup) {
        return BLOCKS.register(name, sup);
    }

    private static Supplier<BlockItem> item(final DeferredRegister<? extends Block> block, Supplier<Callable<IronChestsTypes>> chestType) {
        return () -> new IronChestBlockItem(block.get(), new Item.Properties(), chestType);
    }
}
