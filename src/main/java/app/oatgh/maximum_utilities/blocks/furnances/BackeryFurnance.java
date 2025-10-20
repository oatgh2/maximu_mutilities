package app.oatgh.maximum_utilities.blocks.furnances;

import app.oatgh.maximum_utilities.blocks.MaximumUtilitiesBlockBase;
import app.oatgh.maximum_utilities.menu.containers.BackeryFurnanceContainer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import org.jetbrains.annotations.Nullable;

public class BackeryFurnance extends MaximumUtilitiesBlockBase {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final IntegerProperty LIGHT_LEVEL = IntegerProperty.create("light_level", 0, 14);

    public BackeryFurnance() {
        super(BlockBehaviour.Properties.copy(Blocks.FURNACE));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, LIT, LIGHT_LEVEL);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BackeryFurnanceEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
            BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof BackeryFurnanceEntity bfe) {
                MenuProvider menuProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("maximumutilities.screen.backery.furnance");
                    }

                    @Override
                    public @Nullable AbstractContainerMenu createMenu(int windowId, Inventory inventory,
                            Player player) {
                        return new BackeryFurnanceContainer(windowId, inventory, pPos);
                    }
                };
                NetworkHooks.openScreen((ServerPlayer) pPlayer, menuProvider, bfe.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.hasProperty(LIGHT_LEVEL) ? state.getValue(LIGHT_LEVEL) : 0;
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
