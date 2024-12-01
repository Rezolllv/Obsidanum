package net.rezolv.obsidanum.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ForgeCrucibleEntity extends BlockEntity implements WorldlyContainer {
    private static final int INVENTORY_SIZE = 9; // Количество слотов в инвентаре
    private List<ItemStack> items = new ArrayList<>(INVENTORY_SIZE);
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();


    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
    public ForgeCrucibleEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FORGE_CRUCIBLE.get(), pPos, pBlockState);
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            items.add(ItemStack.EMPTY); // Инициализация слотов пустыми стеками
        }
    }
    public List<ItemStack> getIngredients() {
        List<ItemStack> ingredients = new ArrayList<>();

        CompoundTag blockEntityNBT = this.getPersistentData();
        if (blockEntityNBT.contains("ingredients", Tag.TAG_LIST)) {
            ListTag ingredientsList = blockEntityNBT.getList("ingredients", Tag.TAG_COMPOUND);
            for (int i = 0; i < ingredientsList.size(); i++) {
                CompoundTag ingredientNBT = ingredientsList.getCompound(i);
                String item = ingredientNBT.getString("item");
                int count = ingredientNBT.getInt("count");

                Item itemById = ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
                ItemStack ingredientStack = new ItemStack(itemById, count);
                ingredients.add(ingredientStack);
            }
        }
        return ingredients;
    }

    public void setIngredients(List<ItemStack> ingredients) {
        CompoundTag ingredientsNBT = new CompoundTag();
        ListTag ingredientsList = new ListTag();

        for (ItemStack ingredient : ingredients) {
            if (!ingredient.isEmpty()) { // добавим проверку на пустой стэк
                CompoundTag ingredientNBT = new CompoundTag();
                ingredientNBT.putString("item", ForgeRegistries.ITEMS.getKey(ingredient.getItem()).toString());
                ingredientNBT.putInt("count", ingredient.getCount());
                ingredientsList.add(ingredientNBT);
            }
        }

        // Сохраняем список ингредиентов в NBT
        this.getPersistentData().put("ingredients", ingredientsList);
    }

    private ItemStack output = ItemStack.EMPTY;


    public void setOutput(ItemStack output) {
        this.output = output.isEmpty() ? ItemStack.EMPTY : output.copy();

        CompoundTag outputNBT = new CompoundTag();
        if (!output.isEmpty()) { // Проверка на пустой стэк
            outputNBT.putString("item", ForgeRegistries.ITEMS.getKey(output.getItem()).toString());
            outputNBT.putInt("count", output.getCount());
        }
        // Сохраняем output в NBT
        this.getPersistentData().put("output", outputNBT);
    }

    public ItemStack getOutput() {

        CompoundTag blockEntityNBT = this.getPersistentData();
        if (blockEntityNBT.contains("output", Tag.TAG_COMPOUND)) {
            CompoundTag outputNBT = blockEntityNBT.getCompound("output");
            String item = outputNBT.getString("item");
            int count = outputNBT.getInt("count");

            Item itemById = ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
            if (itemById != null) {
                return new ItemStack(itemById, count);
            }
        }

        return this.output.isEmpty() ? ItemStack.EMPTY : this.output.copy();

    }
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        // Загрузка ингредиентов
        if (tag.contains("RecipeIngredients", Tag.TAG_LIST)) {
            ListTag recipeIngredientsList = tag.getList("RecipeIngredients", Tag.TAG_COMPOUND);
            if (!recipeIngredientsList.isEmpty()) {
                List<ItemStack> ingredients = new ArrayList<>();
                for (int i = 0; i < recipeIngredientsList.size(); i++) {
                    CompoundTag ingredientTag = recipeIngredientsList.getCompound(i);
                    String itemId = ingredientTag.getString("id");
                    int count = ingredientTag.getInt("Count");
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                    if (item != null) {
                        ingredients.add(new ItemStack(item, count));
                    }
                }
                this.setIngredients(ingredients);
                System.out.println("Loaded ingredients: " + ingredients);
            } else {
                System.out.println("No ingredients found in NBT data.");
            }
        }

        // Загрузка output
        if (tag.contains("RecipeOutput", Tag.TAG_COMPOUND)) {
            CompoundTag outputTag = tag.getCompound("RecipeOutput");
            String outputItemId = outputTag.getString("id");
            int outputCount = outputTag.getInt("Count");
            Item outputItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(outputItemId));
            if (outputItem != null) {
                this.output = new ItemStack(outputItem, outputCount);
                System.out.println("Loaded output: " + this.output);
            } else {
                System.out.println("Invalid output item ID: " + outputItemId);
            }
        } else {
            System.out.println("No output found in NBT data.");
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        // Сохранение ингредиентов
        ListTag ingredientsList = new ListTag();
        for (ItemStack ingredient : this.getIngredients()) {
            CompoundTag ingredientNBT = new CompoundTag();
            ingredientNBT.putString("id", ForgeRegistries.ITEMS.getKey(ingredient.getItem()).toString());
            ingredientNBT.putInt("Count", ingredient.getCount());
            ingredientsList.add(ingredientNBT);
        }
        tag.put("RecipeIngredients", ingredientsList);
        System.out.println("Saved ingredients: " + ingredientsList);

        // Сохранение output
        if (!this.output.isEmpty()) {
            CompoundTag outputTag = new CompoundTag();
            outputTag.putString("id", ForgeRegistries.ITEMS.getKey(this.output.getItem()).toString());
            outputTag.putInt("Count", this.output.getCount());
            tag.put("RecipeOutput", outputTag);
            System.out.println("Saved output: " + this.output);
        } else {
            System.out.println("No output to save.");
        }
    }



    @Override
    public AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox();
    }






    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
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

    public void clear() {
        // Очистить внутренний список предметов
        items.clear();
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            items.add(ItemStack.EMPTY); // Инициализируем все слоты как пустые
        }

        // Очистить все данные в NBT
        CompoundTag blockEntityNBT = this.getPersistentData();
        blockEntityNBT.remove("ingredients");  // Удаляем ключ "ingredients"
        blockEntityNBT.remove("output");       // Удаляем ключ "output"
        blockEntityNBT.remove("type");         // Удаляем ключ "type"
        blockEntityNBT.remove("RecipeResult"); // Удаляем ключ "RecipeResult"

        // Обновить состояние блока
        if (!level.isClientSide()) {
            setChanged(); // Обозначить, что блок изменился
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3); // Отправить обновление блока
        }

        // Сбросить данные состояния, если нужно
        // Например, сбросить другие флаги или переменные
        this.output = ItemStack.EMPTY; // Очистить output
    }
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    public void setPersistentData(CompoundTag tag) {
        this.getPersistentData().put("ingredients", tag.getList("ingredients", Tag.TAG_COMPOUND));
        this.getPersistentData().put("output", tag.getCompound("output"));
        // Add any additional persistence logic here
    }
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}