package app.oatgh.maximum_utilities.blocks.furnances;

import app.oatgh.maximum_utilities.blocks.entities.MaximumUtilitiesBlockEntity;
import app.oatgh.maximum_utilities.items.BreadDough;
import app.oatgh.maximum_utilities.registries.MUEntities;
import app.oatgh.maximum_utilities.registries.MUItems;
import app.oatgh.maximum_utilities.registries.MURecipes;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class BackeryFurnanceEntity extends MaximumUtilitiesBlockEntity {

    private static final String ITEMS_TAG = "Bakcery_Inventory";
    private static final String PROGRESS_INDICATOR = "Backery_Progress";

    public static final Logger LOGGER = LogUtils.getLogger();

    private final ItemStackHandler items = createItemHandler();

    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> items);

    private int progress = 0;

    public int getProgress() {
        return progress;
    }

    public int BurnTime = 0;

    public BackeryFurnanceEntity(BlockPos pPos, BlockState pBlockState) {
        super(MUEntities.BACKERY_FURNANCE_ENTITY.get(), pPos, pBlockState);
    }

    @NonNull
    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
                ItemStack itemStack = items.getStackInSlot(0);
                if (items.getStackInSlot(0).getItem() instanceof BreadDough breadDough) {
                    int burnItemTime = breadDough.getBurnTime(items.getStackInSlot(0),
                            RecipeType.simple(MURecipes.BREAD_DOUGH_RECIPE));
                    int backeryBurnRealTime = burnItemTime / 10;
                    BurnTime = backeryBurnRealTime;
                }
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        } else {
            return super.getCapability(cap);
        }
    }


    @Override
    protected void saveClientData(@NotNull CompoundTag tag) {
        tag.put(ITEMS_TAG, items.serializeNBT());
    }

    @Override
    protected void loadClientData(@NotNull CompoundTag pTag) {
        if (pTag.contains(ITEMS_TAG)) {
            items.deserializeNBT(pTag.getCompound(ITEMS_TAG));
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
        items.setStackInSlot(slot, stack);
    }

    public void AddStackInSlot(int slot, int amount) {
        ItemStack itemStack = items.getStackInSlot(slot);
        if (itemStack != ItemStack.EMPTY) {
            itemStack.setCount(itemStack.getCount() + amount);
            items.setStackInSlot(slot, itemStack);
        }
    }

    public void TakeStackFromPlayer(Player player, InteractionHand hand, int slot) {
        ItemStack playerItemStack = player.getItemInHand(hand);
        ItemStack stackInSlot = items.getStackInSlot(slot);
        if (stackInSlot.getCount() < 64 && (stackInSlot.is(playerItemStack.getItem())
                || stackInSlot == ItemStack.EMPTY)){
            int maxItemSlotCountRemaining = 64 - stackInSlot.getCount();

            if(maxItemSlotCountRemaining >= playerItemStack.getCount()){
                if(stackInSlot.is(MUItems.BREAD_DOUGH.get())){
                    stackInSlot.setCount(stackInSlot.getCount() + playerItemStack.getCount());
                }else {
                    items.setStackInSlot(0, new ItemStack(playerItemStack.getItem()
                            , stackInSlot.getCount() + playerItemStack.getCount()));
                }
                playerItemStack.setCount(0);
            }else{
                int countToPlayer = playerItemStack.getCount() - maxItemSlotCountRemaining;
                if(stackInSlot.is(MUItems.BREAD_DOUGH.get())){
                    playerItemStack.setCount(countToPlayer);
                    stackInSlot.setCount(stackInSlot.getCount() + maxItemSlotCountRemaining);
                }else{
                    items.setStackInSlot(0, new ItemStack(playerItemStack.getItem(),
                            stackInSlot.getCount() + maxItemSlotCountRemaining));
                }
            }
        }
    }


    public void AddStackInSlot(int slot, ItemStack stack) {
        ItemStack itemStack = items.getStackInSlot(slot);
        items.setStackInSlot(slot, stack);
    }

    public ItemStack GetStackInSlot(int slot) {
        return items.getStackInSlot(slot);
    }

    public ItemStack ExtractStackInSlot(int slot, int amount) {
        return items.extractItem(slot, amount, false);
    }

    @Override
    public void tickServer() {
        tickTest();
        CompoundTag nbtData = saveWithoutMetadata();

        progress++;
    }
}
