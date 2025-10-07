package app.oatgh.maximum_utilities.blocks.menu.containers;

import java.util.List;
import app.oatgh.maximum_utilities.blocks.furnances.BackeryFurnanceEntity;
import app.oatgh.maximum_utilities.registries.MUBlocks;
import app.oatgh.maximum_utilities.registries.MUMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraft.network.FriendlyByteBuf;

public class BackeryFurnanceContainer extends MaximumUtilitiesContainerBase {
    BlockPos blockPos;
    Player player;
    private final ContainerData data;
    private BackeryFurnanceEntity bfe;
    private int energyStorageProgress = 0;
    private int energyStorageProgressMax = 0;
    private int fuelBurnTime = 0;
    private int fuelTotalBurnTime = 0;

    public BackeryFurnanceContainer(int windowId, Inventory inv, FriendlyByteBuf buf) {
        this(windowId, inv, buf.readBlockPos());
    }

    public BackeryFurnanceContainer(int windowId, Inventory inventory, BlockPos blockPos) {
        super(MUMenus.BACKERY_CONTAINER.get(), windowId);
        this.blockPos = blockPos;
        this.player = inventory.player;
        bfe = (BackeryFurnanceEntity) getBlockEntity();

        this.data = new ContainerData() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public int get(int pIndex) {
                int result = 0;
                switch (pIndex) {
                    case 0:
                        result = bfe.getEnergyProgress();
                        break;
                    case 1:
                        result = bfe.getEnergyProgressMax();
                        break;
                    case 2:
                        result = bfe.getFuelBurnTime();
                        break;
                    case 3:
                        result = bfe.getFuelTotalBurnTime();
                        break;
                    default:
                        break;
                }
                return result;
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0:
                        energyStorageProgress = pValue;
                        break;
                    case 1:
                        energyStorageProgressMax = pValue;
                        break;
                    case 2:
                        fuelBurnTime = pValue; 
                        break;
                    case 3:
                        fuelTotalBurnTime = pValue;
                        break;
                    default:
                        break;
                }
            }
        };
        addDataSlots(data);
        IItemHandler handler = (bfe != null) ? bfe.getInventory() : new ItemStackHandler(3);
        addSlot(new SlotItemHandler(handler, 0, 64, 24));
        addSlot(new SlotItemHandler(handler, 1, 10, 47));
        addSlot(new SlotItemHandler(handler, 2, 108, 24));

        drawPlayerInventory(inventory, 10, 70);
    }

    public int getEnergyProgress() {
        return energyStorageProgress;
    }

    public int getEnergyProgressMax() {
        return energyStorageProgressMax;
    }

    public int getTotalFuelBurnTime() {
        return fuelTotalBurnTime;
    }

    public int getFuelBurnTime() {
        return fuelBurnTime;
    }

    private BlockEntity getBlockEntity() {
        return player.level().getBlockEntity(blockPos);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.level(), blockPos), pPlayer,
                MUBlocks.BACKERY_FURNANCE.get());
    }
}
