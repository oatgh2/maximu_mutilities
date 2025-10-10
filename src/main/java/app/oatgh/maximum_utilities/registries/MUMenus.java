package app.oatgh.maximum_utilities.registries;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.menu.containers.BackeryFurnanceContainer;
import app.oatgh.maximum_utilities.menu.containers.BowlContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MUMenus {
        public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
                        ForgeRegistries.MENU_TYPES,
                        MaximumUtilities.MODID);

        public static final RegistryObject<MenuType<BackeryFurnanceContainer>> BACKERY_CONTAINER = MENU_TYPES.register(
                        "bakcery_menu",
                        () -> IForgeMenuType.create((windowId, inv, data) -> new BackeryFurnanceContainer(windowId, inv,
                                        data.readBlockPos())));

        public static final RegistryObject<MenuType<BowlContainer>> BOWL_CONTAINER = MENU_TYPES.register("bowl_menu",
                        () -> IForgeMenuType.create((int windowId, Inventory playerInv,
                                        FriendlyByteBuf extraData) -> new BowlContainer(windowId, playerInv,
                                                        extraData.readItem())));
}
