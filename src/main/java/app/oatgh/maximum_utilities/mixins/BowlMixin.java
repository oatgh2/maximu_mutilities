package app.oatgh.maximum_utilities.mixins;

import app.oatgh.maximum_utilities.registries.MUItems;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = Item.class, remap = false)
public abstract class BowlMixin {

    @Shadow
    protected static BlockHitResult getPlayerPOVHitResult(Level pLevel, Player pPlayer, ClipContext.Fluid pFluidMode) {
        throw new AssertionError();
    }

    @Shadow public abstract InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand);

    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(at = @At(value = "HEAD"), method = "use", cancellable = true)
    public void useBowlInWater(Level pLevel, @NotNull Player pPlayer, InteractionHand pUsedHand,
                               CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemUsed = pPlayer.getItemInHand(pUsedHand);
        if (itemUsed.getItem().equals(Items.BOWL)) {
            try {
                BlockHitResult blockHitResult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
                Direction hitedDirection = blockHitResult.getDirection();
                BlockPos realAimedPos = blockHitResult.withDirection(hitedDirection).getBlockPos();
                BlockState hitedBlockState = pLevel.getBlockState(realAimedPos);
                if (hitedBlockState.getBlock() instanceof BucketPickup bucketPickup
                        && (hitedBlockState.getBlock() == Blocks.WATER || hitedBlockState.getFluidState().is(Fluids.WATER))) {
                    ItemStack resultedItemStack = new ItemStack(MUItems.WATER_BOWL.get());
                    ItemStack pickedUpItem = bucketPickup.pickupBlock(pLevel, realAimedPos, hitedBlockState);
                    bucketPickup.getPickupSound(hitedBlockState).ifPresent((soundEvent) -> {
                        pPlayer.playSound(soundEvent, 1F, 1F);
                    });
                    pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, realAimedPos);
                    if(!pLevel.isClientSide()){
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)pPlayer, resultedItemStack);
                    }
                }
            } catch (Exception ex) {
                LOGGER.error(ex.toString());
            }
        }
    }
}
