package app.oatgh.maximum_utilities.blocks;

import app.oatgh.maximum_utilities.blocks.entities.MaximumUtilitiesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;


public class MaximumUtilitiesBlockBase extends Block implements EntityBlock {



    public MaximumUtilitiesBlockBase(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        throw new NotImplementedException("Implements this method in block");
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) return null;
        else{
            return (lvl, blockState, st, blockEntityType) -> {
                if(blockEntityType instanceof MaximumUtilitiesBlockEntity mub){
                    mub.tickServer();
                }
            };
        }


    }
}
