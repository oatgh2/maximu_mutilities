package app.oatgh.maximum_utilities.registries;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.blocks.menu.containers.BackeryFurnanceContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MUMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
            MaximumUtilities.MODID);

    public static final RegistryObject<MenuType<BackeryFurnanceContainer>> BACKERY_CONTAINER = MENU_TYPES.register("bakcery_menu",
            () -> IForgeMenuType.create((windowId, inv, data) -> new BackeryFurnanceContainer(windowId, inv,
                    data.readBlockPos())));
}
