package app.oatgh.maximum_utilities.blocks.menu.containers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MaximumUtilitiesContainerBase extends AbstractContainerMenu {

    protected MaximumUtilitiesContainerBase(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    protected int addHorizontalPlayerInventory(Container inventory, int x, int y, int dx, int startIndex, int range)
    {
        int index = startIndex;
        for (int i = 0; i < range; i++)
        {
            addSlot(new Slot(inventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    protected void drawPlayerInventory(Container inventory, int left, int top)
    {
        int index = 9;
        for(int row = 0; row < 3; row++)
        {
           index = addHorizontalPlayerInventory(inventory, left, top, 18, index, 9);
           top += 18;
        }

        drawPlayerHotbar(inventory, left, top + 4);
    }

    private void drawPlayerHotbar(Container inventory, int left, int top)
    {
        addHorizontalPlayerInventory(inventory, left, top, 18, 0, 9);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
