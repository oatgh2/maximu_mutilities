package app.oatgh.maximum_utilities.mixins;

import app.oatgh.maximum_utilities.handlers.BowlItemCraftHandler;
import app.oatgh.maximum_utilities.menu.containers.BackeryFurnanceContainer;
import app.oatgh.maximum_utilities.menu.containers.BowlContainer;
import app.oatgh.maximum_utilities.registries.MUItems;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.network.NetworkHooks;

import java.io.Console;
import java.util.logging.LogRecord;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Item.class, remap = false)
public abstract class UseMixin {

    @Shadow
    protected static BlockHitResult getPlayerPOVHitResult(Level pLevel, Player pPlayer, ClipContext.Fluid pFluidMode) {
        throw new AssertionError();
    }

    @Shadow
    public abstract InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand);

    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(at = @At(value = "HEAD"), method = "use", cancellable = true)
    public void useBowlInWaterCap(Level pLevel, @NotNull Player pPlayer, InteractionHand pUsedHand,
            CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemUsed = pPlayer.getItemInHand(pUsedHand);

        if (itemUsed.is(Items.BOWL)) {

            if (pPlayer.isShiftKeyDown()) {
                if (!pLevel.isClientSide()) {
                    itemUsed.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                        if (handler instanceof BowlItemCraftHandler bowlItemHandler) {
                            bowlItemHandler.SetLevel(pLevel);
                            bowlItemHandler.SetPlayer(pPlayer);
                        }
                    });

                    MenuProvider menuProvider = new MenuProvider() {
                        @Override
                        public Component getDisplayName() {
                            return Component.translatable("maximumutilities.screen.bowl.menu");
                        }

                        @Override
                        public @Nullable AbstractContainerMenu createMenu(int windowId, Inventory inventory,
                                Player player) {
                            return new BowlContainer(windowId, inventory, itemUsed);
                        }
                    };
                    NetworkHooks.openScreen((ServerPlayer) pPlayer, menuProvider, buf -> buf.writeItem(itemUsed));
                }
                cir.setReturnValue(InteractionResultHolder.success(itemUsed));
            } else {
                try {
                    BlockHitResult blockHitResult = getPlayerPOVHitResult(pLevel, pPlayer,
                            ClipContext.Fluid.SOURCE_ONLY);
                    Direction hitedDirection = blockHitResult.getDirection();
                    BlockPos realAimedPos = blockHitResult.withDirection(hitedDirection).getBlockPos();
                    BlockState hitedBlockState = pLevel.getBlockState(realAimedPos);

                    ItemStack handItemStack = pPlayer.getItemInHand(pUsedHand);
                    handItemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                            .ifPresent(fluidHandler -> {
                                FluidHandlerItemStackSimple fluidStack = (FluidHandlerItemStackSimple) fluidHandler;
                                if (hitedBlockState.getBlock() instanceof BucketPickup bucketPickup
                                        && hitedBlockState.getFluidState().is(Fluids.WATER)) {
                                    if (fluidStack.getFluid().isEmpty()) {
                                        fluidStack.fill(new FluidStack(Fluids.WATER, 250),
                                                IFluidHandler.FluidAction.EXECUTE);

                                        ItemStack pickedUpItem = bucketPickup.pickupBlock(pLevel, realAimedPos,
                                                hitedBlockState);
                                        bucketPickup.getPickupSound(hitedBlockState).ifPresent((soundEvent) -> {
                                            pPlayer.playSound(soundEvent, 1F, 1F);
                                        });

                                        pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, realAimedPos);
                                        if (!pLevel.isClientSide()) {
                                            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) pPlayer,
                                                    handItemStack);
                                        }
                                    }
                                } else {
                                    BlockEntity blockEntity = pLevel.getBlockEntity(realAimedPos);
                                    if (!fluidStack.getFluid().isEmpty()) {
                                        blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER)
                                                .ifPresent(fluidBlockHandler -> {
                                                    FluidStack fluidDrained = fluidStack.drain(250,
                                                            IFluidHandler.FluidAction.EXECUTE);
                                                    fluidBlockHandler.fill(fluidDrained,
                                                            IFluidHandler.FluidAction.EXECUTE);
                                                });
                                    }

                                }
                            });

                } catch (Exception ex) {
                    LOGGER.error(ex.toString());
                }
            }
        }
    }

    // deprecated @Inject(at = @At(value = "HEAD"), method = "use", cancellable =
    // true)
    @Deprecated
    public void useBowlInWater(Level pLevel, @NotNull Player pPlayer, InteractionHand pUsedHand,
            CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemUsed = pPlayer.getItemInHand(pUsedHand);
        if (itemUsed.getItem().equals(Items.BOWL)) {
            try {
                BlockHitResult blockHitResult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
                Direction hitedDirection = blockHitResult.getDirection();
                BlockPos realAimedPos = blockHitResult.withDirection(hitedDirection).getBlockPos();
                BlockState hitedBlockState = pLevel.getBlockState(realAimedPos);
                if (hitedBlockState.getBlock() instanceof BucketPickup bucketPickup
                        && (hitedBlockState.getBlock() == Blocks.WATER
                                || hitedBlockState.getFluidState().is(Fluids.WATER))) {
                    ItemStack resultedItemStack = new ItemStack(MUItems.WATER_BOWL.get());
                    ItemStack pickedUpItem = bucketPickup.pickupBlock(pLevel, realAimedPos, hitedBlockState);
                    bucketPickup.getPickupSound(hitedBlockState).ifPresent((soundEvent) -> {
                        pPlayer.playSound(soundEvent, 1F, 1F);
                    });
                    pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, realAimedPos);
                    if (!pLevel.isClientSide()) {
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) pPlayer, resultedItemStack);
                    }
                    ItemStack itemStackInHand = pPlayer.getItemInHand(pUsedHand);
                    if (itemStackInHand.getCount() > 1) {
                        pPlayer.setItemInHand(pUsedHand, new ItemStack(itemStackInHand.getItem(),
                                itemStackInHand.getCount() - 1));
                        pPlayer.addItem(resultedItemStack);
                    } else {
                        pPlayer.setItemInHand(pUsedHand, resultedItemStack);
                    }
                }
            } catch (Exception ex) {
                LOGGER.error(ex.toString());
            }
        }
    }
}
