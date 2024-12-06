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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BackeryFurnanceContainer extends MaximumUtilitiesContainerBase {
    BlockPos blockPos;
    Player player;

    private BlockEntity getBlockEntity(){
        return player.level().getBlockEntity(blockPos);
    }

    public BackeryFurnanceContainer(int windowId, Inventory inventory, BlockPos blockPos) {
        super(MUMenus.BACKERY_CONTAINER.get(), windowId);
        this.blockPos = blockPos;
        this.player = inventory.player;

        if(getBlockEntity() instanceof BackeryFurnanceEntity bfe){
            addSlot(new SlotItemHandler(bfe.getInventory(), 0, 56, 35));
            addSlot(new SlotItemHandler(bfe.getInventory(), 1, 56, 35));
            addSlot(new SlotItemHandler(bfe.getInventory(), 2, 56, 35));
        }

        drawPlayerInventory(inventory, 10, 70);
    }



    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.level(), blockPos), pPlayer,
                MUBlocks.BACKERY_FURNANCE.get());
    }
}
