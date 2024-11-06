package net.rezolv.obsidanum.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.recipes.ForgeScrollCatacombsRecipe;
import net.rezolv.obsidanum.recipes.ForgeScrollNetherRecipe;
import net.rezolv.obsidanum.recipes.ForgeScrollOrderRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForgeCrucibleEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private static final int INVENTORY_SIZE = 9; // Количество слотов в инвентаре
    private List<ItemStack> items = new ArrayList<>(INVENTORY_SIZE);

    public ForgeCrucibleEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.RIGHT_FORGE_SCROLL.get(), pPos, pBlockState);
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            items.add(ItemStack.EMPTY); // Инициализация слотов пустыми стеками
        }
    }


    private CompoundTag scrollNBT = new CompoundTag();

    public List<ItemStack> getRecipeIngredients() {
        Level level = this.level;
        if (level == null) return new ArrayList<>();

        RecipeManager recipeManager = level.getRecipeManager();
        SimpleContainer container = new SimpleContainer(getContainerSize());

        List<ItemStack> ingredients = new ArrayList<>();

        // Проверка для каждого типа рецепта
        Optional<ForgeScrollOrderRecipe> scrollRecipe = recipeManager.getRecipeFor(ForgeScrollOrderRecipe.Type.FORGE_SCROOL_ORDER, container, level);
        Optional<ForgeScrollNetherRecipe> netherRecipe = recipeManager.getRecipeFor(ForgeScrollNetherRecipe.Type.FORGE_SCROOL_NETHER, container, level);
        Optional<ForgeScrollCatacombsRecipe> catacombsRecipe = recipeManager.getRecipeFor(ForgeScrollCatacombsRecipe.Type.FORGE_SCROOL_CATACOMBS, container, level);

        if (scrollRecipe.isPresent()) {
            for (Ingredient ingredient : scrollRecipe.get().getIngredients()) {
                if (ingredient.getItems().length > 0) {
                    ingredients.add(ingredient.getItems()[0].copy());
                }
            }
        } else if (netherRecipe.isPresent()) {
            for (Ingredient ingredient : netherRecipe.get().getIngredients()) {
                if (ingredient.getItems().length > 0) {
                    ingredients.add(ingredient.getItems()[0].copy());
                }
            }
        } else if (catacombsRecipe.isPresent()) {
            for (Ingredient ingredient : catacombsRecipe.get().getIngredients()) {
                if (ingredient.getItems().length > 0) {
                    ingredients.add(ingredient.getItems()[0].copy());
                }
            }
        }
        if (scrollRecipe.isPresent()) {
            // Debug output
            System.out.println("Found scroll recipe.");
            for (Ingredient ingredient : scrollRecipe.get().getIngredients()) {
                if (ingredient.getItems().length > 0) {
                    ingredients.add(ingredient.getItems()[0].copy());
                    System.out.println("Added ingredient: " + ingredient.getItems()[0].getDisplayName().getString());
                }
            }
        }
        return ingredients;
    }

    // Получение результата текущего рецепта
    public ItemStack getRecipeResult() {
        Level level = this.level;
        if (level == null) return ItemStack.EMPTY;

        RecipeManager recipeManager = level.getRecipeManager();
        SimpleContainer container = new SimpleContainer(getContainerSize());

        // Проверка для каждого типа рецепта
        Optional<ForgeScrollOrderRecipe> scrollRecipe = recipeManager.getRecipeFor(ForgeScrollOrderRecipe.Type.FORGE_SCROOL_ORDER, container, level);
        Optional<ForgeScrollNetherRecipe> netherRecipe = recipeManager.getRecipeFor(ForgeScrollNetherRecipe.Type.FORGE_SCROOL_NETHER, container, level);
        Optional<ForgeScrollCatacombsRecipe> catacombsRecipe = recipeManager.getRecipeFor(ForgeScrollCatacombsRecipe.Type.FORGE_SCROOL_CATACOMBS, container, level);

        if (scrollRecipe.isPresent()) {
            return scrollRecipe.get().getResultItem(level.registryAccess()).copy();
        } else if (netherRecipe.isPresent()) {
            return netherRecipe.get().getResultItem(level.registryAccess()).copy();
        } else if (catacombsRecipe.isPresent()) {
            return catacombsRecipe.get().getResultItem(level.registryAccess()).copy();
        }

        return ItemStack.EMPTY;
    }
    public void setScrollNBT(CompoundTag tag) {
        this.scrollNBT = tag.copy();
        this.setChanged();
    }

    public void updateFromScrollNBT(CompoundTag scrollNBT) {
        if (scrollNBT != null && !scrollNBT.isEmpty()) {
            this.load(scrollNBT);
            this.setChanged();
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        int[] slots = new int[INVENTORY_SIZE];
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            slots[i] = i;
        }
        return slots;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return true; // Позволяет размещать любые предметы через любую сторону
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true; // Позволяет забирать предметы через любую сторону
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Forge Crucible");
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null; // Реализуйте создание меню, если нужно
    }

    @Override
    public int getContainerSize() {
        return INVENTORY_SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return i >= 0 && i < INVENTORY_SIZE ? items.get(i) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int i, int count) {
        if (i >= 0 && i < INVENTORY_SIZE && !items.get(i).isEmpty() && count > 0) {
            return items.get(i).split(count);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if (i >= 0 && i < INVENTORY_SIZE) {
            ItemStack stack = items.get(i);
            items.set(i, ItemStack.EMPTY);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i >= 0 && i < INVENTORY_SIZE) {
            items.set(i, itemStack);
            if (itemStack.getCount() > getMaxStackSize()) {
                itemStack.setCount(getMaxStackSize());
            }
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        items.clear();
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            items.add(ItemStack.EMPTY);
        }
    }
}