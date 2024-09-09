package app.oatgh.maximum_utilities.blocks.menu.containers;

import app.oatgh.maximum_utilities.blocks.furnances.BackeryFurnanceEntity;
import app.oatgh.maximum_utilities.registries.MUBlocks;
import app.oatgh.maximum_utilities.registries.MUMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class BackeryFurnanceContainer extends AbstractContainerMenu {

    BlockPos blockPos;

    Player player;

    private BlockEntity getBlockEntity(){
        return player.level().getBlockEntity(blockPos);
    }

    public BackeryFurnanceContainer(int windowId, Inventory inventory, BlockPos blockPos) {
        super(MUMenus.BACKERY_CONTAINER.get(), windowId);
        this.blockPos = blockPos;
        this.player = inventory.player;

       if( getBlockEntity() instanceof BackeryFurnanceEntity backeryFurnance){
            addSlot(new SlotItemHandler(backeryFurnance.getInputs(), 0, 64, 24));
            addSlot(new SlotItemHandler(backeryFurnance.getInputs(), 1, 64, 72));
            addSlot(new SlotItemHandler(backeryFurnance.getOutput(), 0, 108, 24));
       }
    }

    private int addSlotHorizontal(Container playerInventory, int index, int x, int y, int amount, int dx){
        for (int i = 0; i < amount; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Container playerInventoryContainer, int startInventoryIndex, int startX, int startY,
                           int horizontalAmount,
                           int dx,
                           int verticalAmount,
                           int dy){
        for (int i = 0; i < verticalAmount; i++) {
            startInventoryIndex = addSlotHorizontal(playerInventoryContainer, startInventoryIndex,
                    startX, startY, horizontalAmount, dx);
            startY += dy;
        }
        return startInventoryIndex;
    }

    private void drawPlayerInventorySlots(Container playerInventory, int left, int top){
        addSlotBox(playerInventory, 9, left, top, 9, 18, 3, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), blockPos), player,
                MUBlocks.BACKERY_FURNANCE.get());
    }
}
