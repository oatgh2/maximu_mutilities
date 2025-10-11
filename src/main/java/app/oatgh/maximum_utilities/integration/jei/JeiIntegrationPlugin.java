package app.oatgh.maximum_utilities.integration.jei;

import java.util.List;
import java.util.Objects;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.recipes.bowl.BowlRecipe;
import app.oatgh.maximum_utilities.registries.MURecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
@JeiPlugin
public class JeiIntegrationPlugin implements IModPlugin {

  @Override
  public ResourceLocation getPluginUid() {
    return new ResourceLocation(MaximumUtilities.MODID, "jei_integration");
  }
  
  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    registration.addRecipeCategories(new BowlRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
     registerBowlRecipes(registration);
  }

  void registerBowlRecipes(IRecipeRegistration recipeRegistration){
    net.minecraft.world.item.crafting.RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
    List<BowlRecipe> recipes = rm.getAllRecipesFor(MURecipes.BOWL_RECIPE_TYPE);
    recipeRegistration.addRecipes(RecipeType.create(MaximumUtilities.MODID, "bowl_recipe", BowlRecipe.class), recipes);
  }
}
