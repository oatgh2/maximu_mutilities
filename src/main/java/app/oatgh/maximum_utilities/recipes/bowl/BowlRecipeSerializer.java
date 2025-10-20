package app.oatgh.maximum_utilities.recipes.bowl;

import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class BowlRecipeSerializer implements RecipeSerializer<BowlRecipe> {

  @Override
  public BowlRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
    JsonArray ingredientsArr = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
    NonNullList<Ingredient> ingredients = NonNullList.create();
    for (JsonElement ingredient : ingredientsArr)
      ingredients.add(Ingredient.fromJson(ingredientsArr));

    FluidStack fluidStack = null;
      if(pSerializedRecipe.has("fluid")){
        JsonObject fluidObj = GsonHelper.getAsJsonObject(pSerializedRecipe, "fluid");
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(GsonHelper.getAsString(fluidObj, "name")));
        int amount = GsonHelper.getAsInt(fluidObj, "amount");
        fluidStack = new FluidStack(fluid, amount);
    }
    ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));
    return new BowlRecipe(pRecipeId, CraftingBookCategory.BUILDING, ingredients, fluidStack, result);
  }

  @Override
  public @Nullable BowlRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf buf) {
    int ingCount = buf.readInt();
    NonNullList<Ingredient> ingredients = NonNullList.withSize(ingCount, Ingredient.EMPTY);
    for(int i = 0; i < ingCount; i++) ingredients.set(i, Ingredient.fromNetwork(buf));
    FluidStack fluidStack = buf.readFluidStack();
    ItemStack result = buf.readItem();
    return new BowlRecipe(pRecipeId, CraftingBookCategory.BUILDING, ingredients, fluidStack, result);
  }

  @Override
  public void toNetwork(FriendlyByteBuf buf, BowlRecipe recipe) {
     buf.writeInt(recipe.getIngredients().size());
        for (Ingredient i : recipe.getIngredients())
            i.toNetwork(buf);

        buf.writeFluidStack(recipe.getRequiredFluidStack());
        buf.writeItem(recipe.getResultItem(null));
  }

}
