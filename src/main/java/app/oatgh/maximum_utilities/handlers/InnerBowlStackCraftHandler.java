package app.oatgh.maximum_utilities.handlers;

import java.util.List;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

public class InnerBowlStackCraftHandler extends SimpleContainer implements CraftingContainer {

  public InnerBowlStackCraftHandler(int size){
    super(size);
  }

  @Override
  public int getWidth() {
    return 2;
  }

  @Override
  public int getHeight() {
    return 1;
  }

  @Override
  public List<ItemStack> getItems() {
    List<ItemStack> result = List.of();
    for(int i = 0; i < getContainerSize(); i++){
      result.add(getItem(i));
    }
    return result;
  }
}