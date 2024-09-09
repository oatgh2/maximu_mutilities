package app.oatgh.maximum_utilities;

import app.oatgh.maximum_utilities.blocks.menu.screens.BackeryFurnanceScreen;
import app.oatgh.maximum_utilities.registries.MUMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MaximumUtilities.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void Init(FMLClientSetupEvent event){
        event.enqueueWork(() -> {
            MenuScreens.register(MUMenus.BACKERY_CONTAINER.get(), BackeryFurnanceScreen::new);
        });
    }
}
