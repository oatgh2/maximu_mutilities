package app.oatgh.maximum_utilities.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

@Mixin(value = ItemStack.class, remap = false)
public class ItemStackMixin {

  @Inject(method = "isStackable", at = @At("HEAD"), cancellable = true)
  private void preventStackInBowlFilled(CallbackInfoReturnable<Boolean> cir) {
    ItemStack self = (ItemStack) (Object) this;

    if (self.is(Items.BOWL)) {
      self.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(fluidHandler -> {
        IFluidHandlerItem fluidHandlerItem = fluidHandler;
        if(!fluidHandler.getFluidInTank(0).isEmpty()){
          cir.setReturnValue(false);
        }
      });
    }
  }
}
