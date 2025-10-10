package app.oatgh.maximum_utilities.recipes.bowl;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class DrainBowlFluidRecipeSerializer implements RecipeSerializer<DrainBowlFluidRecipe> {

  @Override
  public DrainBowlFluidRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
    return new DrainBowlFluidRecipe(pRecipeId, CraftingBookCategory.BUILDING);
  }

  @Override
  public @Nullable DrainBowlFluidRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
    return new DrainBowlFluidRecipe(pRecipeId, CraftingBookCategory.BUILDING);
  }

  @Override
  public void toNetwork(FriendlyByteBuf pBuffer, DrainBowlFluidRecipe pRecipe) {
  }
}
