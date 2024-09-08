package app.oatgh.maximum_utilities.blocks.furnances;

import app.oatgh.maximum_utilities.blocks.MaximumUtilitiesBlockBase;
import app.oatgh.maximum_utilities.registries.MUItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
        BackeryFurnanceEntity entity =  (BackeryFurnanceEntity)pLevel.getBlockEntity(pPos);
        if(pPlayer.getItemInHand(pHand).is(MUItems.BREAD_DOUGH.get())){
            entity.TakeStackFromPlayer(pPlayer, pHand, 0);
            return InteractionResult.PASS;

        }
        return  InteractionResult.FAIL;
    }
}
