package app.oatgh.maximum_utilities.recipes.bowl;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.recipes.MaximumUtilitiesRecipe;
import app.oatgh.maximum_utilities.registries.MURecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class BowlRecipe extends MaximumUtilitiesRecipe {

  private ResourceLocation pId;
  private CraftingBookCategory pCategory;
  private NonNullList<Ingredient> ingredients;
  private FluidStack reqFluidStack;
  private ItemStack resulItemStack;

  public BowlRecipe(ResourceLocation pId, CraftingBookCategory pCategory, NonNullList<Ingredient> ingredients,
      FluidStack reqFluidStack, ItemStack resulItemStack) {
    super(pId, pCategory);
    this.pId = pId;
    this.pCategory = pCategory;
    this.ingredients = ingredients;
    this.reqFluidStack = reqFluidStack;
    this.resulItemStack = resulItemStack;
  }

  @Override
  public ResourceLocation getId() {
    return new ResourceLocation(MaximumUtilities.MODID, "bowl_recipe");
  }

  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  public FluidStack getRequiredFluidStack() {
    return reqFluidStack;
  }

  public boolean matchesFluid(IFluidHandlerItem fluidHandlerItem) {
    if (reqFluidStack != null) {
      FluidStack containedFluid = fluidHandlerItem.getFluidInTank(0);
      return containedFluid.containsFluid(reqFluidStack);
    }
    return fluidHandlerItem.getFluidInTank(0).isEmpty();
  }

  @Override
  public boolean matches(CraftingContainer container, Level level) {
    for (Ingredient ing : ingredients) {
      boolean found = false;
      for (int i = 0; i < container.getContainerSize(); i++) {
        if (ing.test(container.getItem(i))) {
          found = true;
          break;
        }
      }
      if (!found)
        return false;
    }
    return true;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
    return resulItemStack;
  }

  @Override
  public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
    return resulItemStack.copy();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return MURecipes.BOWL_RECIPE_SERIALIZER.get();
  }

  @Override
  public RecipeType<?> getType() {
    return MURecipes.BOWL_RECIPE_TYPE;
  }

}
