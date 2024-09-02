package app.oatgh.maximum_utilities.handlers;

import app.oatgh.maximum_utilities.registries.MUItems;
import app.oatgh.maximum_utilities.registries.MURecipes;
import app.oatgh.maximum_utilities.utils.PlaySounds;
import com.mojang.logging.LogUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

public class BreadDoughRecipeHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        try {
            Recipe<?> recipe;
            Level level;
            LivingEntity livingEntity = event.getEntity();
            if(event.getEntity().level().isClientSide()){
                LocalPlayer lPlayer = ((LocalPlayer)livingEntity);
                level = lPlayer.level();
                recipe = level.getRecipeManager().byKey(MURecipes.BREAD_DOUGH_RECIPE)
                        .orElse(null);
            }else{
                ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
                level = serverPlayer.level();
                recipe = level.getRecipeManager().byKey(MURecipes.BREAD_DOUGH_RECIPE).orElse(null);
            }


            if(recipe != null && event.getCrafting().getItem() == MUItems.BREAD_DOUGH.get()){
                PlaySounds.PlayBucketEmptySound(level, livingEntity);
                event.getEntity().addItem(new ItemStack(Items.BOWL, 1));
            }
        }catch (Exception ex){
            LOGGER.error(ex.toString());
        }

    }
}
