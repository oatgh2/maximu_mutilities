package app.oatgh.maximum_utilities.blocks.furnances;

import app.oatgh.maximum_utilities.blocks.MaximumUtilitiesBlockBase;
import app.oatgh.maximum_utilities.menu.containers.BackeryFurnanceContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BakceryFurnance extends MaximumUtilitiesBlockBase {
    public BakceryFurnance() {
        super(BlockBehaviour.Properties.of());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BackeryFurnanceEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide()){
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof BackeryFurnanceEntity bfe){
                MenuProvider menuProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("maximumutilities.screen.backery.furnance");
                    }

                    @Override
                    public @Nullable AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
                        return new BackeryFurnanceContainer(windowId, inventory, pPos);
                    }
                };
                NetworkHooks.openScreen((ServerPlayer)pPlayer, menuProvider, bfe.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    public ItemStack extracItemFromHandler(IItemHandler itemHandler) {
        int slotsInHandler = itemHandler.getSlots();
        ItemStack result = null;
        for (int i = 0; i < slotsInHandler; i++) {
            ItemStack actualStack = itemHandler.getStackInSlot(i);
            if (!actualStack.isEmpty()) {
                ItemStack resultItemStack = itemHandler.extractItem(i, actualStack.getCount(), false);
                if (resultItemStack.getCount() > 0) {
                    result = resultItemStack;
                    break;
                }
            }
        }
        return result;
    }

    public ItemStack insertItemInHandler(IItemHandler itemHandler, ItemStack playerItemStack) {
        int slots = itemHandler.getSlots();
        for (int i = 0; i < slots; i++) {
            ItemStack itemStack = itemHandler.getStackInSlot(i);

            if (itemStack.isEmpty()) {
                itemHandler.insertItem(i, playerItemStack, false);
                break;
            }

            if (itemStack.getItem().equals(playerItemStack.getItem())) {
                int sizeAfterInsert = itemStack.getCount() + playerItemStack.getCount();
                if (sizeAfterInsert <= itemStack.getMaxStackSize()) {
                    itemStack.setCount(sizeAfterInsert);
                    break;
                }
            }
        }
        return playerItemStack;
    }
}
