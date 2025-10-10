package app.oatgh.maximum_utilities.menu.containers;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import app.oatgh.maximum_utilities.handlers.BowlItemCraftHandler;
import app.oatgh.maximum_utilities.handlers.BowlResultSlotItemHandler;
import app.oatgh.maximum_utilities.registries.MUMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.SlotItemHandler;

public class BowlContainer extends MaximumUtilitiesContainerBase {
  private final ContainerData data;
  private int waterAmountMax = 0;
  private int waterAmount = 0;
  private final ItemStack bowlStack;

  public BowlContainer(int windowId, Inventory inventory, FriendlyByteBuf buf) {
    this(windowId, inventory, buf.readItem());
  }

  public BowlContainer(int windowId, Inventory inventory, ItemStack itemStack) {
    super(MUMenus.BOWL_CONTAINER.get(), windowId);
    bowlStack = itemStack;

    data = new ContainerData() {

      @Override
      public int get(int pIndex) {
        Optional<IFluidHandlerItem> fluidOptional = bowlStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
            .resolve();
        if (fluidOptional.isPresent()) {
          IFluidHandler handler = fluidOptional.get();
          switch (pIndex) {
            case 0:
              return handler.getFluidInTank(0).getAmount();
            case 1:
              return handler.getTankCapacity(0);
            default:
              return 0;
          }
        }
        return 0;
      }

      @Override
      public void set(int pIndex, int pValue) {
        switch (pIndex) {
          case 0:
            waterAmount = pValue;
            break;
          case 1:
            waterAmountMax = pValue;
          default:
            break;
        }

      }

      @Override
      public int getCount() {
        return 2;
      }

    };
    addDataSlots(data);
    bowlStack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
      addSlot(new SlotItemHandler(itemHandler, 0, 64, 24));
      addSlot(new BowlResultSlotItemHandler(inventory.player, (BowlItemCraftHandler) itemHandler, 1, 108, 24));
    });
    drawPlayerInventory(inventory, 10, 70);
  }

  public int getWaterAmountMax() {
    return waterAmountMax;
  }

  public int getWaterAmount() {
    return waterAmount;
  }
}
