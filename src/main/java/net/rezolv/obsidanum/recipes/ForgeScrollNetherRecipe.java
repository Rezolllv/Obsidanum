package net.rezolv.obsidanum.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.rezolv.obsidanum.Obsidanum;

import javax.annotation.Nullable;

public class ForgeScrollNetherRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<ItemStack> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;

    public ForgeScrollNetherRecipe(NonNullList<ItemStack> inputItems, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        return true;
    }

    public NonNullList<ItemStack> getInputItems() {
        return inputItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.FORGE_SCROOL_NETHER;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.FORGE_SCROOL_NETHER;
    }

    public static class Type implements RecipeType<ForgeScrollNetherRecipe> {
        public static final Type FORGE_SCROOL_NETHER = new Type();
        public static final String ID = "forge_scroll_nether";
    }

    public static class Serializer implements RecipeSerializer<ForgeScrollNetherRecipe> {
        public static final Serializer FORGE_SCROOL_NETHER = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Obsidanum.MOD_ID, "forge_scroll_nether");

        @Override
        public ForgeScrollNetherRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "output"));
            output.setCount(GsonHelper.getAsInt(serializedRecipe, "count", 1)); // Default to 1 if not specified
            JsonArray ingredients = GsonHelper.getAsJsonArray(serializedRecipe, "ingredients");
            NonNullList<ItemStack> inputs = NonNullList.create();

            for (int i = 0; i < ingredients.size(); i++) {
                JsonObject ingredientObject = ingredients.get(i).getAsJsonObject();
                ItemStack itemStack = ShapedRecipe.itemStackFromJson(ingredientObject);
                itemStack.setCount(GsonHelper.getAsInt(ingredientObject, "count", 1)); // Устанавливаем количество
                inputs.add(itemStack);
            }

            return new ForgeScrollNetherRecipe(inputs, output, recipeId);
        }

        @Override
        public @Nullable ForgeScrollNetherRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int inputSize = buffer.readInt();
            NonNullList<ItemStack> inputs = NonNullList.withSize(inputSize, ItemStack.EMPTY);

            for (int i = 0; i < inputSize; i++) {
                inputs.set(i, buffer.readItem());
            }

            ItemStack output = buffer.readItem();
            return new ForgeScrollNetherRecipe(inputs, output, recipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ForgeScrollNetherRecipe recipe) {
            buffer.writeInt(recipe.inputItems.size());

            for (ItemStack stack : recipe.inputItems) {
                buffer.writeItemStack(stack, false);
            }

            buffer.writeItemStack(recipe.output, false);
        }
    }
}
