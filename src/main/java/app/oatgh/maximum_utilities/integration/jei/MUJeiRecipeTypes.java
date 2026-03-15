package app.oatgh.maximum_utilities.integration.jei;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.recipes.bowl.BowlRecipe;
import mezz.jei.api.recipe.RecipeType;

public class MUJeiRecipeTypes {
    public static final RecipeType<BowlRecipe> BOWL =
            RecipeType.create(MaximumUtilities.MODID, "bowl_recipe", BowlRecipe.class);
}
