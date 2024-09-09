package app.oatgh.maximum_utilities.blocks.furnances;

import app.oatgh.maximum_utilities.blocks.entities.MaximumUtilitiesBlockEntity;
import app.oatgh.maximum_utilities.handlers.AdaptedItemHandler;
import app.oatgh.maximum_utilities.registries.MUEntities;
import app.oatgh.maximum_utilities.registries.MUItems;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class BackeryFurnanceEntity extends MaximumUtilitiesBlockEntity {

    private static final String ITEMS_TAG = "Bakcery_Inventory";
    private static final String PROGRESS_INDICATOR = "Backery_Progress";

    public static final Logger LOGGER = LogUtils.getLogger();

    private final ItemStackHandler inputItems = createItemHandler(2);
    private final ItemStackHandler outputItems = createItemHandler(1);

    private final LazyOptional<IItemHandler> itemsHandler = LazyOptional.of(() -> new CombinedInvWrapper(inputItems, outputItems));
    private final LazyOptional<IItemHandler> inputItemsHandler = LazyOptional.of(() -> new AdaptedItemHandler(inputItems){
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });

    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> new AdaptedItemHandler(outputItems){
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack itemStack, boolean simulate) {
            return itemStack;
        }
    });

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemsHandler.invalidate();
        inputItemsHandler.invalidate();
        outputHandler.invalidate();
    }

    public ItemStackHandler getInputs(){
        return inputItems;
    }

    public ItemStackHandler getOutput(){
        return outputItems;
    }

    private int progress = 0;

    public int getProgress() {
        return progress;
    }

    public int BurnItemTime = 0;

    public BackeryFurnanceEntity(BlockPos pPos, BlockState pBlockState) {
        super(MUEntities.BACKERY_FURNANCE_ENTITY.get(), pPos, pBlockState);
    }

    @NonNull
    private ItemStackHandler createItemHandler(int slots) {
        return new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemsHandler.cast();
        } else {
            return super.getCapability(cap);
        }
    }


    @Override
    protected void saveClientData(@NotNull CompoundTag tag) {
        tag.put(ITEMS_TAG, inputItems.serializeNBT());
    }

    @Override
    protected void loadClientData(@NotNull CompoundTag pTag) {
        if (pTag.contains(ITEMS_TAG)) {
            inputItems.deserializeNBT(pTag.getCompound(ITEMS_TAG));
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains(PROGRESS_INDICATOR)) {
            int progressIndicator = pTag.getInt(PROGRESS_INDICATOR);
            progress = progressIndicator;
        }
    }


    void tickTest() {
        int xPos = this.worldPosition.get(Direction.Axis.X);
        int yPos = this.worldPosition.get(Direction.Axis.Y);
        int zPos = this.worldPosition.get(Direction.Axis.Z);
        LOGGER.info("BackeryFurnance Ticking at x: {} y: {} z: {}", xPos, yPos, zPos);
    }


    public void SetStackInSlot(int slot, ItemStack stack) {
        inputItems.setStackInSlot(slot, stack);
    }

    public void AddStackInSlot(int slot, int amount) {
        ItemStack itemStack = inputItems.getStackInSlot(slot);
        if (itemStack != ItemStack.EMPTY) {
            itemStack.setCount(itemStack.getCount() + amount);
            inputItems.setStackInSlot(slot, itemStack);
        }
    }

    public void TakeStackFromPlayer(Player player, InteractionHand hand, int slot) {
        ItemStack playerItemStack = player.getItemInHand(hand);
        ItemStack stackInSlot = inputItems.getStackInSlot(slot);
        if (stackInSlot.getCount() < 64 && (stackInSlot.is(playerItemStack.getItem())
                || stackInSlot == ItemStack.EMPTY)){
            int maxItemSlotCountRemaining = 64 - stackInSlot.getCount();

            if(maxItemSlotCountRemaining >= playerItemStack.getCount()){
                if(stackInSlot.is(MUItems.BREAD_DOUGH.get())){
                    stackInSlot.setCount(stackInSlot.getCount() + playerItemStack.getCount());
                }else {
                    inputItems.setStackInSlot(0, new ItemStack(playerItemStack.getItem()
                            , stackInSlot.getCount() + playerItemStack.getCount()));
                }
                playerItemStack.setCount(0);
            }else{
                int countToPlayer = playerItemStack.getCount() - maxItemSlotCountRemaining;
                if(stackInSlot.is(MUItems.BREAD_DOUGH.get())){
                    playerItemStack.setCount(countToPlayer);
                    stackInSlot.setCount(stackInSlot.getCount() + maxItemSlotCountRemaining);
                }else{
                    inputItems.setStackInSlot(0, new ItemStack(playerItemStack.getItem(),
                            stackInSlot.getCount() + maxItemSlotCountRemaining));
                }
            }
        }
    }


    public void AddStackInSlot(int slot, ItemStack stack) {
        ItemStack itemStack = inputItems.getStackInSlot(slot);
        inputItems.setStackInSlot(slot, stack);
    }

    public ItemStack GetStackInSlot(int slot) {
        return inputItems.getStackInSlot(slot);
    }

    public ItemStack ExtractStackInSlot(int slot, int amount) {
        return inputItems.extractItem(slot, amount, false);
    }

    @Override
    public void tickServer() {
        tickTest();
        CompoundTag nbtData = saveWithoutMetadata();

        if(inputItems.getStackInSlot(0).getCount() > 0 && progress < BurnItemTime
                && inputItems.getStackInSlot(2).getCount() < 64)
            progress++;

        if(progress == BurnItemTime){
            progress = 0;
            inputItems.extractItem(0, 1, false);

            if(inputItems.getStackInSlot(2).getItem() == Items.BREAD){
                inputItems.getStackInSlot(2).setCount(inputItems.getStackInSlot(2).getCount() + 1);
            }else{
                inputItems.setStackInSlot(2, new ItemStack(Items.BREAD.asItem(), 1));
            }
        }
    }
}
