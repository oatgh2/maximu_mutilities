package app.oatgh.maximum_utilities.capabilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.handlers.BowlItemCraftHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;

@Mod.EventBusSubscriber(modid = MaximumUtilities.MODID)
public class AttachVanillaCapabilities {

  @SubscribeEvent
  public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
    ItemStack stack = event.getObject();

    if (stack.is(Items.BOWL)) {
      AddBowlStorageCapatbility(event, stack);
      AddBowlFluidCapability(event, stack);
    }
  }

  private static void AddBowlStorageCapatbility(AttachCapabilitiesEvent<ItemStack> event, ItemStack stack) {
    event.addCapability(new ResourceLocation(MaximumUtilities.MODID, "bowl_storage_handler"),
        new ICapabilitySerializable<CompoundTag>() {
          BowlItemCraftHandler itemHandler = new BowlItemCraftHandler(null, null, stack);
          LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);

          @Override
          public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return cap == ForgeCapabilities.ITEM_HANDLER ? lazyItemHandler.cast() : LazyOptional.empty();
          }

          @Override
          public CompoundTag serializeNBT() {
            return itemHandler.serializeNBT();
          }

          @Override
          public void deserializeNBT(CompoundTag nbt) {
            itemHandler.deserializeNBT(nbt);
          }
        });
  }

  private static void AddBowlFluidCapability(AttachCapabilitiesEvent<ItemStack> event, ItemStack stack) {
    event.addCapability(new ResourceLocation(MaximumUtilities.MODID, "bowl_fluid_handler"),
        new ICapabilitySerializable<CompoundTag>() {
          private final IFluidHandlerItem fluidHandler = new FluidHandlerItemStackSimple(stack, 250) {
            @Override
            public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
              boolean result = false;
              if (stack.getFluid().isSame(Fluids.WATER)) {
                result = true;
              }
              return result;
            }

            @Override
            public int fill(@NotNull FluidStack resource, FluidAction action) {
              int filled = super.fill(resource, action);
              stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
                if(itemHandler instanceof BowlItemCraftHandler craftHandler){
                  craftHandler.tryGenRecipe();
                }
              });
              return filled;
            }

          };
          private final LazyOptional<IFluidHandlerItem> fluidHandlerOptional = LazyOptional.of(() -> fluidHandler);

          @Override
          public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return cap == ForgeCapabilities.FLUID_HANDLER_ITEM ? fluidHandlerOptional.cast() : LazyOptional.empty();
          }

          @Override
          public CompoundTag serializeNBT() {
            return fluidHandler.getContainer().getOrCreateTag();
          }

          @Override
          public void deserializeNBT(CompoundTag nbt) {
            fluidHandler.getContainer().setTag(nbt);
          }
        });
  }
}
