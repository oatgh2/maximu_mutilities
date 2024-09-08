package app.oatgh.maximum_utilities;

import app.oatgh.maximum_utilities.registries.MUBlocks;
import app.oatgh.maximum_utilities.registries.MUEntities;
import app.oatgh.maximum_utilities.registries.MUItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MaximumUtilities.MODID)
public class MaximumUtilities
{
    public static final String MODID = "maximumutilities";
    private static final Logger LOGGER = LogUtils.getLogger();
    public MaximumUtilities()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MUBlocks.BLOCKS.register(modEventBus);
        MUItems.ITEMS.register(modEventBus);
        MUEntities.BLOCK_ENTITIES.register(modEventBus);

        modEventBus.addListener(MUItems::addCreative);

        modEventBus.addListener(this::commonSetup);


        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }



    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
