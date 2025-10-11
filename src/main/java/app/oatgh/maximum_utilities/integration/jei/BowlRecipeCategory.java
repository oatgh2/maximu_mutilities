package app.oatgh.maximum_utilities.integration.jei;

import org.jetbrains.annotations.Nullable;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.recipes.bowl.BowlRecipe;
import app.oatgh.maximum_utilities.registries.MURecipes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BowlRecipeCategory implements IRecipeCategory<BowlRecipe> {

  private final static ResourceLocation TEX = new ResourceLocation(MaximumUtilities.MODID,
      "textures/gui/bowl_crafting.png");

  private final IDrawable background;
  private final IDrawable icon;

  public BowlRecipeCategory(IGuiHelper guiHelper) {
    this.background = guiHelper.createDrawable(TEX, 0, 0, 180, 152);
    this.icon = guiHelper.createDrawable(TEX, 0, 152, 15, 15);
  }

  @Override
  public RecipeType<BowlRecipe> getRecipeType() {
    return new RecipeType<>(new ResourceLocation(MaximumUtilities.MODID, "bowl_recipe"), BowlRecipe.class);
  }

  @Override
  public Component getTitle() {
    return Component.translatable("maximumutilities.screen.bowl.menu");
  }

  @Override
  public IDrawable getBackground() {
    return this.background;
  }

  @Override
  public @Nullable IDrawable getIcon() {
    return this.icon;
  }

  @Override
  public void draw(BowlRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
      double mouseY) {
    IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, BowlRecipe recipe, IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.INPUT, 64, 24).addIngredients(recipe.getIngredients().get(0));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 24).addItemStack(recipe.getResultItem(null));
  }

}
