package app.oatgh.maximum_utilities.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

public abstract class MaximumUtilitiesRecipe extends CustomRecipe {

  public MaximumUtilitiesRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
    super(pId, pCategory);
  }

  @Override

  public abstract boolean matches(CraftingContainer pContainer, Level pLevel);

  @Override
  public abstract ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess);

  @Override
  public boolean canCraftInDimensions(int pWidth, int pHeight) {
    return pWidth >= 1;
  };

  @Override
  public abstract RecipeSerializer<?> getSerializer();
}
