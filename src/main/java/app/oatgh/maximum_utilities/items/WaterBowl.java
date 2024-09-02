package app.oatgh.maximum_utilities.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class WaterBowl extends BucketItem {

    public WaterBowl(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer,
                                                           @NotNull InteractionHand pHand) {

        BlockHitResult blockHitResult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
        Direction hitedDirection = blockHitResult.getDirection();
        BlockPos realAimedPos = blockHitResult.withDirection(hitedDirection).getBlockPos();
        BlockState hitedBlockState = pLevel.getBlockState(realAimedPos);
        if(hitedBlockState.getBlock() != Blocks.AIR){
            InteractionResultHolder<ItemStack> result = super.use(pLevel, pPlayer, pHand);
            return InteractionResultHolder.pass(new ItemStack(Items.BOWL, 1));
        }
        return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));
    }
}
