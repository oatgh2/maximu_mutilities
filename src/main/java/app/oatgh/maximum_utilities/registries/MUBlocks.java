package app.oatgh.maximum_utilities.registries;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.blocks.furnances.BakceryFurnance;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MUBlocks {


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            MaximumUtilities.MODID);


    public static final RegistryObject<Block> BACKERY_FURNANCE = BLOCKS.register(
            "backery_furnance", BakceryFurnance::new);

}
