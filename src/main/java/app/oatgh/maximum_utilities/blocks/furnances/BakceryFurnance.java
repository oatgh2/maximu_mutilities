package app.oatgh.maximum_utilities.blocks.furnances;

import app.oatgh.maximum_utilities.blocks.MaximumUtilitiesBlockBase;
import app.oatgh.maximum_utilities.blocks.menu.containers.BackeryFurnanceContainer;
import app.oatgh.maximum_utilities.blocks.menu.screens.BackeryFurnanceScreen;
import app.oatgh.maximum_utilities.registries.MUItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer,
                                 InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide()){
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if(be instanceof BackeryFurnanceEntity){
                MenuProvider menuProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("maximumutilities.screen.backery.furnance");
                    }

                    @Override
                    public @Nullable AbstractContainerMenu createMenu(int windowId, Inventory playerInventory,
                                                                      Player player) {
                        return new BackeryFurnanceContainer(windowId, playerInventory, pPos);
                    }
                };
                NetworkHooks.openScreen((ServerPlayer) pPlayer, menuProvider, be.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }

}
