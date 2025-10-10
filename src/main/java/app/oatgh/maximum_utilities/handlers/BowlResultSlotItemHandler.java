package app.oatgh.maximum_utilities.handlers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BowlResultSlotItemHandler extends SlotItemHandler {

  BowlItemCraftHandler handler;
  Level level;
  Player player;

  public BowlResultSlotItemHandler(Player player, BowlItemCraftHandler bowlHandler, int index, int xPosition, int yPosition) {
    super(bowlHandler, index, xPosition, yPosition);
    handler = bowlHandler;
    this.player = player;
    this.level = bowlHandler.getSafeLevel();
  }
  

  @Override
  public void onTake(Player pPlayer, ItemStack pStack) {
    super.onTake(pPlayer, pStack);
    handler.commitRecipe();
  }
}
