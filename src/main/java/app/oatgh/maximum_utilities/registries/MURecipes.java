package app.oatgh.maximum_utilities.registries;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.recipes.bowl.DrainBowlFluidRecipeSerializer;
import app.oatgh.maximum_utilities.recipes.bowl.BowlRecipe;
import app.oatgh.maximum_utilities.recipes.bowl.BowlRecipeSerializer;
import app.oatgh.maximum_utilities.recipes.bowl.DrainBowlFluidRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MURecipes {
    public static final ResourceLocation BREAD_DOUGH_RECIPE = new ResourceLocation(MaximumUtilities.MODID,
            "bread_dough_recipe");

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister
            .create(ForgeRegistries.RECIPE_SERIALIZERS, MaximumUtilities.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister
            .create(ForgeRegistries.RECIPE_TYPES, MaximumUtilities.MODID);

    public static final RecipeType<BowlRecipe> BOWL_RECIPE_TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return MaximumUtilities.MODID + ":bowl_recipe";
        }
    };

    public static final RegistryObject<RecipeSerializer<DrainBowlFluidRecipe>> DRAIN_BOWL_FLUID_SERIALIZER = RECIPE_SERIALIZER
            .register("drain_fluid_bowl", DrainBowlFluidRecipeSerializer::new);

    public static final RegistryObject<RecipeSerializer<BowlRecipe>> BOWL_RECIPE_SERIALIZER = RECIPE_SERIALIZER
            .register("bowl_recipe", BowlRecipeSerializer::new);
}
