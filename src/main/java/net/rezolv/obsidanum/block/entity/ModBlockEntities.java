package net.rezolv.obsidanum.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.BlocksObs;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Obsidanum.MOD_ID);

    public static final RegistryObject<BlockEntityType<ModSignBlockEntity>> OBSIDAN_SIGN =
            BLOCK_ENTITIES.register("obsidan_sign", () ->
                    BlockEntityType.Builder.of(ModSignBlockEntity::new,
                            BlocksObs.OBSIDAN_SIGN.get(), BlocksObs.OBSIDAN_WALL_SIGN.get()).build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntity>> OBSIDAN_HANGING_SIGN =
            BLOCK_ENTITIES.register("obsidan_hanging_sign", () ->
                    BlockEntityType.Builder.of(ModHangingSignBlockEntity::new,
                            BlocksObs.OBSIDAN_HANGING_SIGN.get(), BlocksObs.OBSIDAN_WALL_HANGING_SIGN.get()).build(null));


    public static final RegistryObject<BlockEntityType<RightForgeScrollEntity>> RIGHT_FORGE_SCROLL =
            BLOCK_ENTITIES.register("right_forge_scroll", () ->
                    BlockEntityType.Builder.of(RightForgeScrollEntity::new,
                            BlocksObs.RIGHT_FORGE_SCROLL.get()).build(null));

    public static final RegistryObject<BlockEntityType<ForgeCrucibleEntity>> FORGE_CRUCIBLE =
            BLOCK_ENTITIES.register("forge_crucible", () ->
                    BlockEntityType.Builder.of(ForgeCrucibleEntity::new,
                            BlocksObs.FORGE_CRUCIBLE.get()).build(null));



    public static final RegistryObject<BlockEntityType<DispenserBlockEntity>> FLAME_DISPENSER =
            BLOCK_ENTITIES.register("flame_dispenser", () ->
                    BlockEntityType.Builder.of(DispenserBlockEntity::new,
                            BlocksObs.FLAME_DISPENSER.get()).build(null));

    public static final RegistryObject<BlockEntityType<FlamePipeEntity>> FLAME_PIPE =
            BLOCK_ENTITIES.register("flame_pipe", () ->
                    BlockEntityType.Builder.of(FlamePipeEntity::new,
                            BlocksObs.FLAME_PIPE.get()).build(null));

    public static final RegistryObject<BlockEntityType<HammerForgeEntity>> HAMMER_FORGE =
            BLOCK_ENTITIES.register("hammer_forge", () ->
                    BlockEntityType.Builder.of(HammerForgeEntity::new,
                            BlocksObs.HAMMER_FORGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<PranaCrystallEntity>> PRANA_CRYSTALL =
            BLOCK_ENTITIES.register("prana_crystall", () ->
                    BlockEntityType.Builder.of(PranaCrystallEntity::new,
                            BlocksObs.PRANA_CRYSTALL.get()).build(null));
}