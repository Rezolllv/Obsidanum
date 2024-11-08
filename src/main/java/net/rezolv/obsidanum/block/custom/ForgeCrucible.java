package net.rezolv.obsidanum.block.custom;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;
import net.rezolv.obsidanum.block.entity.RightForgeScrollEntity;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        Direction facing = state.getValue(FACING);
        BlockPos rightPos;
        switch (facing) {
            case NORTH -> rightPos = pos.west();
            case SOUTH -> rightPos = pos.east();
            case EAST -> rightPos = pos.north();
            case WEST -> rightPos = pos.south();
            default -> {
                System.out.println("Unknown facing direction!");
                return;
            }
        }

        BlockEntity rightEntity = level.getBlockEntity(rightPos);
        if (rightEntity instanceof RightForgeScrollEntity rightForgeScrollEntity) {
            CompoundTag scrollNBT = rightForgeScrollEntity.getScrollNBT();

            if (!scrollNBT.isEmpty()) {
                System.out.println("RightForgeScroll NBT Data at " + rightPos + ": " + scrollNBT);

                // Проверка на наличие "RecipesPlans" и обработка рецепта
                if (scrollNBT.contains("RecipesPlans")) {
                    String recipePath = scrollNBT.getString("RecipesPlans");
                    System.out.println("Recipe Path: " + recipePath);

                    // Загружаем данные рецепта на основе пути
                    loadRecipeData(recipePath, level, pos);

                } else {
                    System.out.println("No valid recipe data found in RightForgeScroll NBT.");
                }

                // Обновление данных в ForgeCrucibleEntity
                BlockEntity crucibleEntity = level.getBlockEntity(pos);
                if (crucibleEntity instanceof ForgeCrucibleEntity forgeCrucibleEntity) {
                    forgeCrucibleEntity.updateFromScrollNBT(scrollNBT);
                    forgeCrucibleEntity.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            } else {
                System.out.println("RightForgeScroll NBT Data is empty at " + rightPos);
            }
        }
    }

    // Метод для загрузки данных рецепта
    private void loadRecipeData(String recipePath, Level level, BlockPos pos) {
        // Используем правильный путь для ресурса, если необходимо
        ResourceLocation recipeLocation = new ResourceLocation("obsidanum", recipePath);
        ResourceManager resourceManager = level.getServer().getResourceManager();

        try {
            // Попытка получить ресурс
            Resource resource = resourceManager.getResource(recipeLocation).orElseThrow(
                    () -> new IOException("Resource not found: " + recipeLocation)
            );

            // Чтение JSON из ресурса
            try (InputStreamReader reader = new InputStreamReader(resource.open())) {
                JsonObject recipeJson = JsonParser.parseReader(reader).getAsJsonObject();

                // Проверяем, что "type" существует
                if (recipeJson.has("type")) {
                    String type = recipeJson.get("type").getAsString();
                    System.out.println("Recipe Type: " + type);
                } else {
                    System.out.println("No 'type' found in recipe JSON.");
                }

                // Извлечение ингредиентов
                if (recipeJson.has("ingredients")) {
                    System.out.println("Ingredients:");
                    for (var ingredient : recipeJson.getAsJsonArray("ingredients")) {
                        JsonObject ingredientObj = ingredient.getAsJsonObject();
                        if (ingredientObj.has("item") && ingredientObj.has("count")) {
                            String item = ingredientObj.get("item").getAsString();
                            int count = ingredientObj.get("count").getAsInt();
                            System.out.println("- Item: " + item + ", Count: " + count);
                        } else {
                            System.out.println("Invalid ingredient format.");
                        }
                    }
                } else {
                    System.out.println("No ingredients found in recipe JSON.");
                }

                // Извлечение выходного элемента
                if (recipeJson.has("output")) {
                    JsonObject output = recipeJson.getAsJsonObject("output");
                    if (output.has("item") && output.has("count")) {
                        String outputItem = output.get("item").getAsString();
                        int outputCount = output.get("count").getAsInt();
                        System.out.println("Output Item: " + outputItem + ", Count: " + outputCount);
                    } else {
                        System.out.println("Invalid output format.");
                    }
                } else {
                    System.out.println("No output found in recipe JSON.");
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to load recipe data for: " + recipePath);
            e.printStackTrace();
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
