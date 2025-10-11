package app.oatgh.maximum_utilities.handlers;

import java.util.Optional;

import org.checkerframework.checker.units.qual.s;
import org.jetbrains.annotations.NotNull;

import app.oatgh.maximum_utilities.recipes.bowl.BowlRecipe;
import app.oatgh.maximum_utilities.registries.MUItems;
import app.oatgh.maximum_utilities.registries.MURecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.level.NoteBlockEvent.Play;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BowlItemCraftHandler extends ItemStackHandler {
  private Level pLevel;
  private Player pPlayer;
  private ItemStack items;
  private BowlRecipe recipe = null;
  private boolean isProcessing = false;
  public BowlItemCraftHandler(Level pLevel, Player pPlayer, ItemStack items) {
    super(2);
    this.pLevel = pLevel;
    this.pPlayer = pPlayer;
    this.items = items;
  }

  public void SetLevel(Level pLevel) {
    this.pLevel = pLevel;
  }

  public void SetPlayer(Player player) {
    this.pPlayer = player;
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack stack) {
    switch (slot) {
      case 0:
        return stack.is(MUItems.FLOUR_POWDER.get());
      case 1: {
        return stack.is(MUItems.BREAD_DOUGH.get());
      }
      default:
        return false;
    }
  }

  @Override
  public int getSlotLimit(int slot) {
    if(slot == 1) return 1;
    return super.getSlotLimit(slot);
  }

  @Override
  protected void onContentsChanged(int slot) {
    if (getSafeLevel().isClientSide())
      return;

    if (slot == 0 && getStackInSlot(0).isEmpty()){
      if(recipe != null){
        ItemStack recipeResult = recipe.getResultItem(getSafeLevel().registryAccess());
        extractItem(1, recipeResult.getCount(), false);
      }
      recipe = null;
    }

    tryGenRecipe();
  }

  public Level getSafeLevel() {
    return this.pLevel != null ? pLevel : Minecraft.getInstance().level;
  }

  public void commitRecipe() {
    if (recipe == null) return;

    items.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(fluid -> {
        fluid.drain(recipe.getRequiredFluidStack().getAmount(), IFluidHandler.FluidAction.EXECUTE);
        extractItem(0, 1, false);
    });

    // limpa o resultado e reseta o recipe
    setStackInSlot(1, ItemStack.EMPTY);
    recipe = null;
  }

  public void tryGenRecipe() {
    if(isProcessing) return;
    isProcessing = true;
    InnerBowlStackCraftHandler recipeContainer = new InnerBowlStackCraftHandler(getSlots());

    for (int i = 0; i < getSlots(); i++) {
      recipeContainer.setItem(i, getStackInSlot(i));
    }

    items.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(fluid -> {
      Optional<BowlRecipe> match = getSafeLevel().getRecipeManager().getAllRecipesFor(MURecipes.BOWL_RECIPE_TYPE)
          .stream()
          .filter(r -> r.matches(recipeContainer, getSafeLevel()) && r.matchesFluid(fluid)).findFirst();

      if (match.isPresent()) {
        recipe = match.get();
        ItemStack itemResult = recipe.getResultItem(getSafeLevel().registryAccess()).copy();
        insertItem(1, itemResult, false);
      }else{
        recipe = null;
        setStackInSlot(1, ItemStack.EMPTY);
      }
    });
    isProcessing = false;
  }
}
