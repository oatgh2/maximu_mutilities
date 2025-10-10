package app.oatgh.maximum_utilities;

import app.oatgh.maximum_utilities.menu.screens.BackeryFurnanceScreen;
import app.oatgh.maximum_utilities.menu.screens.BowlMenuScreen;
import app.oatgh.maximum_utilities.registries.MUMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MaximumUtilities.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void Init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MUMenus.BACKERY_CONTAINER.get(), BackeryFurnanceScreen::new);
            MenuScreens.register(MUMenus.BOWL_CONTAINER.get(), BowlMenuScreen::new);
        });

        event.enqueueWork(() -> {
            ItemProperties.register(Items.BOWL, new ResourceLocation("has_fluid"), 
            (stack, level, entity, seed) ->  stack.hasTag() && stack.getTag().contains("Fluid") ? 1.0F : 0.0F
            );
        });
    }
}
