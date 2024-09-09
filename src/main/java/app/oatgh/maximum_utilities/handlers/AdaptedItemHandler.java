package app.oatgh.maximum_utilities.handlers;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class AdaptedItemHandler implements IItemHandlerModifiable {

    private final IItemHandlerModifiable handler;

    public AdaptedItemHandler(IItemHandlerModifiable handler){
        this.handler = handler;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack itemStack) {
        handler.setStackInSlot(slot, itemStack);
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack itemStack, boolean simulate) {
        return handler.insertItem(slot, itemStack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return handler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return handler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack itemStack) {
        return handler.isItemValid(slot, itemStack);
    }
}
