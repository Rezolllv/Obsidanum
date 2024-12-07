package net.rezolv.obsidanum.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;
import net.rezolv.obsidanum.block.entity.RightForgeScrollEntity;
import org.jetbrains.annotations.Nullable;


public class ForgeCrucible extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public ForgeCrucible(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        getIngredients(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), pPlayer);
        if (pPlayer.isCrouching()) {
            giveBackIngredients(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), pPlayer);
        }
        return InteractionResult.SUCCESS;
    }

    public static void giveBackIngredients(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null || !(entity instanceof Player player)) {
            return;
        }

        BlockPos pos = BlockPos.containing(x, y, z);
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
            CompoundTag crucibleNBT = forgeCrucibleEntity.getPersistentData();

            if (crucibleNBT.contains("originalIngredients", Tag.TAG_LIST)) {
                ListTag originalList = crucibleNBT.getList("originalIngredients", Tag.TAG_COMPOUND);
                ListTag currentIngredients = crucibleNBT.getList("ingredients", Tag.TAG_COMPOUND);

                for (int i = 0; i < originalList.size(); i++) {
                    CompoundTag originalNBT = originalList.getCompound(i);
                    String itemId = originalNBT.getString("item");
                    int originalCount = originalNBT.getInt("count");

                    for (int j = 0; j < currentIngredients.size(); j++) {
                        CompoundTag currentNBT = currentIngredients.getCompound(j);
                        if (currentNBT.getString("item").equals(itemId)) {
                            int currentCount = currentNBT.getInt("count");
                            int returnedCount = originalCount - currentCount;

                            if (returnedCount > 0) {
                                ItemStack returnedStack = new ItemStack(
                                        ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)), returnedCount
                                );

                                if (!player.addItem(returnedStack)) {
                                    player.drop(returnedStack, false);
                                }

                                currentNBT.putInt("count", originalCount); // Восстанавливаем NBT
                            }
                        }
                    }
                }

                forgeCrucibleEntity.setPersistentData(crucibleNBT);
            }
        }
    }

    public static void getIngredients(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null || !(entity instanceof Player player)) {
            return;
        }

        ItemStack heldItem = player.getMainHandItem();

        if (heldItem.isEmpty()) {
            System.out.println("Player's hand is empty.");
            return;
        }

        BlockPos pos = BlockPos.containing(x, y, z);
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
            CompoundTag crucibleNBT = forgeCrucibleEntity.getPersistentData();

            // Проверяем и запоминаем изначальные значения ингредиентов
            if (!crucibleNBT.contains("originalIngredients")) {
                if (crucibleNBT.contains("ingredients", Tag.TAG_LIST)) {
                    ListTag originalList = crucibleNBT.getList("ingredients", Tag.TAG_COMPOUND).copy();
                    crucibleNBT.put("originalIngredients", originalList);
                }
            }

            if (crucibleNBT.contains("ingredients", Tag.TAG_LIST)) {
                ListTag ingredientsList = crucibleNBT.getList("ingredients", Tag.TAG_COMPOUND);

                for (Tag ingredientTag : ingredientsList) {
                    CompoundTag ingredientNBT = (CompoundTag) ingredientTag;
                    String requiredItemId = ingredientNBT.getString("item");

                    if (heldItem.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation(requiredItemId))) {
                        int ingredientCount = ingredientNBT.getInt("count");

                        if (ingredientCount <= 0) {
                            System.out.println("Not enough ingredients to interact.");
                            return;
                        }

                        heldItem.shrink(1);
                        ingredientNBT.putInt("count", ingredientCount - 1);

                        forgeCrucibleEntity.setPersistentData(crucibleNBT);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        Direction facing = state.getValue(FACING);

        BlockPos expectedRightPos = switch (facing) {
            case NORTH -> pos.west();
            case SOUTH -> pos.east();
            case EAST -> pos.north();
            case WEST -> pos.south();
            default -> null;
        };

        if (expectedRightPos != null && expectedRightPos.equals(fromPos)) {
            BlockEntity rightEntity = level.getBlockEntity(expectedRightPos);
            if (rightEntity instanceof RightForgeScrollEntity rightForgeScrollEntity) {
                CompoundTag scrollNBT = rightForgeScrollEntity.getScrollNBT();
                getNbtScroll(level, pos, state, scrollNBT);
                return;
            }
        }

        BlockPos expectedLeftPos = switch (facing) {
            case NORTH -> pos.east();
            case SOUTH -> pos.west();
            case EAST -> pos.south();
            case WEST -> pos.north();
            default -> null;
        };

        if (expectedLeftPos != null && expectedLeftPos.equals(fromPos)) {
            BlockState leftBlockState = level.getBlockState(expectedLeftPos);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
                CompoundTag crucibleNBT = forgeCrucibleEntity.getPersistentData();
                ListTag currentIngredients = crucibleNBT.getList("ingredients", Tag.TAG_COMPOUND);
                int totalIngredientCount = 0;

                for (int i = 0; i < currentIngredients.size(); i++) {
                    CompoundTag currentNBT = currentIngredients.getCompound(i);
                    totalIngredientCount += currentNBT.getInt("count");
                }

                if (leftBlockState.is(BlocksObs.LEFT_CORNER_LEVEL.get())
                        && leftBlockState.getValue(LeftCornerLevel.IS_PRESSED)
                        && totalIngredientCount == 0) {

                    getClearedItems(level,pos,state,crucibleNBT);

                    if (crucibleNBT.contains("originalIngredients", Tag.TAG_LIST)) {
                        ListTag originalList = crucibleNBT.getList("originalIngredients", Tag.TAG_COMPOUND);

                        for (int i = 0; i < originalList.size(); i++) {
                            CompoundTag originalNBT = originalList.getCompound(i);
                            String itemId = originalNBT.getString("item");
                            int originalCount = originalNBT.getInt("count");

                            for (int j = 0; j < currentIngredients.size(); j++) {
                                CompoundTag currentNBT = currentIngredients.getCompound(j);
                                if (currentNBT.getString("item").equals(itemId)) {
                                    currentNBT.putInt("count", originalCount);
                                }
                            }
                        }

                        forgeCrucibleEntity.setPersistentData(crucibleNBT);
                        forgeCrucibleEntity.setChanged();
                        level.sendBlockUpdated(pos, leftBlockState, leftBlockState, 3);
                        System.out.println("Ingredients restored to their original counts.");
                    } else {
                        System.out.println("No original ingredients found to restore.");
                    }

                }
            }
        }
    }
    private void getOutputItems(Level level, BlockPos pos, BlockState state, CompoundTag scrollNBT) {
        if (!scrollNBT.contains("ingredients", Tag.TAG_LIST) || !scrollNBT.contains("originalIngredients", Tag.TAG_LIST)) {
            return;
        }

        ListTag currentIngredients = scrollNBT.getList("ingredients", Tag.TAG_COMPOUND);
        ListTag originalIngredients = scrollNBT.getList("originalIngredients", Tag.TAG_COMPOUND);

        // Позиция для выпадения предметов
        BlockPos dropPos = pos.above();

        // Сравниваем текущие и оригинальные ингредиенты
        for (int i = 0; i < originalIngredients.size(); i++) {
            CompoundTag originalIngredientNBT = originalIngredients.getCompound(i);
            String itemId = originalIngredientNBT.getString("item");
            int originalCount = originalIngredientNBT.getInt("count");

            // Ищем соответствующий ингредиент в текущем списке
            CompoundTag currentIngredientNBT = null;
            for (Tag tag : currentIngredients) {
                CompoundTag temp = (CompoundTag) tag;
                if (temp.getString("item").equals(itemId)) {
                    currentIngredientNBT = temp;
                    break;
                }
            }

            int currentCount = currentIngredientNBT != null ? currentIngredientNBT.getInt("count") : 0;

            // Если ингредиенты недостающие, сбрасываем их
            int missingCount = originalCount - currentCount;
            if (missingCount > 0) {
                Item ingredientItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                if (ingredientItem != null) {
                    ItemStack itemStack = new ItemStack(ingredientItem, missingCount);

                    // Создаем сущность предмета и добавляем её в мир
                    ItemEntity itemEntity = new ItemEntity(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), itemStack);
                    level.addFreshEntity(itemEntity);
                }
            }
        }

    }


    private void getClearedItems(Level level, BlockPos pos, BlockState state, CompoundTag scrollNBT) {
        BlockPos dropPos = pos.above();
        if (!scrollNBT.contains("output", Tag.TAG_COMPOUND)) {
            return;
        }
        CompoundTag outputTag = scrollNBT.getCompound("output");
        String itemId = outputTag.getString("item");
        int count = outputTag.getInt("count");
        Item outputItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
        if (outputItem == null) {
            return;
        }
        ItemStack outputItemStack = new ItemStack(outputItem, count);
        ItemEntity outputItemEntity = new ItemEntity(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), outputItemStack);
        level.addFreshEntity(outputItemEntity);
    }


    private void getNbtScroll(Level level, BlockPos pos, BlockState state, CompoundTag scrollNBT) {
        if (!scrollNBT.isEmpty()) {
            System.out.println("RightForgeScroll NBT Data at " + pos + ": " + scrollNBT);

            if (scrollNBT.contains("type")) {
                String type = scrollNBT.getString("type");
                System.out.println("Recipe Type: " + type);
            }

            if (scrollNBT.contains("ingredients", Tag.TAG_LIST)) {
                ListTag ingredientsList = scrollNBT.getList("ingredients", Tag.TAG_COMPOUND);
                System.out.println("Ingredients:");
                for (Tag ingredientTag : ingredientsList) {
                    CompoundTag ingredientNBT = (CompoundTag) ingredientTag;
                    String item = ingredientNBT.getString("item");
                    int count = ingredientNBT.getInt("count");
                    System.out.println("- Item: " + item + ", Count: " + count);
                }
            }

            if (!scrollNBT.contains("output", Tag.TAG_COMPOUND)) {
                BlockEntity crucibleEntity = level.getBlockEntity(pos);
                if (crucibleEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
                    forgeCrucibleEntity.setOutput(ItemStack.EMPTY);
                    forgeCrucibleEntity.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            }

            if (scrollNBT.contains("RecipeResult", Tag.TAG_LIST)) {
                ListTag recipeResultList = scrollNBT.getList("RecipeResult", Tag.TAG_COMPOUND);
                if (!recipeResultList.isEmpty()) {
                    CompoundTag outputTag = recipeResultList.getCompound(0); // Берем первый элемент
                    String itemId = outputTag.getString("id");
                    int count = outputTag.getInt("Count");
                    Item outputItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                    if (outputItem != null) {
                        ItemStack output = new ItemStack(outputItem, count);

                        BlockEntity crucibleEntity = level.getBlockEntity(pos);
                        if (crucibleEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
                            forgeCrucibleEntity.setOutput(output);
                            forgeCrucibleEntity.setChanged();
                            level.sendBlockUpdated(pos, state, state, 3);
                        }
                    }
                }
            }

            BlockEntity crucibleEntity = level.getBlockEntity(pos);
            if (crucibleEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
                getClearedItems(level, pos, state, scrollNBT);
                forgeCrucibleEntity.updateFromScrollNBT(scrollNBT);

                forgeCrucibleEntity.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
            }
        } else {
            System.out.println("RightForgeScroll NBT Data is empty at " + pos);
            System.out.println("Ingredients list is empty.");

            BlockEntity crucibleEntity = level.getBlockEntity(pos);
            if (crucibleEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
                // Вызов getClearedItems перед очисткой
                getOutputItems(level, pos, state, forgeCrucibleEntity.getPersistentData());

                forgeCrucibleEntity.clear();
                forgeCrucibleEntity.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
            }
        }
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ForgeCrucibleEntity(pPos, pState);
    }
}