package app.oatgh.maximum_utilities.items;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class BreadDough extends Item {
    public BreadDough(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return pStack.hasTag() && pStack.getTag().contains("kneaded") ? Component.translatable("item.maximumutilities.bread_dough_kneaded")
                : Component.translatable("item.maximumutilities.bread_dough");
    }
}
