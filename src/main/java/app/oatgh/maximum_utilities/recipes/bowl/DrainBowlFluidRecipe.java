package app.oatgh.maximum_utilities.recipes.bowl;

import app.oatgh.maximum_utilities.recipes.MaximumUtilitiesRecipe;
import app.oatgh.maximum_utilities.registries.MURecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class DrainBowlFluidRecipe extends MaximumUtilitiesRecipe {
  public DrainBowlFluidRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
    super(pId, pCategory);
  }

  @Override
  public boolean canCraftInDimensions(int pWidth, int pHeight) {
    return pWidth * pHeight >= 1;
  }

  @Override
  public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
    return new ItemStack(Items.BOWL);
  }

  @Override
  public boolean matches(CraftingContainer container, Level level) {
    boolean foundFilledBowl = false;

    for (int i = 0; i < container.getContainerSize(); i++) {
      ItemStack stack = container.getItem(i);

      if (!stack.isEmpty() && stack.is(Items.BOWL)) {
        // se o bowl tiver NBT e contiver fluido
        if (stack.hasTag() && stack.getTag().contains("Fluid")) {
          foundFilledBowl = true;
        }
      }
    }

    // só retorna true se tiver bowl com fluido
    return foundFilledBowl;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return MURecipes.DRAIN_BOWL_FLUID_SERIALIZER.get();
  }

  @Override
  public RecipeType<?> getType() {
    return RecipeType.CRAFTING;
  }
}
