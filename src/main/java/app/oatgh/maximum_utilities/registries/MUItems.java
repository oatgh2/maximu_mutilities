package app.oatgh.maximum_utilities.registries;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.items.FlourPowder;
import app.oatgh.maximum_utilities.items.WaterBowl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class MUItems {


    public static final RegistryObject<Item> WATER_BOWL = RegistryObject.create(new ResourceLocation(
            MaximumUtilities.MODID, "water_bowl"), ForgeRegistries.ITEMS);

    public static final RegistryObject<Item> FLOUR_POWDER = RegistryObject.create(new ResourceLocation(
            MaximumUtilities.MODID, "flour_powder"), ForgeRegistries.ITEMS);

    public static final RegistryObject<Item> BREAD_DOUGH = RegistryObject.create(new ResourceLocation(
            MaximumUtilities.MODID, "bread_dough"), ForgeRegistries.ITEMS);

    public static void Register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS, helper -> {
                    helper.register(new ResourceLocation(MaximumUtilities.MODID, "water_bowl"),
                            new WaterBowl(() -> Fluids.WATER, new Item.Properties().stacksTo(1)));


                    helper.register(new ResourceLocation(MaximumUtilities.MODID, "flour_powder"),
                            new FlourPowder(new Item.Properties().food(
                                    new FoodProperties.Builder()
                                            .nutrition(1)
                                            .saturationMod(0.1f)
                                            .alwaysEat()
                                            .build()
                            )));

                    helper.register(new ResourceLocation(MaximumUtilities.MODID, "bread_dough"),
                            new FlourPowder(new Item.Properties().food(
                                    new FoodProperties.Builder()
                                            .nutrition(2)
                                            .saturationMod(0.5f)
                                            .alwaysEat()
                                            .build()
                            )));
                }
        );
    }
}
