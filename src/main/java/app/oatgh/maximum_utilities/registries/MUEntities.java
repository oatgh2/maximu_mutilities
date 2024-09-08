package app.oatgh.maximum_utilities.registries;

import app.oatgh.maximum_utilities.MaximumUtilities;
import app.oatgh.maximum_utilities.blocks.furnances.BackeryFurnanceEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MUEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MaximumUtilities.MODID);


    public static final RegistryObject<BlockEntityType<?>> BACKERY_FURNANCE_ENTITY = BLOCK_ENTITIES.register(
            "bakery_furnance_entity",() -> BlockEntityType.Builder
                    .of(BackeryFurnanceEntity::new,
                            MUBlocks.BACKERY_FURNANCE.get())
                    .build(null));
}
