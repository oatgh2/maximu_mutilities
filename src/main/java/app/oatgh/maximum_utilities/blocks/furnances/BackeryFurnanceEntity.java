package app.oatgh.maximum_utilities.blocks.furnances;

import app.oatgh.maximum_utilities.blocks.entities.MaximumUtilitiesBlockEntity;
import app.oatgh.maximum_utilities.registries.MUEntities;
import app.oatgh.maximum_utilities.registries.MUItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackeryFurnanceEntity extends MaximumUtilitiesBlockEntity {
    private final String COMPOUND_INVENTORY_TAG = "backery_furnance_inventory_data";
    private final String COMPOUND_ENERGY_TAG = "backery_furnance_energy_data";
    private final int ENERGY_GEN_COUNT = 3;
    private final int ENERGY_CONSUME_COUNT = 3;

    int fuelTotalBurnTime = 0;
    int fuelBurnTime = 0;
    int progress = 0;
    int maxProgress = 0;

    public int getEnergyGenCount() { return ENERGY_GEN_COUNT; }

    public int getEnergyDrainCount() { return ENERGY_CONSUME_COUNT; }

    public int getEnergyProgress() {
        return getEnergy().getEnergyStored();
    }

    public int getEnergyProgressMax() {
        return getEnergy().getMaxEnergyStored();
    }

    public int getFuelTotalBurnTime() {
        return fuelTotalBurnTime;
    }

    public void setFuelTotalBurnTime(int fuelTotalBurnTime) {
        this.fuelTotalBurnTime = fuelTotalBurnTime;
    }

    public int getFuelBurnTime() {
        return fuelBurnTime;
    }

    public void setFuelBurnTime(int fuelBurnTime) {
        this.fuelBurnTime = fuelBurnTime;
    }

    public int getProgressMax() {
        return maxProgress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public BackeryFurnanceEntity(BlockPos blockPos, BlockState blockState) {
        super(MUEntities.BACKERY_FURNANCE_ENTITY.get(), blockPos, blockState);
        // level = getLevel();
    }

    EnergyStorage energyStorage = new EnergyStorage(1000, 300, 30);
    LazyOptional<EnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    ItemStackHandler inventoryHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            boolean result = false;

            switch (slot) {
                case 0: {
                    if (stack.getItem() == MUItems.BREAD_DOUGH.get())
                        result = true;
                    break;
                }
                case 1: {
                    if (ForgeHooks.getBurnTime(stack, null) > 0) {
                        result = true;
                    }
                    break;
                }
                case 2: {
                    result = false;
                    break;
                }
                default:
                    break;
            }

            return result;
        }
    };
    public LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> inventoryHandler);

    public ItemStackHandler getInventory() {
        return inventory.orElseThrow(NotImplementedException::new);
    }

    public EnergyStorage getEnergy() {
        return energy.orElseThrow(NotImplementedException::new);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventory.cast();
        } else if (cap == ForgeCapabilities.ENERGY) {
            return energy.cast();
        } else
            return super.getCapability(cap);
    }

    @Override
    protected void loadClientData(@NotNull CompoundTag pTag) {
        CompoundTag invenTag = pTag.getCompound(COMPOUND_INVENTORY_TAG);
        int energyCompoundTag = pTag.getInt(COMPOUND_ENERGY_TAG);
        inventoryHandler.deserializeNBT(invenTag);
        energyStorage.deserializeNBT(IntTag.valueOf(energyCompoundTag));
    }

    @Override
    protected void saveClientData(@NotNull CompoundTag pTag) {
        pTag.put(COMPOUND_INVENTORY_TAG, inventoryHandler.serializeNBT());
        pTag.put(COMPOUND_ENERGY_TAG, energyStorage.serializeNBT());
    }

    private void genEnergy(EnergyStorage energyStorage) {
        ItemStack fuelStack = getInventory().getStackInSlot(1);
        if (fuelTotalBurnTime == 0 && !fuelStack.isEmpty()
                && energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
            // Fill fuel and consume from fuel stack
            fuelTotalBurnTime = ForgeHooks.getBurnTime(fuelStack, RecipeType.SMELTING);
            fuelStack.shrink(1);
        }

        if (fuelTotalBurnTime > fuelBurnTime) {
            // Consumes burntime and gen energy
            if (energyStorage.receiveEnergy(ENERGY_GEN_COUNT, false) != 0) {
                fuelBurnTime++;
                setChanged();
            }
        } else {
            fuelTotalBurnTime = 0;
            fuelBurnTime = 0;
        }
    }

    private Optional<SmeltingRecipe> getRecipe(Level pLevel, ItemStack stackInv) {
        if (pLevel != null) {
            SimpleContainer simpleContainer = new SimpleContainer(stackInv);
            return pLevel.getRecipeManager().getRecipeFor(RecipeType.SMELTING, simpleContainer, level);
        }
        return Optional.empty();
    }

    private void toggleSetRedstone(Level level, boolean on) {
        level.setBlock(worldPosition, blockState.setValue(BlockStateProperties.LIT, on), 3);
    }

    private void setLightLevel(Level level, int value) {
        level.setBlock(worldPosition, blockState.setValue(BackeryFurnance.LIGHT_LEVEL, value), 3);
    }

    private void processSmelting(Level pLevel) {
        ItemStack stackInInv = inventoryHandler.getStackInSlot(0);
        ItemStack stackInOut = inventoryHandler.getStackInSlot(2);
        if (stackInOut.getCount() < 64) {
            Optional<SmeltingRecipe> recipe = getRecipe(pLevel, stackInInv);
            if (recipe.isPresent() && maxProgress == 0) {
                maxProgress = (int) (recipe.get().getCookingTime() * 0.60);
            }

            if (recipe.isPresent() && maxProgress > progress) {
                if (energyStorage.getEnergyStored() >= ENERGY_CONSUME_COUNT
                        && energyStorage.extractEnergy(ENERGY_CONSUME_COUNT, false) == 3) {
                    toggleSetRedstone(pLevel, true);
                    progress++;
                }

            } else {
                if (maxProgress > 0 && maxProgress == progress) {
                    craftItem(pLevel, recipe.get());
                    maxProgress = 0;
                    progress = 0;
                    toggleSetRedstone(pLevel, false);
                }
            }
        }
    }

    private void craftItem(Level pLevel, Recipe<?> recipe) {
        ItemStack result = recipe.getResultItem(pLevel.registryAccess()).copy();

        inventoryHandler.extractItem(0, 1, false);

        ItemStack currentOutput = inventoryHandler.getStackInSlot(2);

        if (currentOutput.isEmpty())
            inventoryHandler.setStackInSlot(2, result);
        else
            currentOutput.grow(result.getCount());
    }

    @Override
    public void tickServer(Level pLevel, BlockState pState) {
        setLightLevel(level, getLightLevel());
        genEnergy(energyStorage);
        processSmelting(pLevel);
    }

    private int getLightLevel() {
        float percent = fuelBurnTime > 0 && fuelTotalBurnTime > 0 ? (float) fuelBurnTime / (float) fuelTotalBurnTime
                : 1;
        percent = Mth.clamp(percent, 0f, 1f);
        int maxLight = 14;
        int lightLevel = (int) (maxLight - (percent * maxLight));
        return lightLevel;
    }
}
