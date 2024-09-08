package app.oatgh.maximum_utilities.registries;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.blocks.furnances.BakceryFurnance;
import app.oatgh.maximum_utilities.items.BreadDough;
import app.oatgh.maximum_utilities.items.FlourPowder;
import app.oatgh.maximum_utilities.items.WaterBowl;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class MUItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MaximumUtilities.MODID);

    public static final RegistryObject<Item> WATER_BOWL = ITEMS.register("water_bowl",
            () -> new WaterBowl(() -> Fluids.WATER, new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> BACKERY_FURNANCE_ITEM = ITEMS.register("backery_furnance_item",
            () -> new BlockItem(MUBlocks.BACKERY_FURNANCE.get(), new Item.Properties()));

    public static final RegistryObject<Item> FLOUR_POWDER = ITEMS.register("flour_powder", () ->
            new FlourPowder(new Item.Properties().food(
                    new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationMod(0.5f)
                            .build()
            )));


    public static final RegistryObject<Item> BREAD_DOUGH = ITEMS.register("bread_dough",
            () -> new BreadDough(new Item.Properties(
            ).food(new FoodProperties.Builder().nutrition(1).build())));


    public static void addCreative(@NotNull BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(MUItems.WATER_BOWL);
            event.accept(MUItems.FLOUR_POWDER);
            event.accept(MUItems.BREAD_DOUGH);
        }
    }
}
