package app.oatgh.maximum_utilities.integration.jei;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

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
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JeiIntegrationPlugin implements IModPlugin {

  @Override
  public @NotNull ResourceLocation getPluginUid() {
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
    Minecraft mc = Minecraft.getInstance();
    if(mc.level == null) return;

    net.minecraft.world.item.crafting.RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
    List<BowlRecipe> recipes = rm.getAllRecipesFor(MURecipes.BOWL_RECIPE_TYPE);
    recipeRegistration.addRecipes(MUJeiRecipeTypes.BOWL, recipes);
    System.out.println("Bowl recipes loaded: " + recipes.size());
  }
}
