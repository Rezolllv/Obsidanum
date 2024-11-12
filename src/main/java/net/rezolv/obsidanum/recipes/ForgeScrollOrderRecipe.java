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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.rezolv.obsidanum.Obsidanum;

import javax.annotation.Nullable;

public class ForgeScrollOrderRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<ItemStack> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    public ForgeScrollOrderRecipe(NonNullList<ItemStack> inputItems, ItemStack output, ResourceLocation id) {
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
        return Serializer.FORGE_SCROOL_ORDER;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.FORGE_SCROOL_ORDER;
    }
    public static class Type implements RecipeType<ForgeScrollOrderRecipe> {
        public static final Type FORGE_SCROOL_ORDER = new Type();
        public static final String ID = "forge_scroll_order";
    }
    public static class Serializer implements RecipeSerializer<ForgeScrollOrderRecipe> {
        public static final Serializer FORGE_SCROOL_ORDER = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Obsidanum.MOD_ID, "forge_scroll_order");

        @Override
        public ForgeScrollOrderRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(serializedRecipe, "ingredients");
            NonNullList<ItemStack> inputs = NonNullList.create();

            for (int i = 0; i < ingredients.size(); i++) {
                JsonObject ingredientObject = ingredients.get(i).getAsJsonObject();
                ItemStack itemStack = ShapedRecipe.itemStackFromJson(ingredientObject);
                itemStack.setCount(GsonHelper.getAsInt(ingredientObject, "count", 1)); // Устанавливаем количество
                inputs.add(itemStack);
            }

            return new ForgeScrollOrderRecipe(inputs, output, recipeId);
        }

        @Override
        public @Nullable ForgeScrollOrderRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int inputSize = buffer.readInt();
            NonNullList<ItemStack> inputs = NonNullList.withSize(inputSize, ItemStack.EMPTY);

            for (int i = 0; i < inputSize; i++) {
                inputs.set(i, buffer.readItem());
            }

            ItemStack output = buffer.readItem();
            return new ForgeScrollOrderRecipe(inputs, output, recipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ForgeScrollOrderRecipe recipe) {
            buffer.writeInt(recipe.inputItems.size());

            for (ItemStack stack : recipe.inputItems) {
                buffer.writeItemStack(stack, false);
            }

            buffer.writeItemStack(recipe.output, false);
        }
    }
}
