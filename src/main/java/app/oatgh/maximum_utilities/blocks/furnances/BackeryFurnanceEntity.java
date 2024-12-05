package app.oatgh.maximum_utilities.blocks.furnances;

import app.oatgh.maximum_utilities.blocks.entities.MaximumUtilitiesBlockEntity;
import app.oatgh.maximum_utilities.registries.MUEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackeryFurnanceEntity extends MaximumUtilitiesBlockEntity {
    Level level;
    public BackeryFurnanceEntity(BlockPos blockPos, BlockState blockState) {
        super(MUEntities.BACKERY_FURNANCE_ENTITY.get(), blockPos, blockState);
        level = getLevel();
    }
    public LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() ->  new ItemStackHandler(3){
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    });



    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return inventory.cast();
        }else
            return super.getCapability(cap);
    }

    @Override
    protected void loadClientData(@NotNull CompoundTag pTag) {
    }


    @Override
    protected void saveClientData(@NotNull CompoundTag tag)
    {
    }

    @Override
    public void tickServer() {
        BlockPos pos = getBlockPos();
        System.out.println("Tick at: " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
    }
}
