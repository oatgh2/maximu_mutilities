package app.oatgh.maximum_utilities.blocks.furnances;

import app.oatgh.maximum_utilities.blocks.MaximumUtilitiesBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
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
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        LazyOptional<IItemHandler> cap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
        cap.ifPresent(iItemHandler -> {
            IItemHandler handler =  iItemHandler;

            int slots = handler.getSlots();
            if(!pPlayer.isShiftKeyDown()){
                ItemStack itemStack = pPlayer.getItemInHand(pHand);
                Integer slotEqualsItemStackInHand = null;
                for(int i = 0; i < slots; i++){
                    if(handler.getStackInSlot(i).isEmpty() ||
                            (handler.getStackInSlot(i).getItem() == itemStack.getItem()
                                    && handler.getStackInSlot(i).getCount() < handler.getStackInSlot(i).getMaxStackSize())){
                        slotEqualsItemStackInHand = i;
                        break;
                    }
                }

                if(slotEqualsItemStackInHand != null){
                    ItemStack stack = handler.insertItem(slotEqualsItemStackInHand, itemStack, false);
                    pPlayer.setItemInHand(pHand, stack);
                }
            }else{
                for(int i = 0; i < slots; i++){
                    if(!handler.getStackInSlot(i).isEmpty()){
                        pPlayer.addItem(handler.extractItem(i, handler.getStackInSlot(i).getCount(), false));
                        break;
                    }
                }
            }
        });

        return InteractionResult.SUCCESS;
    }
}
